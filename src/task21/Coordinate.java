package task21;

import java.util.Comparator;

public class Coordinate implements Cloneable {
	public int								x;
	public int								y;

	public static Comparator<Coordinate>	byX	= Comparator.comparingInt(c -> c.x);
	public static Comparator<Coordinate>	byY	= Comparator.comparingInt(c -> c.y);

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Coordinate clone() {
		return new Coordinate(x, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.x;
		result = prime * result + this.y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (this.x != other.x)
			return false;
		if (this.y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + "|" + y + ")";
	}

	public int manhattanDistanceTo(Coordinate target) {
		return Math.abs(x - target.x) + Math.abs(y - target.y);
	}

	public boolean isAdjacentOf(Coordinate key) {
		return this.manhattanDistanceTo(key) == 1;
	}

	public Coordinate neighbourIn(Direction cur) {
		switch (cur) {
		case DOWN:
			return new Coordinate(x, y + 1);
		case LEFT:
			return new Coordinate(x - 1, y);
		default:
		case NEUTRAL:
			return new Coordinate(x, y);
		case RIGHT:
			return new Coordinate(x + 1, y);
		case UP:
			return new Coordinate(x, y - 1);
		}
	}
}
