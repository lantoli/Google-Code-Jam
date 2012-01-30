import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SquishedStatus  {

	final static String FILENAME = "squished_status";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 1; // use 1 to solve them sequentially


	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			M = in.nextInt();
			String c = "";
			while (c.length() == 0) {
				c = in.nextLine().trim();
			}
			encoded = new byte[c.length()];
			for (int i=0; i<c.length(); i++) {
				encoded[i] = (byte) (c.charAt(i)-'0');
				assert encoded[i] >= 0 && encoded[i] <= 9;
			}
			assert M >= 2 && M <= 255;
            assert encoded.length >= 1 && encoded.length <= 1000;
            cache = new long[encoded.length+1];
            Arrays.fill(cache, -1);
            cache[encoded.length] = 1; // final condition for dp
		}


		// TODO: Define input variables

		final int M;
		final byte[] encoded;

		final static long MOD = 4207849484L;

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			String res = String.format("Case #%d: %d", testId, dp(0));

			System.err.println(String.format("%4d ms %s" , (System.nanoTime() - now) / 1000000, res));
			return res;
		}

		long[] cache;

		long dp(int inipos) {

			if (cache[inipos] != -1) {
				return cache[inipos];
			}

			long ret = 0;
			if (encoded[inipos] >= 1 && encoded[inipos] <= M) {
				int number = 0;
				int pos = inipos;
				while (pos < encoded.length && (number = number * 10 + encoded[pos]) <= M) {
					ret = (ret + dp(++pos)) % MOD;
				}
			}
			return cache[inipos] = ret % MOD;
		}


		// PROBLEM SOLUTION ENDS HERE -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------



		final int testId;
	}


	static void debug(Object...os) {
		System.err.println(deepToString(os));
	}
	;

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		long now = System.nanoTime();
		System.setIn(new FileInputStream(FILENAME_IN));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		Scanner in = new Scanner(System.in);
		int numTests = in.nextInt(); in.nextLine();
		System.err.println(String.format("TESTS: %d" , numTests));
		List<Solver> solvers = new ArrayList<Solver>();
		for (int i=0; i<numTests; i++) {
			solvers.add(new Solver(in, i+1));
		}
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		List<Future<String>> solutions = executor.invokeAll(solvers);
		for (int i = 0; i < numTests; i++) {
			try {
				System.out.println(solutions.get(i).get());
			} catch (Exception e) {
				System.out.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				e.printStackTrace(System.err);
			}
		}
		executor.shutdown();
		System.err.println(String.format("TOTAL: %d ms" , (System.nanoTime() - now) / 1000000));
	}


}