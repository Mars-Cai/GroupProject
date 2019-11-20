package WorldEditor.GameObjects;

import WorldEditor.Location;
import WorldEditor.Square;
import renderer.Bitmap;

public abstract class Item extends InteractableObject {
	private Location dropLocation;
	protected double sellValue;

	public static final double CONSUMABLEHITBOXRADIUS = 0.2;

	public Item() {
	}



	public double getSellValue() {
		return sellValue;
	}

}
