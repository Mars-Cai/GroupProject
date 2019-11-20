package persistence;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import WorldEditor.Room;
import WorldEditor.RoomConnection;
import WorldEditor.World;
import WorldEditor.AbstractWorld;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Floor;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import WorldEditor.GameObjects.InanimateObjects.Wall;
import persistence.XMLObjects.LocationXML;
import persistence.XMLObjects.RoomXML;
import persistence.XMLObjects.SeedXML;
import persistence.XMLObjects.WorldXML;

public class Mapper {


	public static void worldToXML(AbstractWorld abworld, XMLEncoder encoder) {
		encoder.writeObject(WorldXML.toXML(abworld));
	}

	public static SeedXML seedToXML(Long seed) {
		return SeedXML.toXML(seed);
	}




	public static AbstractWorld XMLToWorld(XMLDecoder decoder, Long seed) {
		World world = new World(seed);
		Object o = decoder.readObject();
		if(o instanceof WorldXML) {
			return WorldXML.XMLToWorld(world, (WorldXML)o);
		}
		return null;
	}

	public static Long XMLToSeed(XMLDecoder decoder) throws NoSeedException{
		Object o = decoder.readObject();
		if(o instanceof SeedXML) {
			SeedXML seedXML = (SeedXML)o;
			return seedXML.seed;
		}
		throw new NoSeedException();
	}

}
