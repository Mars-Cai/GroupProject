package WorldEditor.GameObjects;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import renderer.Bitmap;

public class Key extends Item {

	private double keyID;
	private Chest chest;

	public Key(double keyID) {
		this.keyID = keyID;
	}

	public double getKeyID() {
		return keyID;
	}
	
	public Chest getChest() {
		return chest;
	}

	public void setKeyID(double keyID) {
		this.keyID = keyID;
	}


	public String toString() {
		return keyID + " Key";
	}

	public boolean equals(Object o) {
		if (o instanceof Key) {
			Key k = (Key) o;
			return this.keyID == (k.keyID);
		}
		return false;
	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.CONSUMABLES;
	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = Item.CONSUMABLEHITBOXRADIUS;
	}
	
	
	@Override
	public void setSquare(Square squre) {
		// TODO Auto-generated method stub
		super.setSquare(squre);
	}

}
