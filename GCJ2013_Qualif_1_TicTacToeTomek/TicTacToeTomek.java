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

public class TicTacToeTomek {

	final static String FILENAME = "A-large";
	// final static String FILENAME = "A-small-attempt0";
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
			for (int i = 0; i < SIZE; i++) {
				String line = in.nextLine();
				assert line.length() == SIZE;
				strin[i] = line;
				for (int j = 0; j < SIZE; j++) {
					char c = line.charAt(j);
					if (c == '.') {
						isFinished = false;
					}
					board[i][j] = c;
				}
			}
			try {
				in.nextLine();
			} catch (Exception e) {
			}
		}

		// TODO: Define input variables

		final static int SIZE = 4;

		final char[][] board = new char[SIZE][SIZE];

		final String[] strin = new String[SIZE];

		boolean isFinished = true;

		private boolean won(char player) {
			boolean ret = false;
			for (int y = 0; y < SIZE; y++) { // horizontal
				ret = ret || checkLine(player, y, 0, 0, 1);
			}
			for (int x = 0; x < SIZE; x++) { // vertical
				ret = ret || checkLine(player, 0, 1, x, 0);
			}
			for (int n = 0; n < SIZE; n++) { // top-left diagonal
				ret = ret || checkLine(player, 0, 1, 0, 1);
			}
			for (int n = 0; n < SIZE; n++) { // top-right diagonal
				ret = ret || checkLine(player, 0, 1, SIZE - 1, -1);
			}
			return ret;
		}

		private boolean checkLine(char player, int yini, int yinc, int xini,
				int xinc) {
			int y = yini, x = xini;
			for (int n = 0; n < SIZE; n++) {
				char c = board[y][x];
				if (c != player && c != 'T')
					return false;
				y += yinc;
				x += xinc;
			}
			return true;
		}

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			String res;

			if (won('X')) {
				res = "X won";
			} else if (won('O')) {
				res = "O won";
			} else {
				res = isFinished ? "Draw" : "Game has not completed";
			}

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