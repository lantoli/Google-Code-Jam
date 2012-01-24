import static java.lang.Math.max;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Billboards {

	public static void main(String[] args) throws FileNotFoundException {

		System.setIn(new FileInputStream("billboards.in"));
		System.setOut(new PrintStream(new FileOutputStream("billboards2.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		System.err.println("tests: " + tests);
		for (long test=1; test<= tests; test++) {

			int width = in.nextInt();
			int height = in.nextInt();
			String[] words = in.nextLine().trim().split(" ");
			int maxLength = 0;
			for (String word : words) {
				maxLength = max(maxLength,word.length());
			}
			int size = width / maxLength;
			assert size <= 1000;
			loop:
				for (; size > 0; --size) {
					int colsLeft = width / size;
					int rowsLeft = height / size;
					int pos = 0;
					while (rowsLeft > 0 && pos < words.length) {
						if (words[pos].length() <= colsLeft) {
							colsLeft -= words[pos].length() + 1;
							pos++;
						} else {
							rowsLeft--;
							colsLeft = width / size;
						}
					}
					if (pos == words.length) {
						break loop;
					}
				}
			System.out.println(String.format("Case #%d: %d", test, size));
		}

	}
}
