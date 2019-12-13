package task13;

public enum Token {
	EMPTY, WALL, BLOCK, PADDLE, BALL;

	@Override
	public String toString() {
		switch (this) {
		case BALL:
			return "⬤";
		case BLOCK:
			return "+";
		case EMPTY:
			return " ";
		case PADDLE:
			return "_";
		case WALL:
			return "#";
		default:
			return " ";
		}
	}
}
