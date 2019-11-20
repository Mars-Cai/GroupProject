package gameWorld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import WorldEditor.World;

public class Raycast2D {

	//not relevant if step magnitude is given
	private static double defaultNumOfSteps = 8;
	private static double maximumStepSize = 0.1;

	/**
	 *
	 * @param start
	 * @param end
	 * @param stepMagnitude,
	 *            set to 0 for default step Size
	 * @return
	 */
	public static List<Location> interpolateX2D(Location start, Location end, double stepMagnitude) {
		return getPoints(start, end, start.distanceTo(end), stepMagnitude);
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param distance
	 * @param stepMagnitude,
	 *            set to 0 for default step Size
	 * @return
	 */
	public static List<Location> interpolateX2D(Location start, Location end, double distance, double stepMagnitude) {
		return getPoints(start, end, distance, stepMagnitude);
	}

	/**
	 *
	 * @param start
	 * @param rotationRad
	 * @param distance
	 * @param stepMagnitude,
	 *            set to 0 for default step Size
	 * @return
	 */
	public static List<Location> interpolateX2D(Location start, double rotationRad, double distance,
			double stepMagnitude) {
		Location end = endFromRad(start, rotationRad, distance);
		return getPoints(start, end, distance, stepMagnitude);
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param distance
	 * @param stepMagnitude
	 * @return
	 */
	private static List<Location> getPoints(Location start, Location end, double distance, double stepMagnitude) {
		List<Location> locations = new ArrayList<>();
		double yStepSize = calculateYStepSize(end, start, stepMagnitude);
		double slope = (end.y - start.y) / (end.x - start.x);

		double x = start.x;
		for (double y = start.y + yStepSize; start.distanceTo(new Location(x, y)) < distance; y += yStepSize) {
			x = ((y - start.y) / slope) + start.x;
			locations.add(new Location(x, y));
		}
		return locations;
	}

	/**
	 *
	 * @param start
	 * @param rotationRad
	 * @param distance
	 * @param stepMagnitude
	 * @param world
	 * @param layer
	 * @return
	 */
	public static Set<Hit> hitCastAll(Location start, double rotationRad, double distance, double stepMagnitude,
			Square[][] world, Layer layer) {
		Location end = endFromRad(start, rotationRad, distance);
//		System.out.println("end: "+end);
		return checkHits(start, end, distance, stepMagnitude, world, layer);
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param stepMagnitude
	 * @param world
	 * @param layer
	 * @return
	 */
	public static Set<Hit> hitCastAll(Location start, Location end, double stepMagnitude, Square[][] world,
			Layer layer) {
		return checkHits(start, end, start.distanceTo(end), stepMagnitude, world, layer);
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param distance
	 * @param stepMagnitude
	 * @return
	 */
	public static Set<Hit> hitCastAll(Location start, Location end, double distance, double stepMagnitude,
			Square[][] world, Layer layer) {
		return checkHits(start, end, distance, stepMagnitude, world, layer);
	}

	/**
	 *
	 * @param start
	 * @param rotationRad
	 * @param distance
	 * @param stepMagnitude
	 * @param world
	 * @param layer
	 * @return
	 */
	public static Hit hitCast(Location start, double rotationRad, double distance, double stepMagnitude,
			Square[][] world, Layer layer) {
		//rotationRad = rotationRad % Math.toRadians(360);

		Location end = endFromRad(start, rotationRad, distance);
		return checkHit(start, end, distance, stepMagnitude, world, layer);
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param stepMagnitude
	 * @param world
	 * @param layer
	 * @return
	 */
	public static Hit hitCast(Location start, Location end, double stepMagnitude, Square[][] world, Layer layer) {
		return checkHit(start, end, start.distanceTo(end), stepMagnitude, world, layer);
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param distance
	 * @param stepMagnitude
	 * @param world
	 * @param layer
	 * @return
	 */
	public static Hit hitCast(Location start, Location end, double distance, double stepMagnitude, Square[][] world,
			Layer layer) {
		return checkHit(start, end, distance, stepMagnitude, world, layer);
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param distance
	 * @param stepMagnitude
	 * @param world
	 * @param layer
	 * @return
	 */
	private static Set<Hit> checkHits(Location start, Location end, double distance, double stepMagnitude,
			Square[][] world, Layer layer) {
		Set<Hit> hits = new HashSet<>();
		double yStepSize = calculateYStepSize(end, start, stepMagnitude);
		double slope = (end.y - start.y) / (end.x - start.x);
		double x = start.x;
		for (double y = start.y + yStepSize; start.distanceTo(new Location(x, y)) < distance; y += yStepSize) {

			x = ((y - start.y) / slope) + start.x;
			hits.addAll(Hits.getHitsAt(new Location(x, y), world, layer));
//			if ((hits = Hits.getHitsAt(new Location(x, y), world, layer)) != null) {
//				break;
//			}
		}
		return hits.stream().collect(Collectors.toSet());
	}

	/**
	 *
	 * @param start
	 * @param end
	 * @param distance
	 * @param stepMagnitude
	 * @param world
	 * @param layer
	 * @return
	 */
	private static Hit checkHit(Location start, Location end, double distance, double stepMagnitude, Square[][] world,
			Layer layer) {

		double yStepSize = calculateYStepSize(end, start, stepMagnitude);
		if (yStepSize == 0) {
			return xChangeOnly(start, end, distance, stepMagnitude, world, layer);
		}

		Hit hit = null;
		double slope = (end.y - start.y) / (end.x - start.x);
		double x = start.x;
		for (double y = start.y + yStepSize; start.distanceTo(new Location(x, y)) < distance; y += yStepSize) {
			x = ((y - start.y) / slope) + start.x;
			Location loc = new Location(x, y);
			if ((hit = Hits.getHitAt(loc, world, layer)) != null) {

				hit.setClosestNonHitLocation(Hits.hitLocation(loc, start, hit.hitObject));
				hit.setInverseHitDir(Hits.inverseHitDir(loc, start));
				break;
			}
		}
		return hit;
	}

	private static Hit xChangeOnly(Location start, Location end, double distance, double stepMagnitude,
			Square[][] world, Layer layer) {
		Hit hit = null;
		double y = start.y;
		for (double x = start.x + stepMagnitude; start.distanceTo(new Location(x, y)) < distance; x += stepMagnitude) {
			Location loc = new Location(x, y);

			if ((hit = Hits.getHitAt(loc, world, layer)) != null) {
				hit.setClosestNonHitLocation(Hits.hitLocation(loc, start, hit.hitObject));
				hit.setInverseHitDir(Hits.inverseHitDir(loc, start));
				break;
			}
		}
		return hit;
	}

	private static double calculateYStepSize(Location end, Location start, double stepMagnitude) {
		if (stepMagnitude != 0) {
			Location unit = Location.minus(end, start).unitVector();
			return Location.scale(unit, stepMagnitude).y;
		}

		double yStepSize = (end.y - start.y) / defaultNumOfSteps;
		int count = 0;
		while (yStepSize > maximumStepSize) {
			yStepSize = (end.y - start.y) / (defaultNumOfSteps + (2 * count++));
		}

		return yStepSize;
	}

	private static Location endFromRad(Location start, double rotationRad, double distance) {
		double x = -Math.sin(rotationRad);
		double y = Math.cos(rotationRad);
		Location end = new Location(x, y);
//		System.out.println("unit: "+end);
//		System.out.println(end);
		end = Location.scale(end, distance);
		return Location.plus(end, start);
	}

}