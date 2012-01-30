import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RecoverTheSequence  {

	final static String FILENAME = "recover_the_sequence";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 1; // use 1 to solve them sequentially

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			N = in.nextInt(); in.nextLine();
			sequence = in.nextLine().trim();
		}


		// TODO: Define input variables

		final int N;
		final String sequence;

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			List<Integer> arr = new ArrayList<Integer>();
			for (int i=0; i<N; i++) {
				arr.add(i);
			}

			List<Integer> sorted = merge_sort(arr);

			Map<Integer,Integer> mapPos = new TreeMap<Integer,Integer>();
			for (int i=0; i<N; i++) {
				mapPos.put(sorted.get(i), i);
			}

			List<Integer> original = new ArrayList<Integer>();
			for (int i=0; i<N; i++) {
				original.add(mapPos.get(i) + 1);
			}

			int result = 1;
			for (int i=0; i<sorted.size(); i++) {
				result = (31 * result + original.get(i)) % 1000003;
			}
			String res = String.format("Case #%d: %d", testId, result);

			System.err.println(String.format("%4d ms %s" , (System.nanoTime() - now) / 1000000, res));
			return res;
		}

		boolean nextSequence() {
			return sequence.charAt(sequencePos++) == '1';
		}
		int sequencePos = 0;

		List<Integer> merge_sort(List<Integer> arr) {
			int	n = arr.size();
			if (n <= 1) {
				return arr;
			}
			int mid = n/2;
			List<Integer> first_half = merge_sort(arr.subList(0, mid));
			List<Integer> second_half = merge_sort(arr.subList(mid, n));
			return merge(first_half, second_half);
		}

		List<Integer> merge(List<Integer> arr1, List<Integer> arr2) {
			List<Integer> result = new ArrayList<Integer>();
			Queue<Integer> q1 = new LinkedList<Integer> (arr1);
			Queue<Integer> q2 = new LinkedList<Integer> (arr2);
			while (!q1.isEmpty() && !q2.isEmpty()) {
				if (nextSequence()) {
					result.add(q1.remove());
				} else {
					result.add(q2.remove());
				}
			}
			result.addAll(q1);
			result.addAll(q2);
			return result;
		}

		// PROBLEM SOLUTION ENDS HERE -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		final int testId;
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