package task12;

public class Planet {
	public Coordinate	pos;
	public Coordinate	v;

	public Planet(String line) {
		line = line.replace("<", "");
		line = line.replace(">", "");
		String[] tokens = line.split(",");
		long x = Long.valueOf(tokens[0].split("=")[1]);
		long y = Long.valueOf(tokens[1].split("=")[1]);
		long z = Long.valueOf(tokens[2].split("=")[1]);
		this.pos = new Coordinate(x, y, z);
		this.v = new Coordinate(0, 0, 0);
	}

	public Planet(Planet p) {
		this.pos = new Coordinate(p.pos.x, p.pos.y, p.pos.z);
		this.v = new Coordinate(p.v.x, p.v.y, p.v.z);
	}

	@Override
	public String toString() {
		return "Planet [pos=" + this.pos + ", v=" + this.v + "]";
	}

	public void adjustVelocity(Planet p2) {
		this.v.x += getGravity(this.pos.x, p2.pos.x);
		this.v.y += getGravity(this.pos.y, p2.pos.y);
		this.v.z += getGravity(this.pos.z, p2.pos.z);
	}

	private long getGravity(long x, long x2) {
		if (x < x2) {
			return 1;
		} else if (x > x2) {
			return -1;
		} else {
			return 0;
		}
	}

	public void adjustPosition() {
		pos.x += v.x;
		pos.y += v.y;
		pos.z += v.z;
	}

	public long getEnergy() {
		System.err.println(this + " " + pos.getEnergy() + " " + v.getEnergy());
		return pos.getEnergy() * v.getEnergy();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.pos == null) ? 0 : this.pos.hashCode());
		result = prime * result + ((this.v == null) ? 0 : this.v.hashCode());
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
		Planet other = (Planet) obj;
		if (this.pos == null) {
			if (other.pos != null)
				return false;
		} else if (!this.pos.equals(other.pos))
			return false;
		if (this.v == null) {
			if (other.v != null)
				return false;
		} else if (!this.v.equals(other.v))
			return false;
		return true;
	}

}
