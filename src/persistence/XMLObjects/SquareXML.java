package persistence.XMLObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import WorldEditor.Location;
import WorldEditor.Square;
import WorldEditor.GameObjects.GameObject;

public class SquareXML implements Serializable{
	public LocationXML location;
	public List<GameObject> gameObjects = new ArrayList<>();
	public boolean used;
	
	public static SquareXML toXML(Square s) {
		SquareXML squareXML = new SquareXML();
		squareXML.location = LocationXML.toXML(s.location);
		squareXML.gameObjects.addAll(s.getGameObjects());
		squareXML.used = s.isUsed();
		return squareXML;
	}
}
