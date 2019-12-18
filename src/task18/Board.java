package task18;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Board extends TreeMap<Coordinate, Token> {
	private Map<ParameterSet, Path> cache = new HashMap<>();

	public Board(CoordComparator instance) {
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

	public Path getShortestPath(Coordinate c, Coordinate c_Target, Set<Character> collectedKeys) {
		ParameterSet ps = new ParameterSet(c, c_Target, collectedKeys);
		if (cache.containsKey(ps)) {
			return cache.get(ps);
		}

		if (getFreeAdjacentCoordinatesOf(c_Target).isEmpty()) {
			cache.put(ps, null);
			return null;
		}
		// System.out.println("getting shortest... " + c + " -> " + c_Target);
		Comparator<Path> comparator = byTotalPlusHeuristicDistance(c_Target);
		PriorityQueue<Path> potentialPaths = new PriorityQueue<>(comparator);
		Path initial = new Path();
		initial.add(c);
		potentialPaths.add(initial);

		Path shortestPath = null;
		Map<Coordinate, Path> bestPaths = new HashMap<>();
		while (!potentialPaths.isEmpty()) {
			Path best = potentialPaths.poll();
			// System.out.println(best);

			if (shortestPath != null) {
				if (!best.getTail().equals(c_Target) || comparator.compare(shortestPath, best) < 0) {
					break;
				} else if (CoordComparator.instance.compare(shortestPath.get(1), best.get(1)) > 0) {
					shortestPath = best;
				}
			} else {
				if (best.getTail().equals(c_Target)) {
					shortestPath = best;
				} else {
					for (Coordinate c2 : getFreeAdjacentCoordinatesOf(best.getTail())) {
						Token t = this.get(c2);
						if (t.isDoor() && !collectedKeys.contains(t.getID().toLowerCase().charAt(0))) {
							continue;
						}

						Path p = new Path(best);
						p.add(c2);

						if (!bestPaths.containsKey(c2) || bestPaths.get(c2).size() > p.size()
								|| CoordComparator.instance.compare(bestPaths.get(c2).get(1), p.get(1)) > 0) {
							potentialPaths.add(p);
							bestPaths.put(c2, p);
						}
					}
				}
			}
		}
		// System.out.println("done!");
		cache.put(ps, shortestPath);
		return shortestPath;
	}

	private Comparator<Path> byTotalPlusHeuristicDistance(Coordinate target) {
		return Comparator.comparingInt(p -> p.size() + p.getTail().manhattanDistanceTo(target));
	}

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
				sb.append(this.get(c) + " ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}
