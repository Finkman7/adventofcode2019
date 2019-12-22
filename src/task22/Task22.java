package task22;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Task22 {
	public static BigInteger		N;
	public static final BigInteger	iterations	= new BigInteger("101741582076661");

	public static void main(String[] args) throws IOException {
		// a
		System.out.println("a): ");
		N = new BigInteger("10007");
		System.out.println(shuffle(new BigInteger("2019")) + "\n\n");

		// b
		System.out.println("b)");
		N = new BigInteger("119315717514047");

		BigInteger b = shuffle(BigInteger.ZERO);
		BigInteger a = shuffle(BigInteger.ONE).subtract(b).mod(N);
		System.out.println("a=" + a);
		System.out.println("b=" + b);
		System.out.println("Single Shuffle:\tf(index)=" + a + "*index + " + b + "\n\n");

		BigInteger b_ = apply(BigInteger.ZERO, a, b, iterations);
		BigInteger a_ = apply(BigInteger.ONE, a, b, iterations).subtract(b_).mod(N);

		System.out.println("a_ " + a_);
		System.out.println("b_ = " + b);
		System.out.println(iterations + " Shuffles:\tf(index)=" + a_ + "*index + " + b_ + "\n\n");
	}

	// Applies 'a*x+b % N' a number of 'iterations' times to x.
	private static BigInteger apply(BigInteger x, BigInteger a, BigInteger b, BigInteger iterations) {
		BigInteger result = x.multiply(a.modPow(iterations, N));

		BigInteger upper = a.modPow(iterations, N).subtract(BigInteger.ONE);
		BigInteger lower = a.subtract(BigInteger.ONE).modInverse(N);
		BigInteger frac = upper.multiply(lower).mod(N);
		BigInteger geomSeries = b.multiply(frac).mod(N);

		result = result.add(geomSeries).mod(N);

		return result;
	}

	private static BigInteger expSquare(BigInteger a, BigInteger b, BigInteger iterations) {
		if (iterations.equals(BigInteger.ZERO)) {
			return new BigInteger("1");
		}

		BigInteger result = new BigInteger("1").multiply(a).add(b).mod(N);

		while (!iterations.equals(BigInteger.ONE)) {
			if (iterations.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
				iterations = iterations.subtract(BigInteger.ONE);
				result = result.multiply(a).add(b).mod(N);
			} else {
				iterations = iterations.divide(BigInteger.TWO);
				result = result.multiply(result).mod(N);
			}
		}

		return result;
	}

	private static BigInteger shuffle(BigInteger index) throws IOException {
		BigInteger lastIndex = N.subtract(new BigInteger("1"));

		for (String line : Files.readAllLines(Paths.get("inputs/input22.txt"))) {
			String[] commands = line.split("\\s");
			if (line.startsWith("cut ")) {
				BigInteger offset = new BigInteger(commands[commands.length - 1]);
				// System.out.println(offset + "= " + index.subtract(offset));
				index = index.subtract(offset).mod(N);
			} else if (line.startsWith("deal with")) {
				BigInteger factor = new BigInteger(commands[commands.length - 1]);
				index = index.multiply(factor).mod(N);
			} else if (line.startsWith("deal into")) {
				index = lastIndex.subtract(index);
			}
		}

		return index;
	}
}
