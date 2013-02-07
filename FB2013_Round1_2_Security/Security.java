import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * NOT WORKING, not efficient for large inputs.
 * 
 */
final public class Security {

	final static String FILENAME = "security";
	final static String FILENAME_IN = FILENAME + ".txt";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 20; // use 1 to solve them sequentially

	// VM arguments: -ea -Xms4096M -Xmx4096M

	static final class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE
		// -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input
			m = in.nextInt();
			in.nextLine();

			String strk1 = in.nextLine();
			String strk2 = in.nextLine();
			n = strk1.length();
			assert n == strk2.length();
			l = n / m;
			k1 = strk1.toCharArray();
			k2 = strk2.toCharArray();

			boolean[] ignore1 = new boolean[m];
			boolean[] ignore2 = new boolean[m];
			int nignore = 0;

			for (int i = 0; i < m; i++) {
				for (int j = 0; j < m; j++) {
					if (!ignore2[j]) {
						if (blocks_equal_no_question(i, j)) {
							ignore1[i] = ignore2[j] = true;
							nignore++;
							break;
						}
					}
				}
			}
			good = m - nignore;
			good1 = new int[good];
			good2 = new int[good];
			for (int i = 0, cur = 0; i < m; i++) {
				if (!ignore1[i]) {
					good1[cur++] = i;
				}
			}
			for (int i = 0, cur = 0; i < m; i++) {
				if (!ignore2[i]) {
					good2[cur++] = i;
				}
			}

			String a = "";
		}

		// TODO: Define input variables

		private boolean blocks_equal_no_question(int block1, int block2) {
			for (int i = 0; i < l; i++) {
				int pos1 = block1 * l + i;
				int pos2 = block2 * l + i;
				if (k1[pos1] != k2[pos2] || k1[pos1] == '?' || k2[pos2] == '?')
					return false;
			}
			return true;
		}

		private boolean blocks_equal(int block1, int block2) {
			for (int i = 0; i < l; i++) {
				int pos1 = block1 * l + i;
				int pos2 = block2 * l + i;
				if (k1[pos1] == '?' || k2[pos2] == '?') {
					continue;
				}
				if (k1[pos1] != k2[pos2])
					return false;
			}
			return true;
		}

		final int n;
		final int m;
		final int l;
		final char[] k1;
		final char[] k2;

		final int good;
		final int[] good1;
		final int[] good2;

		int[] positions;
		int npositions = 0;

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			positions = new int[n];

			for (int i = n - 1; i >= 0; i--) {
				if (k1[i] == '?') {
					k1[i] = 'a';
					positions[npositions++] = i;
				}
			}

			String res = "IMPOSSIBLE";

			do {
				if (validPerm()) {
					res = new String(k1);
					break;
				}
			} while (nextComb());

			System.err.println(String.format("%4d ms %s",
					(System.nanoTime() - now) / 1000000, res));
			return res;
		}

		final private boolean nextComb() {
			int i = 0;
			while (i < npositions) {
				if (++k1[positions[i]] != 'g') {
					break;
				}
				k1[positions[i]] = 'a';
				i++;
			}
			return i < npositions;
		}

		final private boolean validPerm() {
			boolean[][] candidates = new boolean[good][good];
			for (int i = 0; i < good; i++) {
				for (int j = 0; j < good; j++) {
					candidates[i][j] = blocks_equal(good1[i], good2[j]);
				}
			}

			// all rows and columns with some true
			for (int i = 0; i < good; i++) {
				boolean foundRow = false;
				for (int j = 0; j < good; j++) {
					if (candidates[i][j]) {
						foundRow = true;
						break;
					}
				}
				if (!foundRow)
					return false;
			}
			for (int j = 0; j < good; j++) {
				boolean foundCol = false;
				for (int i = 0; i < good; i++) {
					if (candidates[i][j]) {
						foundCol = true;
						break;
					}
				}
				if (!foundCol)
					return false;
			}

			return true;
		}

		// PROBLEM SOLUTION ENDS HERE
		// -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		final int testId;
	}

	static void debug(Object... os) {
		System.err.println(deepToString(os));
	};

	public static void main(String[] args) throws FileNotFoundException,
			InterruptedException {
		long now = System.nanoTime();
		System.setIn(new FileInputStream(FILENAME_IN));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		Scanner in = new Scanner(System.in);
		int numTests = in.nextInt();
		in.nextLine();
		System.err.println(String.format("TESTS: %d", numTests));
		List<Solver> solvers = new ArrayList<Solver>();
		for (int i = 0; i < numTests; i++) {
			solvers.add(new Solver(in, i + 1));
		}
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		List<Future<String>> solutions = executor.invokeAll(solvers);
		for (int i = 0; i < numTests; i++) {
			try {
				System.out.println(String.format("Case #%d: %s",
						solvers.get(i).testId, solutions.get(i).get()));
			} catch (Exception e) {
				System.out.println(String.format("Case #%d: EXCEPTION !!!!!",
						solvers.get(i).testId));
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!",
						solvers.get(i).testId));
				e.printStackTrace(System.err);
			}
		}
		executor.shutdown();
		System.err.println(String.format("TOTAL: %d ms",
				(System.nanoTime() - now) / 1000000));
	}

}