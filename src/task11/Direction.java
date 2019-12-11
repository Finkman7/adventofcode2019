package task11;

public enum Direction {
	UP, DOWN, LEFT, RIGHT;

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
}
