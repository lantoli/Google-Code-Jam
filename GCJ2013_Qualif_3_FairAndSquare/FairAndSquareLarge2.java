import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.math.BigIntegerMath;

public class FairAndSquareLarge2 {

	final static String FILENAME = "C-large-2";
	// final static String FILENAME = "C-large-1";
	// final static String FILENAME = "C-small-attempt0";
	// final static String FILENAME = "sample";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 20; // use 1 to solve them sequentially

	// VM arguments: -ea -Xms4096M -Xmx4096M

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE
		// -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input
			A = in.nextBigInteger();
			B = in.nextBigInteger();
			in.nextLine();

			// BigInteger a = new BigInteger("979");
			// boolean hola = true;
			// while (hola) {
			// a = nextNice(a);
			// }

		}

		// TODO: Define input variables
		final BigInteger A, B;

		private boolean nice(BigInteger number) {
			char[] str = number.toString().toCharArray();
			int len = str.length;
			int limit = len / 2;
			for (int i = 0; i < limit; i++)
				if (str[i] != str[len - i - 1])
					return false;
			return true;
		}

		private BigInteger nextNice2(BigInteger n) {
			String str = n.toString();
			int len = str.length();
			boolean even = (len & 1) == 0;
			String first = str.substring(0, (len + 1) / 2);
			if (first.charAt(first.length() - 1) == '9') {
				do {
					n = n.add(BigInteger.ONE);
				} while (!nice(n));
				return n;

			}
			String firstOne = new BigInteger(first).add(BigInteger.ONE)
					.toString();
			String templateReverse = even ? firstOne : firstOne.substring(0,
					firstOne.length() - 1);
			String firstOneReverse = new StringBuilder(templateReverse)
					.reverse().toString();
			return new BigInteger(firstOne + firstOneReverse);
		}

		private BigInteger nextNice(BigInteger n) {
			String str = n.toString();
			int len = str.length();
			boolean even = (len & 1) == 0;
			String first = str.substring(0, (len + 1) / 2);
			boolean incr = first.charAt(first.length() - 1) == '9';
			String firstOne = new BigInteger(first).add(BigInteger.ONE)
					.toString();
			String templateReverse = even ? firstOne : firstOne.substring(0,
					firstOne.length() - 1);
			String firstOneReverse = new StringBuilder(templateReverse)
					.reverse().toString();
			if (incr) {
				firstOne = firstOne.substring(0, firstOne.length() - 1);
			}
			if (firstOne.length() + firstOneReverse.length() < len) {
				firstOne = firstOne + "0";
			}
			return new BigInteger(firstOne + firstOneReverse);
		}

		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			BigInteger Asqrt = BigIntegerMath.sqrt(A, RoundingMode.DOWN);
			BigInteger Bsqrt = BigIntegerMath.sqrt(B, RoundingMode.UP);
			while (Asqrt.multiply(Asqrt).compareTo(A) < 0) {
				Asqrt = Asqrt.add(BigInteger.ONE);
			}
			while (Bsqrt.multiply(Bsqrt).compareTo(B) > 0) {
				Bsqrt = Bsqrt.subtract(BigInteger.ONE);
			}
			BigInteger count = BigInteger.ZERO;

			BigInteger n = Asqrt;
			while (n.compareTo(Bsqrt) <= 0 && !nice(n)) {
				n = n.add(BigInteger.ONE);
			}

			while (n.compareTo(Bsqrt) <= 0) {
				if (nice(n.multiply(n))) {
					count = count.add(BigInteger.ONE);
				}
				n = nextNice(n);
			}

			String res = count.toString();

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