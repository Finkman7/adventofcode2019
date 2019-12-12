package task12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class Task12 {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input12.txt"));

		List<Planet> planets = new ArrayList<>();
		for (String line : lines) {
			Planet p = new Planet(line);
			planets.add(p);
		}

		Map<Integer, Map<AxisState, Long>> axisStates = new HashMap<>();
		for (int i = 0; i < 3; i++) {
			axisStates.put(i, new HashMap<>());
		}

		Map<Integer, Long> periodStarts = new HashMap<>();
		Map<Integer, Long> periods = new HashMap<>();

		long z = 0l;
		loop: while (true) {
			// if (z == 1000) {
			// System.out.println(planets.stream().mapToLong(p -> p.getEnergy()).sum());
			// }

			// System.out.println("\nAfter " + z + " steps:");
			// for (Planet p : planets) {
			// System.out.println(p);
			// }

			for (int i = 0; i < 3; i++) {
				if (periodStarts.containsKey(i)) {
					continue;
				}

				Long[] values;
				if (i == 0) {
					values = planets.stream().flatMapToLong(p -> LongStream.of(p.pos.x, p.v.x)).boxed()
							.toArray(Long[]::new);
				} else if (i == 1) {
					values = planets.stream().flatMapToLong(p -> LongStream.of(p.pos.y, p.v.y)).boxed()
							.toArray(Long[]::new);
				} else {
					values = planets.stream().flatMapToLong(p -> LongStream.of(p.pos.z, p.v.z)).boxed()
							.toArray(Long[]::new);
				}

				AxisState axisState = new AxisState(values);

				if (axisStates.get(i).containsKey(axisState)) {
					Long start = axisStates.get(i).get(axisState);
					long period = z - start;
					System.out.println("Period " + i + " every " + period + " from " + z);
					periodStarts.put(i, start);
					periods.put(i, period);

					if (periods.keySet().size() == 3) {
						long latestStart = periodStarts.values().stream().mapToLong(l -> Long.valueOf(l)).max()
								.getAsLong();
						System.out.println(latestStart);
						System.out.println(latestStart + lcm(periods.values().stream().toArray(Long[]::new)));
						break loop;
					}
				} else {
					axisStates.get(i).put(axisState, z);
				}
			}

			for (Planet p1 : planets) {
				for (Planet p2 : planets) {
					if (p1 == p2) {
						continue;
					}

					p1.adjustVelocity(p2);
				}
			}

			for (Planet p : planets) {
				p.adjustPosition();
			}

			z++;
		}
	}

	private static long gcd(Long x, Long y) {
		return (y == 0) ? x : gcd(y, x % y);
	}

	public static long gcd(Long... numbers) {
		return Arrays.stream(numbers).reduce(0l, (x, y) -> gcd(x, y));
	}

	public static long lcm(Long... numbers) {
		return Arrays.stream(numbers).reduce(1l, (x, y) -> x * (y / gcd(x, y)));
	}
}
