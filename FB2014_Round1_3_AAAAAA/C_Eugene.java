import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.max;
import static java.lang.System.exit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class C_Eugene {

	static BufferedReader in;
	static PrintWriter out;
	static StringTokenizer tok;
	static int test;

	static void solve() throws Exception {
		int n = nextInt();
		int m = nextInt();
		char map[][] = new char[n][];
		for (int i = 0; i < n; i++)
			map[i] = next().toCharArray();
		int ans = 1;
		int dyn1[][] = new int[n][m];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++) {
				if (i == 0 && j == 0) {
					dyn1[i][j] = 1;
					continue;
				}
				int cur = Integer.MIN_VALUE;
				if (map[i][j] == '.') {
					if (i > 0)
						cur = max(cur, dyn1[i - 1][j] + 1);
					if (j > 0)
						cur = max(cur, dyn1[i][j - 1] + 1);
				}
				dyn1[i][j] = cur;
				ans = max(ans, cur);
			}
		int dyn2[][] = new int[n][m];
		for (int i = n - 1; i >= 0; i--)
			for (int j = m - 1; j >= 0; j--) {
				int cur = 0;
				if (map[i][j] == '.') {
					cur = 1;
					if (i < n - 1)
						cur = max(cur, dyn2[i + 1][j] + 1);
					if (j < m - 1)
						cur = max(cur, dyn2[i][j + 1] + 1);
				}
				dyn2[i][j] = cur;
			}
		for (int i = 1; i < n; i++) {
			int cur = 0;
			for (int j = 0; j < m; j++) {
				if (map[i][j] == '.')
					cur = max(cur, (i < n - 1 ? dyn2[i + 1][j] : 0) - j);
				else
					cur = -j - 1;
				ans = max(ans, dyn1[i - 1][j] + cur + j + 1);
			}
		}
		for (int j = 1; j < m; j++) {
			int cur = 0;
			for (int i = 0; i < n; i++) {
				if (map[i][j] == '.')
					cur = max(cur, (j < m - 1 ? dyn2[i][j + 1] : 0) - i);
				else
					cur = -i - 1;
				ans = max(ans, dyn1[i][j - 1] + cur + i + 1);
			}
		}
		printCase();
		out.println(ans);
	}

	static void printCase() {
		out.print("Case #" + test + ": ");
	}

	static void printlnCase() {
		out.println("Case #" + test + ":");
	}

	static int nextInt() throws IOException {
		return parseInt(next());
	}

	static long nextLong() throws IOException {
		return parseLong(next());
	}

	static double nextDouble() throws IOException {
		return parseDouble(next());
	}

	static String next() throws IOException {
		while (tok == null || !tok.hasMoreTokens())
			tok = new StringTokenizer(in.readLine());
		return tok.nextToken();
	}

	public static void main(String[] args) {
		try {

			System.setIn(new FileInputStream("aaaaaa.txt"));
			System.setOut(new PrintStream(new FileOutputStream("aaaaaa.out")));

			in = new BufferedReader(new InputStreamReader(System.in));
			out = new PrintWriter(new OutputStreamWriter(System.out));
			int tests = nextInt();
			for (test = 1; test <= tests; test++)
				solve();
			in.close();
			out.close();
		} catch (Throwable e) {
			e.printStackTrace();
			exit(1);
		}
	}
}