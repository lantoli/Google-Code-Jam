import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;

public class T03_LostInLost {

	// VM arguments for hard problems: -ea -Xms4096M -Xmx4096M -Xss1024m
	// Guava library must be added to classpath, current ver. 14.0.1

	// Set FILENAME = null for standard input/output
	final static String FILENAME = null;
	// final static String FILENAME = "sample_lost";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 1; // use 1 to solve them sequentially

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE
		// -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			script = in.nextLine().toCharArray();
		}

		// TODO: Define input variables

		final char[] script;

		// TODO: Define helper methods and classes here

		private void splitScript(char[] script, String delimiters, List<String> list, List<Character> listChar) {
			int posIni = 1;
			listChar.add(script[0]);
			for (int i = 1; i < script.length; i++) {
				char ch = script[i];
				if (delimiters.indexOf(ch) >= 0) {
					String line = new String(script, posIni, i - posIni);
					list.add(line);
					listChar.add(script[i]);
					posIni = i + 1;
				}
			}
			list.add(new String(script, posIni, script.length - posIni));
		}

		private interface Predicate {
			boolean apply(ArrayListMultimap<String, String> edges, String v);
		}

		private final static Predicate outgoing = new Predicate() {
			@Override
			public boolean apply(ArrayListMultimap<String, String> edges, String v) {
				return !edges.containsValue(v);
			}
		};
		final private static Predicate incoming = new Predicate() {
			@Override
			public boolean apply(ArrayListMultimap<String, String> edges, String v) {
				return edges.get(v).size() == 0;
			}
		};

		private boolean exactlyOne(Set<String> vertexes, ArrayListMultimap<String, String> edges, Predicate p) {
			boolean found = false;
			for (String v : vertexes) {
				if (p.apply(edges, v)) {
					if (found)
						return false; // found more than once
					else {
						found = true;
					}
				}
			}
			return found;
		}

		private void removeEdgesTo(ArrayListMultimap<String, String> edges, String sink) {
			for (String v : new HashSet<String>(edges.keySet())) {
				while (edges.get(v).remove(sink)) {
					;
				}
			}
		}

		private String listToString(List<String> list) {
			StringBuilder str = new StringBuilder();
			boolean first = true;
			for (String item : list) {
				if (!first) {
					str.append(',');
				}
				first = false;
				str.append(item);
			}
			return str.toString();
		}

		@Override
		public String call() throws Exception {

			// TODO: Solve problem here and return result as a string

			List<String> list = new ArrayList<String>();
			List<Character> listChar = new ArrayList<Character>();
			splitScript(script, ".<>", list, listChar);

			// Create adjacency list of the directed graph
			ArrayListMultimap<String, String> edges = ArrayListMultimap.create();
			Set<String> vertexes = new HashSet<>();
			String lastInSeq = list.get(0);
			vertexes.add(lastInSeq);
			for (int i = 1; i < list.size(); i++) {
				String line = list.get(i);
				vertexes.add(line);
				char ch = listChar.get(i);
				if (ch == '.') {
					edges.put(lastInSeq, line);
					lastInSeq = line;
				} else if (ch == '<') {
					edges.put(line, lastInSeq);
				} else {
					edges.put(lastInSeq, line);
				}
			}

			// If not a unique beginning nor end scene
			if (!exactlyOne(vertexes, edges, incoming) || !exactlyOne(vertexes, edges, outgoing))
				return "invalid";

			// Calculate topological order of the directed graph looking for sink vertexes
			// Could be done more efficiently using DFS (Depth First Search) but no need here

			String sink = null;
			boolean multipleSinkFound = false;
			List<String> order = new ArrayList<String>();
			while (!vertexes.isEmpty()) {
				sink = null;
				for (String v : vertexes) {
					if (edges.get(v).size() == 0) { // sink
						if (sink == null) {
							sink = v;
						} else {
							multipleSinkFound = true;
							break;
						}
					}
				}
				if (sink == null) {
					break;
				} else {
					order.add(sink);
					removeEdgesTo(edges, sink);
					vertexes.remove(sink);
				}
			}

			if (!vertexes.isEmpty()) // A cycle exists so no topological order is possible
				return "invalid";
			else if (multipleSinkFound) // multiple topological orders
				return "valid";
			else
				return listToString(Lists.reverse(order));
		}

		// PROBLEM SOLUTION ENDS HERE
		// -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		final int testId;
	}

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}
		Scanner in = new Scanner(System.in);
		int numTests = in.nextInt();
		in.nextLine();
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
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				e.printStackTrace(System.err);
			}
		}
		executor.shutdown();
	}

}