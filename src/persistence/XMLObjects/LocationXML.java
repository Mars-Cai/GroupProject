package persistence.XMLObjects;

import java.io.Serializable;

import WorldEditor.Location;

public class LocationXML implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3164084780634590994L;
	/**
	 * 
	 */
	public double x;
	public double y;

	
	public static LocationXML toXML(Location loc) {
		LocationXML locationXML = new LocationXML();
		locationXML.x = loc.x;
		locationXML.y = loc.y;
		return locationXML;
	}


	public Location XMLTo() {
		// TODO Auto-generated method stub
		return new Location(x, y);
	}
}