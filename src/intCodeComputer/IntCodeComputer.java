package intCodeComputer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
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
		int pointer = 0;

		execution: while (true) {
			Integer instruction = mem.get(pointer);
			Integer opCode = getOPCode(instruction);
			Operation operation = Operation.ofCode(opCode);
			List<Integer> argumentModes = getArgumentModes(instruction, operation.numArguments);
			List<Integer> arguments = getArguments(mem, pointer, operation, argumentModes);

			switch (operation) {
			case ADDITION:
				mem.put(arguments.get(2), arguments.get(0) + arguments.get(1));
				break;
			case HALT:
				System.out.println(this.getName() + " halting.");
				break execution;
			case INPUT:
				synchronized (input) {
					if (input.isEmpty()) {
						try {
							// System.out.println("Waiting for input");
							input.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				System.out.println(this.getName() + " taking " + input.peek());
				mem.put(arguments.get(0), input.poll());
				break;
			case MULTIPLICATION:
				mem.put(arguments.get(2), arguments.get(0) * arguments.get(1));
				break;
			case OUTPUT:
				synchronized (output) {
					output.add(arguments.get(0));
					System.out.println(this.getName() + " putting " + arguments.get(0));
					output.notifyAll();
				}
				// System.out.println(arguments.get(0));
				break;
			case EQUALS:
				mem.put(arguments.get(2), arguments.get(0).equals(arguments.get(1)) ? 1 : 0);
				break;
			case JUMP_IF_FALSE:
				if (arguments.get(0) == 0) {
					pointer = arguments.get(1);
					continue;
				}
				break;
			case JUMP_IF_TRUE:
				if (arguments.get(0) != 0) {
					pointer = arguments.get(1);
					continue;
				}
				break;
			case LESS_THAN:
				mem.put(arguments.get(2), arguments.get(0) < arguments.get(1) ? 1 : 0);
				break;
			default:
				break;
			}

			pointer += operation.numArguments + 1;
		}
	}

	public ConcurrentLinkedQueue<Integer> getOutput() {
		return this.output;
	}

	private static List<Integer> getArguments(Map<Integer, Integer> mem, int curPointer, Operation operation, List<Integer> argumentModes) {
		return IntStream.range(0, operation.numArguments).mapToObj(i -> {
			if (argumentModes.get(i) == 0
					&& !((i == 2) && (operation.equals(Operation.ADDITION) || operation.equals(Operation.MULTIPLICATION)
							|| operation.equals(Operation.LESS_THAN) || operation.equals(Operation.EQUALS)))
					&& !(i == 0 && operation.equals(Operation.INPUT))) {
				return mem.get(mem.get(curPointer + 1 + i));
			} else {
				return mem.get(curPointer + 1 + i);
			}
		}).collect(Collectors.toList());
	}

	private static List<Integer> getArgumentModes(Integer opCode, int numArguments) {
		List<Integer> modes = new ArrayList<>();

		for (int i = 0; i < numArguments; i++) {
			int divisor = 1;
			for (int j = 0; j < i + 2; j++) {
				divisor *= 10;
			}
			modes.add(opCode / divisor % 10);
		}

		return modes;
	}

	private static Integer getOPCode(Integer instruction) {
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

		public static Operation ofCode(int opCode) {
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
