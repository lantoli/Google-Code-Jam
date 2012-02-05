import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author Pavel Mavrin
 */
public class Pavel15 {

	BufferedReader br;
	StringTokenizer st;
	PrintWriter out;
	private int[] p;

	public Pavel15() throws FileNotFoundException {
		br = new BufferedReader(new FileReader("monopoly.in"));
		out = new PrintWriter("monopoly_15.out");
	}

	String next() throws IOException {
		while (st == null || !st.hasMoreTokens()) {
			st = new StringTokenizer(br.readLine());
		}
		return st.nextToken();
	}

	int nextInt() throws IOException {
		return Integer.parseInt(next());
	}

	public static void main(String[] args) throws IOException {
		new Pavel15().solve();
	}

	private void solve() throws IOException {
		int n = nextInt();
		for (int i = 0; i < n; i++) {
			String s = solveCase();
			out.println("Case #" + (i + 1) + ": " + s);
			System.out.println("Case #" + (i + 1) + ": " + s);
		}
		out.close();
	}

	private int get(int i) {
		if (p[i] == i) {
			return i;
		}
		p[i] = get(p[i]);
		return p[i];
	}

	private void union(int x, int y) {
		x = get(x);
		y = get(y);
		p[x] = y;
	}

	private String solveCase() throws IOException {
		int n = nextInt();
		int d = nextInt();
		p = new int[n];
		for (int i = 0; i < n; i++) {
			p[i] = i;
		}
		int[] z = new int[d + 1];
		Arrays.fill(z, 1);
		int[][] dd = new int[n][];
		for (int i = 0; i < n; i++) {
			dd[i] = z;
		}

		for (int i = 0; i < n - 1; i++) {
			int x = nextInt() - 1;
			int y = nextInt() - 1;
			x = get(x);
			y = get(y);
			z = new int[d + 1];
			int[] z1 = dd[x];
			int[] z2 = dd[y];
			z[0] = 1000000000;
			for (int j = 1; j <= d; j++) {
				z[j] = Math.min(Math.max(z1[d] + 1, z2[j - 1]), Math.max(z1[j - 1], z2[d] + 1));
			}
			union(x, y);
			dd[get(x)] = z;
		}

		return "" + dd[get(0)][d];
	}
}
