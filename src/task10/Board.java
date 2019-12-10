package task10;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Board extends TreeMap<Coordinate, Token> {
	public final int WIDTH, HEIGHT;

	public Board(int width, int height) {
		super(CoordComparator.instance);
		this.WIDTH = width;
		this.HEIGHT = height;
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

		return List.of(up, left, right, down).stream().filter(c2 -> isValid(c2)).collect(Collectors.toList());
	}

	public List<Coordinate> getFreeAdjacentCoordinatesOf(Coordinate c) {
		return getAdjacentCoordinatesof(c).stream().filter(c2 -> !isOccupied(c2)).collect(Collectors.toList());
	}

	private boolean isValid(Coordinate c2) {
		return c2.x >= 0 && c2.x < WIDTH && c2.y >= 0 && c2.y < HEIGHT;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		this.keySet().stream().forEach(c -> {
			if (isOccupied(c)) {
				sb.append(this.get(c).toShortString());
			} else {
				sb.append(".");
			}

			if (c.x == this.WIDTH - 1) {
				sb.append(" ");
				// sb.append(this.getUnitEntries().stream().filter(e -> e.getKey().y == c.y)
				// .map(e -> e.getValue().toString()).collect(Collectors.joining(", ")));
				sb.append("\n");
			}
		});

		return sb.toString();
	}

	public List<java.util.Map.Entry<Coordinate, Token>> getWalls() {
		return this.entrySet().stream().filter(e -> e.getValue() instanceof Wall).collect(Collectors.toList());
	}
}
