package WorldEditor.Spawners.EnemySpawners;

import java.util.Set;

import WorldEditor.Location;
import WorldEditor.Rand;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.MeleeEnemy;
import renderer.Bitmap;

public class StandardEnemySpawner extends EnemySpawner {

	public StandardEnemySpawner(Bitmap[] enemyImages) {
		super(enemyImages);
		// TODO Auto-generated constructor stub
	}



	public StandardEnemySpawner() {
		// TODO Auto-generated constructor stub
		super();
	}



	@Override
	public void spawn(Room room, World world) {
		// TODO Auto-generated method stub
		int numToSpawn = Rand.ranInt(3, 0);
		Set<Location> spawnLocations = generateSpawnLocations(room, numToSpawn);
		spawnLocations.forEach(l->create(l, room, world));
	}

	public void create(Location currentLoc, Room room, World world) {
		MeleeEnemy instance = new MeleeEnemy();
		createEnemy(currentLoc, room, world, instance);
	}

	//save
	public MeleeEnemy create(Location currentLoc, World world) {
		MeleeEnemy instance = new MeleeEnemy();
		createEnemy(currentLoc, world, instance);
		return instance;
	}



}
