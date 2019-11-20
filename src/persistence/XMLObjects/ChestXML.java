package persistence.XMLObjects;

import WorldEditor.Direction;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Vendor;

public class ChestXML {
public LocationXML location;
public Direction facingDir;
	
	public static ChestXML toXML(Chest c) {
		ChestXML chestXML = new ChestXML();
		chestXML.location = LocationXML.toXML(c.getExactLocation());
		chestXML.facingDir = c.getFacingDir();
		return chestXML;
	}
}
