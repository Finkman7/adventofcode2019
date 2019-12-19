package task18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

public class Task18 {
	private static final int										NUM_PHASES		= 100;
	private static final Board										board			= new Board(
			CoordComparator.instance);
	private static Coordinate										startPosition;
	private static Map<Character, Coordinate>						keyPositions	= new TreeMap<>();
	private static Map<Character, Map<Character, ConditionalPath>>	pairWisePaths;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input18.txt"));
		parse(lines);
		System.out.println(board);

		pairWisePaths = new HashMap<>();
		for (Character key1 : keyPositions.keySet()) {
			pairWisePaths.put(key1, new HashMap<>());
			for (Character key2 : keyPositions.keySet()) {
				if (key1 != key2) {
					ConditionalPath conditionalPath = board.getShortedConditionalPath(keyPositions.get(key1),
							keyPositions.get(key2));
					// System.out.println(key1 + " -> " + key2 + ": " + conditionalPath);
					pairWisePaths.get(key1).put(key2, conditionalPath);
				}
			}
		}

		bfsKeySearch();
	}

	private static void bfsKeySearch() {
		Queue<SearchNode> q = new PriorityQueue<SearchNode>();

		for (Character key : keyPositions.keySet()) {
			ConditionalPath route = board.getShortedConditionalPath(startPosition, keyPositions.get(key));
			if (route.prereqs.isEmpty()) {
				SearchNode s = new SearchNode(List.of(key), route.shortestPath.steps());
				q.add(s);
			}
		}

		Map<SearchHash, Integer> cache = new HashMap<>();
		while (!q.isEmpty()) {
			SearchNode s = q.poll();

			if (s.asSearchHash().collected.containsAll(keyPositions.keySet())) {
				System.out.println("Solution: " + s);
				return;
			} else {
				// System.out.println(s);
			}

			if (cache.containsKey(s.asSearchHash()) && cache.get(s.asSearchHash()) <= s.steps) {
				// System.out.println("Pruned!");
				continue;
			} else {
				cache.put(s.asSearchHash(), s.steps);
			}

			Character key = s.getLastKey();

			for (Character otherKey : pairWisePaths.get(key).keySet()) {
				if (s.asSearchHash().collected.contains(otherKey)) {
					continue;
				}
				ConditionalPath path = pairWisePaths.get(key).get(otherKey);
				if (s.asSearchHash().collected.containsAll(path.prereqs)) {
					SearchNode ext = s.extend(otherKey, path.shortestPath.steps());
					q.add(ext);
				}
			}
		}
	}

	private static void parse(List<String> lines) {
		for (int y = 0; y < lines.size(); y++) {
			String line = lines.get(y).trim();
			for (int x = 0; x < line.length(); x++) {
				char c = line.charAt(x);
				Coordinate coords = new Coordinate(x, y);

				if (c == '#') {
					board.put(coords, Token.WALL);
				} else if (c >= 'A' && c <= 'z') {
					board.put(coords, new Token(String.valueOf(c)));
					if (c >= 'a') {
						keyPositions.put(c, coords);
					}
				} else if (c == '.') {
					board.put(coords, Token.FREE);
				} else if (c == '@') {
					board.put(coords, Token.FREE);
					startPosition = coords;
				}
			}
		}
	}

	private static Map<Integer, Integer> parse(String string) {
		Map<Integer, Integer> result = new HashMap<>();

		for (int i = 0; i < string.length(); i++) {
			result.put(i, Integer.valueOf(String.valueOf(string.charAt(i))));
		}

		return result;
	}

}
