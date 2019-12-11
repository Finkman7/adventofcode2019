package task11;

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
		return "(" + x + "," + y + ")";
	}

	public int manhattanDistanceTo(Coordinate target) {
		return Math.abs(x - target.x) + Math.abs(y - target.y);
	}

	public boolean isAdjacentOf(Coordinate key) {
		return this.manhattanDistanceTo(key) == 1;
	}

	public double getAngle(Coordinate target) {
		double angle = Math.toDegrees(Math.atan2(target.y - y, target.x - x)) - 90;

		if (angle < 0) {
			angle += 360;
		}

		return angle;
	}

	public Coordinate neighbour(Direction dir) {
		switch (dir) {
		default:
		case DOWN:
			return new Coordinate(x, y + 1);
		case LEFT:
			return new Coordinate(x - 1, y);
		case RIGHT:
			return new Coordinate(x + 1, y);
		case UP:
			return new Coordinate(x, y - 1);
		}
	}
}
