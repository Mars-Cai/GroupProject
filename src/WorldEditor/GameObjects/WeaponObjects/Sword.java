package WorldEditor.GameObjects.WeaponObjects;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import renderer.Bitmap;

public class Sword extends Weapon {


	public Sword() {

		// TODO Auto-generated constructor stub
		damage = 10;
		range = 1.5;
		sellValue = 7;
		name = "Sword";

	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.CONSUMABLES;

	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = 0.2;
	}







}
