package persistence.XMLObjects;

public class SeedXML {
	public Long seed;
	
	public static SeedXML toXML(Long seed) {
		SeedXML seedXML = new SeedXML();
		seedXML.seed = seed;
		return seedXML;
	}
}
