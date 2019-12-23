package task23;

public enum Direction {
	UP, DOWN, LEFT, RIGHT, NEUTRAL;

	public Direction turnLeft() {
		switch (this) {
		case UP:
			return LEFT;
		case DOWN:
			return RIGHT;
		case LEFT:
			return DOWN;
		case RIGHT:
			return UP;
		default:
			return null;
		}
	}

	public Direction turnRight() {
		switch (this) {
		case UP:
			return RIGHT;
		case DOWN:
			return LEFT;
		case LEFT:
			return UP;
		case RIGHT:
			return DOWN;
		default:
			return null;
		}
	}

	public String toArrow() {
		switch (this) {
		default:
		case UP:
			return "^";
		case DOWN:
			return "v";
		case LEFT:
			return "<";
		case RIGHT:
			return ">";
		}
	}

	public static Direction get(Coordinate c1, Coordinate c2) {
		if (Coordinate.byX.compare(c1, c2) < 0) {
			return Direction.RIGHT;
		} else if (Coordinate.byX.compare(c1, c2) > 0) {
			return Direction.LEFT;
		} else if (Coordinate.byY.compare(c1, c2) < 0) {
			return Direction.DOWN;
		} else if (Coordinate.byY.compare(c1, c2) > 0) {
			return Direction.UP;
		} else {
			return Direction.NEUTRAL;
		}
	}

	public long toLong() {
		switch (this) {
		default:
		case UP:
			return 1l;
		case DOWN:
			return 2l;
		case LEFT:
			return 3l;
		case RIGHT:
			return 4l;
		}
	}

	public String toChar() {
		switch (this) {
		default:
		case UP:
			return "U";
		case DOWN:
			return "D";
		case LEFT:
			return "L";
		case RIGHT:
			return "R";
		}
	}
}
