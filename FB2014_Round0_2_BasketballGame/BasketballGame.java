import static java.util.Arrays.deepToString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.base.Joiner;
import com.google.common.collect.ComparisonChain;

public class BasketballGame {

	final static String FILENAME = "basketball_game";
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
			M = in.nextInt();
			P = in.nextInt();
			for (int i = 0; i < N; i++) {
				Player p = new Player();
				p.name = in.next();
				p.shotpctg = in.nextInt();
				p.height = in.nextInt();
				in.nextLine();
				list.add(p);
			}
		}

		// TODO: Define input variables

		int N, M, P;

		class Player {
			String name;
			int shotpctg;
			int height;
			int time;

			@Override
			public String toString() {
				return name;
			}

		}

		Comparator<Player> ascending = new Comparator<Player>() {
			@Override
			public int compare(Player o1, Player o2) {
				return ComparisonChain.start().compare(o1.time, o2.time).compare(o2.shotpctg, o1.shotpctg)
						.compare(o2.height, o1.height).result();
			}
		};
		Comparator<Player> descending = Collections.reverseOrder(ascending);

		int maxN = 30;
		PriorityQueue<Player> list = new PriorityQueue<>(maxN, ascending);
		PriorityQueue<Player> bench1 = new PriorityQueue<>(maxN, ascending);
		PriorityQueue<Player> bench2 = new PriorityQueue<>(maxN, ascending);
		PriorityQueue<Player> play1 = new PriorityQueue<>(maxN, descending);
		PriorityQueue<Player> play2 = new PriorityQueue<>(maxN, descending);

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			for (int i = 0; i < P; i++) {
				play1.add(list.remove());
				play2.add(list.remove());
			}
			while (!list.isEmpty()) {
				bench1.add(list.remove());
				if (!list.isEmpty())
					bench2.add(list.remove());
			}

			for (int i = 0; i < M; i++) {
				for (Player p : play1)
					p.time++;
				for (Player p : play2)
					p.time++;
				bench1.add(play1.remove());
				bench2.add(play2.remove());
				play1.add(bench1.remove());
				play2.add(bench2.remove());
			}
			List<String> names = new ArrayList<>();
			for (Player p : play1)
				names.add(p.name);
			for (Player p : play2)
				names.add(p.name);
			Collections.sort(names);
			String res = Joiner.on(' ').skipNulls().join(names);

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