package task17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	private static Coordinate									coords		= new Coordinate(0, 0);
	private static int											minX		= -20, maxX = 20, minY = -20, maxY = 20;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input17.txt"));

		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));
		// comp = new IntCodeComputer(initState, input, output, eventQueue);
		// comp.start();
		//
		// int x = 0;
		// int y = 0;
		// while (comp.isAlive()) {
		// comp.waitForEvent();
		// IntCodeComputerEvent event = eventQueue.poll();
		// if (event.equals(IntCodeComputerEvent.HALT)) {
		// break;
		// }
		//
		// Long code = output.poll();
		// if (code == 10) {
		// y++;
		// x = 0;
		// } else {
		// Coordinate c = new Coordinate(x, y);
		// if (code != 46 && code != 35) {
		// coords = c;
		// }
		// board.put(c, Character.toString((char) code.intValue()));
		// x++;
		// }
		// }
		//
		// System.out.println(board);
		// // System.out.println(board.intersectionParameters());
		// System.out.println(coords);
		//
		// Direction cur = Direction.UP;
		// Coordinate next = coords.neighbour(cur);
		// int steps = 0;
		// while (true) {
		// while (board.isScaffold(next)) {
		// coords = next;
		// next = coords.neighbour(cur);
		// steps++;
		// }
		//
		// Direction turnedLeft = cur.turnLeft();
		// Direction turnedRight = cur.turnRight();
		// if (board.isScaffold(coords.neighbour(turnedLeft))) {
		// System.out.print(steps + "," + "L");
		// cur = turnedLeft;
		// } else if (board.isScaffold(coords.neighbour(turnedRight))) {
		// System.out.print(steps + "," + "R");
		// cur = turnedRight;
		// } else {
		// System.out.print(steps);
		// break;
		// }
		//
		// coords = coords.neighbour(cur);
		// next = coords.neighbour(cur);
		// steps = 1;
		// }
		//
		// System.out.println();

		String input1 = "A,B,A,C,B,C,B,C,A,C";
		String inputA = "R,12,L,6,R,12";
		String inputB = "L,8,L,6,L,10";
		String inputC = "R,12,L,10,L,6,R,10";
		String inputVideo = "n";

		initState = IntCodeComputer.initMemory(lines.get(0));
		initState.put(0l, 2l);
		comp = new IntCodeComputer(initState, input, output, eventQueue);
		comp.start();
		int x = 0;
		int y = 0;
		while (comp.isAlive()) {
			comp.waitForEvent();
			IntCodeComputerEvent event = eventQueue.poll();
			if (event.equals(IntCodeComputerEvent.HALT)) {
				break;
			} else if (event.equals(IntCodeComputerEvent.INPUT_REQUEST)) {
				System.out.println(board);
				break;
			}

			Long code = output.poll();
			if (code == 10) {
				y++;
				x = 0;
			} else {
				Coordinate c = new Coordinate(x, y);
				if (code != 46 && code != 35) {
					coords = c;
				}
				board.put(c, Character.toString((char) code.intValue()));
				x++;
			}
		}

		feed(input1);
		readOutputs();
		feed(inputA);
		readOutputs();
		feed(inputB);
		readOutputs();
		feed(inputC);
		readOutputs();
		feed(inputVideo);
		readOutputs();

		// waitForOutput();
		// long result = output.poll();
		// System.out.println(result);
	}

	private static void readOutputs() {
		while (comp.isAlive()) {
			comp.waitForEvent();
			IntCodeComputerEvent event = eventQueue.poll();
			if (event.equals(IntCodeComputerEvent.HALT)) {
				System.exit(0);
			} else if (event.equals(IntCodeComputerEvent.INPUT_REQUEST)) {
				return;
			}

			Long code = output.poll();
			if (code == 10) {
				System.out.println();
			} else if (code < 256) {
				System.out.print(Character.toString((char) code.intValue()));
			} else {
				System.out.println(code);
			}
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

	private static void waitForOutput() {
		synchronized (output) {
			while (output.isEmpty()) {
				try {
					output.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static void put(long nextInput) {
		synchronized (input) {
			input.add(nextInput);
			input.notifyAll();
		}
	}
}
