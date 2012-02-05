import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Petr06 {
	public static void main(String[] args) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream("road_removal.in");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream("road_removal_06.out");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Roads solver = new Roads();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++) {
			solver.solve(i, in, out);
		}
		out.close();
	}
}

class Roads {
	public void solve(int testNumber, InputReader in, PrintWriter out) {
		int n = in.nextInt();
		int m = in.nextInt();
		int k = in.nextInt();
		int[] a = new int[m];
		int[] b = new int[m];
		for (int i = 0; i < m; ++i) {
			a[i] = in.nextInt();
			b[i] = in.nextInt();
		}
		int[] p = new int[n];
		for (int i = 0; i < n; ++i) {
			p[i] = i;
		}
		int res = 0;
		for (int i = 0; i < m; ++i) {
			if (a[i] >= k && b[i] >= k) {
				int aa = get(p, a[i]);
				int bb = get(p, b[i]);
				p[aa] = bb;
			} else {
				++res;
			}
		}
		for (int i = 0; i < n; ++i) {
			if (p[i] == i) {
				--res;
			}
		}
		for (int i = 0; i < m; ++i) {
			if (!(a[i] >= k && b[i] >= k)) {
				int aa = get(p, a[i]);
				int bb = get(p, b[i]);
				p[aa] = bb;
			}
		}
		for (int i = 0; i < n; ++i) {
			if (p[i] == i) {
				++res;
			}
		}
		out.println("Case #" + testNumber + ": " + res);
	}

	private int get(int[] p, int at) {
		if (p[at] == at) {
			return at;
		}
		p[at] = get(p, p[at]);
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

