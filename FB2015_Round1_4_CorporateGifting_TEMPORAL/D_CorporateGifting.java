import java.io.FileInputStream;
import java.util.Scanner;

import org.jacop.constraints.Sum;
import org.jacop.constraints.XneqY;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

// Facebook Hacker Cup 2015, round 1 D: https://www.facebook.com/hackercup/problems.php?pid=759650454070547&round=344496159068801

// Using jacop-4.0.0.jar

// BAD PERFORMANCE FOR BIG INPUT

public class D_CorporateGifting {

	final static String FILENAME = "gift";

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		// System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();
			for (int test = 1; test <= tests; test++) {

				int N = in.nextInt();

				Store store = new Store();
				IntVar[] v = new IntVar[N];
				for (int i = 0; i < N; i++) {
					v[i] = new IntVar(store, "v" + i, 1, N);
				}

				in.nextInt();
				for (int i = 1; i < N; i++) {
					int manager = in.nextInt();
					store.impose(new XneqY(v[i], v[manager - 1]));
				}

				IntVar sum = new IntVar(store, "sum", 0, 10000000);
				store.impose(new Sum(v, sum));

				Search<IntVar> search = new DepthFirstSearch<IntVar>();

				SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v, new IndomainMin<IntVar>());

				// search.setTimeOut(f);
				search.setPrintInfo(false);

				boolean result = search.labeling(store, select, sum);

				/*
				 * if (result) { System.out.println("SUM: " + sum.value()); for (int i = 0; i < N; i++)
				 * System.out.format("%s %d\n", v[i].id, v[i].value()); System.out.println(); } else {
				 * System.out.println("NO"); }
				 */

				long res = sum.value();

				System.out.format("Case #%d: %d%n", test, res);
			}
		}

	}
}
