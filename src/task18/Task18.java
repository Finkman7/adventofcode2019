package task18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Task18 {
	private static final int						NUM_PHASES			= 100;
	private static final Board						board				= new Board(CoordComparator.instance);
	private static Coordinate						startPosition;
	private static Map<Character, Coordinate>		keyPositions		= new TreeMap<>();
	// private static Map<Character, Coordinate> doorPositions = new TreeMap<>();
	private static Integer							bestSolution		= null;
	private static Map<Set<Character>, PartialCost>	partialSolutions	= new HashMap<>();

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input18.txt"));
		parse(lines);
		System.out.println(board);

		Map<Character, Map<Character, ConditionalPath>> pairWisePaths = new HashMap<>();
		for (Character key1 : keyPositions.keySet()) {
			pairWisePaths.put(key1, new HashMap<>());
			for (Character key2 : keyPositions.keySet()) {
				if (Character.compare(key1, key2) > 0) {
					ConditionalPath conditionalPath = board.getShortedConditionalPath(keyPositions.get(key1),
							keyPositions.get(key2));
					System.out.println(conditionalPath);
					pairWisePaths.get(key1).put(key2, conditionalPath);
				}
			}
		}

		// bfsKeySearch();
		// Map<Character, Path> possiblePaths = getPossiblePaths(List.of(), keyPositions.keySet(), pos);
		// Map<Character, Integer> posCaptures = possiblePaths.keySet().stream()
		// .collect(Collectors.toMap(key -> key, key -> getShortestSteps(List.of(key),
		// possiblePaths.get(key).getTail(), possiblePaths.get(key).steps())));
		//
		// Character bestFirst = posCaptures.keySet().stream().sorted(Comparator.comparingInt(key ->
		// posCaptures.get(key)))
		// .findFirst().get();
		// System.out.println("starting with " + bestFirst + " gives " + posCaptures.get(bestFirst));
	}

	private static void bfsKeySearch() {
		Map<SearchHash, Integer> cache = new HashMap<>();

		Queue<SearchNode> queue = new LinkedList<>();
		SearchNode s0 = new SearchNode(List.of(), 0);
		queue.add(s0);
		Integer bestSolution = null;
		int count = 0;
		do {
			SearchNode s = queue.poll();
			if (cache.containsKey(s.asSearchHash())) {
				if (cache.get(s.asSearchHash()) < s.steps) {
					System.out.println(s + " pruned!");
					continue;
				}
			} else {
				cache.put(s.asSearchHash(), s.steps);
			}

			System.out.println(s);
			Set<Character> toCollect = keyPositions.keySet().stream().filter(key -> !s.contains(key))
					.collect(Collectors.toSet());

			if (toCollect.isEmpty()) {
				if (bestSolution == null || s.steps < bestSolution) {
					bestSolution = s.steps;
				}
			} else {
				Coordinate coords = s.getLastKey() != null ? keyPositions.get(s.getLastKey()) : startPosition;
				Map<Character, Path> possiblePaths = getPossiblePaths(s.keyOrder, toCollect, coords);
				for (Character key : possiblePaths.keySet()) {
					Path p = possiblePaths.get(key);
					if (!walksThroughOtherKey(p, toCollect)) {
						SearchNode ext = s.extend(key, p.steps());
						queue.add(ext);
					}
				}
			}
		} while (!queue.isEmpty());

		System.out.println(bestSolution);
	}

	private static boolean walksThroughOtherKey(Path p, Set<Character> toCollect) {
		Set<Coordinate> forbidden = toCollect.stream().map(key -> keyPositions.get(key)).collect(Collectors.toSet());

		return p.stream().filter(coords -> forbidden.contains(coords)).count() > 1;
	}

	private static Map<Character, Path> getPossiblePaths(List<Character> collectedKeys, Set<Character> toCollect, Coordinate pos) {
		Map<Character, Path> paths = new HashMap<>();

		for (Character key : toCollect) {
			Path p = board.getShortestPath(pos, keyPositions.get(key), new HashSet<>(collectedKeys));
			if (p != null) {
				paths.put(key, p);
			}
		}

		return paths;
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
