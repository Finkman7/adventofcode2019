package task23;

public enum Token {
	FREE, WALL, SOLUTION;

	@Override
	public String toString() {
		switch (this) {
		case SOLUTION:
			return "�?�";
		default:
		case FREE:
			return " ";
		case WALL:
			return "■";
		}
	}
}
