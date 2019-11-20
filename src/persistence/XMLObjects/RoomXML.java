package persistence.XMLObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;

import WorldEditor.Location;
import WorldEditor.Room;
import WorldEditor.RoomConnection;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Floor;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import WorldEditor.GameObjects.InanimateObjects.Wall;

public class RoomXML implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 899748104664212831L;
	// public double BottomLeftDoubleX;
	// public double BottomLeftDoubleY;
	//
	// public double BottomRightDoubleX;
	// public double BottomRightDoubleY;
	//
	// public double TopRightDoubleX;
	// public double TopRightDoubleY;
	//
	// public double TopLeftDoubleX;
	// public double TopLeftDoubleY;

	// private double BottomLeftDoubleX;
	// private double BottomLeftDoubleY;
	//
	// private double BottomRightDoubleX;
	// private double BottomRightDoubleY;
	//
	// private double TopRightDoubleX;
	// private double TopRightDoubleY;
	//
	// private double TopLeftDoubleX;
	// private double TopLeftDoubleY;

	public LocationXML BottomLeftLocation;
	public LocationXML BottomRightLocation;
	public LocationXML TopRightLocation;
	public LocationXML TopLeftLocation;

	// private LocationXML BottomLeftLocation;
	// private LocationXML BottomRightLocation;
	// private LocationXML TopRightLocation;
	// private LocationXML TopLeftLocation;

	// public Map<Direction, RoomConnection> connections = new HashMap<>();
	//
	// public List<GameObject> objectsInRoom = new ArrayList<>();
	// public List<Enemy> enemiesInRoom = new ArrayList<>();
	// public List<ChestObject> chestsInRoom = new ArrayList<>();
	//
	// public List<WallObject> verticalWallsInRoom = new ArrayList<>();
	// public List<WallObject> horizontalWallsInRoom = new ArrayList<>();
	// public List<FloorObject> floorsInRoom = new ArrayList<>();
	// public List<DoorObject> doorsInRoom = new ArrayList<>();
	// private boolean isBossRoom;
	// private VendorObject vendor;

	public static RoomXML toXML(Room room) {
		RoomXML rXML = new RoomXML();
		rXML.BottomLeftLocation = LocationXML.toXML(room.BottomLeftLocation);
		rXML.BottomRightLocation = LocationXML.toXML(room.BottomRightLocation);
		rXML.TopLeftLocation = LocationXML.toXML(room.TopLeftLocation);
		rXML.TopRightLocation = LocationXML.toXML(room.TopRightLocation);
		return rXML;
	}

}
