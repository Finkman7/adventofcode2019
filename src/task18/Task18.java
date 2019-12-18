package task18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task18 {
	private static final int						NUM_PHASES			= 100;
	private static final Board						board				= new Board(CoordComparator.instance);
	private static Coordinate						pos;
	private static Map<Character, Coordinate>		keyPositions		= new TreeMap<>();
	// private static Map<Character, Coordinate> doorPositions = new TreeMap<>();
	private static Integer							bestSolution		= null;
	private static Map<Set<Character>, PartialCost>	partialSolutions	= new HashMap<>();

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input18.txt"));
		parse(lines);
		System.out.println(board);

		Map<Character, Path> possiblePaths = getPossiblePaths(List.of(), keyPositions.keySet(), pos);
		Map<Character, Integer> posCaptures = possiblePaths.keySet().stream()
				.collect(Collectors.toMap(key -> key, key -> getShortestSteps(List.of(key),
						possiblePaths.get(key).getTail(), possiblePaths.get(key).steps())));

		Character bestFirst = posCaptures.keySet().stream().sorted(Comparator.comparingInt(key -> posCaptures.get(key)))
				.findFirst().get();
		System.out.println("starting with " + bestFirst + " gives " + posCaptures.get(bestFirst));
	}

	private static Integer getShortestSteps(List<Character> collectedKeys, Coordinate pos, int stepsSoFar) {
		System.out.print(collectedKeys + " " + stepsSoFar + " - ");

		if (bestSolution != null) {
			if (stepsSoFar >= bestSolution) {
				System.out.println("Pruned");
				return stepsSoFar;
			}
		}

		Set<Character> toCollect = keyPositions.keySet().stream().filter(key -> !collectedKeys.contains(key))
				.collect(Collectors.toSet());

		System.out.print(toCollect + " - ");

		if (partialSolutions.containsKey(toCollect)) {
			PartialCost partial = partialSolutions.get(toCollect);
			Path connection = board.getShortestPath(pos, partial.pos, new HashSet<>(collectedKeys));

			if (connection != null && partial.steps + connection.steps() < stepsSoFar) {
				System.out.println("Pruned 2");
			}
		} else {
			partialSolutions.put(toCollect, new PartialCost(pos, toCollect, stepsSoFar));
		}
		// if (partialSolutions.containsKey(s)) {
		// if (partialSolutions.get(s) <= stepsSoFar) {
		// System.out.println("Pruned 2");
		// return stepsSoFar;
		// }
		// } else {
		// partialSolutions.put(s, stepsSoFar);
		// }
		System.out.println();

		if (toCollect.isEmpty()) {
			if (bestSolution == null || bestSolution > stepsSoFar) {
				bestSolution = stepsSoFar;
				System.out.println("Best Solution: " + bestSolution);
				return stepsSoFar;
			}
		}

		Map<Character, Path> possiblePaths = getPossiblePaths(collectedKeys, toCollect, pos);

		Map<Character, Integer> posCaptures = new HashMap<>();
		possiblePaths.keySet().stream().sorted(Comparator.comparingInt(key -> possiblePaths.get(key).steps()))
				.forEach(key -> {
					Integer captureCost = getShortestSteps(
							Stream.of(collectedKeys, List.of(key)).flatMap(set -> set.stream())
									.collect(Collectors.toList()),
							possiblePaths.get(key).getTail(), stepsSoFar + possiblePaths.get(key).steps());

					if (captureCost != null) {
						posCaptures.put(key, captureCost);
					}
				});

		if (posCaptures.isEmpty()) {
			return null;
		}

		Character bestNext = posCaptures.keySet().stream().sorted(Comparator.comparingInt(key -> posCaptures.get(key)))
				.findFirst().get();

		int result = posCaptures.get(bestNext);

		return result;
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
					pos = coords;
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
