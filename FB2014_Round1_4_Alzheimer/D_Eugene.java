import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.System.exit;
import static java.util.Arrays.sort;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class D_Eugene {

	static BufferedReader in;
	static PrintWriter out;
	static StringTokenizer tok;
	static int test;

	static final int PRIMES[] = new int[64];
	static {
		i: for (int i = 0, p = 2; i < PRIMES.length; p++) {
			for (int j = 2; j * j <= p; j++)
				if (p % j == 0)
					continue i;
			PRIMES[i++] = p;
		}
	}

	static final long MASKS[] = new long[256];
	static {
		MASKS[0] = -1;
		for (int i = 1; i < MASKS.length; i++) {
			int cur = i;
			long curMask = 0;
			for (int j = 0; j < PRIMES.length && cur > 1; j++)
				while (cur % PRIMES[j] == 0) {
					cur /= PRIMES[j];
					curMask |= 1L << j;
				}
			if (cur != 1)
				throw new AssertionError();
			MASKS[i] = curMask;
		}
	}

	static int n;
	static int a[];
	static int s[];

	static void solve() throws Exception {
		n = nextInt();
		int k = nextInt();
		a = new int[n];
		s = new int[n + 1];
		int ans = 0;
		for (int i = 0; i < n; i++) {
			a[i] = nextInt();
			ans += (k - a[i] % k) % k;
			a[i] += (k - a[i] % k) % k;
			a[i] /= k;
		}
		sort(a);
		for (int i = n - 1; i >= 0; i--)
			s[i] = s[i + 1] + a[i];
		for (int rem = 0;; rem++)
			if (go(0, 0, 0, rem)) {
				printCase();
				out.println(ans + k * rem);
				return;
			}
	}

	static boolean go(int i, int v, long mask, int rem) {
		if (i >= n)
			return true;
		int top = a[i] + rem;
		int limit = min(top, (s[i] + rem) / (n - i));
		for (int cur = max(a[i], v); cur <= limit; cur++) {
			long curMask = MASKS[cur];
			if ((mask & curMask) != 0)
				continue;
			if (go(i + 1, cur, mask | curMask, top - cur))
				return true;
		}
		return false;
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

			System.setIn(new FileInputStream("preventing_alzheimers.txt"));
			System.setOut(new PrintStream(new FileOutputStream("preventing_alzheimers.out")));

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