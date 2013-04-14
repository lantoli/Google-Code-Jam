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

public class Lawnmower {

	final static String FILENAME = "B-large";
	// final static String FILENAME = "B-small-attempt0";
	// final static String FILENAME = "sample";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 1; // use 1 to solve them sequentially

	// VM arguments: -ea -Xms4096M -Xmx4096M

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE
		// -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input
			N = in.nextInt();
			M = in.nextInt();
			board = new int[N][M];
			in.nextLine();
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					int num = in.nextInt();
					board[i][j] = num;
					min = Math.min(min, num);
					max = Math.max(max, num);
				}
			}
			assert min >= 1 && min <= 100;
			assert max >= 1 && max <= 100;
			assert min <= max;
		}

		// TODO: Define input variables
		final int[][] board;
		final int N, M;
		int min = 101, max = 0;

		private boolean isGood(int height, int ii, int jj) {
			return isGoodRow(height, ii) || isGoodCol(height, jj);
		}

		private boolean isGoodCol(int height, int jj) {
			for (int i = 0; i < N; i++) {
				if (board[i][jj] > height)
					return false;
			}
			return true;
		}

		private boolean isGoodRow(int height, int ii) {
			for (int j = 0; j < M; j++) {
				if (board[ii][j] > height)
					return false;
			}
			return true;
		}

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string
			boolean good = true;

			out: for (int n = min; n <= max; n++) {
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < M; j++) {
						if (board[i][j] == n && !isGood(n, i, j)) {
							good = false;
							break out;
						}
					}
				}
			}

			String res = good ? "YES" : "NO";

			System.err.println(String.format("%4d ms %s",
					(System.nanoTime() - now) / 1000000, res));
			return res;
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