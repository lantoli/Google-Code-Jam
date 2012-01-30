import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class Checkpoint  {

	final static String FILENAME = "checkpoint";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		final static int MAX_SHORTEST = 10000002;

		final static int MAX_COMB = 4472; // To get S < 10,000,000

		final static int MAX_FACT = MAX_COMB * 2; // To get S < 10,000,000

		static int[][] comb = new int[MAX_COMB][];


		static BigInteger[] fact = new BigInteger[MAX_FACT];

		static int comb(int n, int m) {
			if (n == 0 || m == 0) {
				return 1;
			}
			if (n == 1 || m == 1) {
				return n+m;
			}
			if (n >= MAX_COMB || m >= MAX_COMB) {
				return MAX_SHORTEST;
			}
			return comb[n-2][m-2];
		}

		static {
			fact[0] = BigInteger.ONE;
			for (int i=1; i<fact.length; i++) {
				fact[i] = fact[i-1].multiply(BigInteger.valueOf(i));
			}

			for (int i=0; i<MAX_COMB-2; i++) {
				comb[i] = new int[i+1];
				Arrays.fill(comb[i], MAX_SHORTEST);
				for (int j=0; j < i+1; j++) {
					long calc =  fact[i+j+4].divide(fact[i+2]).divide(fact[j+2]).longValue();
					if (calc > MAX_SHORTEST) {
						break;
					}
					comb[i][j] = (int)calc;
				}
			}
		}

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			Shortest = in.nextInt();
		}

		// TODO: Define input variables

		final int Shortest;



		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			long T = Long.MAX_VALUE;

			int sqrt = (int) Math.sqrt(Shortest);
			outer:
				for (int factor1 = 1; factor1 <= sqrt; factor1++ ) {
					if (Shortest % factor1 == 0) {
						int factor2 = Shortest / factor1;
						boolean found1 = false;
						int i=0, j=0;
						inner1:
							for (i=0; i<MAX_SHORTEST; i++) {
								inner11:
									for (j=0; j<=i; j++) {
										long c = comb(i,j);
										if (c > factor1) {
											break inner11;
										}
										if ((i>0 || j>0) && (factor1 == c)) {
											found1 = true;
											break inner1;
										}
									}
							}
						if (found1) {
							boolean found2 = false;
							int ii=0, jj=0;
							inner2:
								for (ii=i; ii<MAX_SHORTEST; ii++) {
									inner22:
										for (jj=j; jj<=ii; jj++) {
											long cc = comb(max(ii-i,jj-j),min(ii-i,jj-j));
											if (cc > factor2) {
												break inner22;
											}
											if ((ii>i || jj>j) && (factor2 == cc)) {
												found2 = true;
												break inner2;
											}
										}
								}
							if (found2) {
								T = min(T, ii+jj);
								continue outer;
							}
						}
					}
				}

			String res = String.format("Case #%d: %d", testId, T);

			System.err.println(String.format("%4d ms %s" , (System.nanoTime() - now) / 1000000, res));
			return res;
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
		for (int i=0; i<numTests; i++) {
			Solver solver = new Solver(in, i+1);
			try {
				System.out.println(solver.call());
			} catch (Exception e) {
				System.out.println(String.format("Case #%d: EXCEPTION !!!!!", solver.testId));
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!", solver.testId));
				e.printStackTrace(System.err);
			}
		}
		System.err.println(String.format("TOTAL: %d ms" , (System.nanoTime() - now) / 1000000));
	}


}