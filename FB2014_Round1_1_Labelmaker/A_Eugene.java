import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.exit;
import static java.math.BigInteger.ONE;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class A_Eugene {

	static BufferedReader in;
	static PrintWriter out;
	static StringTokenizer tok;
	static int test;

	static void solve() throws Exception {
		String l = next();
		int n = l.length();
		BigInteger idx = BigInteger.valueOf(nextLong() - 1);
		BigInteger cnt = ONE;
		for (int len = 1;; len++) {
			cnt = cnt.multiply(BigInteger.valueOf(n));
			if (idx.compareTo(cnt) < 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < len; i++) {
					sb.append(l.charAt(idx.mod(BigInteger.valueOf(n)).intValue()));
					idx = idx.divide(BigInteger.valueOf(n));
				}
				sb.reverse();
				printCase();
				out.println(sb.toString());
				return;
			} else
				idx = idx.subtract(cnt);
		}
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

			System.setIn(new FileInputStream("labelmaker.txt"));
			System.setOut(new PrintStream(new FileOutputStream("labelmaker.out")));

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
