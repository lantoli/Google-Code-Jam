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

public class FairAndSquare {

	final static String FILENAME = "C-large-1";
	// final static String FILENAME = "C-small-attempt0";
	// final static String FILENAME = "sample";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 8; // use 1 to solve them sequentially

	// VM arguments: -ea -Xms4096M -Xmx4096M

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE
		// -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input
			A = in.nextLong();
			B = in.nextLong();
			in.nextLine();
		}

		// TODO: Define input variables
		final long A, B;

		private boolean nice(long number) {
			char[] str = Long.toString(number).toCharArray();
			int len = str.length;
			int limit = len / 2;
			for (int i = 0; i < limit; i++)
				if (str[i] != str[len - i - 1])
					return false;
			return true;
		}

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			long Asqrt = (long) Math.sqrt(A);
			long Bsqrt = (long) Math.sqrt(B) + 1L;
			while (Asqrt * Asqrt < A) {
				Asqrt++;
			}
			while (Bsqrt * Bsqrt > B) {
				Bsqrt--;
			}
			long count = 0;
			for (long n = Asqrt; n <= Bsqrt; n++) {
				if (nice(n) && nice(n * n)) {
					count++;
				}
			}

			String res = Long.toString(count);

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