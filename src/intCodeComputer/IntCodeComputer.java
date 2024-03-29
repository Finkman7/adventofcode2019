package intCodeComputer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class IntCodeComputer extends Thread {
	private Map<Long, Long>								mem;
	private ConcurrentLinkedQueue<Long>					input, output;
	private ConcurrentLinkedQueue<IntCodeComputerEvent>	eventQueue;
	private long										instruction_pointer	= 0l, relBase = 0l;
	private static long									computers			= 0;
	private final long									id					= computers++;
	private long										outputCount			= 0;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntCodeComputer other = (IntCodeComputer) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	@Override
	public long getId() {
		return this.id;
	}

	public IntCodeComputer(Map<Long, Long> mem, ConcurrentLinkedQueue<Long> input, ConcurrentLinkedQueue<Long> output,
			ConcurrentLinkedQueue<IntCodeComputerEvent> eventQueue) {
		this.mem = mem;
		this.input = input;
		this.output = output;
		this.eventQueue = eventQueue;
	}

	@Override
	public void run() {
		String name = "IntCodeComputer-" + this.getId();

		execution: while (true) {
			long instruction = read(instruction_pointer);
			int opCode = getOPCodeFrom(instruction);
			Operation operation = Operation.ofOPCode(opCode);
			long[] argumentModes = getArgumentModes(instruction, operation.numArguments);
			long[] p_args = getArgumentPointers(instruction_pointer, operation.numArguments, argumentModes);

			switch (operation) {
			case ADDITION:
				mem.put(p_args[2], read(p_args[0]) + read(p_args[1]));
				break;
			case MULTIPLICATION:
				mem.put(p_args[2], read(p_args[0]) * read(p_args[1]));
				break;
			case INPUT:
				synchronized (input) {
					if (input.isEmpty()) {
						fireEvent(IntCodeComputerEventType.INPUT_REQUEST);
					}
					while (input.isEmpty()) {
						try {
							input.wait();
						} catch (InterruptedException e) {
						}
					}
				}
				mem.put(p_args[0], input.poll());
				break;
			case OUTPUT:
				synchronized (output) {
					output.add(read(p_args[0]));
					output.notifyAll();
					// System.out.println(this + " firing output " + read(p_args[0]));
					fireEvent(IntCodeComputerEventType.OUTPUT);
					// System.out.println(name + " [out]-->\t" + read(p_args[0]));
				}
				break;
			case EQUALS:
				mem.put(p_args[2], read(p_args[0]).equals(read(p_args[1])) ? 1l : 0l);
				break;
			case LESS_THAN:
				mem.put(p_args[2], read(p_args[0]) < read(p_args[1]) ? 1l : 0l);
				break;
			case JUMP_IF_FALSE:
				if (read(p_args[0]) == 0) {
					instruction_pointer = read(p_args[1]);
					continue;
				}
				break;
			case JUMP_IF_TRUE:
				if (read(p_args[0]) != 0) {
					instruction_pointer = read(p_args[1]);
					continue;
				}
				break;
			case HALT:
				// System.out.println(name + " halting.");
				fireEvent(IntCodeComputerEventType.HALT);
				break execution;
			case RELBASE_OFFSET:
				relBase += read(p_args[0]);
				break;
			default:
				System.err.println(name + " halting because of unknown opcode.");
				break execution;
			}

			instruction_pointer += 1 + operation.numArguments;
		}
	}

	private Long read(long address) {
		Long value = mem.get(address);

		return value != null ? value : 0l;
	}

	private static int getOPCodeFrom(long instruction) {
		return (int) (instruction % 100);
	}

	private long[] getArgumentModes(Long opCode, int numArguments) {
		long[] modes = new long[numArguments];

		for (int i = 0; i < numArguments; i++) {
			long divisor = 1;
			for (int j = 0; j < i + 2; j++) {
				divisor *= 10;
			}
			modes[i] = (opCode / divisor % 10);
		}

		return modes;
	}

	private long[] getArgumentPointers(long curPointer, int numArguments, long[] argumentModes) {
		return IntStream.range(0, numArguments).mapToLong(i -> {
			if (argumentModes[i] == 0) {
				return read(curPointer + 1 + i);
			} else if (argumentModes[i] == 1) {
				return curPointer + 1 + i;
			} else {
				return relBase + read(curPointer + 1 + i);
			}
		}).toArray();
	}

	private void fireEvent(IntCodeComputerEventType type) {
		if (type.equals(IntCodeComputerEventType.OUTPUT) && outputCount++ % 3 != 1) {
			return;
		}

		synchronized (eventQueue) {
			eventQueue.add(new IntCodeComputerEvent(type, this));
			eventQueue.notifyAll();
		}
	}

	public void waitForEvent() {
		synchronized (eventQueue) {
			while (eventQueue.isEmpty()) {
				try {
					eventQueue.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void waitForOutput() {
		waitForEvent();
		IntCodeComputerEvent event = eventQueue.poll();
		if (!event.type.equals(IntCodeComputerEventType.OUTPUT)) {
			System.err.println("Protocol Error! Output expected but got " + event + " event");
		}
	}

	public void waitForInputRequest() {
		waitForEvent();
		IntCodeComputerEvent event = eventQueue.poll();
		if (!event.type.equals(IntCodeComputerEventType.INPUT_REQUEST)) {
			System.err.println("Protocol Error! Input request expected but got " + event + " event");
		}
	}

	public void skipToOutput() {
		IntCodeComputerEvent event;
		do {
			waitForEvent();
			event = eventQueue.poll();
		} while (!event.type.equals(IntCodeComputerEventType.OUTPUT));
	}

	public void skipToInputRequest() {
		IntCodeComputerEvent event;
		do {
			waitForEvent();
			event = eventQueue.poll();
		} while (!event.type.equals(IntCodeComputerEventType.INPUT_REQUEST));
	}

	public ConcurrentLinkedQueue<Long> getOutput() {
		return this.output;
	}

	public static HashMap<Long, Long> initMemory(String line) {
		HashMap<Long, Long> mem = new HashMap<>();
		String[] tokens = line.split(",");

		for (int i = 0; i < tokens.length; i++) {
			mem.put((long) i, Long.valueOf(tokens[i]));
		}

		return mem;
	}

	@Override
	public String toString() {
		return "IntCodeComputer " + getId();
	}

	private enum Operation {
		ADDITION(3), MULTIPLICATION(3), HALT(0), INPUT(1), OUTPUT(1), JUMP_IF_TRUE(2), JUMP_IF_FALSE(2), LESS_THAN(
				3), EQUALS(3), RELBASE_OFFSET(1);

		public final int numArguments;

		Operation(int numArgument) {
			this.numArguments = numArgument;
		}

		public static Operation ofOPCode(int opCode) {
			switch (opCode) {
			case 1:
				return ADDITION;
			case 2:
				return MULTIPLICATION;
			case 99:
				return HALT;
			case 3:
				return INPUT;
			case 4:
				return OUTPUT;
			case 5:
				return JUMP_IF_TRUE;
			case 6:
				return JUMP_IF_FALSE;
			case 7:
				return LESS_THAN;
			case 8:
				return EQUALS;
			case 9:
				return RELBASE_OFFSET;
			}

			return null;
		}

	}
}
