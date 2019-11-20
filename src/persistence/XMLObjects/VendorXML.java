package persistence.XMLObjects;

import WorldEditor.GameObjects.InanimateObjects.Vendor;

public class VendorXML {
	public LocationXML location;
	
	public static VendorXML toXML(Vendor v) {
		VendorXML vendorXML = new VendorXML();
		vendorXML.location = LocationXML.toXML(v.getSquareLocation());
		return vendorXML;
	}
}
