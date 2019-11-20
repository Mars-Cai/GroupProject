package persistence;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.*;

import javax.xml.bind.annotation.XmlList;

import WorldEditor.AbstractWorld;
import WorldEditor.Location;
import WorldEditor.Room;
import WorldEditor.TabMap;
import WorldEditor.World;
import persistence.XMLObjects.RoomXML;
import persistence.XMLObjects.WorldXML;

public class Saver {

	public static void main(String[] args) {
		World world = new World();
		save("save", world);
		TabMap map = new TabMap(world);
	}

	public static void save(String saveName, AbstractWorld abstarctWorld) {
		abstarctWorld.updateReferences();

		try {
			FileOutputStream fileOut = new FileOutputStream(saveName+".xml");
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(fileOut));
			encoder.setExceptionListener(new ExceptionListener() {

				@Override
				public void exceptionThrown(Exception e) {

					// TODO Auto-generated method stub

				}
			});

			XMLSave(encoder, abstarctWorld);
			encoder.close();
			System.out.printf("Serialized data is saved in "+saveName+".xml\n");
		} catch (IOException i) {
			i.printStackTrace();
		}


	}

	private static void XMLSave(XMLEncoder encoder, AbstractWorld abstarctWorld) {
		encoder.writeObject(Mapper.seedToXML(abstarctWorld.getSeed()));
		Mapper.worldToXML(abstarctWorld, encoder);

	}



}
