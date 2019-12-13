package task09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;

public class Task09 {
	private static HashMap<Long, Long> initState;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input09.txt"));
		initState = IntCodeComputer.initMemory(lines.get(0));

		ConcurrentLinkedQueue<Long> input = new ConcurrentLinkedQueue<>(), output = new ConcurrentLinkedQueue<>();
		input.add(2l);
		IntCodeComputer c = new IntCodeComputer((Map<Long, Long>) initState.clone(), input, output,
				new ConcurrentLinkedQueue<>());
		c.run();
		System.out.println(Arrays.toString(output.toArray()));
	}
}
