package WorldEditor.Spawners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import WorldEditor.Direction;
import WorldEditor.Location;
import WorldEditor.Rand;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.Coin;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.WeaponObjects.Gun;
import WorldEditor.GameObjects.WeaponObjects.Sword;
import renderer.Bitmap;

public class ChestSpawner implements Spawner {

	private Bitmap[] chestImages;
	private int totalNumberOfChestsSpawned;
	private int spawnEveryXRooms;
	private final int maxNumberOfChets = 3;
	private Direction dropDir;
	private Rand rand;
	private Set<Chest> chestSpawned = new HashSet<>();
	private List<Key> keys = new ArrayList<>();


	public ChestSpawner(int numberOfRooms, Rand rand) {
		// TODO Auto-generated constructor stub
		this.spawnEveryXRooms = numberOfRooms / maxNumberOfChets;
		totalNumberOfChestsSpawned = 0;
		this.rand = rand;
	}

	public ChestSpawner() {
	}

	@Override
	public void spawn(Room room, World world) {
		if (totalNumberOfChestsSpawned % spawnEveryXRooms != 0)
			return;
		while (!room.hasChest()) {
			int xSpawn = rand.ranIntSeed((int) room.BottomRightLocation.x - 1, (int) room.BottomLeftLocation.x + 1);
			int ySpawn = rand.ranIntSeed((int) room.TopLeftLocation.y - 1, (int) room.BottomLeftLocation.y + 1);
			Location spawnLoc;
			if (spawnOnHorizontalWall(room)) {
				spawnLoc = new Location(xSpawn, topOrBottom(room));
				if (roomForChest(room, spawnLoc)) {
					createChest(spawnLoc, room, world);
					return;
				}
			}

			spawnLoc = new Location(leftOrRight(room), ySpawn);
			if (roomForChest(room, spawnLoc)) {
				createChest(spawnLoc, room, world);
				return;
			}
		}
	}

	private double leftOrRight(Room room) {
		if (Rand.ranDouble(1, 0) > 0.5) {
			dropDir = Direction.EAST;
			return room.BottomLeftLocation.x + 1;
		}
		dropDir = Direction.WEST;
		return room.BottomRightLocation.x - 1;
	}

	private double topOrBottom(Room room) {
		if (Rand.ranDouble(1, 0) > 0.5) {
			dropDir = Direction.SOUTH;
			return room.BottomLeftLocation.y + 1;
		}
		dropDir = Direction.NORTH;
		return room.TopLeftLocation.y - 1;
	}

	private boolean roomForChest(Room room, Location loc) {
		if (blockingDoor(room, loc))
			return false;
		return !room.getObjectsInRoom().stream().anyMatch(o -> o.getSquareLocation().equals(loc));
	}

	private boolean blockingDoor(Room room, Location loc) {
		return room.doorsInRoom.stream()
				.anyMatch(o -> Location.moveInDirection(o.getSquareLocation(), dropDir, 1).equals(loc));

	}

	private boolean spawnOnHorizontalWall(Room room) {
		return Rand.ranDouble(1, 0) > 0.5;
	}

	private void createChest(Location location, Room room, World world) {
		Chest instance = new Chest(dropDir);
		Key key = KeySpawner.generateKey();
		instance.setKey(key);
		keys.add(key);
		world.world[(int) location.x][(int) location.y].addGameObject(instance);
		room.addObjectToRoom(instance);
		
		totalNumberOfChestsSpawned++;
		world.chestObjects.add(instance);
		chestSpawned.add(instance);
	}
	
	

	public void createChestFromSave(Location location, World world, Direction facingDir) {
		Chest instance = new Chest(facingDir);
		Key key = KeySpawner.generateKey();
		instance.setKey(key);
		keys.add(key);
		world.world[(int) location.x][(int) location.y].addGameObject(instance);
		world.chestObjects.add(instance);
		chestSpawned.add(instance);
	}

	private boolean gunSpawned;
	private boolean swordSpawned;

	public void setChestTypes() {
		Item i;
		for (Chest c : chestSpawned) {
			if (!swordSpawned) {
				i = new Sword();
				swordSpawned = true;
			} else if (!gunSpawned) {
				i = new Gun();
				gunSpawned = true;
			} else {
				i = new Coin(Chest.COINDROPVALUE);
			}
			c.initialiseChestType(i);
		}
	}
	
	public List<Key> getKeys() {
		return keys;
	}

}
