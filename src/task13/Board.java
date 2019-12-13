package task13;

import java.util.TreeMap;

public class Board extends TreeMap<Coordinate, Token> {

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
				if (this.isOccupied(c)) {
					sb.append(this.get(c));
				} else {
					sb.append(" ");
				}
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public long count(Token block) {
		return this.values().stream().filter(t -> t.equals(block)).count();
	}
}
