package WorldEditor.GameObjects.InanimateObjects;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import WorldEditor.GameObjects.InteractableObject;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Key;
import WorldEditor.Direction;
import WorldEditor.Layer;
import WorldEditor.Location;
import renderer.Bitmap;

public class Chest extends InteractableObject {

	private Item containedItem;
	private final Direction facingDir;
	private boolean isOpen;
	private Key key;

	public final static double COINDROPVALUE = 8;

	public Chest(Direction dropDir) {
		this.facingDir = dropDir;
		// TODO Auto-generated constructor stub
	}

	public void initialiseChestType(Item i) {
		containedItem = i;
	}

	public Item getContainedItem() {
		return containedItem;
	}

	public boolean open(Key key) {
		if (isOpen) {
			GUI.Frame.appendText("chest already open!");
			return false;
		}
		if (true) {
			Location dropLoc = Location.moveInDirection(exactLocation, facingDir, .2);
			containedItem.setExactLocation(dropLoc);
			square.addGameObject(containedItem);
			
			isOpen = true;
			return true;
		} else {
			GUI.Frame.appendText("Wrong key!");
			return false;

		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	public Direction getFacingDir() {
		return facingDir;
	}

	@Override
	public String toString() {
		return "C";

	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.MOVEMENTBLOCKING;
	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = 0.5;
	}

	public void setKey(Key key) {
		// TODO Auto-generated method stub
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

}
