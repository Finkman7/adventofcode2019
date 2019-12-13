package task13;

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
		} else if (Coordinate.byX.compare(c1, c2) == 0) {
			return Direction.NEUTRAL;
		} else {
			return Direction.LEFT;
		}
	}
}
