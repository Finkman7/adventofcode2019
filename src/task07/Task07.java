package task07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import intCodeComputer.IntCodeComputer;

public class Task07 {

	private static HashMap initState;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input07.txt"));
		initState = new HashMap<>();

		for (String line : lines) {
			IntCodeComputer.memoryFromFile(initState, line);
		}

		// a
		// int bestOutput = 0;
		// for (int a = 0; a <= 4; a++) {
		// for (int b = 0; b <= 4; b++) {
		// for (int c = 0; c <= 4; c++) {
		// for (int d = 0; d <= 4; d++) {
		// for (int e = 0; e <= 4; e++) {
		// if (a == b || a == c || a == d || a == e || b == c || b == d || b == e || c == d || c == e
		// || d == e) {
		// continue;
		// }
		// List<Integer> combination = List.of(a, b, c, d, e);
		// int lastOutput = 0;
		// for (int i = 0; i < 5; i++) {
		// List<Integer> input = List.of(combination.get(i), lastOutput);
		// lastOutput = IntCodeComputer.execute((Map<Integer, Integer>) initState.clone(), input)
		// .get(0);
		// }
		//
		// if (lastOutput > bestOutput) {
		// bestOutput = lastOutput;
		// }
		// }
		// }
		// }
		// }
		// }
		//
		// System.out.println(bestOutput);

		// b
		int bestOutput = 0;
		for (int a = 5; a <= 9; a++) {
			for (int b = 5; b <= 9; b++) {
				for (int c = 5; c <= 9; c++) {
					for (int d = 5; d <= 9; d++) {
						for (int e = 5; e <= 9; e++) {
							if (a == b || a == c || a == d || a == e || b == c || b == d || b == e || c == d || c == e
									|| d == e) {
								continue;
							}
							List<Integer> combination = List.of(a, b, c, d, e);

							Map<Integer, IntCodeComputer> amplifiers = new HashMap<>();
							ConcurrentLinkedQueue<Integer> feedbackLink = new ConcurrentLinkedQueue<>();
							for (int i = 0; i < 5; i++) {
								ConcurrentLinkedQueue<Integer> input, output;
								if (i == 0) {
									input = feedbackLink;
									feedbackLink.add(combination.get(i));
									feedbackLink.add(0);
								} else {
									input = amplifiers.get(i - 1).getOutput();
									input.add(combination.get(i));
								}
								if (i == 4) {
									output = feedbackLink;
								} else {
									output = new ConcurrentLinkedQueue<>();
								}

								IntCodeComputer amp = new IntCodeComputer((Map<Integer, Integer>) initState.clone(),
										input, output);
								amplifiers.put(i, amp);
							}

							for (int i : amplifiers.keySet()) {
								amplifiers.get(i).start();
							}

							try {
								amplifiers.get(4).join();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							int output = amplifiers.get(4).getOutput().poll();
							if (output > bestOutput) {
								bestOutput = output;
							}
						}

					}
				}
			}
		}

		System.out.println(bestOutput);
	}
}
