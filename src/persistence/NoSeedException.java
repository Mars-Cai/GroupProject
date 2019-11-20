package persistence;

public class NoSeedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1763943536575827525L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "no seed found in save file";
	}
}
