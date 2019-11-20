package WorldEditor.GameObjects;

import WorldEditor.Layer;
import renderer.Bitmap;

public class Torch extends Item{
	double lumins = 20;

	public Torch() {
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

}
