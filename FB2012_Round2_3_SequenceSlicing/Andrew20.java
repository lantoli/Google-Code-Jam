import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Andrew20 implements Runnable {
	public void solve() throws IOException {
		//        stress();

		Scanner in = new Scanner(new File("sequence_slicing.in"));
		PrintWriter out = new PrintWriter(new File("sequence_slicing_20.out"));

		int testNo = in.nextInt();
		for (int test = 1; test <= testNo; test++) {
			long time = System.currentTimeMillis();
			int n = in.nextInt();
			long k = in.nextLong();
			long r = in.nextLong();
			int[] s = new int[n];
			for (int i = 0; i < n; i++) {
				s[i] = in.nextInt();
			}

			String ans = solveSmart(n, k, r, s);

			time = System.currentTimeMillis() - time;
			out.printf("Case #%d: %s\n", test, ans);
			System.err.println("Test " + test + " - " + time + "ms");
		}

		in.close();
		out.close();
	}

	int MAX = 100000;

	String solveSmart(int n, long k, long r, int[] s) {
		if (k == 1) {
			return "1/1";
		}

		boolean[] was = new boolean[4 * MAX + 1];
		long[] needForK = new long[n];
		work:
			for (int i = 0; i < n; i++) {
				Arrays.fill(was, false);
				int passed = 0;
				int delta = 0;
				int distinct = 0;
				int start = i;
				search:
					for (int iter = 0; passed < MAX; iter++, delta += n) {
						for (int index = start; index < n; index++) {
							int v = s[index] + delta;
							passed++;
							if (!was[v]) {
								distinct++;
							}
							was[v] = true;
							if (distinct == k) {
								needForK[i] = passed;
								break search;
							}
						}
						start = 0;
					}
				if (needForK[i] != 0) {
					continue;
				}

				int[] distinctBy = new int[n + 1];
				int newDistinct = 0;
				for (int index = 0; index < n; index++) {
					int v = s[index] + delta;
					passed++;
					if (!was[v]) {
						distinct++;
						newDistinct++;
					}
					distinctBy[index + 1] = newDistinct;
					was[v] = true;
					if (distinct == k) {
						needForK[i] = passed;
						continue work;
					}
				}

				long canHave = distinct + (k - distinct) / newDistinct * newDistinct;
				long need = (k - distinct) / newDistinct * n;
				if ((k - distinct) % newDistinct == 0) {
					canHave -= newDistinct;
					need -= n;
				}
				int more = 0;
				while (canHave + distinctBy[more] < k) {
					more++;
				}

				needForK[i] = passed + need + more;
			}

		long total = (r + 1) * (r + 1);
		long good = 0;
		for (int i = 0; i < n; i++) {
			long start = needForK[i] + i - 1;
			if (start > r) {
				continue;
			}
			long goodStarts = ((r - start) / n + 1);
			long firstGood = r - start + 1;

			good += (firstGood + (firstGood - n * (goodStarts - 1))) * goodStarts / 2;
		}

		good *= 2;
		long gcd = gcd(good, total);
		good /= gcd;
		total /= gcd;
		return good + "/" + total;
	}

	void stress() {
		Random rnd = new Random(5594724774L);
		int cnt = 0;
		while (true) {
			int n = rnd.nextInt(30) + 1;
			long r = rnd.nextInt(100) + 1;
			long k = rnd.nextInt((int) r) + 1;
			int[] s = new int[n];
			for (int i = 0; i < n; i++) {
				s[i] = rnd.nextInt(MAX) + 1;
			}
			System.err.println(n + " " + k + " " + r + " " + Arrays.toString(s));
			String smart = solveSmart(n, k, r, s);
			String stupid = solveStupid(n, k, r, s);
			if (!smart.equals(stupid)) {
				System.err.println(n + " " + k + " " + r + " " + Arrays.toString(s) + " " + stupid + " " + smart);
				throw new AssertionError();
			}
			System.err.println(smart);
			cnt++;
			System.err.println(cnt + " passed");
		}
	}

	String solveStupid(int n, long k, long r, int[] s) {
		long total = 0;
		long good = 0;
		for (int a = 0; a <= r; a++) {
			for (int b = 0; b <= r; b++) {
				total++;
				HashSet<Integer> used = new HashSet<Integer>();
				for (long v = min(a, b); v <= max(a, b); v++) {
					int c = s[(int) (v % n)] + n * (int) (v / n);
					used.add(c);
				}
				if (used.size() >= k) {
					good++;
				}
			}
		}
		long d = gcd(good, total);
		good /= d;
		total /= d;
		return good + "/" + total;
	}

	long gcd(long a, long b) {
		while (b != 0) {
			long t = a % b;
			a = b;
			b = t;
		}
		return a;
	}

	public void run() {
		try {
			solve();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] s) {
		new Thread(new Andrew20()).start();
	}
}