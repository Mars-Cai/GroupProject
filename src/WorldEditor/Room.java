
package WorldEditor;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Floor;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import WorldEditor.GameObjects.InanimateObjects.Wall;

public class Room implements Serializable {

	public final Location BottomLeftLocation;
	public final Location BottomRightLocation;
	public final Location TopRightLocation;
	public final Location TopLeftLocation;

	public Map<Direction, RoomConnection> connections = new HashMap<>();

	private List<GameObject> objectsInRoom = new ArrayList<>();
	private List<Enemy> enemiesInRoom = new ArrayList<>();
	private List<Chest> chestsInRoom = new ArrayList<>();

	public List<Wall> verticalWallsInRoom = new ArrayList<>();
	public List<Wall> horizontalWallsInRoom = new ArrayList<>();
	public List<Floor> floorsInRoom = new ArrayList<>();
	public List<Door> doorsInRoom = new ArrayList<>();
	private boolean isBossRoom;
	private Vendor vendor;
	private boolean isVendorRoom;
	public List<Location> wallsToRemove = new ArrayList<>();

	public Room(Location bottomLeftLocation, Location topRightLocation) {
		this.BottomLeftLocation = bottomLeftLocation;
		this.TopRightLocation = topRightLocation;
		BottomRightLocation = new Location(topRightLocation.x, bottomLeftLocation.y);
		TopLeftLocation = new Location(bottomLeftLocation.x, topRightLocation.y);
	}

	public Boolean doesRoomIntersect(Room newRoom) {
		return (BottomLeftLocation.x < newRoom.TopRightLocation.x+1 && TopRightLocation.x+1 > newRoom.BottomLeftLocation.x
				&& BottomLeftLocation.y < newRoom.TopRightLocation.y+1 && TopRightLocation.y+1 > newRoom.BottomLeftLocation.y);

	}

	public void removeWall(Wall wall) {
		verticalWallsInRoom.remove(wall);
		horizontalWallsInRoom.remove(wall);
	}

	public void setBossRoom(boolean b, World world) {
		// TODO Auto-generated method stub
		world.floorObjects.removeAll(floorsInRoom);

		floorsInRoom.forEach(f -> {
			f.setInBossRoom(b);
		});
		world.floorObjects.addAll(floorsInRoom);

		this.isBossRoom = b;
	}

	public boolean isBossRoom() {
		return isBossRoom;
	}

	public boolean hasChest() {
		// TODO Auto-generated method stub
		return !chestsInRoom.isEmpty();
	}

	public void removeAllEnemies(World world) {
		enemiesInRoom.forEach(e -> e.getSquare().removeAllEnemies());
		world.enemyObjects.removeAll(enemiesInRoom);
		enemiesInRoom.clear();
	}

	public void addVendor(Vendor vendor) {
		// TODO Auto-generated method stub
		this.vendor = vendor;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void addObjectToRoom(GameObject o) {
		if (o instanceof Enemy) {
			enemiesInRoom.add((Enemy) o);
		}
		if (o instanceof Chest) {
			chestsInRoom.add((Chest) o);
		}
		if (o instanceof Vendor) {
			vendor = (Vendor) o;
		}

		objectsInRoom.add(o);
	}

	public List<Enemy> getEnemiesInRoom() {
		return enemiesInRoom;
	}

	public void setEnemiesInRoom(List<Enemy> enemiesInRoom) {
		this.enemiesInRoom = enemiesInRoom;
	}

	public List<Chest> getChestsInRoom() {
		return chestsInRoom;
	}

	public void setChestsInRoom(List<Chest> chestsInRoom) {
		this.chestsInRoom = chestsInRoom;
	}

	public void setVendorRoom(boolean b) {
		// TODO Auto-generated method stub
		this.isVendorRoom = b;
	}

	public List<GameObject> getObjectsInRoom() {
		// TODO Auto-generated method stub
		return objectsInRoom;
	}

	public void discoverRoom() {
		// TODO Auto-generated method stub
		verticalWallsInRoom.forEach(f->f.getSquare().setDiscovered(true));
		horizontalWallsInRoom.forEach(f->f.getSquare().setDiscovered(true));
		doorsInRoom.forEach(f->f.getSquare().setDiscovered(true));
		floorsInRoom.forEach(f->f.getSquare().setDiscovered(true));
	}

}
