package WorldEditor.Spawners;

import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;

public interface Spawner {
	public void spawn(Room room, World world);
}
