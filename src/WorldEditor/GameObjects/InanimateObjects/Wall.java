package WorldEditor.GameObjects.InanimateObjects;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import WorldEditor.AbstractWorld.Axis;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Room;
import renderer.Bitmap;

public class Wall extends GameObject {

	private final Location to;
	private Axis xy;
	public final Room room;
	public final static double HITBOXRADIUS = 0.2;

	public Wall(Location to, Axis xy,Room room) {

		// TODO Auto-generated constructor stub
		this.to = to;
		this.xy = xy;
		this.room = room;
		
	}

	@Override
	public String toString() {
		return "W";

	}

	public Location getLocation2() {
		return to;
	}

	public Axis getXY() {
		return xy;
	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.MOVEMENTBLOCKING;

	}

	@Override
	protected void setHitBoxAndRadius() {
		hitBoxRadius = HITBOXRADIUS;
		if (xy.equals(Axis.X)) {
			hitBox = new Rectangle2D.Double(exactLocation.x, exactLocation.y, to.x - exactLocation.x, hitBoxRadius);

		} else {
			hitBox = new Rectangle2D.Double(exactLocation.x, exactLocation.y, hitBoxRadius, to.y - exactLocation.y);
		}
	}

}
