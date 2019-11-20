package gameWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import WorldEditor.Direction;
import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;

public class AstarIteritive {

	private Location end;
	private Location start;

	private Square[][] world;
	private double stepSize = 0.2;

	private TestFrame testFrame;
	private Set<Location> visitedLocations = new HashSet<>();
	private List<Direction> directionsToCheck = Arrays.asList(Direction.EAST, Direction.SOUTH, Direction.WEST,
			Direction.NORTH, Direction.NORTHWEST, Direction.NORTHEAST, Direction.SOUTHWEST, Direction.SOUTHEAST);

	private AstarQueue nodesToVisit = new AstarQueue();

	private List<Location> path = new ArrayList<>();

	public AstarIteritive(Location start, Location end, Square[][] world) {
		// TODO Auto-generated constructor stub
		this.world = world;
		this.start = start;
		this.end = end;
		findRoute();
	}

	private void findRoute() {
		// TODO Auto-generated method stub
		nodesToVisit.add(new AstarNode(0, start.distanceTo(end), null, start));

		while (!nodesToVisit.isEmpty()) {
			AstarNode current = nodesToVisit.poll();
			if (current.loc.distanceTo(end) <= stepSize) {
				backTrack(current);
				break;
			}
			visitedLocations.add(current.loc);
			List<AstarNode> neighbours = getNeighbouringLocations(current);
			neighbours.forEach(n -> {
				if (!visitedLocations.contains(n.loc)) {
					if (!nodesToVisit.add(n)) {

					}
				}
			});

		}
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

	private List<AstarNode> getNeighbouringLocations(AstarNode current) {
		return directionsToCheck.stream().map(d -> Location.moveInDirection(current.loc, d, stepSize))
				.filter(n -> !getInvalidNeighbour(n))
				.map(n -> new AstarNode(current.g + heuristic(current.loc, n),
						heuristic(n, end) + current.g + heuristic(current.loc, n), current, n))
				.collect(Collectors.toList());

	}

	private boolean getInvalidNeighbour(Location n) {
		return world[(int) n.x][(int) n.y].getObjectsOnLayer(Layer.MOVEMENTBLOCKING).stream()
				.anyMatch(o -> o.hitBoxContains(n));

	}

	private double heuristic(Location from, Location to) {
		// double dMax = Math.max(from.x - to.x, from.y - to.y);
		// double dMin = Math.min(from.x - to.x, from.y - to.y);
		// return 0.282 + 0.2*(dMax -dMin);
		return from.distanceTo(to);
	}

	private double nonDiagH(Location from, Location to) {
		return (from.x - to.x) + (from.y - to.y);
	}
}

class AstarNode implements Comparable<AstarNode> {
	public final double g;
	public final double heuristic;
	public final AstarNode prev;
	public final Location loc;

	public AstarNode(double g, double heuristic, AstarNode prev, Location loc) {
		// TODO Auto-generated constructor stub
		this.g = g;
		this.heuristic = heuristic;
		this.prev = prev;
		this.loc = loc;
	}

	@Override
	public int compareTo(AstarNode a) {
		// TODO Auto-generated method stub
		if (this.heuristic == a.heuristic)
			return 0;
		if (this.heuristic > a.heuristic)
			return 1;
		return -1;

		// return (int) Math.ceil(this.heuristic - a.heuristic);
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof AstarNode) {
			AstarNode a = (AstarNode) o;
			return loc.equals(a.loc);
		}
		return false;

		// return (int) Math.ceil(this.heuristic - a.heuristic);
	}

	@Override
	public String toString() {
		return "loc: " + loc + " h: " + heuristic + " g: " + g;
	}

}

class AstarQueue {
	private PriorityQueue<AstarNode> queue = new PriorityQueue<>();
	private Set<Location> set = new HashSet<>();

	public boolean add(AstarNode toAdd) {
		if (set.add(toAdd.loc)) {
			queue.add(toAdd);
			return true;
		}
		return false;
	}

	public int size() {
		// TODO Auto-generated method stub
		return queue.size();
	}

	public AstarNode peek() {
		// TODO Auto-generated method stub
		return queue.poll();
	}

	public void addAll(Collection<AstarNode> toAdds) {
		if (set.addAll(toAdds.stream().map(a -> a.loc).collect(Collectors.toList()))) {
			queue.addAll(toAdds);
		}
	}

	public AstarNode poll() {
		return queue.poll();
	}

	public void clear() {
		queue.clear();
		set.clear();
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return queue.isEmpty();
	}
}
