package task13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;
import intCodeComputer.IntCodeComputerEvent;

public class Task13 {
	private static ConcurrentLinkedQueue<Long>					input		= new ConcurrentLinkedQueue<>(),
			output = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<IntCodeComputerEvent>	eventQueue	= new ConcurrentLinkedQueue<>();
	private static IntCodeComputer								c;

	static Board												board		= new Board();
	static Coordinate											ball		= new Coordinate(0, 0);
	static Coordinate											paddle		= null;
	static int													score		= 0;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input13.txt"));
		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));

		c = new IntCodeComputer(initState, input, output, eventQueue);
		initState.put(0l, 2l);

		c.start();
		while (true) {
			c.waitForEvent();

			IntCodeComputerEvent event = eventQueue.poll();
			if (event.equals(IntCodeComputerEvent.HALT)) {
				break;
			} else if (event.equals(IntCodeComputerEvent.INPUT_REQUEST)) {
				printState();
				provideInput();
			} else if (event.equals(IntCodeComputerEvent.OUTPUT)) {
				processOutput();
			}
		}

		printState();
	}

	private static void provideInput() {
		if (Coordinate.byX.compare(ball, paddle) < 0) {
			System.out.println(paddle + " Paddle LEFT");
			put(-1l);
		} else if (Coordinate.byX.compare(ball, paddle) == 0) {
			System.out.println(paddle + " Paddle STAY");
			put(0l);
		} else {
			System.out.println(paddle + " Paddle RIGHT");
			put(1l);
		}
	}

	private static void put(long nextInput) {
		synchronized (input) {
			input.add(nextInput);
			input.notifyAll();
		}
	}

	private static void processOutput() {
		int x = output.poll().intValue();

		c.waitForOutput();
		int y = output.poll().intValue();
		c.waitForOutput();
		int arg = output.poll().intValue();

		if (x == -1 && y == 0) {
			score = arg;
		} else {
			Coordinate c = new Coordinate(x, y);
			Token token = Token.values()[arg];
			board.put(c, token);

			if (token.equals(Token.BALL)) {
				ball = c;
			} else if (token.equals(Token.PADDLE)) {
				paddle = c;
			}
		}
	}

	private static void printState() {
		System.out.println(board);
		System.out.println("Score: " + score);
		System.out.println("Ball: " + ball);
		System.out.println("Paddle: " + paddle);
	}
}
