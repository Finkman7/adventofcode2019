package task11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;
import intCodeComputer.IntCodeComputerEventType;

public class Task11 {
	static ConcurrentLinkedQueue<Long>					input		= new ConcurrentLinkedQueue<>(),
			output = new ConcurrentLinkedQueue<>();
	static ConcurrentLinkedQueue<IntCodeComputerEventType>	eventQueue	= new ConcurrentLinkedQueue<>();
	private static IntCodeComputer						c;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input11.txt"));
		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));

		c = new IntCodeComputer(initState, input, output, eventQueue);
		c.start();

		Board board = new Board();
		Coordinate cur = new Coordinate(0, 0);
		Direction dir = Direction.UP;
		board.put(cur, "#");
		Set<Coordinate> visited = new HashSet<>();
		while (true) {
			c.waitForEvent();
			IntCodeComputerEventType event = eventQueue.poll();

			if (event.equals(IntCodeComputerEventType.HALT)) {
				break;
			} else if (event.equals(IntCodeComputerEventType.INPUT_REQUEST)) {
				System.out.println(board.paint(cur, dir));
				visited.add(cur);

				long nextInput = 0l;
				if (!board.isOccupied(cur) || board.get(cur).equals("#")) {
					nextInput = 1l;
				}
				put(nextInput);
			} else if (event.equals(IntCodeComputerEventType.OUTPUT)) {
				long color = output.poll();
				if (color == 0) {
					board.put(cur, ".");
				} else {
					board.put(cur, "#");
				}

				c.waitForOutput();
				long turn = output.poll();
				if (turn == 0) {
					dir = dir.turnLeft();
				} else if (turn == 1) {
					dir = dir.turnRight();
				}

				cur = cur.neighbour(dir);
			}
		}

		System.out.println(visited.size());
		System.out.println(board);
	}

	private static void put(long nextInput) {
		synchronized (input) {
			input.add(nextInput);
			input.notifyAll();
		}
	}
}
