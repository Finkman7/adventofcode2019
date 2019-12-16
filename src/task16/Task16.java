package task16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task16 {
	private static final int	NUM_PHASES	= 100;
	private static final int	OFFSET		= 5971981;
	private static Integer[]	base		= { 0, 1, 0, -1 };
	private static int			initialSize;
	private static int			scale		= 10000;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input16.txt"));
		Map<Integer, Integer> first = parse(lines.get(0));

		initialSize = first.size() * scale;

		// for (int i = 0; i < NUM_PHASES; i++) {
		// first = fft(first);
		// System.out.println("After " + (i + 1) + " phases: " + first);
		// }
		//
		// System.out.println(print(first));

		Map<Integer, Integer> initial = new HashMap<>();
		for (int i = 0; i < scale; i++) {
			for (int l : first.keySet()) {
				int index = l + first.size() * i;
				initial.put(index, first.get(l));
			}
		}

		Map<Integer, Integer> solution = new HashMap<>();
		for (int i = OFFSET; i < initial.size(); i++) {
			solution.put(i, initial.get(i));
		}

		for (int i = 0; i < NUM_PHASES; i++) {
			solution = hack(initial, solution);
			System.out.println(i + " done.");
			System.out.println("After " + (i + 1) + " phases: " + print(solution));
		}

		// System.out.println(solution);
	}

	private static Map<Integer, Integer> hack(Map<Integer, Integer> initial, Map<Integer, Integer> solution) {
		Map<Integer, Integer> result = new HashMap<>();
		Integer sum = null;
		Integer firstNonNullIndex = null;

		for (int i = initial.size() - 1; i >= OFFSET; i--) {
			if (sum == null) {
				// int digit = 0;
				//
				// for (int j = OFFSET; j < initial.size(); j++) {
				// int index = (j + 1) / (i + 1);
				// index = index % base.length;
				//
				// // System.out.print(first.get(j) + "*" + base[index] + " + ");
				// int factor = base[index];
				// if (firstNonNullIndex == null && factor != 0) {
				// firstNonNullIndex = j;
				// }
				// digit += factor * solution.get(j);
				// }

				sum = solution.get(initial.size() - 1);
				// String asString = String.valueOf(digit);
				// digit = Integer.valueOf(String.valueOf(asString.charAt(asString.length() - 1)));
				result.put(i, sum % 10);
			} else {
				// sum = sum + solution.get(--firstNonNullIndex);
				sum = sum + solution.get(initial.size() - 1 - (initial.size() - 1 - i));
				String asString = String.valueOf(sum);
				result.put(i, Integer.valueOf(String.valueOf(asString.charAt(asString.length() - 1))));
			}
		}

		return result;
	}

	private static Set<Integer> getNeededIndices() {
		HashSet<Integer> indices = new HashSet<>();

		for (int i = OFFSET; i < OFFSET + 8; i++) {
			for (int j = OFFSET; j < initialSize; j++) {
				int index = (j + 1) / (i + 1);
				index = index % base.length;

				if (base[index] != 0) {
					indices.add(j);
				}
			}
		}

		return indices;
	}

	private static String print(Map<Integer, Integer> initial) {
		return IntStream.range(OFFSET, OFFSET + 8).mapToObj(i -> String.valueOf(initial.get(i)))
				.collect(Collectors.joining());
	}

	// private static Map<Integer, Integer> fft(Map<Integer, Integer> initial) {
	// Map<Integer, Integer> result = new HashMap<>(initial.size());
	//
	// int lastSum = 0;
	// for (int i : neededIndices) {
	// int digit = lastSum;
	//
	// for (int j = OFFSET; j < initialSize; j++) {
	// int index = (j + 1) / (i + 1);
	// index = index % base.length;
	//
	// // System.out.print(first.get(j) + "*" + base[index] + " + ");
	// int factor = base[index];
	// digit += factor * initial.get(j);
	// }
	//
	// lastSum = digit;
	// String asString = String.valueOf(digit);
	// digit = Integer.valueOf(String.valueOf(asString.charAt(asString.length() - 1)));
	// result.put(i, digit);
	// // System.out.println(" = " + digit);
	// }
	//
	// return result;
	// }

	private static Map<Integer, Integer> parse(String string) {
		Map<Integer, Integer> result = new HashMap<>();

		for (int i = 0; i < string.length(); i++) {
			result.put(i, Integer.valueOf(String.valueOf(string.charAt(i))));
		}

		return result;
	}

}
