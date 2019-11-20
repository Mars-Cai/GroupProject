package WorldEditor.Spawners;

import WorldEditor.Location;
import WorldEditor.Rand;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import renderer.Bitmap;

public class VendorSpawner implements Spawner {
	Bitmap[] sprites;
	private final Location start = new Location(1, 1);
	private Rand rand;
	private Room vendorRoom;
	private Room maxRoom;

	public VendorSpawner(Rand rand) {
		this.rand = rand;
	}

	public VendorSpawner() {
	}

	@Override
	public void spawn(Room room, World world) {

		if (vendorRoom == null)
			vendorRoom = room;
		if(maxRoom == null)
			maxRoom = room;
		maxRoom(room);
		middleRoom(room);
	}

	private void maxRoom(Room room) {
		double distToRoom = start.distanceTo(room.BottomLeftLocation);
		double distToMax = start.distanceTo(maxRoom.BottomLeftLocation);
		if (distToRoom > distToMax) {
			maxRoom = room;
		}
	}

	private void middleRoom(Room room) {
		double startToRoom = start.distanceTo(room.BottomLeftLocation);
		double roomToMax = room.BottomLeftLocation.distanceTo(maxRoom.BottomLeftLocation);

		double startToMiddle = start.distanceTo(vendorRoom.BottomLeftLocation);
		double middleToMax = vendorRoom.BottomLeftLocation.distanceTo(maxRoom.BottomLeftLocation);

		if(Math.abs(startToRoom - roomToMax) < Math.abs(startToMiddle - middleToMax)) {
			vendorRoom.setVendorRoom(false);
			vendorRoom = room;
			vendorRoom.setVendorRoom(true);
		}
	}

	public void spawnVendor(World world, Key key) {
		vendorRoom.removeAllEnemies(world);
		Location spawnLoc = placeVendorInCentre(vendorRoom);
		createVendor(spawnLoc, vendorRoom, world, key);
	}

	private Location placeVendorInCentre(Room room) {
		int x = (int) ((room.BottomLeftLocation.x + room.BottomRightLocation.x) / 2);
		int y = (int) ((room.BottomLeftLocation.y + room.TopLeftLocation.y) / 2);
		return new Location(x, y);
	}

	private void createVendor(Location loc, Room room, World world, Key key) {
		Vendor vendor = new Vendor(key,world.player);
		world.world[(int) loc.x][(int) loc.y].addGameObject(vendor);
		room.addVendor(vendor);
		world.vendorObjects.add(vendor);
	}

	// from save
	public Vendor createVendor(Location loc, World world, Key key) {
		Vendor vendor = new Vendor(key,world.player);
		world.world[(int) loc.x][(int) loc.y].addGameObject(vendor);
		world.vendorObjects.add(vendor);
		return vendor;
	}

}
