import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

//VM arguments: -ea -Xms4096M -Xmx8192M

public class A_Boomerang {

	final static String FILENAME = "boomerang_constellations";

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();
			for (int test = 1; test <= tests; test++) {
				int N = in.nextInt();
				int x[] = new int[N];
				int y[] = new int[N];
				for (int i=0; i<N; i++) {
					x[i] = in.nextInt();
					y[i] = in.nextInt();
					in.nextLine();
				}
				long total = 0;
				for (int i=0; i<N; i++) {
					Multiset<Integer> set = TreeMultiset.create();
					for (int j=0; j<N; j++) {
						if (i != j) {
							int dx = x[i] - x[j];
							int dy = y[i] - y[j];
							int dist = dx*dx + dy*dy;
							set.add(dist);
							int count = set.count(dist);
							if (count>1) {
								total -= (count-2) * (count-1);
								total += count * (count-1);
							}
						}
					}					
				}
				total /= 2;
				System.out.format("Case #%d: %d%n", test, total);
				System.err.format("Case #%d: %d%n", test, total);
			}
		}

	}
}
