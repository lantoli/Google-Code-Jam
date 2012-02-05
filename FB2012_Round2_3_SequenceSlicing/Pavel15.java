import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.StringTokenizer;

/**
 * @author Pavel Mavrin
 */
public class Pavel15 {

	BufferedReader br;
	StringTokenizer st;
	PrintWriter out;

	public Pavel15() throws FileNotFoundException {
		br = new BufferedReader(new FileReader("sequence_slicing.in"));
		out = new PrintWriter("sequence_slicing_15.out");
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

	private String solveCase() throws IOException {
		int n = nextInt();
		long k = nextInt();
		long r = nextInt();
		int[] s = new int[n];
		for (int i = 0; i < n; i++) {
			s[i] = nextInt();
		}
		if (k == 1) {
			return "1/1";
		}
		long den = 0;
		for (int i = 0; i < n; i++) {
			int[] ss = new int[n];
			for (int j = 0; j < n; j++) {
				ss[j] = s[(i + j) % n] + n * ((i + j) / n);
			}
			int l = find(ss, k);
			//            System.out.println(i + " " + l);
			long c0 = (r - (i + l) + 1);
			if (c0 > 0) {
				long m = c0 / n;
				den += m * (m + 1) / 2 * n;
				den += (m + 1) * (c0 % n);
			}
			//            System.out.println(den);
		}
		den *= 2;
		long nom = (r + 1) * (r + 1);

		long dd = BigInteger.valueOf(den).gcd(BigInteger.valueOf(nom)).longValue();
		den /= dd;
		nom /= dd;
		if (den > nom) {
			System.out.println("!!!!");
		}
		return den + "/" + nom;
	}

	private int find(int[] ss, long k) {
		int n = ss.length;
		boolean[] z = new boolean[200001];
		for (int i = 0; i < n; i++) {
			if (z[ss[i]]) {
				ss[i] = -1;
			} else {
				z[ss[i]] = true;
			}
		}
		int[] pp = new int[n];
		for (int j = 0; j < n; j++) {
			int i = ss[j];
			if (i >= 0) {
				int p = 1;
				int ii = i + n;
				while (ii < z.length && !z[ii]) {
					p++;
					ii += n;
				}
				if (ii >= z.length) {
					pp[j] = 1000000001;
				} else {
					pp[j] = p;
				}
			}
		}
		int l = -1;
		int r = 1000000001;
		while (r > l + 1) {
			int m = (l + r) / 2;
			int cc = 0;
			for (int j = 0; j < n; j++) {
				if (ss[j] >= 0) {
					int kk = m / n + ((j <= m % n) ? 1 : 0);
					kk = Math.min(kk, pp[j]);
					cc += kk;
				}
			}
			if (cc >= k) {
				r = m;
			} else {
				l = m;
			}
		}
		return r;
	}
}
