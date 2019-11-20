package WorldEditor;

import java.awt.Window.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import WorldEditor.GameObjects.Coin;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.Fuel;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.Player;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Floor;
import WorldEditor.GameObjects.InanimateObjects.Wall;
import renderer.Bitmap;

public class Square {
	public final Location location;
	private Set<GameObject> gameObjects = new HashSet<>();
	private boolean used;
	private boolean isDiscovered;
	public static final double PIXELSPERSQUARE = 16;
	public Room room;
	public Map<Direction, Square> neighbourSquares = new HashMap<>();

	public Square(Location location) {
		this.location = location;
	}

	public List<GameObject> getGameObjects() {
		return this.gameObjects.stream().collect(Collectors.toList());
	}

	public void removeGameObject(GameObject objToRemove) {
		if (gameObjects.remove(objToRemove)) {
			objToRemove.setSquare(null);
			used = !gameObjects.isEmpty();
		}

	}

	public void removeAllOfType(Class<?> type) {
		for (GameObject g : gameObjects) {
			if (type.isInstance(g)) {
				removeGameObject(g);
			}
		}
	}

	public void removeAllEnemies() {
		List<GameObject> toRemove = new ArrayList<>();
		for (GameObject g : gameObjects) {
			if (g instanceof Enemy) {
				toRemove.add(g);
			}
		}
		toRemove.forEach(o -> removeGameObject(o));
	}

	public void addInitialObject(GameObject toAdd) {
		used = true;
		if (toAdd instanceof Floor) {
			Floor f = (Floor) toAdd;
			room = f.room;
		}else
		if(toAdd instanceof Door) {
			Door f = (Door) toAdd;
			room = f.room;
		}else
		if(toAdd instanceof Wall) {
			Wall f = (Wall) toAdd;
			room = f.room;
		}

		if (!isDiscovered && toAdd instanceof Player) {
			if (room != null) {
				room.discoverRoom();
			}
		}
		gameObjects.add(toAdd);
		// Initialization
		updtaeGameObjectsSquare(toAdd);
	}
	public void addGameObject(GameObject toAdd) {
		used = true;
		if (!isDiscovered && toAdd instanceof Player) {
			if (room != null) {
				room.discoverRoom();
			}
		}
		gameObjects.add(toAdd);
		// Initialization
		updtaeGameObjectsSquare(toAdd);
	}

	private void updtaeGameObjectsSquare(GameObject obj) {
		// Remove from old square
		if (obj.getSquare() != null && !obj.getSquare().location.equals(this.location)) {
			obj.getSquare().removeGameObject(obj);
		}
		// update obj's square
		obj.setSquare(this);

		// initialse exactLoc when obj spawned
		if (obj.getExactLocation() == null) {
			obj.setExactLocation(this.location);
		}
		obj.updateHitBox();
	}

	public boolean isUsed() {
		return used;
	}

	public Square getSquare() {
		return this;
	}

	/**
	 * gets all objects on the square that are an instance of given type
	 *
	 * @param cls
	 * @return
	 */
	public <T extends GameObject> List<T> getObjectsOfType(Class<T> cls) {
		return gameObjects.stream().filter(o -> cls.isInstance(o)).map(o -> cls.cast(o)).collect(Collectors.toList());

	}

	/**
	 * returns an object of given type. If more than one object of type is present
	 * on square, returns the first one
	 *
	 * @param cls
	 * @return an object of type
	 */
	public <T extends GameObject> T getObjectOfType(Class<T> cls) {
		return gameObjects.stream().filter(o -> cls.isInstance(o)).map(o -> cls.cast(o)).findFirst().orElse(null);

	}

	/**
	 * gets all objects on the square that are on a given layer
	 *
	 * @param layer
	 * @return
	 */
	public List<GameObject> getObjectsOnLayer(Layer layer) {
		if (layer.equals(Layer.ALL))
			return getObjectsOfType(GameObject.class);
		return gameObjects.stream().filter(o -> o.getLayer().equals(layer)).collect(Collectors.toList());

	}

	/**
	 * returns the first object on this square on a layer
	 *
	 * @param layer
	 * @return
	 */
	public GameObject getObjectOnLayer(Layer layer) {
		if (layer.equals(Layer.ALL))
			return getObjectOfType(GameObject.class);
		return gameObjects.stream().filter(o -> o.getLayer().equals(layer)).findFirst().orElse(null);

	}

	@Deprecated
	public Wall getWallObject() {
		return (Wall) gameObjects.stream().filter(o -> o instanceof Wall).findFirst().orElse(null);
	}

	@Deprecated
	public Chest getChestObject() {
		return (Chest) gameObjects.stream().filter(o -> o instanceof Chest).findFirst().orElse(null);
	}

	@Deprecated
	public Door getDoorObject() {
		return (Door) gameObjects.stream().filter(o -> o instanceof Door).findFirst().orElse(null);
	}

	@Deprecated
	public List<Enemy> getEnemyObjects() {
		return gameObjects.stream().filter(o -> o instanceof Enemy).map(e -> (Enemy) e).collect(Collectors.toList());
	}

	@Deprecated
	public Fuel getFuelObject() {
		return (Fuel) gameObjects.stream().filter(o -> o instanceof Fuel).findFirst().orElse(null);
	}

	@Deprecated
	public List<Coin> getCoinObjects() {
		return gameObjects.stream().filter(o -> o instanceof Coin).map(e -> (Coin) e).collect(Collectors.toList());
	}

	@Deprecated
	public Key getKeyObject() {
		return (Key) gameObjects.stream().filter(o -> o instanceof Key).findFirst().orElse(null);
	}

	@Deprecated
	public List<Item> getConsumableLayerObjects() {
		return gameObjects.stream().filter(o -> o.getLayer().equals(Layer.CONSUMABLES)).map(e -> (Item) e)
				.collect(Collectors.toList());
	}

	@Deprecated
	public List<GameObject> getMovementBlockingLayerObjects() {
		return gameObjects.stream().filter(o -> o.getLayer().equals(Layer.MOVEMENTBLOCKING))
				.collect(Collectors.toList());
	}

	public boolean isDiscovered() {
		return isDiscovered;
	}

	@Override
	public String toString() {
		String res = "";
		if (used) {
			res += gameObjects.stream().map(o -> o.toString()).reduce((x, y) -> x += y).orElse(null);
		} else {
			res += "not dused, ";
		}
		return res + ", " + location;

	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Square) {
			Square s = (Square) o;
			return this.location.equals(s.location);
		}
		return false;
	}

	public void initialiseNeigbours(Square[][] world) {
		for (int x = (int) location.x - 1; x <= location.x + 1; x++) {
			for (int y = (int) location.y - 1; y <= location.y + 1; y++) {
				if ((x < 0 || y < 0) || (x == location.x && y == location.y)
						|| (x >= World.MAXBOARDWIDTH || y >= World.MAXBOARDHEIGHT)) {

				} else {
					neighbourSquares.put(Direction.intToDir(x - location.x, y - location.y), world[x][y]);
				}
			}
		}
	}

	public void setDiscovered(boolean b) {
		// TODO Auto-generated method stub
		isDiscovered = b;
	}

}
