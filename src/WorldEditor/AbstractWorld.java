package WorldEditor;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;

import renderer.Bitmap;
import renderer.Core;
import renderer.Input;

import java.lang.reflect.WildcardType;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.Fuel;
import WorldEditor.GameObjects.Player;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Floor;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import WorldEditor.GameObjects.InanimateObjects.Wall;
import WorldEditor.Spawners.RoomPopulator;
import gameWorld.*;
import renderer.Bitmap;

public abstract class AbstractWorld {

	public class Count {
		public int min;
		public int max;

		public Count(int min, int max) {
			this.min = min;
			this.max = max;
		}
	}

	public enum Axis {
		X, Y;

		public static Axis toXY(Direction direction) {
			// TODO Auto-generated method stub
			if (direction.equals(Direction.EAST) || direction.equals(Direction.WEST)) {
				return X;
			} else
				return Y;
		}
	}

	public Square[][] world = new Square[MAXBOARDWIDTH][MAXBOARDHEIGHT];

	public static Bitmap testImage;

	protected Bitmap[] floorImages;
	protected Bitmap[] wallImages;
	protected Bitmap[] doorImages;
	protected Bitmap[] enemyImages;

	public final static int MAXBOARDWIDTH = 300;
	public final static int MAXBOARDHEIGHT = 300;

	public Player player;
	public List<Room> rooms = new ArrayList<Room>();
	public List<Wall> horizontalWallObjects = new ArrayList<>();
	public List<Wall> verticalWallObjects = new ArrayList<>();
	public List<Floor> floorObjects = new ArrayList<>();
	public List<Door> doorObjects = new ArrayList<>();
	public List<Enemy> enemyObjects = new ArrayList<>();
	public List<Chest> chestObjects = new ArrayList<>();
	public List<Vendor> vendorObjects = new ArrayList<>();
	public List<Fuel> fuelObjects = new ArrayList<>();
	// private List<Character> characters = new ArrayList<Character>();

	protected Count roomWidthBoundry;
	protected Count roomHeightBoundry;

	protected Rand generator;
	protected GameLogic gameLogic = new GameLogic();
	// Clears the list gridPositions and prepares it to generate a new board.

	protected TabMap tabMap;

	protected Location generateDoorLocation(Room previous, Room newRoom, Direction connecttionDir) {
		// TODO Auto-generated method stub
		double x;
		double y;
		if (connecttionDir.equals(Direction.EAST)) {
			y = generator.ranIntSeed((int) previous.TopRightLocation.y - 1, (int) newRoom.BottomLeftLocation.y + 1);
			x = previous.BottomRightLocation.x + (1.0 - Wall.HITBOXRADIUS);
			horiWallsToDelete(newRoom.BottomLeftLocation.y, previous.TopRightLocation.y, newRoom, x + 1);

		} else {// direction is north
			y = previous.TopLeftLocation.y + (1.0 - Wall.HITBOXRADIUS);
			x = generator.ranIntSeed((int) previous.TopRightLocation.x - 1, (int) newRoom.BottomLeftLocation.x + 1);
			vertWallsToDelete(newRoom.BottomLeftLocation.x, previous.TopRightLocation.x, newRoom, y + 1);
		}

		return new Location(x, y);
	}

	private void vertWallsToDelete(double from, double to, Room newRoom, double y) {
		// TODO Auto-generated method stub
		for (int i = (int) from; i <= to; i++) {
			newRoom.wallsToRemove.add(new Location(i, y));
		}
	}

	private void horiWallsToDelete(double from, double to, Room newRoom, double x) {
		// TODO Auto-generated method stub
		for (int i = (int) from; i <= to; i++) {
			newRoom.wallsToRemove.add(new Location(x, i));
		}
	}

	protected int generateWestWallXOffset(Room previousRoom) {
		return generator.ranIntSeed((int) previousRoom.TopRightLocation.x - 1, (int) previousRoom.TopLeftLocation.x);
	}

	protected int generateWestWallYOffset(Room previousRoom) {
		return generator.ranIntSeed((int) previousRoom.TopRightLocation.y - 1,
				(int) previousRoom.BottomRightLocation.y);
	}

	protected Location generateRoomSize() {
		return new Location(generator.ranIntSeed(roomWidthBoundry.max, roomWidthBoundry.min),
				generator.ranIntSeed(roomHeightBoundry.max, roomHeightBoundry.min));
	}

	protected Boolean doesRoomIntersectWithAnyOtherRooms(Room room, Room prev) {
		return rooms.stream().anyMatch(rm -> !rm.equals(prev) && rm.doesRoomIntersect(room));
	}

	protected List<Floor> generateFloor(Room room) {
		Location topRight = new Location((int) room.TopRightLocation.x - 1, (int) room.TopRightLocation.y - 1);
		Location bottomLeft = new Location((int) room.BottomLeftLocation.x + 1, (int) room.BottomLeftLocation.y + 1);
		List<Floor> newFloors = new ArrayList<>();
		for (int x = (int) bottomLeft.x; x <= topRight.x; x++) {
			for (int y = (int) bottomLeft.y; y <= topRight.y; y++) {
				newFloors.add(createFloor(x, y, room));
			}
		}
		return newFloors;

	}

	protected Door createDoor(double x, double y, Direction alignmentDir, Direction facingDir, Room room) {
		// Bitmap doorImage = doorImages[(int) generator.ranIntSeed(doorImages.length -
		// 1, 0)];
		Location to = Location.moveInDirection(new Location(x, y), alignmentDir, 1);
		Door instance = new Door(to, Axis.toXY(alignmentDir), facingDir, room);
		instance.setExactLocation(new Location(x, y));
		world[(int) x][(int) y].addInitialObject(instance);
		doorObjects.add(instance);
		return instance;
	}

	protected Wall createWall(double x, double y, Direction alignmentDir, Room room) {
		// Bitmap wallImage = wallImages[(int) generator.ranIntSeed(wallImages.length -
		// 1, 0)];
		Location to = Location.moveInDirection(new Location(x, y), alignmentDir, 1);
		Wall instance = new Wall(to, Axis.toXY(alignmentDir), room);
		instance.setExactLocation(new Location(x, y));
		world[(int) x][(int) y].addInitialObject(instance);
		if (alignmentDir.equals(Direction.EAST))// horizontal wall
		{
			horizontalWallObjects.add(instance);
		} else // vertical wall
		{
			verticalWallObjects.add(instance);
		}
		return instance;
	}

	protected List<Wall> createHorizontalWall(int left, int right, double y, Room room) {
		List<Wall> newWalls = new ArrayList<>();
		for (int x = left; x <= right; x++) {
			newWalls.add(createWall(x, y, Direction.EAST, room));
		}
		return newWalls;
	}

	protected List<Wall> createVerticalWall(int bottom, int top, double x, Room room) {
		List<Wall> newWalls = new ArrayList<>();
		for (int y = bottom; y <= top; y++) {
			newWalls.add(createWall(x, y, Direction.NORTH, room));
		}
		return newWalls;
	}

	protected Floor createFloor(int x, int y, Room room) {

		// Bitmap floorImage = floorImages[(int) generator.ranIntSeed(floorImages.length
		// - 1, 0)];
		Floor instance = new Floor(room);
		world[x][y].addInitialObject(instance);
		floorObjects.add(instance);
		return instance;
	}

	@Override
	public String toString() {
		return Arrays.deepToString(world);

	}

	protected abstract void initialise();

	protected void initialiseSquareGrid() {
		for (int y = 0; y < MAXBOARDHEIGHT; y++) {
			for (int x = 0; x < MAXBOARDWIDTH; x++) {
				world[x][y] = new Square(new Location(x, y));
			}
		}
		for (int y = 0; y < MAXBOARDHEIGHT; y++) {
			for (int x = 0; x < MAXBOARDWIDTH; x++) {
				world[x][y].initialiseNeigbours(world);
			}
		}
	}

	public Long getSeed() {
		return generator.seed;
	}

	public Rand getGenerator() {
		// TODO Auto-generated method stub
		return generator;
	}

	// called before save not efficient
	public void updateReferences() {
		enemyObjects.clear();
		chestObjects.clear();
		for (int x = 0; x < MAXBOARDWIDTH; x++) {
			for (int y = 0; y < MAXBOARDHEIGHT; y++) {
				Chest c = world[x][y].getObjectOfType(Chest.class);
				List<Enemy> es = world[x][y].getObjectsOfType(Enemy.class);
				if (!es.isEmpty()) {
					enemyObjects.addAll(world[x][y].getObjectsOfType(Enemy.class));

				}
				if (c != null) {
					chestObjects.add(c);

				}
			}
		}
	}

}
