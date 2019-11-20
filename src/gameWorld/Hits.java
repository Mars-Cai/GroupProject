package gameWorld;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import WorldEditor.GameObjects.GameObject;

public class Hits {

	public static Set<Hit> getAllHits(List<Location> locations, Square[][] world, Layer layer) {
		Set<Hit> hits = new HashSet<>();
		for (Location l : locations) {
			if(l.outOfBounds()) {
				return hits;
			}
			hits.addAll(getHitsAt(l, world, layer));
		}
		return hits;
	}

	public static Hit getFirstHit(List<Location> locations, Square[][] world, Layer layer) {
		Hit hit = null;
		for (Location l : locations) {
			if(l.outOfBounds()) {
				return hit;
			}
			hit = getHitAt(l, world, layer);
			if (hit != null)
				break;
		}
		return hit;
	}

	public static Hit getHitAt(Location l, Square[][] world, Layer layer) {
		Hit hit = null;
		if(l.outOfBounds()) {
			return hit;
		}
		List<Square> sToCheck = Location.getPossibleOverlappingSquares(world, l, GameObject.MAXIMUMHITBOXRADIUS);
		for (Square s : sToCheck) {
			GameObject hitObj = s.getObjectsOnLayer(layer).stream().filter(o -> o.hitBoxContains(l)).findFirst()
					.orElse(null);
			if (hitObj == null)
				break;
			Hit newHit = new Hit(hitObj);
			hit = newHit;
		}
		return hit;
	}

	public static Set<Hit> getHitsAt(Location l, Square[][] world, Layer layer) {
		Set<Hit> hits = new HashSet<>();
		if(l.outOfBounds()) {
			return hits;
		}
		List<Square> sToCheck = Location.getPossibleOverlappingSquares(world, l, GameObject.MAXIMUMHITBOXRADIUS);
		for (Square s : sToCheck) {
			hits.addAll(s.getObjectsOnLayer(layer).stream().filter(o -> o.hitBoxContains(l)).map(o -> new Hit(o))
					.collect(Collectors.toSet()));
		}
		return hits;
	}

	public static Location inverseHitDir(Location l, Location start) {
		return Location.minus(start,l).unitVector();
	}

	public static Location hitLocation(Location triggerLoc, Location start, GameObject hitObj) {
		Location unitL = Location.minus(start,triggerLoc).unitVector();
		Location newLoc = Location.plus(triggerLoc, unitL);
		double scaler = 1;
		while (hitObj.hitBoxContains(newLoc)) {
			newLoc = Location.plus(triggerLoc, Location.scale(unitL, scaler));
			scaler += 0.1;
		}
		return newLoc;

	}
}
