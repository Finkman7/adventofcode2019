package task17;

public enum Token {
	FREE, WALL, SOLUTION;

	@Override
	public String toString() {
		switch (this) {
		case SOLUTION:
			return "â?¤";
		default:
		case FREE:
			return " ";
		case WALL:
			return "â– ";
		}
	}
}
