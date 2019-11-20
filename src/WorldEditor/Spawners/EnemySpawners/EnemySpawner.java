package WorldEditor.Spawners.EnemySpawners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import WorldEditor.Location;
import WorldEditor.Rand;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.MeleeEnemy;
import WorldEditor.Spawners.Spawner;
import renderer.Bitmap;

public abstract class EnemySpawner implements Spawner {

	Bitmap[] enemyImages;

	public EnemySpawner(Bitmap[] enemyImages) {
		this.enemyImages = enemyImages;
	}

	public EnemySpawner() {
		// TODO Auto-generated constructor stub
	}

	protected Set<Location> generateSpawnLocations(Room room, int numberToSpawn) {
		Set<Location> locations = new HashSet<>();
		Location bottomLeftLocation = room.BottomLeftLocation;
		Location topRightLocation = room.TopRightLocation;

		for (int i = 0; i < numberToSpawn; i++) {

			int x = Rand.ranInt(topRightLocation.x - 1, bottomLeftLocation.x + 1);
			int y = Rand.ranInt(topRightLocation.y - 1, bottomLeftLocation.y + 1);

			Location currentLoc = new Location(x, y);
			if (!Location.isTooClose(currentLoc, 1, locations)) {
				locations.add(currentLoc);
			}
		}
		return locations;
	}

	protected void createEnemy(Location loc, Room room, World world, Enemy type) {
		world.world[(int) loc.x][(int) loc.y].addGameObject(type);
		room.addObjectToRoom(type);
		world.enemyObjects.add(type);
	}

	protected void createEnemy(Location loc, World world, Enemy instance) {
		// TODO Auto-generated method stub
		world.world[(int) loc.x][(int) loc.y].addGameObject(instance);
		world.enemyObjects.add(instance);
	}

}
