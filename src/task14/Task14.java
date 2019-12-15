package task14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Task14 {
	private static Map<String, Recipe>	recipes;
	private static Set<String>			baseMaterials	= new HashSet<>();
	private static Comparator<String>	byRecipeLength	= Comparator
			.comparingInt(ing -> recipes.get(ing).getIngredients().size());
	private static long					maxOre			= 1000000000000l;

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input14.txt"));

		recipes = readRecipes(lines);

		for (long x = 0l; x < Long.MAX_VALUE; x++) {
			long cost = getORECostsNew(x, "FUEL");
			if (cost > maxOre) {
				System.err.println(x - 1);
				System.exit(0);
			} else {
				System.out.println(x);
			}
		}

		long a = 0;
		long b = 10000000000l;
		do {
			long fuel = (b - a) / 2 + a;
			System.out.println(fuel + " - " + a + "/" + b);
			long requiredOre = getORECostsNew(fuel, "FUEL");
			if (requiredOre < maxOre) {
				a = fuel;
				if (fuel == b - 1) {
					break;
				}
			} else if (requiredOre > maxOre) {
				b = fuel;
			} else {
				a = fuel;
				b = fuel;
			}
		} while (a < b);

		System.out.println(a + " -> " + getORECostsNew(a, "FUEL"));
		System.out.println(b + " -> " + getORECostsNew(b, "FUEL"));
	}

	private static long getORECostsNew(long N, String string) {
		Map<String, Long> neededQuantities = new HashMap<>();
		Map<String, Long> overflowQuantities = new HashMap<>();

		add(neededQuantities, new Quantity(N, string));

		while (neededQuantities.keySet().stream().filter(i -> !i.equals("ORE")).findAny().isPresent()) {
			// String ing = neededQuantities.keySet().stream().filter(i -> !i.equals("ORE"))
			// .sorted(byRecipeLength.reversed()).findAny().get();
			String ing = neededQuantities.keySet().stream().filter(i -> !i.equals("ORE"))
					.sorted(byOccurence(neededQuantities)).findAny().get();
			long requiredOfIng = neededQuantities.get(ing);
			// System.out.println("\t\t" + overflowQuantities + " , " + neededQuantities);
			// System.out.print("\nREPLACE " + requiredOfIng + " " + ing);
			if (overflowQuantities.containsKey(ing)) {
				long overflowOfQ = overflowQuantities.get(ing);

				if (overflowOfQ <= requiredOfIng) {
					overflowQuantities.remove(ing);
					requiredOfIng -= overflowOfQ;
				} else {
					overflowQuantities.put(ing, overflowOfQ - requiredOfIng);
				}

				if (requiredOfIng == 0) {
					// System.out.println(" (" + requiredOfIng + " " + ing + " after overflow)");
					neededQuantities.remove(ing);
					continue;
				}
			}

			// System.out.println(" (" + requiredOfIng + " " + ing + " after overflow)");
			Recipe r = recipes.get(ing);
			long formulaNeeded = requiredOfIng / r.head.N + (requiredOfIng % r.head.N == 0 ? 0 : 1);

			// System.out.print("\t" + r + " is required " + formulaNeeded + " times ");
			long overflow = r.head.N * formulaNeeded - requiredOfIng;
			if (overflow > 0) {
				overflowQuantities.put(ing, r.head.N * formulaNeeded - requiredOfIng);
			}
			// System.out.println(" ( -> " + overflow + " overflow " + ing + ").");

			for (Quantity q : r.reqs) {
				long requiredOfQ = formulaNeeded * q.N;
				// System.out.println("\t" + requiredOfQ + " " + q.ing + " required.");

				if (!neededQuantities.containsKey(q.ing)) {
					neededQuantities.put(q.ing, requiredOfQ);
				} else {
					neededQuantities.put(q.ing, neededQuantities.get(q.ing) + requiredOfQ);
				}
			}

			neededQuantities.remove(ing);
		}

		overflowQuantities.entrySet().stream().filter(e -> recipes.get(e.getKey()).head.N <= e.getValue())
				.forEach(e -> {
					System.err.println(e);
				});

		overflowQuantities.entrySet().stream().filter(e -> recipes.get(e.getKey()).head.N > e.getValue()).forEach(e -> {
			// System.out.println(e.getValue() + " < " + recipes.get(e.getKey()).head.N);
		});

		// System.out.println(overflowQuantities);
		// System.out.println(neededQuantities);
		return neededQuantities.values().iterator().next();
	}

	private static Comparator<String> byOccurence(Map<String, Long> neededQuantities) {
		return Comparator.comparingLong(ing -> neededQuantities.keySet().stream()
				.filter(ing1 -> !ing1.equals("ORE") && recipes.get(ing1).getIngredients().contains(ing)).count());
	}

	private static void add(Map<String, Long> neededQuantities, Quantity q) {
		if (!neededQuantities.containsKey(q.ing)) {
			neededQuantities.put(q.ing, q.N);
		} else {
			neededQuantities.put(q.ing, neededQuantities.get(q.ing) + q.N);
		}
	}

	private static Map<String, Recipe> readRecipes(List<String> lines) {
		Map<String, Recipe> recipes = new LinkedHashMap<>();

		for (String line : lines) {
			String[] tokens = line.split(" => ");
			Quantity head = parseQuantity(tokens[1]);
			List<Quantity> reqs = new ArrayList<>(tokens.length - 1);
			String[] reqTokens = tokens[0].split(", ");
			for (int i = 0; i < reqTokens.length; i++) {
				reqs.add(parseQuantity(reqTokens[i]));
			}

			if (recipes.containsKey(head.ing)) {
				System.err.println("Double Recipe!");
				System.exit(0);
			}
			recipes.put(head.ing, new Recipe(head, reqs));
			if (reqs.size() == 1 && reqs.get(0).toString().endsWith("ORE")) {
				baseMaterials.add(head.ing);
			}
		}

		return recipes;
	}

	private static Quantity parseQuantity(String string) {
		String[] quantityTokens = string.split(" ");
		long n = Integer.valueOf(quantityTokens[0]);
		String ing = quantityTokens[1].replace(",", "");

		return new Quantity(n, ing);
	}

	private static void printRecipes() {
		for (String r : recipes.keySet()) {
			if (baseMaterials.contains(r)) {
				// System.out.print("* ");
			}
			System.out.println(recipes.get(r));
		}
	}

}
