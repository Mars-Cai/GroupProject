package WorldEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import WorldEditor.GameObjects.GameObject;

public class Location implements Serializable {

	public final double x;
	public final double y;

	public Location(double x, double y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}

	public double magnitude() {
		return Math.sqrt((x * x) + (y * y));
	}

	public static Location minus(Location first, Location second) {
		return new Location(first.x - second.x, first.y - second.y);
	}

	public static Location plus(Location first, Location second) {
		return new Location(first.x + second.x, first.y + second.y);
	}

	public static Location scale(Location loc, double scaler) {
		return new Location(loc.x * scaler, loc.y * scaler);
	}

	public Location unitVector() {
		double mag = this.magnitude();
		return new Location(x / mag, y / mag);
	}

	@Override
	public boolean equals(Object locationToFind) {
		if (locationToFind instanceof Location) {
			Location location = (Location) locationToFind;
			return (this.x == location.x) && (this.y == location.y);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	public double distanceTo(Location objLocation) {
		return Math.abs(Location.minus(objLocation, this).magnitude());
	}

	public static <T extends GameObject> Boolean isTooClose(Location loc, int tooCloseRadius, List<T> blockingObjects) {
		return blockingObjects.stream().anyMatch(g -> g.getExactLocation().distanceTo(loc) < tooCloseRadius);
	}

	public static Boolean isTooClose(Location loc, int tooCloseRadius, Set<Location> blockingLocations) {
		return blockingLocations.stream().anyMatch(g -> g.distanceTo(loc) < tooCloseRadius);
	}

	public static Location moveInDirection(Location loc, Direction dir, double distance) {
		if (dir.equals(Direction.NORTH)) {
			return new Location(loc.x, loc.y + distance);
		}
		if (dir.equals(Direction.EAST)) {
			return new Location(loc.x + distance, loc.y);
		}
		if (dir.equals(Direction.SOUTH)) {
			return new Location(loc.x, loc.y - distance);
		}
		if (dir.equals(Direction.WEST)) {
			return new Location(loc.x - distance, loc.y);
		}
		if (dir.equals(Direction.NORTHWEST)) {
			return new Location(loc.x - distance, loc.y + distance);
		}
		if (dir.equals(Direction.NORTHEAST)) {
			return new Location(loc.x + distance, loc.y + distance);
		}
		if (dir.equals(Direction.SOUTHWEST)) {
			return new Location(loc.x - distance, loc.y - distance);
		}
		if (dir.equals(Direction.SOUTHEAST)) {
			return new Location(loc.x + distance, loc.y - distance);
		}

		return loc;
	}

	/**
	 * Gets the surrounding squares from the exacLoc that could have a hitbox that
	 * overlaps with the exacLoc's square.
	 *
	 * @param world
	 * @param exacLoc,
	 *            origin
	 * @param radius
	 *            to check, a good idea would be to use
	 *            GameObject.MAXIMUMHITBOXRADIUS
	 * @return HasSet of all the squares that could have overlapping objects
	 */
	public static List<Square> getPossibleOverlappingSquares(Square[][] world, Location exacLoc, double radius) {
		List<Square> squares = new ArrayList<>();

		Square center = world[(int) exacLoc.x][(int) exacLoc.y];
		squares.add(center);
		boolean toTheRight = exacLoc.x % 1 > (1 - radius);
		boolean above = exacLoc.y % 1 > (1 - radius);
		Square neiB;

		if (toTheRight) {
			// squares.add(world[(int) (exacLoc.x + radius)][(int) exacLoc.y]);
			if ((neiB = center.neighbourSquares.get(Direction.EAST)) != null)
				squares.add(neiB);

			if (above) {
				if ((neiB = center.neighbourSquares.get(Direction.NORTHEAST)) != null)
					squares.add(neiB);

			} else {
				if ((neiB = center.neighbourSquares.get(Direction.SOUTHEAST)) != null)
					squares.add(neiB);

			}
		} else {
			if ((neiB = center.neighbourSquares.get(Direction.WEST)) != null)
				squares.add(neiB);

			// squares.add(world[(int) (exacLoc.x - radius)][(int) exacLoc.y]);
			if (above) {
				if ((neiB = center.neighbourSquares.get(Direction.NORTHEAST)) != null)
					squares.add(neiB);

			} else {
				if ((neiB = center.neighbourSquares.get(Direction.SOUTHWEST)) != null)
					squares.add(neiB);

			}
		}

		if (above) {
			// squares.add(world[(int) (exacLoc.x)][(int) (exacLoc.y + radius)]);
			if ((neiB = center.neighbourSquares.get(Direction.NORTH)) != null)
				squares.add(neiB);

		} else {
			if ((neiB = center.neighbourSquares.get(Direction.SOUTH)) != null)
				squares.add(neiB);

			// squares.add(world[(int) exacLoc.x][(int) (exacLoc.y + radius)]);
		}

		return squares;


		//do not uncomment
		// Square center = world[(int) exacLoc.x][(int) exacLoc.y];
		// List<Square> squares =
		// center.neighbourSquares.values().stream().collect(Collectors.toList());
		// squares.add(center);
		// return squares;

	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	public boolean outOfBounds() {
		// TODO Auto-generated method stub
		return x < 0 || x >= World.MAXBOARDWIDTH || y < 0 || y >= World.MAXBOARDHEIGHT;
	}
}
