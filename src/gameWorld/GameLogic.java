package gameWorld;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_W;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import WorldEditor.*;
import WorldEditor.GameObjects.Boss;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.Player;
import renderer.Core;
import renderer.Input;

public class GameLogic {

	// The Board used to represent this Game .
	private World world;
	// Check the game whether is over or not
	private boolean gameOver = false;
	private Player player;
	int count = 0,count2=0;

	public GameLogic() {
		this.setGameOver(false);
	}

	public void gameLogicTick(Input input, World world) {
		this.world = world;
		player = world.player;
		if (isGameOver()||!player.statemanager.isAlive()) {
			if(!player.statemanager.isAlive()) {
				if (count == 0) {
					GUI.Frame.isDead = true;
					count++;
				}
			}else {
				if (count2 == 0) {
					GUI.Frame.passed = true;
					count2++;
				}
			}
			return;
		}

		player.playerTick(world, input);
		enemiesTurn();

	}

	private void enemiesTurn() {
		world.enemyObjects.stream().forEach((e -> {
			if (!e.isDestroyed()) {
				e.tick(world);
			} else {
				if (e instanceof Boss) {
					setGameOver(true);
				}
			}
		}));
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

}
