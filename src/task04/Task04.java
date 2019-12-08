package task04;

import java.util.regex.Pattern;

public class Task04 {
	static Pattern	p1	= Pattern.compile(".*((\\d)\\2)(?!\\2).*");
	static Pattern	p2	= Pattern.compile(".*((\\d)\\2\\2).*");

	public static void main(String[] args) {
		int counter = 0;
		outer: for (int i = 278384; i <= 824795; i++) {
			int[] digits = new int[6];
			for (int j = 0; j < 6; j++) {
				digits[j] = Integer.valueOf(String.valueOf(String.valueOf(i).charAt(j)));
			}

			for (int j = 1; j < 6; j++) {
				if (digits[j] < digits[j - 1]) {
					continue outer;
				}
			}

			String asString = String.valueOf(i);

			// Matcher m = p1.matcher(asString);
			// while (m.find()) {
			// String theChar = m.group(2);
			//
			// if (!asString.matches(".*" + theChar + theChar + theChar + ".*")) {
			// System.out.println(i);
			// counter++;
			// continue outer;
			// }
			// }
			//
			// System.err.println(i);

			for (int j = 0; j < 5; j++) {
				int k = j + 1;

				while (digits[j] == digits[k]) {
					k++;
					if (k == 6) {
						break;
					}
				}

				if (k == j + 2) {
					counter++;
					System.out.println(i);
					continue outer;
				} else {
					j = k - 1;
				}
			}

			System.err.println(i);
		}

		System.out.println(counter);
	}

}
