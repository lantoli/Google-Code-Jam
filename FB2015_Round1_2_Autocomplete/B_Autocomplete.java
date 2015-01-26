import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

// Facebook Hacker Cup 2015, round 1 B: https://www.facebook.com/hackercup/problems.php?pid=313229895540583&round=344496159068801

public class B_Autocomplete {

	final static String FILENAME = "autocomplete";

	public final static int NUM_LETTERS = 26;

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();

			for (int test = 1; test <= tests; test++) {

				Object[] root = new Object[NUM_LETTERS];

				long total = 0;

				int words = in.nextInt();
				in.nextLine();
				for (int i = 0; i < words; i++) {
					String word = in.nextLine();
					int len = word.length();
					Object[] cur = root;
					boolean updated = false;
					for (int j = 0; j < len; j++) {
						int posLetter = word.charAt(j) - 'a';
						Object[] next = (Object[]) cur[posLetter];
						if (next == null) {
							cur[posLetter] = new Object[NUM_LETTERS];
							next = (Object[]) cur[posLetter];
							if (!updated) {
								updated = true;
								total += j + 1;
							}
						}
						cur = next;
					}
					if (!updated) {
						total += word.length();
					}

				}

				System.out.format("Case #%d: %d%n", test, total);
			}
		}

	}
}
