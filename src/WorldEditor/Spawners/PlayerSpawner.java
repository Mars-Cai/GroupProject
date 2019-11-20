package WorldEditor.Spawners;

import WorldEditor.Room;
import WorldEditor.World;
import WorldEditor.GameObjects.Player;
import renderer.Bitmap;

public class PlayerSpawner implements Spawner{
	
	private Bitmap image;
	public PlayerSpawner() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void spawn(Room room, World world) {
		// TODO Auto-generated method stub
		world.player = new Player();
		world.world[2][2].addGameObject(world.player);
	}
	

}
