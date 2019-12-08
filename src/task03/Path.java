package task03;

public class Path {
	public Path(long curX, long curY) {
		this.x1 = curX;
		this.y1 = curY;
	}

	public long	x1;
	public long	x2;
	public long	y1;
	public long	y2;
	public long	distance;

	public void finish(long curX, long curY, long distance) {
		this.x2 = curX;
		this.y2 = curY;
		this.distance = distance;
	}

	public Intersection intersect(Path l2) {
		long leftX = 0, rightX = 0, otherX = 0, upperY = 0, lowerY = 0, otherY = 0;
		long distance = this.distance + l2.distance;

		if (this.x1 == this.x2 && l2.x1 != l2.x2) {
			leftX = Long.min(l2.x1, l2.x2);
			rightX = Long.max(l2.x1, l2.x2);
			otherX = this.x1;
			upperY = Long.max(this.y1, this.y2);
			lowerY = Long.min(this.y1, this.y2);
			otherY = l2.y1;
			distance += Math.abs(otherX - l2.x1);
			distance += Math.abs(otherY - this.y1);
		} else if (this.x1 != this.x2 && l2.x1 == l2.x2) {
			leftX = Long.min(this.x1, this.x2);
			rightX = Long.max(this.x1, this.x2);
			otherX = l2.x1;
			upperY = Long.max(l2.y1, l2.y2);
			lowerY = Long.min(l2.y1, l2.y2);
			otherY = this.y1;
			distance += Math.abs(otherX - this.x1);
			distance += Math.abs(otherY - l2.y1);
		} else {
			return null;
		}

		if (leftX <= otherX && rightX >= otherX && upperY >= otherY && lowerY <= otherY) {
			return new Intersection(otherX, otherY, distance);
		}

		return null;
	}
}
