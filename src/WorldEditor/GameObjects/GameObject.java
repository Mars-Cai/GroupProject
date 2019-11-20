package WorldEditor.GameObjects;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.ImageIcon;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import WorldEditor.World;
import gameWorld.AstarIteritive;
import gameWorld.AstarPathFindingAlorithm;
import renderer.Bitmap;

public abstract class GameObject {

	protected Bitmap bitmapImage;
	protected ImageIcon imageIcon;
	protected Square square;
	private boolean destroyed;
	protected Layer layer;
	protected double hitBoxRadius;
	protected Location exactLocation;
	protected Rectangle2D.Double hitBox;
	protected boolean hitBoxActive = true;

	public static double MAXIMUMHITBOXRADIUS = 0.3;

	public GameObject() {
		setLayer();
	}

	public Location getExactLocation() {
		return exactLocation;
	}

	/**
	 * updates the non-int-casted location of this object, Do not use this to move
	 * an object instead use method moveTo!
	 *
	 * @param exactLocation
	 */
	public void setExactLocation(Location exactLocation) {
		this.exactLocation = exactLocation;
	}

	/**
	 * Use this to move and object to a new position, it will also update the square
	 *
	 * @param exactLocation
	 * 			@param(world[(int)exactLocation.x][(int)exactLaction.y]) ie new
	 *            square loc
	 */
	public void moveTo(Location newLocation, Square newSquare) {
		this.exactLocation = newLocation;
		newSquare.addGameObject(this);
	}

	public Bitmap getBitmapImage() {
		return bitmapImage;
	}

	public void setBitmapImage(Bitmap image) {
		this.bitmapImage = image;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public Square getSquare() {
		return square;
	}

	public void setSquare(Square squre) {
		this.square = squre;
	}

	public Location getSquareLocation() {
		// TODO Auto-generated method stub
		return square.location;
	}

	protected abstract void setHitBoxAndRadius();

	public double getHitBoxRadius() {
		return hitBoxRadius;
	}

	protected abstract void setLayer();

	public Layer getLayer() {
		return layer;
	}

	protected void destroy() {
		if (square != null) {
			this.square.removeGameObject(this);
		}
		this.destroyed = true;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public String toString() {
		return "O";

	}

	public List<Location> generateAstarPathToLocation(Location end, Square[][] world) {
		setHitBoxActive(false);
		// AstarPathFindingAlorithm astar = new AstarPathFindingAlorithm(exactLocation,
		// end, world);
		AstarIteritive astar = new AstarIteritive(exactLocation, end, world);
		setHitBoxActive(true);
		return astar.getPath();
	}

	public boolean hitBoxContains(Location location) {
		if (!hitBoxActive)
			return false;
		if (hitBox != null) {
			return hitBox.contains(location.x, location.y);
		} else {
			return Math.abs(location.distanceTo(exactLocation)) < hitBoxRadius;
		}
	}

	public Rectangle2D.Double getHitBox() {
		return hitBox;
	}

	public void setHitBoxActive(boolean isActive) {
		this.hitBoxActive = isActive;
	}

	public boolean isHitBoxActive() {
		return this.hitBoxActive;
	}

	public void updateHitBox() {
		setHitBoxAndRadius();
	}

}
