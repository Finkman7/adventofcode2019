package task03;

import java.util.Comparator;

public class Intersection implements Comparable<Intersection> {
	public static Comparator<Intersection>	compareByDistance	= new Comparator<Intersection>() {

																	@Override
																	public int compare(Intersection o1, Intersection o2) {
																		return Long.compare(o1.walkDistance(),
																				o2.walkDistance());
																	}
																};
	long									x;
	long									y;
	long									walkDistance;

	public Intersection(long x, long y, long walkDistance) {
		this.x = x;
		this.y = y;
		this.walkDistance = walkDistance;
	}

	@Override
	public String toString() {
		return "Intersection [x=" + this.x + ", y=" + this.y + ", manhattanDistance=" + this.manhattanDistance()
				+ ", walkDistance=" + this.walkDistance + "]";
	}

	@Override
	public int compareTo(Intersection o) {
		return Double.compare(this.manhattanDistance(), o.manhattanDistance());
	}

	public long manhattanDistance() {
		return Math.abs(x) + Math.abs(y);
	}

	public long walkDistance() {
		return walkDistance;
	}

}
