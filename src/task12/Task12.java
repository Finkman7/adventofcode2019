package task12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;

public class Task12 {
	static ConcurrentLinkedQueue<Long>	input	= new ConcurrentLinkedQueue<>(), output = new ConcurrentLinkedQueue<>();
	private static IntCodeComputer		c;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input12.txt"));
		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));

		c = new IntCodeComputer(initState, input, output);
		c.start();
	}

	private static void put(long nextInput) {
		synchronized (input) {
			input.add(nextInput);
			input.notifyAll();
		}
	}

	private static void waitForOutput() {
		synchronized (output) {
			if (c.isAlive() && output.isEmpty()) {
				try {
					output.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
