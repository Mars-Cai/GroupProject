package WorldEditor.GameObjects.InanimateObjects;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import WorldEditor.AbstractWorld.Axis;
import WorldEditor.Direction;
import WorldEditor.GameObjects.InteractableObject;
import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Room;
import renderer.Bitmap;

public class Door extends InteractableObject {

	private Location to;
	private Location openTo;
	private Axis xy;
	private boolean isOpen;
	private Direction facingDir;
	public final Room room;

	public Door(Location to, Axis xy, Direction facingDir, Room room) {
		// TODO Auto-generated constructor stub
		this.to = to;
		this.xy = xy;
		//isOpen = true;
		this.facingDir = facingDir;
		this.room = room;

	}

	@Override
	public String toString() {
		return "D";
	}
	public Location getTo() {

		return to;
	}

	public void setExactLocation(Location loc) {
		exactLocation = loc;
		openTo = Location.moveInDirection(exactLocation, facingDir, 1);
	}
	public Axis getXY() {
		return xy;
	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.MOVEMENTBLOCKING;
	}

	private void swapTos() {
		Location temp = to;
		to = openTo;
		openTo = temp;
	}
	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = 0.1;
		if(isOpen) {
			hitBoxActive = false;
		}
		if (xy.equals(Axis.X)) {
			hitBox = new Rectangle2D.Double(exactLocation.x, exactLocation.y, to.x - exactLocation.x, hitBoxRadius);

		} else {
			hitBox = new Rectangle2D.Double(exactLocation.x, exactLocation.y, hitBoxRadius, to.y - exactLocation.y);
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = true;
		updateHitBox();
	}


}
