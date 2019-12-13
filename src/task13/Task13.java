package task13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;

public class Task13 {
	static ConcurrentLinkedQueue<Long>	input	= new ConcurrentLinkedQueue<>(), output = new ConcurrentLinkedQueue<>();
	private static IntCodeComputer		c;

	static Coordinate					ball	= new Coordinate(0, 0);
	static Coordinate					paddle	= null;
	static int							score	= 0;
	static Board						board	= new Board();

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input13.txt"));
		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));

		c = new IntCodeComputer(initState, input, output);
		initState.put(0l, 2l);

		c.start();
		while (true) {
			if (!c.isAlive()) {
				break;
			} else if (!output.isEmpty()) {
				emptyTheOutput();
			} else if (c.requestedInput() && input.isEmpty()) {
				emptyTheOutput();
				provideInput();
			} else {
				synchronized (c) {
					try {
						System.out.println("waiting for c");
						c.wait();
						System.out.println("notified from c");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			if (!c.isAlive()) {
				break;
			} else {
				synchronized (input) {
					if (c.requestedInput() && input.isEmpty()) {
						provideInput();
					} else {

					}
				}
			}
		}
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

	private static void emptyTheOutput() {
		while (!output.isEmpty()) {
			int x = output.poll().intValue();
			waitForOutput();
			int y = output.poll().intValue();
			waitForOutput();
			int id = output.poll().intValue();

			if (x == -1 && y == 0) {
				score = id;
				System.out.println("Score: " + score);
			} else {
				Coordinate c = new Coordinate(x, y);
				board.put(c, Token.values()[id]);

				if (id == Token.BALL.ordinal()) {
					System.out.println(board);
					System.out.println("Ball: " + c);

					// dir = Direction.get(ball, c);
					// System.out.println("Direction: " + dir);
					ball = c;
				} else if (id == Token.PADDLE.ordinal()) {
					// System.out.println(board);
					// System.out.println("Ball: " + ball);
					System.out.println("Paddle: " + c);
					// System.out.println("Direction: " + dir);
					paddle = c;

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

	private static void waitForOutput() {
		synchronized (output) {
			while (c.isAlive() && output.isEmpty()) {
				try {
					output.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
