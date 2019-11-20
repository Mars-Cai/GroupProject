package WorldEditor;

import java.util.ArrayList;
import java.util.List;

import GUI.Frame;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Wall;
import WorldEditor.Spawners.RoomPopulator;
import WorldEditor.Spawners.EnemySpawners.BossSpawner;
import WorldEditor.Spawners.EnemySpawners.StandardEnemySpawner;
import persistence.XMLObjects.WorldXML;
import renderer.Bitmap;
import renderer.Core;
import renderer.Input;

public class World extends AbstractWorld {

	// int columns;
	private int maxNumOfRooms;
	private Count maxNumberOfRoomsBoundry = new Count(6, 6);

	// private boolean mapToggle;

	public final static double chanceToSpawnNewRoom = 0.5;

	// private List<Character> characters = new ArrayList<Character>();

	// Clears the list gridPositions and prepares it to generate a new board.
	public World() {
		generator = new Rand(System.currentTimeMillis());
		initialise();
		RoomPopulator roomPopulator = new RoomPopulator(this, false);

	}

	public World(Long seed) {
		generator = new Rand(seed);
		initialise();
	}

	public void worldTick(Input input) {
		gameLogic.gameLogicTick(input, this);
		tabMap.toggleMap(input.tabPressed);

	}

	@Override
	protected void initialise() {
		world = new Square[MAXBOARDWIDTH][MAXBOARDHEIGHT];
		initialiseSquareGrid();
		this.tabMap = new TabMap(this);
		roomHeightBoundry = new Count(5, 7);
		roomWidthBoundry = new Count(5, 7);

		testImage = Core.loadTexture("TestFloorSWEN.jpg");
		floorImages = new Bitmap[] { Core.loadTexture("TestFloorSWEN.jpg") };
		wallImages = new Bitmap[] { Core.loadTexture("TestWallSWEN.jpg") };
		doorImages = new Bitmap[] { testImage };
		enemyImages = new Bitmap[] { testImage };

		maxNumOfRooms = generator.ranIntSeed(maxNumberOfRoomsBoundry.max, maxNumberOfRoomsBoundry.min);
		Room initialRoom = createInitialRoom();
		rooms.add(initialRoom);
		generateRoomLocations(initialRoom, 0);
		boardSetUp();

	}

	private Room createInitialRoom() {
		return new Room(new Location(0, 0), generateRoomSize());
	}

	private void generateRoomLocations(Room previousRoom, int numRoomsCreated) {
		if (maxNumOfRooms <= numRoomsCreated) {
			return;
		}

		// int columns = Math.random()(columnCount.min, columnCount.max) + xStart;
		// int rows = Math.random()(rowCount.min, rowCount.max) + yStart;

		// chance to spawn a new room, always at least generate 2 rooms
		if (numRoomsCreated < maxNumberOfRoomsBoundry.min) {
			forceSpawnRooms(previousRoom, numRoomsCreated);
		} else {

			if (chanceToSpawnNewRoom >= (generator.ranDoubleSeed(1, 0))) {
				spawnRoomToTheRight(previousRoom, numRoomsCreated);
			}
			if (chanceToSpawnNewRoom >= (generator.ranDoubleSeed(1, 0))) {
				spawnRoomAbove(previousRoom, numRoomsCreated);

			}
		}
	}

	private void forceSpawnRooms(Room previousRoom, int numRoomsCreated) {
		// TODO Auto-generated method stub
		boolean roomSpawned = false;
		while (!roomSpawned) {
			roomSpawned = spawnRoomAbove(previousRoom, numRoomsCreated)
					| spawnRoomToTheRight(previousRoom, numRoomsCreated);
		}

	}

	private boolean spawnRoomAbove(Room previousRoom, int numRoomsCreated) {
		// TODO Auto-generated method stub
		Location bottomLeft = new Location(generateWestWallXOffset(previousRoom), previousRoom.TopRightLocation.y + 1);
		Location topRight = Location.plus(bottomLeft, generateRoomSize());
		Room newRoom = new Room(bottomLeft, topRight);
		return validRoom(previousRoom, newRoom, Direction.NORTH, numRoomsCreated);

	}

	private boolean spawnRoomToTheRight(Room previousRoom, int numRoomsCreated) {
		Location bottomLeft = new Location(previousRoom.TopRightLocation.x + 1, generateWestWallYOffset(previousRoom));
		Location topRight = Location.plus(bottomLeft, generateRoomSize());
		Room newRoom = new Room(bottomLeft, topRight);
		return validRoom(previousRoom, newRoom, Direction.EAST, numRoomsCreated);

	}

	private boolean validRoom(Room previous, Room newRoom, Direction dir, int numRoomsCreated) {
		if (doesRoomIntersectWithAnyOtherRooms(newRoom, previous)) {
			return false;
		}

		Location doorLoc = generateDoorLocation(previous, newRoom, dir);
		Square doorSquarePrev = world[(int) doorLoc.x][(int) doorLoc.y];
		RoomConnection rc = new RoomConnection(previous, newRoom, doorSquarePrev, doorLoc, dir);
		previous.connections.put(dir, rc);

//		doorLoc = Location.moveInDirection(doorLoc, dir, Wall.HITBOXRADIUS);
//		Square doorSquareNew = world[(int) doorLoc.x][(int) doorLoc.y];
		newRoom.connections.put(Direction.inverse(dir),
				new RoomConnection(newRoom, previous, null, null, Direction.inverse(dir)));

		rooms.add(newRoom);
		generateRoomLocations(newRoom, numRoomsCreated + 1);
		return true;
	}

	/**
	 * initilises all doors, floors and doors
	 */
	private void boardSetUp() {
		for (int i = 0; i < rooms.size(); i++) {
			Room currentRoom = rooms.get(i);
			generateWallsAndFloors(currentRoom);
			removeDoubleWalls(currentRoom);
			if (i >= 0) {
				currentRoom.doorsInRoom.addAll(generateDoors(currentRoom));
			}
		}
	}

	private void removeDoubleWalls(Room currentRoom) {
		// TODO Auto-generated method stub
		currentRoom.wallsToRemove.forEach(l -> removeDoubleWall(l, currentRoom));
		

	}

	/**
	 * 
	 * @param l
	 * @param room
	 */
	private void removeDoubleWall(Location l, Room room) {
		// TODO Auto-generated method stub
		Square s = world[(int) l.x][(int) l.y];
		List<Wall> walls = s.getObjectsOfType(Wall.class);
		Door door = s.getObjectOfType(Door.class);
		if(walls.isEmpty()) return;
		if(walls.size() >1) {
			
			if(room.connections.get(Direction.WEST) != null) {
				Wall wall = walls.stream().filter(w->w.getXY().equals(Axis.Y)).findFirst().orElse(null);
				verticalWallObjects.remove(wall);
				s.removeGameObject(wall);
				room.removeWall(wall);
				
			}else if(room.connections.get(Direction.SOUTH) != null) {
				Wall wall = walls.stream().filter(w->w.getXY().equals(Axis.X)).findFirst().orElse(null);
				horizontalWallObjects.remove(wall);
				s.removeGameObject(wall);
				room.removeWall(wall);
			}
			
		}else {
			verticalWallObjects.remove(walls.get(0));
			horizontalWallObjects.remove(walls.get(0));
			s.removeGameObject(walls.get(0));
			
			room.removeWall(walls.get(0));
		}
		
		
	}

	/**
	 * Creates all teh walls and floors in a room
	 * @param room
	 */
	private void generateWallsAndFloors(Room room) {
		room.horizontalWallsInRoom.addAll(generateTopWall(room.TopLeftLocation, room.TopRightLocation, room));
		room.horizontalWallsInRoom.addAll(generateBottomWall(room.BottomLeftLocation, room.BottomRightLocation,room));
		room.verticalWallsInRoom.addAll(generateLeftWall(room.TopLeftLocation, room.BottomLeftLocation,room));
		room.verticalWallsInRoom.addAll(generateRightWall(room.BottomRightLocation, room.TopRightLocation,room));
		room.floorsInRoom.addAll(generateFloor(room));

	}

	private List<Wall> generateBottomWall(Location bottomLeft, Location bottomRight,Room room) {
		int left = (int) bottomLeft.x;
		int right = (int) bottomRight.x;
		double y = bottomLeft.y;

		return createHorizontalWall(left, right, y,room);
	}

	private List<Wall> generateTopWall(Location topLeft, Location topRight,Room room) {
		int left = (int) topLeft.x;
		int right = (int) topRight.x;
		double y = topLeft.y + (1.0 - Wall.HITBOXRADIUS);

		return createHorizontalWall(left, right, y,room);
	}

	private List<Wall> generateLeftWall(Location topLeft, Location bottomLeft,Room room) {
		int bottom = (int) bottomLeft.y;
		int top = (int) topLeft.y;
		double x = bottomLeft.x;
		return createVerticalWall(bottom, top, x,room);
	}

	private List<Wall> generateRightWall(Location bottomRight, Location topRight,Room room) {
		int bottom = (int) bottomRight.y;
		int top = (int) topRight.y;
		double x = bottomRight.x + (1.0 - Wall.HITBOXRADIUS);
		return createVerticalWall(bottom, top, x,room);
	}

	private List<Door> generateDoors(Room currentRoom) {
		List<Door> newDoors = new ArrayList<>();
		for (RoomConnection rc : currentRoom.connections.values()) {
			Location doorLoc = rc.doorExactLoc;
			makeRoomForDoor(rc.doorSquare, rc.from);
			if(doorLoc == null)continue;
			Door d = createDoor(doorLoc.x, doorLoc.y, Direction.ClockWiseSnap(Direction.doorToDir((rc.direction))),
					Direction.inverse(rc.direction), currentRoom);
			newDoors.add(d);
		}
		return newDoors;
	}

	private void makeRoomForDoor(Square doorSquare, Room current) {
		// TODO Auto-generated method stub
		if(doorSquare == null) return;
		current.removeWall(doorSquare.getObjectOfType(Wall.class));
		// to.removeWall(doorSquare.getObjectOfType(Wall.class));
		verticalWallObjects.remove(doorSquare.getObjectOfType(Wall.class));
		horizontalWallObjects.remove(doorSquare.getObjectOfType(Wall.class));
		doorSquare.removeGameObject(doorSquare.getObjectOfType(Wall.class));

	}

	
	public AbstractWorld populateFromSave(WorldXML wXML) {
		// TODO Auto-generated method stub
		RoomPopulator roomPopulator = new RoomPopulator(this, true);
		// ChestSpawner cS = new ChestSpawner();
		// wXML.chestObjects.forEach(c->cS.createChest(c.location.XMLTo(),this,
		// testImage, c.facingDir));
		// VendorSpawner vS = new VendorSpawner();
		// wXML.chestObjects.forEach(v->vS.createVendor(v.location.XMLTo(),this,
		// testImage));
		StandardEnemySpawner eS = new StandardEnemySpawner();
		BossSpawner bS = new BossSpawner();
		wXML.enemyObjects.forEach(e -> {
			if (e.isBoss) {
				bS.create(e.location.XMLTo(), this);
			} else {
				eS.create(e.location.XMLTo(), this);
			}
		});
		this.player = wXML.player.XMLto();
		world[(int) player.getExactLocation().x][(int) player.getExactLocation().y].addGameObject(player);
		return this;
	}


}
