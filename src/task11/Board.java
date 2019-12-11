package task11;

import java.util.TreeMap;
import java.util.stream.Stream;

public class Board extends TreeMap<Coordinate, String> {

	public Board() {
		super(CoordComparator.instance);
	}

	public boolean isOccupied(Coordinate c) {
		return this.get(c) != null;
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
				if (this.isOccupied(c) && this.get(c).equals("#")) {
					sb.append("#");
				} else {
					sb.append(".");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public String paint(Coordinate cur, Direction dir) {
		StringBuilder sb = new StringBuilder();

		int leftX = Stream.concat(Stream.of(cur), this.keySet().stream()).mapToInt(c -> c.x).min().getAsInt();
		int rightX = Stream.concat(Stream.of(cur), this.keySet().stream()).mapToInt(c -> c.x).max().getAsInt();
		int leftY = Stream.concat(Stream.of(cur), this.keySet().stream()).mapToInt(c -> c.y).min().getAsInt();
		int rightY = Stream.concat(Stream.of(cur), this.keySet().stream()).mapToInt(c -> c.y).max().getAsInt();

		for (int j = leftY; j <= rightY; j++) {
			for (int i = leftX; i <= rightX; i++) {
				Coordinate c = new Coordinate(i, j);
				if (c.equals(cur)) {
					sb.append(dir.toArrow());
				} else if (this.isOccupied(c) && this.get(c).equals("#")) {
					sb.append("#");
				} else {
					sb.append(".");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}
