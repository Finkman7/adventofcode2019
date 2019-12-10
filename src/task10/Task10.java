package task10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Task10 {

	public static void main(String[] args) throws IOException {
		// Coordinate yAxis = new Coordinate(0, 10000);
		// System.out.println(yAxis.getAngle(new Coordinate(0, 5000)));
		// System.out.println(yAxis.getAngle(new Coordinate(1, 1)));
		// System.out.println(yAxis.getAngle(new Coordinate(2, 2)));
		// System.out.println((new Coordinate(26, 28)).getAngle(new Coordinate(26, 29)));
		// System.out.println(yAxis.getAngle(new Coordinate(1, 0)));
		// System.exit(0);

		List<String> lines = Files.readAllLines(Paths.get("inputs/input10.txt"));
		Board board = new Board(lines.get(0).length(), lines.size());
		initBoard(board, lines);
		System.out.println(board);

		List<Entry<Coordinate, Token>> walls = board.getWalls();

		int best = 0;
		Coordinate bestC = null;
		for (Entry<Coordinate, Token> e : walls) {
			Set<Coordinate> visible = new HashSet<>();

			outer: for (Coordinate other : walls.stream().filter(e2 -> e2 != e).map(e2 -> e2.getKey())
					.sorted(byDistance(e.getKey())).collect(Collectors.toList())) {
				for (Coordinate c : visible) {
					if (blocked(e.getKey(), other, c)) {
						continue outer;
					}
				}

				visible.add(other);
			}

			if (best < visible.size()) {
				best = visible.size();
				bestC = e.getKey();
			}

			// System.out.println(e.getKey() + " -> " + visible.size());
		}

		System.out.println(bestC + " = " + best);
		final Coordinate bestCC = bestC;
		int count = 0;
		while (true) {
			List<Coordinate> visible = new ArrayList<>();

			outer: for (Coordinate other : walls.stream().filter(e2 -> e2.getKey() != bestCC).map(e2 -> e2.getKey())
					.sorted(byDistance(bestC)).collect(Collectors.toList())) {
				System.err.println(other.toString() + other.getAngle(bestCC));
				for (Coordinate c : visible) {
					if (blocked(bestC, other, c)) {
						continue outer;
					}
				}

				visible.add(other);
			}

			java.util.Collections.sort(visible, byRadian(bestCC));
			for (Coordinate c : visible) {
				System.out.println("killing " + c);
				board.put(c, null);
				// System.out.println(board);
				if (++count == 200) {
					System.out.println(c.x * 100 + c.y);
					System.exit(0);
				}
			}
		}
	}

	private static Comparator<Coordinate> byRadian(Coordinate bestC) {
		return Comparator.comparingDouble(c -> c.getAngle(bestC));
	}

	private static boolean blocked(Coordinate c, Coordinate further, Coordinate closer) {
		if (allQuadrant1(c, further, closer) || allQuadrant2(c, further, closer) || allQuadrant3(c, further, closer)
				|| allQuadrant4(c, further, closer)) {
			int dx_further = Math.abs(further.x - c.x), dx_closer = Math.abs(closer.x - c.x);
			int dy_further = Math.abs(further.y - c.y), dy_closer = Math.abs(closer.y - c.y);

			if ((dx_further == 0 && dx_closer == 0) || (dy_further == 0 && dy_closer == 0)) {
				return true;
			} else {
				double frac1 = dx_closer / (1.0 * dx_further);
				double frac2 = dy_closer / (1.0 * dy_further);

				if (Math.abs(frac1 - frac2) < 0.0001) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean allQuadrant4(Coordinate c, Coordinate further, Coordinate closer) {
		Comparator<Coordinate> byX = Coordinate.byX, byY = Coordinate.byY;
		return (byX.compare(c, further) <= 0 && byX.compare(c, closer) <= 0 && byY.compare(c, further) <= 0
				&& byY.compare(c, closer) <= 0);
	}

	private static boolean allQuadrant3(Coordinate c, Coordinate further, Coordinate closer) {
		Comparator<Coordinate> byX = Coordinate.byX, byY = Coordinate.byY;
		return (byX.compare(c, further) <= 0 && byX.compare(c, closer) <= 0 && byY.compare(c, further) >= 0
				&& byY.compare(c, closer) >= 0);
	}

	private static boolean allQuadrant2(Coordinate c, Coordinate further, Coordinate closer) {
		Comparator<Coordinate> byX = Coordinate.byX, byY = Coordinate.byY;
		return (byX.compare(c, further) >= 0 && byX.compare(c, closer) >= 0 && byY.compare(c, further) >= 0
				&& byY.compare(c, closer) >= 0);
	}

	private static boolean allQuadrant1(Coordinate c, Coordinate further, Coordinate closer) {
		Comparator<Coordinate> byX = Coordinate.byX, byY = Coordinate.byY;
		return (byX.compare(c, further) >= 0 && byX.compare(c, closer) >= 0 && byY.compare(c, further) <= 0
				&& byY.compare(c, closer) <= 0);
	}

	private static Comparator<Coordinate> byDistance(Coordinate reference) {
		return Comparator.comparingInt(c -> Math.abs(c.x - reference.x) + Math.abs(c.y - reference.y));
	}

	private static void initBoard(Map<Coordinate, Token> board, List<String> lines) {
		for (int j = 0; j < lines.size(); j++) {
			String line = lines.get(j).trim();
			for (int i = 0; i < line.length(); i++) {
				Coordinate c = new Coordinate(i, j);
				char type = line.charAt(i);
				Token tok = null;

				if (type == '#') {
					tok = new Wall();
				}

				board.put(c, tok);
			}
		}
	}
}
