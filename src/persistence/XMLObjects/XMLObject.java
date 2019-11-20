package persistence.XMLObjects;

public interface XMLObject {

	public <T extends XMLObject> T toXML(Object o);
}
