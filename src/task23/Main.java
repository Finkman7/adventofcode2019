package task23;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import intCodeComputer.IntCodeComputer;
import intCodeComputer.IntCodeComputerEvent;
import intCodeComputer.IntCodeComputerEventType;

public class Main {
	private static final int											IDLE_THRESHOLD	= 2;

	private static List<IntCodeComputer>								computers		= new ArrayList<>();
	private static Map<IntCodeComputer, ConcurrentLinkedQueue<Long>>	outputs			= new HashMap<>();
	private static Map<IntCodeComputer, ConcurrentLinkedQueue<Long>>	inputs			= new HashMap<>();
	private static ConcurrentLinkedQueue<IntCodeComputerEvent>			eventQueue		= new ConcurrentLinkedQueue<>();
	private static Map<IntCodeComputer, Integer>						idle;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input23.txt"));

		Map<Long, Long> initState = IntCodeComputer.initMemory(lines.get(0));

		for (int i = 0; i < 50; i++) {
			ConcurrentLinkedQueue<Long> input = new ConcurrentLinkedQueue<>(), output = new ConcurrentLinkedQueue<>();
			input.add((long) i);

			IntCodeComputer comp = new IntCodeComputer(new HashMap<>(initState), input, output, eventQueue);
			computers.add(comp);
			inputs.put(comp, input);
			outputs.put(comp, output);

			comp.start();
		}

		idle = computers.stream().collect(Collectors.toMap(c -> c, c -> -1));
		// idle.put(computers.get(36), 0);

		Long lastX = null, lastY = null;
		Long xToSend = null, yToSend = null;
		while (true) {
			if (eventQueue.isEmpty() && !idle.values().stream().filter(v -> v < IDLE_THRESHOLD).findAny().isPresent()) {
				// System.out.println(idle.get(computers.get(23)));
				if (xToSend == lastX && yToSend == lastY) {
					System.out.println("Solution: " + yToSend);
					System.exit(0);
				} else {
					System.out.println("WAKING with " + xToSend + "|" + yToSend);
					put(computers.get(0), xToSend, yToSend);
					lastX = xToSend;
					lastY = yToSend;
				}
			}

			computers.get(0).waitForEvent();
			IntCodeComputerEvent event = eventQueue.poll();
			if (event.type.equals(IntCodeComputerEventType.HALT)) {
				System.out.println("Halting");
				System.exit(0);
				break;
			} else if (event.type.equals(IntCodeComputerEventType.INPUT_REQUEST)) {
				IntCodeComputer source = event.source;

				idle.put(source, idle.get(source) + 1);

				if (idle.get(source) < IDLE_THRESHOLD) {
					put(source, -1L);
				}
			} else if (event.type.equals(IntCodeComputerEventType.OUTPUT)) {
				IntCodeComputer source = event.source;
				idle.put(source, 0);
				ConcurrentLinkedQueue<Long> queue = outputs.get(source);
				long address = queue.poll();
				waitFor(queue);
				long x = queue.poll();
				waitFor(queue);
				long y = queue.poll();

				if (address < 50) {
					put(computers.get((int) address), x, y);
					System.out.println(source.getId() + " -> " + address + ": " + x + " | " + y);
				} else if (address == 255) {
					xToSend = x;
					yToSend = y;
					System.out.println(source.getId() + " -> " + address + ": " + x + " | " + y);
				}
			}
		}
	}

	private static void waitFor(ConcurrentLinkedQueue<Long> queue) {
		synchronized (queue) {
			while (queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void put(IntCodeComputer intCodeComputer, Long... values) {
		ConcurrentLinkedQueue<Long> queue = inputs.get(intCodeComputer);

		synchronized (queue) {
			for (Long v : values) {
				queue.add(v);
			}
			queue.notifyAll();
		}

		if (values.length > 1 || values[0] != -1) {
			idle.put(intCodeComputer, 0);
		} else {
			System.out.println("Putting " + Arrays.toString(values) + " to " + intCodeComputer);
		}
	}
}
