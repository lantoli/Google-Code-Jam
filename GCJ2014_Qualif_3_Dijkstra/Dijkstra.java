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

public class Dijkstra {

	// final static String FILENAME = "sample_dijkstra";
	final static String FILENAME = "C-small-attempt1";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 1; // use 1 to solve them sequentially

	// VM arguments: -ea -Xms4096M -Xmx4096M

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			int L = in.nextInt();
			int X = in.nextInt();
			in.nextLine();
			String line = in.nextLine();
			ch = new int[L * X];
			for (int i = 0; i < line.length(); i++) {
				char elm = line.charAt(i);
				int val = elm == 'i' ? 2 : elm == 'j' ? 3 : 4;
				for (int j = 0; j < X; j++) {
					ch[i + L * j] = val;
				}
			}

		}

		// TODO: Define input variables

		final int[] ch;

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			int calc = 1;
			for (int i = 0; i < ch.length; i++) {
				calc = mult(calc, ch[i]);
			}

			boolean found = calc == -1;

			if (found) {
				found = false;

				int ii = 1;
				loop: for (int posi = 0; posi < ch.length; posi++) {
					ii = mult(ii, ch[posi]);
					if (ii == 2) {

						int jj = 1;
						for (int posj = posi + 1; posj < ch.length; posj++) {
							jj = mult(jj, ch[posj]);
							if (jj == 3) {

								int kk = 1;
								for (int posk = posj + 1; posk < ch.length; posk++) {
									kk = mult(kk, ch[posk]);
								}
								if (kk == 4) {
									found = true;
									break loop;
								}
							}
						}
					}
				}
			}

			String res = found ? "YES" : "NO";

			System.err.println(String.format("%4d ms %s", (System.nanoTime() - now) / 1000000, res));
			return res;
		}

		// PROBLEM SOLUTION ENDS HERE -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		int[][] table = { { 1, 2, 3, 4 }, { 2, -1, 4, -3 }, { 3, -4, -1, 2 }, { 4, 3, -2, -1 } };

		private int mult(int row, int col) {
			boolean negate = Integer.signum(row) != Integer.signum(col);

			int next = table[Math.abs(row) - 1][Math.abs(col) - 1];

			return negate ? -next : next;
		}

		final int testId;
	}

	static void debug(Object... os) {
		System.err.println(deepToString(os));
	};

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
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
				System.out.println(String.format("Case #%d: %s", solvers.get(i).testId, solutions.get(i).get()));
			} catch (Exception e) {
				System.out.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				e.printStackTrace(System.err);
			}
		}
		executor.shutdown();
		System.err.println(String.format("TOTAL: %d ms", (System.nanoTime() - now) / 1000000));
	}

}