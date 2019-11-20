package gameWorld;

import WorldEditor.Location;
import WorldEditor.GameObjects.GameObject;

public class Hit {

	public final GameObject hitObject;
	private Location closestNonHitLoc;
	private Location inverseHitDir;
	
	public Hit(GameObject hitObject) {
		// TODO Auto-generated constructor stub
		this.hitObject = hitObject;
	}
	
	public void setClosestNonHitLocation(Location hitLocation) {
		this.closestNonHitLoc = hitLocation;
	}
	public Location getClosestNonHitLocation() {
		return closestNonHitLoc;
	}
	public Location getInverseHitDir() {
		return inverseHitDir;
	}
	public void setInverseHitDir(Location inverseHitDir) {
		this.inverseHitDir = inverseHitDir;
	}
	
	
}
