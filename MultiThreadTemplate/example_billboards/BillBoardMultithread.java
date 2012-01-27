import static java.lang.Math.max;
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

public class BillBoardMultithread  {

	final static String FILENAME = "billboards";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 4; // use 1 to solve them sequentially

	// VM arguments: -Xms2048M -Xmx2048M

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			width = in.nextInt();
			height = in.nextInt();
			words = in.nextLine().trim().split(" ");
		}


		// TODO: Define input variables

		int width, height;
		String[] words;


		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			int maxLength = 0;
			for (String word : words) {
				maxLength = max(maxLength,word.length());
			}
			int size = width / maxLength;
			assert size <= 1000;
			loop:
				for (; size > 0; --size) {
					int colsLeft = width / size;
					int rowsLeft = height / size;
					int pos = 0;
					while (rowsLeft > 0 && pos < words.length) {
						if (words[pos].length() <= colsLeft) {
							colsLeft -= words[pos].length() + 1;
							pos++;
						} else {
							rowsLeft--;
							colsLeft = width / size;
						}
					}
					if (pos == words.length) {
						break loop;
					}
				}


			String res = String.format("Case #%d: %d", testId, size);

			System.err.println(String.format("%4d ms %s" , (System.nanoTime() - now) / 1000000, res));
			return res;
		}

		// PROBLEM SOLUTION ENDS HERE -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		int testId;
	}


	static void debug(Object...os) {
		System.err.println(deepToString(os));
	}

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