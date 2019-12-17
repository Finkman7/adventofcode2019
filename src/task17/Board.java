package task17;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Board extends TreeMap<Coordinate, String> {

	public Board(Comparator<Coordinate> instance) {
		super(instance);
	}

	public boolean isOccupied(Coordinate c) {
		return this.get(c) != null;
	}

	public List<Coordinate> getAdjacentCoordinatesof(Coordinate c) {
		Coordinate up = c.clone();
		up.y--;
		Coordinate down = c.clone();
		down.y++;
		Coordinate left = c.clone();
		left.x--;
		Coordinate right = c.clone();
		right.x++;

		return List.of(up, left, right, down).stream().collect(Collectors.toList());
	}

	public List<Coordinate> getFreeAdjacentCoordinatesOf(Coordinate c) {
		return getAdjacentCoordinatesof(c).stream().filter(c2 -> !isWall(c2)).collect(Collectors.toList());
	}

	// public Path getShortestPath(Coordinate c, Coordinate c_Target) {
	// if (getFreeAdjacentCoordinatesOf(c_Target).isEmpty()) {
	// return null;
	// }
	// // System.out.println("getting shortest... " + c + " -> " + c_Target);
	// Comparator<Path> comparator = byTotalPlusHeuristicDistance(c_Target);
	// PriorityQueue<Path> potentialPaths = new PriorityQueue<>(comparator);
	// Path initial = new Path();
	// initial.add(c);
	// potentialPaths.add(initial);
	//
	// Path shortestPath = null;
	// Map<Coordinate, Path> bestPaths = new HashMap<>();
	// while (!potentialPaths.isEmpty()) {
	// Path best = potentialPaths.poll();
	// // System.out.println(best);
	//
	// if (shortestPath != null) {
	// if (!best.getTail().equals(c_Target) || comparator.compare(shortestPath, best) < 0) {
	// break;
	// } else if (CoordComparator.instance.compare(shortestPath.get(1), best.get(1)) > 0) {
	// shortestPath = best;
	// }
	// } else {
	// if (best.getTail().equals(c_Target)) {
	// shortestPath = best;
	// } else {
	// for (Coordinate c2 : getFreeAdjacentCoordinatesOf(best.getTail())) {
	// Path p = new Path(best);
	// p.add(c2);
	//
	// if (!bestPaths.containsKey(c2) || bestPaths.get(c2).size() > p.size()
	// || CoordComparator.instance.compare(bestPaths.get(c2).get(1), p.get(1)) > 0) {
	// potentialPaths.add(p);
	// bestPaths.put(c2, p);
	// }
	// }
	// }
	// }
	// }
	// // System.out.println("done!");
	// return shortestPath;
	// }

	public List<Coordinate> getFreeCoordsWithDistance(Coordinate coords, int d) {
		List<Coordinate> layer = new ArrayList<>();

		for (int dx = -d; dx <= d; dx++) {
			for (int dy = -d; dy <= d; dy++) {
				if ((Math.abs(dx) + Math.abs(dy)) == d) {
					layer.add(new Coordinate(coords.x + dx, coords.y + dy));
				}
			}
		}

		return layer;
	}

	public boolean isWall(Coordinate c) {
		return isOccupied(c) && get(c).equals(Token.WALL);
	}

	public List<Coordinate> getAllCoordsInRange(Coordinate coords, int size) {
		List<Coordinate> result = new ArrayList<>();

		for (int dx = -size; dx <= size; dx++) {
			for (int dy = -size; dy <= size; dy++) {
				if (dx == 0 && dy == 0) {
					continue;
				}

				result.add(new Coordinate(coords.x + dx, coords.y + dy));
			}
		}

		return result;
	}

	public int intersectionParameters() {
		int sum = 0;

		for (Coordinate c : this.keySet()) {
			if (isIntersection(c)) {
				System.out.println(c + " is intersection. " + c.x + "*" + c.y + "=" + c.x * c.y);
				sum += c.x * c.y;
			}
		}

		return sum;
	}

	private boolean isIntersection(Coordinate c) {
		return isScaffold(c) && getAdjacentCoordinatesof(c).stream()
				.filter(c1 -> isOccupied(c1) && !isFree(c1) && c1.x == c.x).count()
				* getAdjacentCoordinatesof(c).stream().filter(c1 -> isOccupied(c1) && !isFree(c1) && c1.y == c.y)
						.count() >= 4;
	}

	public boolean isScaffold(Coordinate c) {
		return isOccupied(c) && this.get(c).equals("#");
	}

	public boolean isFree(Coordinate c1) {
		return isOccupied(c1) && this.get(c1).equals(".");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		int leftX = this.keySet().stream().mapToInt(c -> c.x).min().getAsInt();
		int rightX = this.keySet().stream().mapToInt(c -> c.x).max().getAsInt();
		int leftY = this.keySet().stream().mapToInt(c -> c.y).min().getAsInt();
		int rightY = this.keySet().stream().mapToInt(c -> c.y).max().getAsInt();

		for (int j = leftY; j <= rightY; j++) {
			for (int i = leftX; i <= rightX; i++) {
				Coordinate c = new Coordinate(i, j);
				if (isIntersection(c)) {
					sb.append("!");
				} else if (this.isOccupied(c)) {
					sb.append(this.get(c));
				} else {
					sb.append(" ");
				}
				sb.append(" ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}
