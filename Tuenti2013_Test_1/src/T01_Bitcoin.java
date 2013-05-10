import static java.lang.Math.max;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class T01_Bitcoin {

	// Set FILENAME = null for standard input/output
	final static String FILENAME = null;
	// final static String FILENAME = "sample_bitcoin";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 8; // use 1 to solve them sequentially

	// VM arguments for hard problems: -ea -Xms4096M -Xmx4096M -Xss1024m

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE
		// -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			budget = in.nextLong();
			in.nextLine();
			String[] ratesStr = in.nextLine().split("\\s+");
			rates = new long[ratesStr.length];
			for (int i = 0; i < ratesStr.length; i++) {
				rates[i] = Long.parseLong(ratesStr[i]);
			}
		}

		// TODO: Define input variables

		final long budget;

		final long[] rates;

		// TODO: Define helper methods and classes here

		class State {
			private final long euros;
			private final long bitcoins;
			private final int ratePos;

			private State(long euros, long bitcoins, int ratePos) {
				this.euros = euros;
				this.bitcoins = bitcoins;
				this.ratePos = ratePos;
			}

			public State(long euros) {
				this(euros, 0, 0);
			}

			private State nextState(long euros, long bitcoins) {
				return new State(euros, bitcoins, ratePos + 1);
			}

			@Override
			public String toString() {
				return "State [euros=" + euros + ", bitcoins=" + bitcoins
						+ ", ratePos=" + ratePos + ", rate=" + rates[ratePos]
						+ "]";
			}

			public long getEuros() {
				return euros;
			}

			public State dontBuySell() {
				return nextState(euros, bitcoins);
			}

			public State buyMax() {
				long newBitcoins = euros / rates[ratePos];
				return nextState(euros - newBitcoins * rates[ratePos], bitcoins
						+ newBitcoins);
			}

			public State sellMax() {
				return nextState(euros + bitcoins * rates[ratePos], 0);
			}

			public boolean isNotLast() {
				return ratePos < rates.length;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getOuterType().hashCode();
				result = prime * result + (int) (bitcoins ^ (bitcoins >>> 32));
				result = prime * result + (int) (euros ^ (euros >>> 32));
				result = prime * result + ratePos;
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				State other = (State) obj;
				if (!getOuterType().equals(other.getOuterType()))
					return false;
				if (bitcoins != other.bitcoins)
					return false;
				if (euros != other.euros)
					return false;
				if (ratePos != other.ratePos)
					return false;
				return true;
			}

			private Solver getOuterType() {
				return Solver.this;
			}

		}

		Map<State, Long> cache = new HashMap<State, Long>();

		// dynamic programming recursive function
		private long dp(State state) {
			Long val = cache.get(state);
			if (val != null)
				return val.longValue();
			long euros = state.getEuros();
			if (state.isNotLast()) {
				euros = max(euros, dp(state.dontBuySell()));
				euros = max(euros, dp(state.buyMax()));
				euros = max(euros, dp(state.sellMax()));
			}
			cache.put(state, euros);
			return euros;
		}

		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			long amount = dp(new State(budget));

			String res = Long.toString(amount);

			System.err.println(String.format("%4d ms %s",
					(System.nanoTime() - now) / 1000000, res));
			return res;
		}

		// PROBLEM SOLUTION ENDS HERE
		// -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		final int testId;
	}

	public static void main(String[] args) throws FileNotFoundException,
			InterruptedException {
		long now = System.nanoTime();
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}
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
				System.out.println(String.format("%s", solutions.get(i).get()));
			} catch (Exception e) {
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