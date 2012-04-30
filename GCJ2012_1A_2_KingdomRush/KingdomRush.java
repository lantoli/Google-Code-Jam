import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KingdomRush  {

	final static String FILENAME = "B-large";
	//	final static String FILENAME = "B-small-attempt1";
	//final static String FILENAME = "sample";
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

			N = in.nextInt();
			a = new int[N];
			b = new int[N];
			for (int i=0; i<N; i++) {
				a[i] = in.nextInt();
				b[i] = in.nextInt();
				level.add(new Level(a[i],b[i]));
			}
			Collections.sort(level);
		}


		// TODO: Define input variables

		final int N;
		int[] a;
		int[] b;
		List<Level> level = new ArrayList<Level>();


		public static class Level implements Comparable<Level> {
			public int estado = 0;   // 0 nada, 1 nivel 1, 2 completado
			public int a,b;
			public Level(int a, int b) {
				this.a = a;
				this.b = b;
			}
			@Override
			public String toString() {
				return "[a=" + a + ", b=" + b + ", est= "+ estado + "]";
			}
			@Override
			public int compareTo(Level other) {
				int ret = other.b - b;
				return ret != 0 ? ret : other.a - a;
			}
		}

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			int count = 0;
			int score = 0;
			outer: while (level.size() > 0) {
				for (int i=0; i<level.size(); i++) {
					Level l = level.get(i);
					if (l.estado == 0 && score >= l.b) {
						count++;
						score += 2;
						level.remove(i);
						continue outer;
					}
				}
				for (int i=0; i<level.size(); i++) {
					Level l = level.get(i);
					if (l.estado == 1 && score >= l.b) {
						count++;
						score += 1;
						level.remove(i);
						continue outer;
					}
				}
				for (Level l : level) {
					if (l.estado == 0 && score >= l.a) {
						l.estado = 1;
						count++;
						score += 1;
						continue outer;
					}
				}
				break;
			}
			String res = level.size() > 0 ? "Too Bad" : Integer.toString(count);

			System.err.println(String.format("%4d ms %s" , (System.nanoTime() - now) / 1000000, res));
			return res;
		}

		int digitCount(int n) {
			return Integer.toString(n).length();
		}

		int recycleFun(int n, int rotations) {
			String str = Integer.toString(n);
			String part1 = str.substring(0, str.length() - rotations);
			String part2 = str.substring(str.length() - rotations);
			String rot = part2 + part1;
			return Integer.parseInt(rot);
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
				System.out.println(String.format("Case #%d: %s", solvers.get(i).testId, solutions.get(i).get()));
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