package WorldEditor.Spawners;

import WorldEditor.Room;
import WorldEditor.World;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.InanimateObjects.Chest;

public class KeySpawner implements Spawner{
	
	@Override
	public void spawn(Room room, World world) {
		// TODO Auto-generated method stub
		
	}
	
	public static Key generateKey() {
		return new Key(System.currentTimeMillis());
	}

}
