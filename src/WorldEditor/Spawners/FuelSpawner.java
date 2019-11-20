package WorldEditor.Spawners;

import java.util.HashSet;
import java.util.Set;

import WorldEditor.Location;
import WorldEditor.Rand;
import WorldEditor.Room;
import WorldEditor.World;
import WorldEditor.GameObjects.Fuel;

public class FuelSpawner implements Spawner{

	@Override
	public void spawn(Room room, World world) {
		generateSpawnLocations(room, 1).forEach(fL->createFuel(fL, room, world));
	}
	
	private Set<Location> generateSpawnLocations(Room room, int numberToSpawn) {
		Set<Location> locations = new HashSet<>();
		Location bottomLeftLocation = room.BottomLeftLocation;
		Location topRightLocation = room.TopRightLocation;

		for (int i = 0; i < numberToSpawn; i++) {

			int x = Rand.ranInt(topRightLocation.x - 1, bottomLeftLocation.x + 1);
			int y = Rand.ranInt(topRightLocation.y - 1, bottomLeftLocation.y + 1);

			Location currentLoc = new Location(x, y);
			if (!Location.isTooClose(currentLoc, 1, room.getObjectsInRoom())) {
				locations.add(currentLoc);
			}
		}
		return locations;
	}
	
	private void createFuel(Location currentLoc, Room room, World world) {
		Fuel fuel = new Fuel();
		world.world[(int) currentLoc.x][(int) currentLoc.y].addGameObject(fuel);
		room.addObjectToRoom(fuel);
		world.fuelObjects.add(fuel);
	}

}
