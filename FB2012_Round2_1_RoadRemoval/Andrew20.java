import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Andrew20 implements Runnable {
	int n;
	int k;
	ArrayList<Integer>[] edges;

	boolean[] u;

	void dfs1(int i) {
		u[i] = true;
		for (int j : edges[i]) {
			if (!u[j]) {
				dfs1(j);
			}
		}
	}

	void dfs2(int i) {
		u[i] = true;
		for (int j : edges[i]) {
			if (j >= k && !u[j]) {
				dfs2(j);
			}
		}
	}

	public void solve() throws IOException {
		Scanner in = new Scanner(new File("road_removal.in"));
		PrintWriter out = new PrintWriter(new File("road_removal_20.out"));

		int testNo = in.nextInt();
		for (int test = 1; test <= testNo; test++) {
			long time = System.currentTimeMillis();
			n = in.nextInt();
			int m = in.nextInt();
			k = in.nextInt();
			System.err.println(n + " " + m + " " + k);
			edges = new ArrayList[n];
			for (int i = 0; i < n; i++) {
				edges[i] = new ArrayList<Integer>();
			}

			int inner = 0;
			for (int i = 0; i < m; i++) {
				int u = in.nextInt();
				int v = in.nextInt();
				edges[u].add(v);
				edges[v].add(u);
				if (u >= k && v >= k) {
					inner++;
				}
			}

			int cc = 0;
			u = new boolean[n];
			for (int i = 0; i < n; i++) {
				if (!u[i]) {
					cc++;
					dfs1(i);
				}
			}

			Arrays.fill(u, false);
			int otherC = 0;
			for (int i = k; i < n; i++) {
				if (!u[i]) {
					otherC++;
					dfs2(i);
				}
			}

			int ans = m - (inner + (k + otherC - cc));
			out.printf("Case #%d: %d\n", test, ans);
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