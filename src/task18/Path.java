package task18;

import java.util.ArrayList;

public class Path extends ArrayList<Coordinate> {

	public Path(Path best) {
		super(best);
	}

	public Path() {

	}

	public int steps() {
		return size() - 1;
	}

	public Coordinate getTail() {
		return this.get(this.size() - 1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < this.size() - 1; i++) {
			sb.append(Direction.get(this.get(i), this.get(i + 1)).toArrow()).append(" ");
		}

		return sb.toString();
	}
}
