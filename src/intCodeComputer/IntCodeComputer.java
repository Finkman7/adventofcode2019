package intCodeComputer;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

public class IntCodeComputer extends Thread {
	private Map<Integer, Integer>			mem;
	private ConcurrentLinkedQueue<Integer>	input;
	private ConcurrentLinkedQueue<Integer>	output;

	public IntCodeComputer(Map<Integer, Integer> mem, ConcurrentLinkedQueue<Integer> input,
			ConcurrentLinkedQueue<Integer> output) {
		this.mem = mem;
		this.input = input;
		this.output = output;
	}

	@Override
	public void run() {
		int instruction_pointer = 0;
		String name = "IntCodeComputer-" + this.getId();

		execution: while (true) {
			Integer instruction = mem.get(instruction_pointer);
			Integer opCode = getOPCodeFrom(instruction);
			Operation operation = Operation.ofOPCode(opCode);
			int[] argumentModes = getArgumentModes(instruction, operation.numArguments);
			int[] p_args = getArgumentPointers(instruction_pointer, operation.numArguments, argumentModes);

			switch (operation) {
			case ADDITION:
				mem.put(p_args[2], mem.get(p_args[0]) + mem.get(p_args[1]));
				break;
			case MULTIPLICATION:
				mem.put(p_args[2], mem.get(p_args[0]) * mem.get(p_args[1]));
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
					output.add(mem.get(p_args[0]));
					System.out.println(name + " [out]-->\t" + mem.get(p_args[0]));
					output.notifyAll();
				}
				break;
			case EQUALS:
				mem.put(p_args[2], mem.get(p_args[0]).equals(mem.get(p_args[1])) ? 1 : 0);
				break;
			case LESS_THAN:
				mem.put(p_args[2], mem.get(p_args[0]) < mem.get(p_args[1]) ? 1 : 0);
				break;
			case JUMP_IF_FALSE:
				if (mem.get(p_args[0]) == 0) {
					instruction_pointer = mem.get(p_args[1]);
					continue;
				}
				break;
			case JUMP_IF_TRUE:
				if (mem.get(p_args[0]) != 0) {
					instruction_pointer = mem.get(p_args[1]);
					continue;
				}
				break;
			case HALT:
				System.out.println(name + " halting.");
				break execution;
			default:
				break;
			}

			instruction_pointer += 1 + operation.numArguments;
		}
	}

	public ConcurrentLinkedQueue<Integer> getOutput() {
		return this.output;
	}

	private int[] getArgumentPointers(int curPointer, int numArguments, int[] argumentModes) {
		return IntStream.range(0, numArguments).map(i -> {
			if (argumentModes[i] == 0) {
				return mem.get(curPointer + 1 + i);
			} else {
				return curPointer + 1 + i;
			}
		}).toArray();
	}

	private int[] getArgumentModes(Integer opCode, int numArguments) {
		int[] modes = new int[numArguments];

		for (int i = 0; i < numArguments; i++) {
			int divisor = 1;
			for (int j = 0; j < i + 2; j++) {
				divisor *= 10;
			}
			modes[i] = (opCode / divisor % 10);
		}

		return modes;
	}

	private static Integer getOPCodeFrom(Integer instruction) {
		return instruction % 100;
	}

	public static void memoryFromFile(Map<Integer, Integer> mem, String line) {
		String[] tokens = line.split(",");

		for (int i = 0; i < tokens.length; i++) {
			mem.put(i, Integer.valueOf(tokens[i]));
		}
	}

	private enum Operation {
		ADDITION(3), MULTIPLICATION(3), HALT(0), INPUT(1), OUTPUT(1), JUMP_IF_TRUE(2), JUMP_IF_FALSE(2), LESS_THAN(
				3), EQUALS(3);

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
			}

			return null;
		}

	}

}
