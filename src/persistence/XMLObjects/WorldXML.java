package persistence.XMLObjects;

import java.beans.XMLDecoder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import WorldEditor.AbstractWorld;
import WorldEditor.Room;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.Enemy;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Floor;
import WorldEditor.GameObjects.InanimateObjects.Wall;
import renderer.Bitmap;

public class WorldXML implements Serializable {

	public List<EnemyXML> enemyObjects = new ArrayList<>();
	public List<ChestXML> chestObjects = new ArrayList<>();
	public List<VendorXML> vendorObjects = new ArrayList<>();
	public PlayerXML player;

	public static WorldXML toXML(AbstractWorld abWorld) {
		WorldXML worldXML = new WorldXML();
//		worldXML.world = toSquareXML(abWorld.world);
		List<EnemyXML> enemyXMLs = abWorld.enemyObjects.stream().map(e->EnemyXML.toXML(e)).collect(Collectors.toList());
		List<VendorXML> vendorXMLs = abWorld.vendorObjects.stream().map(e->VendorXML.toXML(e)).collect(Collectors.toList());
		List<ChestXML> chestXMLs = abWorld.chestObjects.stream().map(e->ChestXML.toXML(e)).collect(Collectors.toList());
		
		worldXML.enemyObjects.addAll(enemyXMLs);
		worldXML.chestObjects.addAll(chestXMLs);
		worldXML.vendorObjects.addAll(vendorXMLs);
		worldXML.player = PlayerXML.toXML(abWorld.player);
		return worldXML;
	}

	private static SquareXML[][] toSquareXML(Square[][] world) {
		SquareXML[][] s = new SquareXML[World.MAXBOARDWIDTH][World.MAXBOARDHEIGHT];
		for(int x = 0; x<World.MAXBOARDWIDTH; x++) {
			for(int y = 0; y<World.MAXBOARDHEIGHT; y++) {
				s[x][y] = SquareXML.toXML(world[x][y]);

			}
			System.out.println(x);
		}
		return s;
	}

	public static AbstractWorld XMLToWorld(World world, WorldXML worldXML) {
		// TODO Auto-generated method stub
		return world.populateFromSave(worldXML);
	}

}
