package renderer;

import WorldEditor.Square;
import WorldEditor.World;
import static java.awt.event.KeyEvent.*;
import WorldEditor.GameObjects.Player;

public class Game {

	public Player player;
	public int time;
	public Square[][] world;

	public Game(World world) {

		player = world.player;

		this.world = world.world;
	}

	public void tick(boolean[] keys) {
		this.time++;

		boolean forward = keys[VK_W];
		boolean backward = keys[VK_S];
		boolean left = keys[VK_A];
		boolean right = keys[VK_D];
		boolean turnRight = keys[VK_Q];
		boolean turnLeft = keys[VK_E];


		//player.tick(forward, backward, left, right, turnLeft, turnRight);
	}
}
