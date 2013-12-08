import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Alzheimer {

	final static String FILENAME = "example";
	final static String FILENAME_IN = FILENAME + ".txt";
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
			K = in.nextInt();
			A = new int[N];
			for (int i = 0; i < N; i++)
				A[i] = in.nextInt();
		}

		// TODO: Define input variables

		int N, K;
		int[] A;

		class MyArray {
			int[] elms;

			int sum;

			MyArray(int[] elms) {
				Arrays.sort(elms);
				this.elms = elms;
			}

			MyArray(MyArray a, int pos) {
				int[] temp = Arrays.copyOf(a.elms, a.elms.length);
				temp[pos]++;
				Arrays.sort(temp);
				this.elms = temp;
				sum++;
			}

			@Override
			public int hashCode() {
				return Arrays.hashCode(elms);
			}

			@Override
			public boolean equals(Object obj) {
				return Arrays.equals(elms, ((MyArray) obj).elms);
			}

			@Override
			public String toString() {
				return "MyArray [elms=" + Arrays.toString(elms) + "]";
			}

		}

		int best = Integer.MAX_VALUE;
		private final Comparator<MyArray> comparator = new Comparator<MyArray>() {

			@Override
			public int compare(MyArray o1, MyArray o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o1.sum, o2.sum);
			}

		};

		private int solve(MyArray a) {
			PriorityQueue<MyArray> q = new PriorityQueue<>(100000, comparator);
			q.add(a);

			while (true) {
				MyArray elm = q.poll();
				if (elm == null)
					return 0;
				if (isGCD(elm))
					return elm.sum;

				for (int i = 0; i < N; i++) {
					MyArray a2 = new MyArray(a, i);
					q.add(a2);
				}
			}

		}

		private boolean isGCD(MyArray a) {
			for (int i = 0; i < N - 1; i++)
				for (int j = 1; j < N; j++)
					if (gcd(a.elms[i], a.elms[j]) != K)
						return false;
			return true;
		}

		private int dp(MyArray a, int sum) {

			if (sum > 10)
				return -1;

			int[] example = { 3, 5, 7, 16 };
			if (Arrays.equals(example, a.elms)) {
				String aaa = "";
				String bbb = "";
			}

			boolean finish = true;
			for (int i = 0; i < N - 1; i++)
				for (int j = 1; j < N; j++)
					if (gcd(a.elms[i], a.elms[j]) != K) {
						finish = false;
						break;
					}

			if (finish)
				return sum;

			int min = Integer.MAX_VALUE;
			for (int i = 0; i < N; i++) {
				int cand = dp(new MyArray(a, i), sum);
				if (cand > -1)
					min = Math.min(min, cand + 1);
			}
			best = Math.min(best, min);
			return min;
		}

		public int gcd(int a, int b) {
			return b == 0 ? a : gcd(b, a % b);
		}

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			String res = Integer.toString(solve(new MyArray(A)));

			System.err.println(String.format("%4d ms %s", (System.nanoTime() - now) / 1000000, res));
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

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		long now = System.nanoTime();
		System.setIn(new FileInputStream(FILENAME_IN));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		Scanner in = new Scanner(System.in);
		int numTests = in.nextInt();
		in.nextLine();
		System.err.println(String.format("TESTS: %d", numTests));
		List<Solver> solvers = new ArrayList<Solver>();
		for (int i = 0; i < numTests; i++)
			solvers.add(new Solver(in, i + 1));
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		List<Future<String>> solutions = executor.invokeAll(solvers);
		for (int i = 0; i < numTests; i++)
			try {
				System.out.println(String.format("Case #%d: %s", solvers.get(i).testId, solutions.get(i).get()));
			} catch (Exception e) {
				System.out.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				e.printStackTrace(System.err);
			}
		executor.shutdown();
		System.err.println(String.format("TOTAL: %d ms", (System.nanoTime() - now) / 1000000));
	}

}