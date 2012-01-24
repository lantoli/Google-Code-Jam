import static java.lang.Math.min;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class AlphabetSoup {

	public static void main(String[] args) throws FileNotFoundException {

		System.setIn(new FileInputStream("alphabet_soup.in"));
		System.setOut(new PrintStream(new FileOutputStream("alphabet_soup.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		System.err.println("tests: " + tests);
		for (long test=1; test<= tests; test++) {

			int[] hacker = count("HACKERCUP");
			int[] occ = count(in.nextLine().trim());
			int res = Integer.MAX_VALUE;
			for (int i=0; i<hacker.length; i++) {
				if (hacker[i] != 0) {
					res = min(res, occ[i]/hacker[i]);
				}
			}

			System.out.println(String.format("Case #%d: %d", test, res));
		}

	}

	static int[] count(String str) {
		int[] ret = new int['Z'-'A'+1];
		for (char c : str.toCharArray()) {
			if (c != ' ') {
				ret[c-'A'] ++;
			}
		}
		return ret;
	}
}
