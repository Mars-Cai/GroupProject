package WorldEditor.Spawners;

import WorldEditor.AbstractWorld;
import WorldEditor.Location;
import WorldEditor.Rand;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;

public class BossRoomSpawner implements Spawner{

	private final Location start = new Location(1, 1);
	private Room bossRoom;
	private Rand rand;
	
	
	public BossRoomSpawner(Rand rand) {
		// TODO Auto-generated constructor stub
		this.rand = rand;
	}

	@Override
	public void spawn(Room room, World world) {
		// TODO Auto-generated method stub
		if(bossRoom == null) bossRoom = room;
		Double distToRoom = start.distanceTo(room.BottomLeftLocation);
		Double distToPrevMax = start.distanceTo(bossRoom.BottomLeftLocation);

		if(distToPrevMax < distToRoom) {
			bossRoom.setBossRoom(false, world);
			bossRoom = room;
			room.setBossRoom(true, world);
			
		}
	}
	
	public Room getBossRoom() {
		return bossRoom;
	}

	

}
