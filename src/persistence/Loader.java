package persistence;

import WorldEditor.*;
import persistence.XMLObjects.WorldXML;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.beans.XMLDecoder;
import java.io.*;

public class Loader {

	public static void main(String[] args) {
//		XMLtestclass test = test();
		World world = (World) load("save.xml");
		TabMap map = new TabMap(world);
	}
	
	public static AbstractWorld load(String fileName) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(fis));
			
			AbstractWorld abWorld = decode(decoder);
			
			decoder.close();
			fis.close();
			return abWorld;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static AbstractWorld decode(XMLDecoder decoder) throws NoSeedException {
		Long seed = Mapper.XMLToSeed(decoder);
		AbstractWorld abWorld = Mapper.XMLToWorld(decoder, seed);
		return abWorld;
	}

}
