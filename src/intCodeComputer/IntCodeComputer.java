package intCodeComputer;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class IntCodeComputer extends Thread {
	private Map<Long, Long>				mem;
	private ConcurrentLinkedQueue<Long>	input;
	private ConcurrentLinkedQueue<Long>	output;
	private long						instruction_pointer	= 0l, relBase = 0l;

	public IntCodeComputer(Map<Long, Long> mem, ConcurrentLinkedQueue<Long> input, ConcurrentLinkedQueue<Long> output) {
		this.mem = mem;
		this.input = input;
		this.output = output;
	}

	@Override
	public void run() {
		String name = "LongCodeComputer-" + this.getId();

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
						try {
							input.wait();
						} catch (InterruptedException e) {
						}
					}
				}
				System.out.println(name + " <--[in]\t" + input.peek());
				mem.put(p_args[0], input.poll());
				break;
			case OUTPUT:
				synchronized (output) {
					output.add(read(p_args[0]));
					System.out.println(name + " [out]-->\t" + read(p_args[0]));
					output.notifyAll();
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
				System.out.println(name + " halting.");
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

	public ConcurrentLinkedQueue<Long> getOutput() {
		return this.output;
	}

	public static void memoryFromFile(Map<Long, Long> mem, String line) {
		String[] tokens = line.split(",");

		for (int i = 0; i < tokens.length; i++) {
			mem.put((long) i, Long.valueOf(tokens[i]));
		}
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
