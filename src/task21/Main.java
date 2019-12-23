package task21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;
import intCodeComputer.IntCodeComputerEventType;

public class Main {
	private static ConcurrentLinkedQueue<Long>					input		= new ConcurrentLinkedQueue<>(),
			output = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<IntCodeComputerEventType>	eventQueue	= new ConcurrentLinkedQueue<>();
	private static IntCodeComputer								comp;
	private static Board										board		= new Board(CoordComparator.instance);
	private static int											sum			= 0;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input21.txt"));

		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));
		comp = new IntCodeComputer(new HashMap<>(initState), input, output, eventQueue);
		comp.start();

		List<String> program = List.of("NOT C J", "NOT B T", "OR T J", "NOT A T", "OR T J", "AND D J", "OR H T",
				"OR E T", "AND T J", "RUN");

		while (true) {
			comp.waitForEvent();
			IntCodeComputerEventType event = eventQueue.poll();
			if (event.equals(IntCodeComputerEventType.HALT)) {
				break;
			} else if (event.equals(IntCodeComputerEventType.INPUT_REQUEST)) {
				for (String line : program) {
					feed(line);
				}
				program = List.of();
			} else if (event.equals(IntCodeComputerEventType.OUTPUT)) {
				readOutput();
			}
		}
	}

	private static void readOutput() {
		Long code = output.poll();

		if (code == 10) {
			System.out.println();
		} else if (code < 256) {
			System.out.print(Character.toString((char) code.intValue()));
		} else {
			System.out.println(code);
		}
	}

	private static void feed(String line) {
		System.out.println("Feeding " + line);
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			long asLong = c;
			System.out.print(asLong + " ");
			put(asLong);
			comp.waitForInputRequest();
		}
		put(10L);
		System.out.println(10L);
	}

	private static void put(long nextInput) {
		synchronized (input) {
			input.add(nextInput);
			input.notifyAll();
		}
	}
}
