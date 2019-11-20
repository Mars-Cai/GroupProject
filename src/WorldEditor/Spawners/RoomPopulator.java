package WorldEditor.Spawners;

import renderer.Bitmap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.print.DocFlavor.CHAR_ARRAY;

import WorldEditor.Location;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.Spawners.EnemySpawners.BossSpawner;
import WorldEditor.Spawners.EnemySpawners.StandardEnemySpawner;


public class RoomPopulator {
	private List<Room> rooms;
	private List<Spawner> spawners;
	private List<Spawner> fromSeedSpawners;
	PlayerSpawner pS;
	StandardEnemySpawner std;
	BossRoomSpawner bR;
	ChestSpawner cS;
	VendorSpawner vS;
	FuelSpawner fS;
	private World world;


	public RoomPopulator(World world, boolean fromSave) {
		// TODO Auto-generated constructor stub
		this.rooms = world.rooms;
		this.world = world;
		fS = new FuelSpawner();
		pS = new PlayerSpawner();
		bR = new BossRoomSpawner(world.getGenerator());
		cS = new ChestSpawner(world.rooms.size(), world.getGenerator());// need to change
		vS = new VendorSpawner(world.getGenerator());
		if(fromSave) {
			spawnFromSave();
		}else {
			spawnFromNewGame();
		}
		spawnObjects();
	}
	
	private void spawnFromSave() {
		spawners = Arrays.asList(cS, bR, vS, fS);
	}
	
	private void spawnFromNewGame() {
		std = new StandardEnemySpawner();
		spawners = Arrays.asList(cS, std, bR, vS, fS);
	}

	void spawnObjects() {
		int roomsPopulated = 0;
		for (Room room : rooms) {
			if (roomsPopulated == 0) {// don't spawn anything in first room
				pS.spawn(room, world);
				roomsPopulated++;
			} else {
				spawners.forEach(s -> s.spawn(room, world));
			}
		}
		cS.setChestTypes();
		vS.spawnVendor(world, cS.getKeys().stream().findFirst().orElse(null));
		spawnKeysOnEnemies();
		spawnBoss();
	}

	private void spawnKeysOnEnemies() {
		// TODO Auto-generated method stub
		world.enemyObjects.forEach(e->{
			if(!cS.getKeys().isEmpty()) {
				e.giveKey(cS.getKeys().remove(0));
			}
		});
	}

	private void spawnBoss() {
		BossSpawner boss = new BossSpawner();
		boss.spawn(bR.getBossRoom(), world);
	}

}
