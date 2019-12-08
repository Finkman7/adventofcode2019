package task02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Task02 {

	private static Map<Integer, Integer> initState;

	public static void main(String[] args) throws IOException {
		initState = new HashMap<>();

		for (String line : Files.readAllLines(Paths.get("inputs/input02.txt"))) {
			init(initState, line);
		}

		a();
		b();
	}

	private static void init(Map<Integer, Integer> state, String line) {
		String[] tokens = line.split(",");

		for (int i = 0; i < tokens.length; i++) {
			state.put(i, Integer.valueOf(tokens[i]));
		}

		state.put(1, 12);
		state.put(2, 2);
	}

	private static void a() throws IOException {
		Map<Integer, Integer> state = new HashMap<>();

		for (String line : Files.readAllLines(Paths.get("inputs/input02.txt"))) {
			init(state, line);
		}

		System.out.println(loop(state));
	}

	private static Integer loop(Map<Integer, Integer> state) {
		int pointer = 0;

		while (true) {
			Integer opCode = state.get(pointer);
			if (opCode.equals(1)) {
				state.put(state.get(pointer + 3),
						state.get(state.get(pointer + 1)) + state.get(state.get(pointer + 2)));
			} else if (opCode.equals(2)) {
				state.put(state.get(pointer + 3),
						state.get(state.get(pointer + 1)) * state.get(state.get(pointer + 2)));
			} else if (opCode.equals(99)) {
				return state.get(0);
			}

			pointer += 4;
		}
	}

	private static void b() {
		for (int i = 0; i <= 99; i++) {
			for (int j = 0; j <= 99; j++) {
				Map<Integer, Integer> memory = new HashMap<>(initState);
				memory.put(1, i);
				memory.put(2, j);
	
				Integer result = loop(memory);
				if (result.equals(19690720)) {
					System.out.println(100 * i + j);
				}
			}
		}
	}

}
