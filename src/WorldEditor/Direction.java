package WorldEditor;

public enum Direction {
	NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

	public static Direction inverse(Direction dir) {
		// TODO Auto-generated method stub
		if (dir.equals(EAST))
			return WEST;
		if (dir.equals(NORTHEAST))
			return SOUTHWEST;
		if (dir.equals(SOUTHEAST))
			return NORTHWEST;
		if (dir.equals(SOUTH))
			return NORTH;
		if (dir.equals(SOUTHWEST))
			return NORTHEAST;
		if (dir.equals(WEST))
			return EAST;
		if (dir.equals(NORTHWEST))
			return SOUTHEAST;
		return SOUTH;
	}

	public static Direction ClockWise(Direction dir) {
		if (dir.equals(EAST))
			return SOUTHEAST;
		if (dir.equals(NORTHEAST))
			return EAST;
		if (dir.equals(SOUTHEAST))
			return SOUTH;
		if (dir.equals(SOUTH))
			return SOUTHWEST;
		if (dir.equals(SOUTHWEST))
			return WEST;
		if (dir.equals(WEST))
			return NORTHWEST;
		if (dir.equals(NORTHWEST))
			return NORTH;
		return NORTHEAST;
	}

	public static Direction ClockWiseSnap(Direction dir) {
		if (dir.equals(EAST))
			return SOUTH;
		if (dir.equals(SOUTH))
			return WEST;
		if (dir.equals(WEST))
			return NORTH;
		if (dir.equals(NORTH))
			return EAST;
		if (dir.equals(NORTHEAST))
			return EAST;
		if (dir.equals(SOUTHEAST))
			return SOUTH;

		if (dir.equals(SOUTHWEST))
			return WEST;

		if (dir.equals(NORTHWEST))
			return NORTH;
		return dir;
	}

	public static Direction doorToDir(Direction dir) {
		if (dir.equals(EAST))
			return EAST;
		if (dir.equals(SOUTH))
			return NORTH;
		if (dir.equals(WEST))
			return EAST;
		if (dir.equals(NORTH))
			return NORTH;
		return dir;
	}

	public static Direction intToDir(double d, double e) {
		// TODO Auto-generated method stub
		if(d >0 && e>0) {
			return NORTHEAST;
		}
		if(d >0 && e==0) {
			return EAST;
		}
		if(d >0 && e<0) {
			return SOUTHEAST;
		}
		if(d ==0 && e<0) {
			return SOUTH;
		}
		if(d <0 && e<0) {
			return SOUTHWEST;
		}
		if(d <0 && e==0) {
			return WEST;
		}if(d <0 && e>0) {
			return NORTHWEST;
		}
		return NORTH;
	}
}