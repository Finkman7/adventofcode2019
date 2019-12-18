package task18;

public class Token {
	public static final Token	FREE	= new Token("."), WALL = new Token("#");
	private String				id;

	public Token(String id) {
		this.id = id;
	}

	public boolean isKey() {
		return id.charAt(0) >= 'a' && id.charAt(0) <= 'z';
	}

	public boolean isDoor() {
		return id.charAt(0) >= 'A' && id.charAt(0) <= 'Z';
	}

	@Override
	public String toString() {
		return this.id;
	}

	public String getID() {
		return this.id;
	}
}
