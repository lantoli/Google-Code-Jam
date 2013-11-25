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

public class SquareDetector {

	final static String FILENAME = "square_detector";
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
			size = in.nextInt();
			in.nextLine();
			cells = new boolean[size][size];
			for (int i = 0; i < size; i++) {
				String line = in.nextLine();
				for (int j = 0; j < size; j++) {
					cells[i][j] = line.charAt(j) == '#';
				}
			}
		}

		// TODO: Define input variables

		boolean[][] cells;
		int size;

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			boolean squared = true;
			int xleft = 0, ytop = 0;
			loop1: for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					if (cells[y][x]) {
						xleft = x;
						ytop = y;
						break loop1;
					}
				}
			}

			int xright = 0, ybottom = 0;
			loop2: for (int y = size - 1; y >= 0; y--) {
				for (int x = size - 1; x >= 0; x--) {
					if (cells[y][x]) {
						xright = x;
						ybottom = y;
						break loop2;
					}
				}
			}

			if (xright - xleft != ybottom - ytop) {
				squared = false;
			} else {
				loop3: for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						boolean inside = x >= xleft && x <= xright && y >= ytop && y <= ybottom;
						if (cells[y][x] != inside) {
							squared = false;
							break loop3;
						}
					}
				}
			}

			String res = squared ? "YES" : "NO";

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