package WorldEditor.Spawners.EnemySpawners;

import java.util.HashSet;
import java.util.Set;

import WorldEditor.Location;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.Boss;
import WorldEditor.GameObjects.MeleeEnemy;
import renderer.Bitmap;

public class BossSpawner extends EnemySpawner {

	public BossSpawner(Bitmap[] enemyImages) {
		super(enemyImages);
	}

	public BossSpawner() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void spawn(Room room, World world) {
		Location spawnLocation =placeInCentre(room);
		create(spawnLocation, room, world);
	}
	
	private Location placeInCentre(Room room) {
		int x = (int) ((room.BottomLeftLocation.x + room.BottomRightLocation.x) / 2);
		int y = (int) ((room.BottomLeftLocation.y + room.TopLeftLocation.y) / 2);
		return new Location(x, y);
	}

	private void create(Location l, Room room, World world) {
		Boss boss = new Boss();
		room.removeAllEnemies(world);
		createEnemy(l, room, world, boss);
	}
	
	//from save
	public Boss create(Location currentLoc, World world) {
		Boss instance = new Boss();
		createEnemy(currentLoc, world, instance);
		return instance;
	}
	
}
