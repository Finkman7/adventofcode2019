package task19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;
import intCodeComputer.IntCodeComputerEvent;

public class Main {
	private static ConcurrentLinkedQueue<Long>					input		= new ConcurrentLinkedQueue<>(),
			output = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<IntCodeComputerEvent>	eventQueue	= new ConcurrentLinkedQueue<>();
	private static IntCodeComputer								comp;
	private static Board										board		= new Board(CoordComparator.instance);
	private static int											sum			= 0;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input19.txt"));

		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));

		// for (int y = 0; y < 50; y++) {
		// int countline = 0;
		// for (int x = 0; x < 50; x++) {
		int count = 0;
		int offset = 1100;
		for (int y = offset; y < 500000; y++) {
			int countline = 0;
			for (int x = y - (y / 3); x < y + (y / 30); x++) {
				if (x < 0) {
					continue;
				}
				// System.out.println(x + "|" + y);
				comp = new IntCodeComputer(new HashMap<>(initState), input, output, eventQueue);
				comp.start();
				put(x);
				put(y);
				while (true) {
					comp.waitForEvent();
					IntCodeComputerEvent event = eventQueue.poll();
					if (event.equals(IntCodeComputerEvent.HALT)) {
						break;
					} else if (event.equals(IntCodeComputerEvent.INPUT_REQUEST)) {

					} else if (event.equals(IntCodeComputerEvent.OUTPUT)) {
						if (readOutput(x, y) == 1L) {
							countline++;
							int gridSize = 100;
							if (countline == gridSize) {
								// System.out.println(board);
								Coordinate leftUpper = new Coordinate(x - gridSize + 1, y - gridSize + 1);
								Coordinate rightUpper = new Coordinate(x, y - gridSize + 1);
								Coordinate leftLower = new Coordinate(x - gridSize + 1, y);

								if (board.isWall(leftUpper) && board.isWall(leftLower) && board.isWall(rightUpper)) {
									System.out.println(
											"Solution: " + leftUpper + " = " + (10000 * leftUpper.x + leftUpper.y));

									System.exit(0);
								}
							}
						}
					}
				}
			}
			if (y % 100 == 0) {
				System.out.println(countline);
			}
			// System.out.println(board);
		}
		//
		// while (true) {
		// comp.waitForEvent();
		// IntCodeComputerEvent event = eventQueue.poll();
		// if (event.equals(IntCodeComputerEvent.HALT)) {
		// break;
		// } else if (event.equals(IntCodeComputerEvent.INPUT_REQUEST)) {
		//
		// } else if (event.equals(IntCodeComputerEvent.OUTPUT)) {
		// readOutput();
		// }
		// }

		// System.out.println(sum);

		// System.out.println(board);
	}

	private static long readOutput(int x, int y) {
		Long code = output.poll();
		// if (code == 10) {
		// System.out.println();
		// } else if (code < 256) {
		// System.out.print(Character.toString((char) code.intValue()));
		// } else {
		// System.out.println(code);
		// }
		Coordinate c = new Coordinate(x, y);
		if (code == 1) {
			board.put(c, "#");
			sum++;

			return code;
		} else {
			board.put(c, ".");
			return code;
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
