package gameWorld;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_W;

import WorldEditor.Location;
import WorldEditor.GameObjects.Player;

public class PlayerMove {
	public boolean forward;
	public boolean backward;
	public boolean left;
	public boolean right;
	private boolean turnLeft;
	private boolean turnRight;
	private double x;
	private double y;
	private double xd;
	private double yd;
	public double rd;
	public double rotation;
	private Player player;
	// what are these for - MOUSE MOVEMENT BOIIIIIIIIII
	private static boolean turningLeft;
	private static boolean turningRight;

	public PlayerMove() {
	}

	public void setPlayer(Player player) {
		this.player = player;
		this.x = player.getExactLocation().x;
		this.y = player.getExactLocation().y;

	}

	/**
	 * creates a move where the player will be if move can be executed
	 *
	 * @param keys
	 * @param player
	 * @return
	 */
	public static PlayerMove newPlayerMove(boolean[] keys, Player player) {
		PlayerMove playerMove = new PlayerMove();
		playerMove.forward = keys[VK_W];
		playerMove.backward = keys[VK_S];
		playerMove.left = keys[VK_A];
		playerMove.right = keys[VK_D];
		playerMove.turnLeft = keys[VK_E];
		playerMove.turnRight = keys[VK_Q];
		playerMove.rotation = player.getPlayerMove().rotation;

		if (playerMove.turnRight)
			playerMove.rd = player.getPlayerMove().rd - 1;
		if (playerMove.turnLeft)
			playerMove.rd = player.getPlayerMove().rd + 1;
		playerMove.rotation += playerMove.rd * player.statemanager.getRunSpeed();
		return playerMove;
	}

	public static PlayerMove newRotationOnly(boolean[] keys, Player player) {
		PlayerMove playerMove = new PlayerMove();

		playerMove.turnLeft = keys[VK_E];
		playerMove.turnRight = keys[VK_Q];
		if (playerMove.turnRight)
			playerMove.rd = player.getPlayerMove().rd - 1;
		if (playerMove.turnLeft)
			playerMove.rd = player.getPlayerMove().rd + 1;
		playerMove.rotation = player.getPlayerMove().rotation;
		playerMove.rotation +=  playerMove.rd * player.statemanager.getRunSpeed() ;
		return playerMove;
	}

	public Location execute(PlayerMove newMove) {
		double wSpeed = player.statemanager.getRunSpeed();
		double rSpeed = player.statemanager.getRunSpeed();
		x = player.getExactLocation().x;
		y = player.getExactLocation().y;

		if (newMove.forward)
			yd++;
		if (newMove.backward)
			yd--;
		if (newMove.left)
			xd--;
		if (newMove.right)
			xd++;
		if (newMove.turnRight)
			rd++;
		if (newMove.turnLeft)
			rd--;

		double rCos = Math.cos(rotation);
		double rSin = Math.sin(rotation);

		x += (xd * rCos + yd * -rSin) * wSpeed;
		y += (xd * rSin + yd * rCos) * wSpeed;

		rotation += rd * rSpeed;
		if (x < 0 && y < 0) {
			return new Location(0.21, 0.21);
		} else {
			if (x < 0) {
				return new Location(0.21, y);
			}
			if (y < 0) {
				return new Location(x, 0.21);
			}
		}
		xd *= 0.6;
		yd *= 0.6;
		rd *= 0.6;

		rotation = rotation % Math.toRadians(360);
		return new Location(x, y);
	}

}
