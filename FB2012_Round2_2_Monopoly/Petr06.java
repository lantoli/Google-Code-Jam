import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Petr06 {
	public static void main(String[] args) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream("sample.in");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream("sample.out");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Monopoly06 solver = new Monopoly06();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++) {
			solver.solve(i, in, out);
		}
		out.close();
	}
}

class Monopoly06 {
	static final int INF = (int) 1e9;

	public void solve(int testNumber, InputReader in, PrintWriter out) {
		int n = in.nextInt();
		int d = in.nextInt();
		int[] p = new int[n];
		for (int i = 0; i < n; ++i) {
			p[i] = i;
		}
		int[][] best = new int[n][];
		for (int i = 0; i < n; ++i) {
			best[i] = new int[]{1};
		}
		for (int step = 0; step < n - 1; ++step) {
			int a = getRoot(p, in.nextInt() - 1);
			int b = getRoot(p, in.nextInt() - 1);
			if (a == b) {
				throw new RuntimeException();
			}
			int[] a1 = best[a];
			int[] a2 = best[b];
			int ma1 = INF;
			for (int x : a1) {
				ma1 = Math.min(ma1, x);
			}
			int ma2 = INF;
			for (int x : a2) {
				ma2 = Math.min(ma2, x);
			}
			p[a] = b;
			int[] r = new int[Math.min(Math.max(a1.length, a2.length) + 1, d + 1)];
			best[a] = null;
			best[b] = r;
			Arrays.fill(r, INF);
			for (int i = 0; i < a1.length; ++i){
				if (i + 1 <= d) {
					r[i + 1] = Math.min(r[i + 1], Math.max(a1[i], ma2 + 1));
				}
			}
			for (int i = 0; i < a2.length; ++i){
				if (i + 1 <= d) {
					r[i + 1] = Math.min(r[i + 1], Math.max(a2[i], ma1 + 1));
				}
			}
		}
		int res = INF;
		for (int x : best[getRoot(p, 0)]) {
			res = Math.min(res, x);
		}
		out.println("Case #" + testNumber + ": " + res);
	}

	private int getRoot(int[] p, int at) {
		if (p[at] == at) {
			return at;
		}
		p[at] = getRoot(p, p[at]);
		return p[at];
	}
}

class InputReader {
	private final BufferedReader reader;
	private StringTokenizer tokenizer;

	public InputReader(InputStream stream) {
		reader = new BufferedReader(new InputStreamReader(stream));
		tokenizer = null;
	}

	public String next() {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(reader.readLine());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return tokenizer.nextToken();
	}

	public int nextInt() {
		return Integer.parseInt(next());
	}

}

