package WorldEditor.GameObjects;

import WorldEditor.Layer;

public class Fuel extends Item{

	private double fuelAmount = 40000;


	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.CONSUMABLES;
	}

	@Override
	protected void setHitBoxAndRadius() {
		hitBoxRadius = 0.2;
	}

	public double getFuelAmount() {
		// TODO Auto-generated method stub
		return fuelAmount;
	}



}
