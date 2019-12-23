package task15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
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
	private static Coordinate									coords		= new Coordinate(0, 0);
	private static int											minX		= -20, maxX = 20, minY = -20, maxY = 20;
	private static Coordinate									oxygen;
	private static Coordinate									curTarget;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input15.txt"));

		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));
		comp = new IntCodeComputer(initState, input, output, eventQueue);
		comp.start();

		int size = 20;
		update(coords, Token.FREE);

		List<Coordinate> nextTargets = board.getAllCoordsInRange(new Coordinate(0, 0), size);

		while (!nextTargets.isEmpty()) {
			Coordinate nextTarget = nextTargets.stream()
					.sorted(Comparator.comparingInt(c -> c.manhattanDistanceTo(coords))).findAny().get();
			nextTargets.remove(nextTarget);
			if (!board.isOccupied(nextTarget)) {
				System.out.println("TARGET: " + nextTarget);
				moveTo(nextTarget);
			}
		}

		int max = 0;
		for (int x = -size; x <= size; x++) {
			for (int y = -size; y <= size; y++) {
				Path route = board.getShortestPath(oxygen, new Coordinate(x, y));
				if (route != null) {
					if (route.size() - 1 > max) {
						max = route.size() - 1;
					}
				}

			}
		}

		System.out.println(max);

	}

	private static void moveTo(Coordinate target) {
		if (target.x == -16 && target.y == -6) {
			printBoard();
		}
		curTarget = target;
		Path shortest = board.getShortestPath(coords, target);

		int i = 1;
		do {
			if (shortest != null) {
				Coordinate nextStep = shortest.get(i++);
				Direction d = Direction.get(coords, nextStep);
				put(d.toLong());
				waitForOutput();

				long reaction = output.poll();
				if (reaction == 0l) {
					update(nextStep, Token.WALL);
					if (nextStep.equals(target)) {
						System.out.println("UNREACHABLE: " + target);
						break;
					} else {
						shortest = board.getShortestPath(coords, target);
						i = 1;
					}
				} else if (reaction == 1l) {
					coords = nextStep;
					update(coords, Token.FREE);
				} else if (reaction == 2l) {
					System.out.println("FOUND!");
					coords = nextStep;
					oxygen = coords;
					update(coords, Token.SOLUTION);
				}
			} else {
				System.out.println("UNREACHABLE: " + target);
				break;
			}
		} while (!coords.equals(target));
	}

	private static void findShortestSolutionPath(Coordinate c1, Coordinate c2) {
		Path route = board.getShortestPath(c1, c2);
		int guess = route.size();
		moveTo(c2);
		route = board.getShortestPath(c2, c1);
		if (route.size() == guess) {
			System.out.println("Shortest Path confirmed! ");
			System.out.println(Arrays.toString(route.toArray()));
			System.out.println(route);
			System.out.println(route.size() - 1);
		} else {
			findShortestSolutionPath(c2, c1);
		}
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

	private static void update(Coordinate nextStep, Token token) {
		board.put(nextStep, token);
		minX = Math.min(minX, nextStep.x);
		maxX = Math.max(maxX, nextStep.x);
		minY = Math.min(minY, nextStep.y);
		maxY = Math.max(maxY, nextStep.y);
		printBoard();

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {

		}
	}

	private static void printBoard() {
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				Coordinate c = new Coordinate(x, y);
				if (coords.equals(c)) {
					System.out.print("X");
				} else if (!board.isOccupied(c)) {
					System.out.print("○");
				} else if (curTarget.equals(c)) {
					System.out.print("G");
				} else if (c.x == 0 && c.y == 0) {
					System.out.print("*");
				} else {
					System.out.print(board.get(c));
				}
				System.out.print(" ");
			}
			System.out.println();
		}

		System.out.println();
	}

	private static void put(long nextInput) {
		synchronized (input) {
			input.add(nextInput);
			input.notifyAll();
		}
	}
}
