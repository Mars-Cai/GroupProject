package gameWorld;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.plaf.SliderUI;

import WorldEditor.Direction;
import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;

@Deprecated
public class AstarPathFindingAlorithm {

	private Location start;
	private Location end;
	private Square[][] world;
	private double stepSize = 0.2;

	private TestFrame testFrame;
	private Set<Location> visitedLocations = new HashSet<>();
	private List<Direction> directionsToCheck = Arrays.asList(Direction.EAST, Direction.SOUTH, Direction.WEST,
			Direction.NORTH/*, Direction.NORTHWEST, Direction.NORTHEAST, Direction.SOUTHWEST, Direction.SOUTHEAST*/);
	// private PriorityQueue<AstarNode> nodes = new PriorityQueue<>((a1, a2) ->
	// a1.compareTo(a2));
	private AstarQueue nodesToVisit = new AstarQueue();

	private List<Location> path = new ArrayList<>();

	private int count = 0;

	public AstarPathFindingAlorithm(Location start, Location end, Square[][] world) {
//		testFrame = new TestFrame();
		this.start = start;
		this.end = end;
		this.world = world;
		AstarNode firstN = new AstarNode(0, start.distanceTo(end), null, start);
		// visited.add(firstN.loc);
		// nodes.add(firstN);
		findRoute(firstN);
	}

	private void findRoute(AstarNode current) {
		if (Math.abs(current.loc.distanceTo(end)) <= stepSize) {
			backTrack(current);
			// Path found
			return;
		}
		if (visitedLocations.contains(current.loc)) {
			// Already visited
			System.out.println("x: " + count++);
//			testFrame.line(current.prev.loc, current.loc, Color.RED);

			findRoute(nodesToVisit.poll());

			return;
		}

		List<Location> neighbours = getNeighbouringLocations(current.loc);
		double g = current.g;

		neighbours.removeAll(getInvalidNeighbours(neighbours));

		nodesToVisit.addAll(neighbours.stream()
				.map(n -> new AstarNode(g + current.loc.distanceTo(n)/* start.distanceTo(n) */,
						n.distanceTo(end) + g + current.loc.distanceTo(n)/* start.distanceTo(n) */, current, n))
				.collect(Collectors.toList()));

//		System.out.println(nodesToVisit.size());

//		testFrame.line(nodesToVisit.peek().loc, nodesToVisit.peek().prev.loc, Color.BLUE);

		visitedLocations.add(current.loc);
		findRoute(nodesToVisit.poll());
	}

	private List<Location> getNeighbouringLocations(Location loc) {
		return directionsToCheck.stream().map(d -> Location.moveInDirection(loc, d, stepSize))
				.collect(Collectors.toList());
	}

	private List<Location> getInvalidNeighbours(List<Location> neighbours) {
		List<Location> locations = neighbours.stream().filter(n -> world[(int) n.x][(int) n.y]
				.getObjectsOnLayer(Layer.MOVEMENTBLOCKING).stream().anyMatch(o -> o.hitBoxContains(n)))
				.collect(Collectors.toList());
		return locations;
	}

	private void backTrack(AstarNode current) {
		if (current.prev == null)
			return;
		path.add(0, current.loc);
		backTrack(current.prev);
	}

	public List<Location> getPath() {
		return path;
	}

}


