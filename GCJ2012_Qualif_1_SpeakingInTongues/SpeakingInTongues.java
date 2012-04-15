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

public class SpeakingInTongues  {

	final static String FILENAME = "A-small-attempt0";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 1; // use 1 to solve them sequentially

	// VM arguments: -ea -Xms4096M -Xmx4096M

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		static char[] mapGooglereseToEnglish = new char['z'-'a'+1];

		//boolean[] ooglereseUsed = new boolean

		static String googler = "y qee ejp mysljylc kd kxveddknmc re jsicpdrysi rbcpc ypc rtcsra dkh wyfrepkym veddknkmkrkcd de kr kd eoya kw aej tysr re ujdr lkgc jv";
		static String english = "a zoo our language is impossible to understand there are twenty six factorial possibilities so it is okay if you want to just give up";

		static boolean[] assignedG = new boolean['z'-'a'+1];
		static boolean[] assignedE = new boolean['z'-'a'+1];

		static {
			assert googler.length() == english.length();
			for (int i=0; i<googler.length(); i++) {
				char g = googler.charAt(i);
				char e = english.charAt(i);
				if (g == ' ') {
					continue;
				}
				if (mapGooglereseToEnglish[g-'a'] == 0) {
					assignedG[g-'a'] = true;
					assignedE[e-'a'] = true;
					mapGooglereseToEnglish[g-'a'] = e;
				} else {
					assert mapGooglereseToEnglish[g-'a'] == e;
				}
			}
			char unassignedG = 0;
			char unassignedE = 0;

			for (int i=0; i< 'z'-'a'+1; i++) {
				if (!assignedG[i]) {
					unassignedG = (char) ('a'+i);
				}
				if (!assignedE[i]) {
					unassignedE = (char) ('a'+i);
				}
			}
			mapGooglereseToEnglish[unassignedG-'a'] = unassignedE;
		}



		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			text = in.nextLine();
		}


		// TODO: Define input variables

		final String text;


		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			StringBuilder strAux = new StringBuilder();
			for (char c : text.toCharArray()) {
				if (c == ' ') {
					strAux.append(' ');
				} else {
					strAux.append(mapGooglereseToEnglish[c-'a']);
				}
			}

			String res = strAux.toString();

			System.err.println(String.format("%4d ms %s" , (System.nanoTime() - now) / 1000000, res));
			return res;
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