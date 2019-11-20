package WorldEditor.GameObjects;

import javax.swing.ImageIcon;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import renderer.Bitmap;
import renderer.Bitmap3D;
import renderer.Core;

public class Coin extends Item {

	public static final double CHANCETOSPAWN = 0.6;
	private int value;
	

	public Coin(double value) {
		// TODO Auto-generated constructor stub
		this.value = (int) value;
		this.imageIcon = new ImageIcon("money.jpg");
		this.bitmapImage = Core.loadTexture("money.jpg");
	}

	public String toString() {
		return "Coin";
	}


	public double getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		
		return result;
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
