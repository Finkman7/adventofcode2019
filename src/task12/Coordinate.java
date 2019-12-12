package task12;

public class Coordinate implements Cloneable {
	public long	x;
	public long	y;
	public long	z;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.x ^ (this.x >>> 32));
		result = prime * result + (int) (this.y ^ (this.y >>> 32));
		result = prime * result + (int) (this.z ^ (this.z >>> 32));
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
		if (this.z != other.z)
			return false;
		return true;
	}

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(long x, long y, long z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Coordinate clone() {
		return new Coordinate(x, y, z);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

	public long getEnergy() {
		return Math.abs(x) + Math.abs(y) + Math.abs(z);
	}
}
