import static java.lang.Math.max;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Andrew20 implements Runnable {
	class DSU {
		int[] p;
		int[] r;

		DSU(int n) {
			p = new int[n];
			r = new int[n];
			for (int i = 0; i < n; i++) {
				p[i] = i;
			}
		}

		int get(int v) {
			if (v != p[v]) {
				p[v] = get(p[v]);
			}
			return p[v];
		}

		int union(int u, int v) {
			if (r[u] == r[v]) {
				r[u]++;
			}
			if (r[u] > r[v]) {
				p[v] = u;
				return u;
			} else {
				p[u] = v;
				return v;
			}
		}
	}

	final static int INF = 1000000000;

	public void solve() throws IOException {
		Scanner in = new Scanner(new File("monopoly.in"));
		PrintWriter out = new PrintWriter(new File("monopoly_20.out"));

		int testNo = in.nextInt();
		for (int test = 1; test <= testNo; test++) {
			long time = System.currentTimeMillis();
			int n = in.nextInt();
			int d = in.nextInt();

			int[][] a = new int[n][d + 1];
			for (int[] z : a) {
				Arrays.fill(z, 1);
			}
			DSU dsu = new DSU(n);

			for (int i = 0; i < n - 1; i++) {
				int u = in.nextInt() - 1;
				int v = in.nextInt() - 1;
				u = dsu.get(u);
				v = dsu.get(v);
				int w = dsu.union(u, v);
				int aud = a[u][d];
				int avd = a[v][d];
				for (int j = d; j >= 0; j--) {
					a[w][j] = INF;
					if (j > 0 && a[u][j - 1] != INF) {
						int t = max(a[u][j - 1], avd + 1);
						if (a[w][j] > t) {
							a[w][j] = t;
						}
					}
					if (j > 0 && a[v][j - 1] != INF) {
						int t = max(a[v][j - 1], aud + 1);
						if (a[w][j] > t) {
							a[w][j] = t;
						}
					}
				}
			}

			int u = dsu.get(0);
			out.printf("Case #%d: %d\n", test, a[u][d]);
			time = System.currentTimeMillis() - time;
			System.err.println("Test " + test + " in " + time + "ms");
		}

		in.close();
		out.close();
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