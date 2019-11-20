package renderer;

import WorldEditor.Square;
import WorldEditor.World;

import java.util.ArrayList;
import java.util.List;

import WorldEditor.AbstractWorld.Axis;
import WorldEditor.GameObjects.Boss;
import WorldEditor.GameObjects.Coin;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.Fuel;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.HealthPotion;
import WorldEditor.GameObjects.Player;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Wall;
import WorldEditor.GameObjects.WeaponObjects.Sword;

public class Bitmap3D extends Bitmap {

	private double xCam;
	private double yCam;
	private double zCam;
	private double fov;
	private double rotation;
	private double rSin;
	private double rCos;
	private double[] depthBuffer;
	private double[] wallDepthBuffer;
	private Game game;

	private List<Enemy> enemies = new ArrayList<>(20);
	private List<Chest> chests = new ArrayList<>(15);
	private List<Fuel> fuel = new ArrayList<>(15);
	private List<HealthPotion> healthPotions = new ArrayList<>(15);
	private List<Sword> swords = new ArrayList<>(15);
	private List<Coin> coins = new ArrayList<>(15);

	public Bitmap3D(int width, int height) {
		super(width, height);

		depthBuffer = new double[width * height];
		wallDepthBuffer = new double[width];
	}

	public void render(Game g) {
		this.game = g;

		for (int i = 0; i < width; i++)
			wallDepthBuffer[i] = 0;

		// implement setting slider
		fov = 200;

		// xyz of Player
		xCam = g.player.getExactLocation().x;
		yCam = g.player.getExactLocation().y;
		zCam = 3;

		// Players rotation
		rotation = g.player.getRotation();

		rSin = Math.sin(rotation);
		rCos = Math.cos(rotation);

		renderFloorAndCeiling(8, 32);
		renderFromWorld(g.world);

	}

	public void renderFloorAndCeiling(double floorPos, double ceilingPos) {
		for (int y = 0; y < height; y++) {

			double yd = ((y + 0.5) - (height / 2)) / fov;
			double zd = (floorPos + zCam) / yd;
			if (yd < 0)
				zd = (ceilingPos - zCam) / -yd;

			for (int x = 0; x < width; x++) {
				double xd = (x - (width / 2)) / fov;
				xd *= zd;

				double xx = xd * rCos - zd * rSin + (xCam + 0.5) * 16;
				double yy = xd * rSin + zd * rCos + (yCam) * 16;

				int xPixel = (int) xx;
				int yPixel = (int) yy;

				if (xx < 0)
					xPixel--;

				if (yy < 0)
					yPixel--;

				depthBuffer[x + y * width] = zd;

				if (yd >= 0)
					pixels[x + y * width] = Core.textures.pixels[(xPixel & 15) | (yPixel & 15) * Core.textures.width]; // Apply
																														// floor
																														// textures
				else
					pixels[x + y * width] = Core.textures.pixels[(xPixel & 15) + 16
							| (yPixel & 15) * Core.textures.width]; // Apply
				// ceiling
				// textures
				// pixels[x + y * width] = ((yPix & 15) * 16) << 8 | ((xPix & 15) * 16);
			}
		}
	}

	public void renderSprite(double x, double y, double z, int xTexPos, int yTexPos, int size) {

		double xo = x - xCam;
		double yo = y + zCam / 16;
		double zo = z - yCam;

		double xx = xo * rCos + zo * rSin;
		double yy = yo;
		double zz = -xo * rSin + zo * rCos;

		if (zz < 0.1)
			return;

		double xPixel = xx / zz * fov + 320.0;
		double yPixel = yy / zz * fov + 240.0;

		double xPixelL = xPixel - size / zz;
		double xPixelR = xPixel + size / zz;

		double yPixelL = yPixel - size / zz;
		double yPixelR = yPixel + size / zz;

		int xp0 = (int) xPixelL;
		int xp1 = (int) xPixelR;
		int yp0 = (int) yPixelL;
		int yp1 = (int) yPixelR;

		if (xp0 < 0) {
			xp0 = 0;
		}
		if (xp1 > width) {
			xp1 = width;
		}
		if (yp0 < 0) {
			yp0 = 0;
		}
		if (yp1 > height) {
			yp1 = height;
		}

		zz *= 12;
		for (int yp = yp0; yp < yp1; yp++) {
			double py = (yp - yPixelL) / (yPixelR - yPixelL);
			int yt = (int) (py * 16);

			for (int xp = xp0; xp < xp1; xp++) {

				double px = (xp - xPixelL) / (xPixelR - xPixelL);
				int xt = (int) (px * 16);

				int texture = Core.textures.pixels[(xt & 15) + xTexPos + (((yt & 15) + yTexPos) * Core.textures.width)];

				if (texture != 0xffffff) {
					if (depthBuffer[xp + yp * width] > zz) {
						pixels[xp + yp * width] = texture;
						depthBuffer[xp + yp * width] = zz;
					}
				}
			}
		}
	}

	public void renderFromWorld(Square[][] world) {
		clearlists();
		for (int x = 0; x < World.MAXBOARDWIDTH; x++) {
			for (int y = 0; y < World.MAXBOARDHEIGHT; y++) {
				if (!world[x][y].isUsed())
					continue;
				List<Wall> maybeWall = world[x][y].getObjectsOfType(Wall.class);
				for (Wall wall : maybeWall) {
					if (wall.getXY().equals(Axis.X)) {
						renderWalls(wall.getExactLocation().x, wall.getExactLocation().y, wall.getLocation2().x,
								wall.getLocation2().y, 1.5, 0, 48);
						renderWalls(wall.getLocation2().x, wall.getExactLocation().y, wall.getExactLocation().x,
								wall.getLocation2().y, 1.5, 0, 48);
					} else {
						renderWalls(wall.getExactLocation().x, wall.getExactLocation().y, wall.getLocation2().x,
								wall.getLocation2().y, 1.5, 0, 48);
						renderWalls(wall.getExactLocation().x, wall.getLocation2().y, wall.getLocation2().x,
								wall.getExactLocation().y, 1.5, 0, 48);
					}

				}
				GameObject maybeDoor = world[x][y].getObjectOfType(Door.class);
				if (maybeDoor instanceof Door) {
					Door door = (Door) maybeDoor;
					if (door.getXY().equals(Axis.X)) {
						if (!door.isOpen()) {
							renderDoor(door.getExactLocation().x, door.getExactLocation().y + 0.001, door.getTo().x,
									door.getTo().y + 0.001, 0.5, 1, 0.5, 64, 0);
							renderDoor(door.getExactLocation().x, door.getExactLocation().y + 0.001, door.getTo().x,
									door.getTo().y + 0.001, 0, 0.5, 0.5, 80, 0);
							renderDoor(door.getExactLocation().x, door.getExactLocation().y + 0.001, door.getTo().x,
									door.getTo().y + 0.001, 0.5, 1, 0, 64, 16);
							renderDoor(door.getExactLocation().x, door.getExactLocation().y + 0.001, door.getTo().x,
									door.getTo().y + 0.001, 0, 0.5, 0, 80, 16);
							renderWalls(door.getExactLocation().x, door.getExactLocation().y, door.getTo().x,
									door.getTo().y, 1.5, 1, 28.8);
						}
						else {
							renderDoor(door.getExactLocation().x, door.getExactLocation().y + 0.001, door.getTo().x - 0.5,
									door.getTo().y + 0.001 + 0.5, 0.5, 1, 0.5, 64, 0);
							renderDoor(door.getExactLocation().x + 0.5, door.getExactLocation().y + 0.001 + 0.5, door.getTo().x,
									door.getTo().y + 0.001, 0, 0.5, 0.5, 80, 0);
							renderDoor(door.getExactLocation().x, door.getExactLocation().y + 0.001, door.getTo().x - 0.5,
									door.getTo().y + 0.001 + 0.5, 0.5, 1, 0, 64, 16);
							renderDoor(door.getExactLocation().x + 0.5, door.getExactLocation().y + 0.001 + 0.5, door.getTo().x,
									door.getTo().y + 0.001, 0, 0.5, 0, 80, 16);
							renderWalls(door.getExactLocation().x, door.getExactLocation().y, door.getTo().x,
									door.getTo().y, 1.5, 1, 28.8);
						}
					} /*
						 * else { renderDoor(door.getExactLocation().x, door.getExactLocation().y + 1,
						 * door.getTo().x + 0.5, door.getTo().y + 1.5, 0, 0.5d, 0.5, 0, 0, 64);
						 * renderDoor(door.getExactLocation().x, door.getExactLocation().y + 1,
						 * door.getTo().x + 0.5, door.getTo().y + 1.5, 16, 0, 0, 0, 0, 64);
						 * renderDoor(door.getExactLocation().x, door.getExactLocation().y + 0.5,
						 * door.getTo().x + 0.5, door.getTo().y + 1, 0, 0.5, 0.5, 0, 0, 80);
						 * renderDoor(door.getExactLocation().x, door.getExactLocation().y + 0.5,
						 * door.getTo().x + 0.5, door.getTo().y + 1, 16, 0, 0, 0, 0, 80);
						 * renderWalls(door.getExactLocation().x - 0.001, door.getExactLocation().y + 1,
						 * door.getTo().x - 0.001, door.getTo().y + 1, 1.5, 1, 32);
						 * renderDoor(door.getExactLocation().x, door.getTo().y + 1, door.getTo().x +
						 * 0.5, door.getExactLocation().y + 0.5, 0, 0.5, 0.5, 0, 0, 64);
						 * renderDoor(door.getExactLocation().x, door.getTo().y + 1, door.getTo().x +
						 * 0.5, door.getExactLocation().y + 0.5, 16, 0, 0, 0, 0, 64);
						 * renderDoor(door.getExactLocation().x, door.getTo().y + 1.5, door.getTo().x +
						 * 0.5, door.getTo().y + 2, 0, 0.5, 0.5, 0, 0, 80);
						 * renderDoor(door.getExactLocation().x, door.getTo().y + 1.5, door.getTo().x +
						 * 0.5, door.getTo().y + 2, 16, 0, 0, 0, 0, 80);
						 * renderWalls(door.getExactLocation().x + 0.001, door.getTo().y + 1,
						 * door.getTo().x + 0.001, door.getExactLocation().y + 1, 1.5, 1, 32); }
						 */
				}

				enemies.addAll(world[x][y].getObjectsOfType(Enemy.class));
				chests.addAll(world[x][y].getObjectsOfType(Chest.class));
				fuel.addAll(world[x][y].getObjectsOfType(Fuel.class));
				healthPotions.addAll(world[x][y].getObjectsOfType(HealthPotion.class));
				swords.addAll(world[x][y].getObjectsOfType(Sword.class));
				coins.addAll(world[x][y].getObjectsOfType(Coin.class));

			}
		}
		renderEverythingElse();
	
	}

	private void clearlists() {
		// TODO Auto-generated method stub
		enemies.clear();
		chests.clear();
		fuel.clear();
		healthPotions.clear();
		swords.clear();
		coins.clear();
	}

	public void renderDoor(double x0, double y0, double x1, double y1, double leftPos, double rightPos, double yPos,
			int xTexPos, int yTexPos) {

		double leftSideX = x0 - xCam - leftPos;
		double topLeftY = -yPos + zCam / 16;
		double bottemLeftY = +0.5 + zCam / 16 - yPos;
		double leftSideDepth = y0 - yCam;

		double leftSideXRot = leftSideX * rCos + leftSideDepth * rSin;
		double leftSideDepthRot = -leftSideX * rSin + leftSideDepth * rCos;

		double rightSideX = x1 - xCam - rightPos;
		double topRightY = -yPos + zCam / 16;
		double bottemRightY = +0.5 + zCam / 16 - yPos;
		double rightSideDepth = y1 - yCam;

		double rightSideXRot = rightSideX * rCos + rightSideDepth * rSin;
		double rightSideDepthRot = -rightSideX * rSin + rightSideDepth * rCos;

		// Cohen�Sutherland algorithm
		double t0 = 0;
		double t1 = 16;

		double clip = 0.1;

		if (leftSideDepthRot < clip) {
			double p = (clip - leftSideDepthRot) / (rightSideDepthRot - leftSideDepthRot);
			leftSideDepthRot = leftSideDepthRot + (rightSideDepthRot - leftSideDepthRot) * p;
			leftSideXRot = leftSideXRot + (rightSideXRot - leftSideXRot) * p;
			t0 = t0 + (t1 - t0) * p;
		}

		if (rightSideDepthRot < clip) {
			double p = (clip - rightSideDepthRot) / (rightSideDepthRot - leftSideDepthRot);
			rightSideDepthRot = rightSideDepthRot + (rightSideDepthRot - leftSideDepthRot) * p;
			rightSideXRot = rightSideXRot + (rightSideXRot - leftSideXRot) * p;
			t1 = t1 + (t1 - t0) * p;
		}

		double xPixel0 = leftSideXRot / leftSideDepthRot * fov + width / 2.0;
		double xPixel1 = rightSideXRot / rightSideDepthRot * fov + width / 2.0;

		if (xPixel0 > xPixel1)
			return;

		int xp0 = (int) Math.ceil(xPixel0);
		int xp1 = (int) Math.ceil(xPixel1);
		if (xp0 < 0)
			xp0 = 0;

		if (xp1 > width)
			xp1 = width;

		double yPixel00 = (topLeftY / leftSideDepthRot * fov + height / 2.0) + 0.5;
		double yPixel10 = (topRightY / rightSideDepthRot * fov + height / 2.0) + 0.5;
		double yPixel01 = (bottemLeftY / leftSideDepthRot * fov + height / 2.0) + 0.5;
		double yPixel11 = (bottemRightY / rightSideDepthRot * fov + height / 2.0) + 0.5;

		double iz0 = 1 / leftSideDepthRot;
		double iz1 = 1 / rightSideDepthRot;

		double xt0 = t0 * iz0;
		double xt1 = t1 * iz1;

		for (int x = xp0; x < xp1; x++) {

			double p = (x - xPixel0) / (xPixel1 - xPixel0);

			double yPixel0 = yPixel00 + (yPixel10 - yPixel00) * p;
			double yPixel1 = yPixel01 + (yPixel11 - yPixel01) * p;

			double iz = iz0 + (iz1 - iz0) * p;
			if (wallDepthBuffer[x] > iz)
				continue;

			wallDepthBuffer[x] = iz;

			int xTex = (int) ((xt0 + (xt1 - xt0) * p) / iz);

			if (yPixel0 > yPixel1)
				return;

			int yp0 = (int) yPixel0;
			int yp1 = (int) yPixel1;
			if (yp0 < 0)
				yp0 = 0;

			if (yp1 > height)
				yp1 = height;

			for (int y = yp0; y < yp1; y++) {
				double py = (y - yPixel0) / (yPixel1 - yPixel0);
				int yTex = (int) (py * 16);

				int texture = Core.textures.pixels[(xTex & 15) + xTexPos
						+ (((yTex & 15) + yTexPos) * Core.textures.width)];

				// Enable alpha in door texture
				if (texture != 0xffffff) {
					pixels[x + y * width] = texture;
					depthBuffer[x + y * width] = 15 / iz;
				}
			}
		}
	}

	/**
	 * Renders walls with the use of Linear interpolation alogrithm.
	 *
	 * @param x0
	 *            x position for start of wall
	 * @param y0
	 *            y position for start of wall
	 * @param x1
	 *            x position for wall end/
	 * @param y1
	 *            y position for wall end
	 */
	public void renderWalls(double x0, double y0, double x1, double y1, double top, double bottom, double size) {

		double xo0 = x0 - 0.5 - xCam;
		double up0 = -0.5 + zCam / 16 - top;
		double down0 = +0.5 + zCam / 16 - bottom;
		double zo0 = y0 - yCam;

		double xx0 = xo0 * rCos + zo0 * rSin;
		double zz0 = -xo0 * rSin + zo0 * rCos;

		double xo1 = x1 - 0.5 - xCam;
		double up1 = -0.5 + zCam / 16 - top;
		double down1 = +0.5 + zCam / 16 - bottom;
		double zo1 = y1 - yCam;

		double xx1 = xo1 * rCos + zo1 * rSin;
		double zz1 = -xo1 * rSin + zo1 * rCos;

		// Cohen�Sutherland algorithm
		double t0 = 0;
		double t1 = 16;

		double clip = 0.1;

		if (zz0 < clip) {
			double p = (clip - zz0) / (zz1 - zz0);
			zz0 = zz0 + (zz1 - zz0) * p;
			xx0 = xx0 + (xx1 - xx0) * p;
			t0 = t0 + (t1 - t0) * p;
		}

		if (zz1 < clip) {
			double p = (clip - zz1) / (zz1 - zz0);
			zz1 = zz1 + (zz1 - zz0) * p;
			xx1 = xx1 + (xx1 - xx0) * p;
			t1 = t1 + (t1 - t0) * p;
		}

		double xPixel0 = xx0 / zz0 * fov + width / 2.0;
		double xPixel1 = xx1 / zz1 * fov + width / 2.0;

		if (xPixel0 > xPixel1)
			return;

		int xp0 = (int) Math.ceil(xPixel0);
		int xp1 = (int) Math.ceil(xPixel1);
		if (xp0 < 0)
			xp0 = 0;

		if (xp1 > width)
			xp1 = width;

		double yPixel00 = (up0 / zz0 * fov + height / 2.0) + 0.5;
		double yPixel10 = (up1 / zz1 * fov + height / 2.0) + 0.5;
		double yPixel01 = (down0 / zz0 * fov + height / 2.0) + 0.5;
		double yPixel11 = (down1 / zz1 * fov + height / 2.0) + 0.5;

		double iz0 = 1 / zz0;
		double iz1 = 1 / zz1;

		double xt0 = t0 * iz0;
		double xt1 = t1 * iz1;

		for (int x = xp0; x < xp1; x++) {

			double p = (x - xPixel0) / (xPixel1 - xPixel0);

			double yPixel0 = yPixel00 + (yPixel10 - yPixel00) * p;
			double yPixel1 = yPixel01 + (yPixel11 - yPixel01) * p;

			double iz = iz0 + (iz1 - iz0) * p;
			if (wallDepthBuffer[x] > iz)
				continue;

			wallDepthBuffer[x] = iz;

			int xTex = (int) ((xt0 + (xt1 - xt0) * p) / iz);

			if (yPixel0 > yPixel1)
				return;

			int yp0 = (int) yPixel0;
			int yp1 = (int) yPixel1;
			if (yp0 < 0)
				yp0 = 0;

			if (yp1 > height)
				yp1 = height;

			for (int y = yp0; y < yp1; y++) {
				double py = (y - yPixel0) / (yPixel1 - yPixel0);
				int yTex = (int) (py * size);

				depthBuffer[x + y * width] = 15 / iz;
				pixels[x + y * width] = Core.textures.pixels[(xTex & 15) + 16 + ((yTex & 15) * Core.textures.width)];
			}
		}
	}

	public void renderEverythingElse() {
		for (Enemy e : enemies) {
			if(e instanceof Boss) {
				renderSprite(e.getExactLocation().x, 0, e.getExactLocation().y, 96, 0, 60);
			}
			else {
				renderSprite(e.getExactLocation().x, 0, e.getExactLocation().y, 112, 0, 60);
			}
		}
		for(Sword s : swords) {
			renderSprite(s.getExactLocation().x, 0.3, s.getExactLocation().y, 32, 16, 60);
		}
		for (Chest c : chests) {
			if(c.isOpen()) {
				renderSprite(c.getExactLocation().x, 0, c.getExactLocation().y, 16, 16, 100);
			}
			else {
				renderSprite(c.getExactLocation().x, 0, c.getExactLocation().y, 0, 16, 100);
			}
		}
		for (Fuel f : fuel) {
			renderSprite(f.getExactLocation().x, 0.3, f.getExactLocation().y, 112, 16, 30);
		}
		for(HealthPotion hp: healthPotions) {
			renderSprite(hp.getExactLocation().x, 0.3, hp.getExactLocation().y, 32, 0, 40);
		}
		for(Coin c : coins) {
			renderSprite(c.getExactLocation().x, 0.3, c.getExactLocation().y, 0, 32, 40);
		}



	}

	public void renderFog() {
		for (int i = 0; i < depthBuffer.length; i++) {
			int color = pixels[i];

			/*
			 * 0xff corresponding to white. Using bitwise operators ">>" and "&" enabling us
			 * to only keep the values of each component of color - r,g,b
			 */
			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;

			double brightness = game.player.fuelGage.getCurrent() / (depthBuffer[i] * depthBuffer[i]); // change for distance
			if (brightness < 0)
				brightness = 0;

			if (brightness > 255)
				brightness = 255;

			r = (int) (r / 255.0 * brightness);
			g = (int) (g / 255.0 * brightness);
			b = (int) (b / 255.0 * brightness);

			pixels[i] = r << 16 | g << 8 | b;
		}
	}

}
