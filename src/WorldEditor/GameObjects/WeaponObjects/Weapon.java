package WorldEditor.GameObjects.WeaponObjects;

import WorldEditor.Location;
import WorldEditor.GameObjects.Item;
import renderer.Bitmap;

public abstract class Weapon extends Item {

	protected double range;
	protected double damage;
	protected String name;
	
	public Weapon() {
		
	}
	
	public double getRange() {
		return range;
	}

	public double getSellValue() {
		return sellValue;
	}

	public double getDamage() {
		return damage;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
}
