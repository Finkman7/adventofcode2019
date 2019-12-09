package task05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;

public class Task05 {
	private static HashMap<Long, Long> initState;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input05.txt"));
		initState = new HashMap<>();

		for (String line : lines) {
			IntCodeComputer.memoryFromFile(initState, line);
		}

		ConcurrentLinkedQueue<Long> input = new ConcurrentLinkedQueue<>(), output = new ConcurrentLinkedQueue<>();
		input.add(1l);

		IntCodeComputer c = new IntCodeComputer((Map<Long, Long>) initState.clone(), input, output);
		c.run();
		System.out.println(output);

		input.clear();
		input.add(5l);
		output.clear();

		c = new IntCodeComputer((Map<Long, Long>) initState.clone(), input, output);
		c.run();
		System.out.println(output);
	}
}
