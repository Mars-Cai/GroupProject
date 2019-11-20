package WorldEditor.GameObjects;

import WorldEditor.Layer;
import WorldEditor.Location;
import renderer.Bitmap;

public class HealthPotion extends Item {

	public static final double CHANCETOSPAWN = 1;
	private double healthRestored;
	public HealthPotion() {
		healthRestored = 20;
		// TODO Auto-generated constructor stub
	}

	public double getHealthRestored() {
		return healthRestored;
	}

	@Override
	protected void setLayer() {
		layer = Layer.CONSUMABLES;
	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = Item.CONSUMABLEHITBOXRADIUS;
	}
}
