package WorldEditor.GameObjects.WeaponObjects;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import renderer.Bitmap;

public class Gun extends Weapon {

	private Square square;

	public Gun() {

		// TODO Auto-generated constructor stub
		damage = 8;
		range = 6;
		sellValue = 3;
		name = "Gun";
	}

	public Square getSquare() {
		return square;
	}

	public void setSquare(Square square) {
		this.square = square;
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
