package task03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Task03 {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input03.txt"));
		List<Path> lines1 = parseToPaths(lines.get(0));
		List<Path> lines2 = parseToPaths(lines.get(1));

		List<Intersection> intersections = new ArrayList<>();
		for (Path l1 : lines1) {
			for (Path l2 : lines2) {
				Intersection intersection = l1.intersect(l2);
				if (intersection != null) {
					if (intersection.x != 0 || intersection.y != 0)
						intersections.add(intersection);
				}
			}
		}

		Intersection closestByManhattan = intersections.stream().sorted().findFirst().get();
		System.out.println(closestByManhattan);
		Intersection closestByDistanceWalked = intersections.stream().sorted(Intersection.compareByDistance).findFirst()
				.get();
		System.out.println(closestByDistanceWalked);
	}

	private static List<Path> parseToPaths(String string) {
		List<Path> paths = new ArrayList<>();
		String[] directions = string.split(",");

		long curX = 0;
		long curY = 0;
		long distanceSoFar = 0;
		for (String dir : directions) {
			long offset = Long.valueOf(dir.substring(1));
			Path path = new Path(curX, curY);

			if (dir.startsWith("L")) {
				curX -= offset;
			} else if (dir.startsWith("R")) {
				curX += offset;
			} else if (dir.startsWith("U")) {
				curY += offset;
			} else if (dir.startsWith("D")) {
				curY -= offset;
			}

			path.finish(curX, curY, distanceSoFar);

			distanceSoFar += offset;
			paths.add(path);
		}

		return paths;
	}

}
