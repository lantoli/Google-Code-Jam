import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
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
		Sequence06 solver = new Sequence06();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++) {
			solver.solve(i, in, out);
		}
		out.close();
	}
}

class Sequence06 {
	public void solve(int testNumber, InputReader in, PrintWriter out) {
		System.err.println(testNumber);
		long startTime = System.currentTimeMillis();
		int n = in.nextInt();
		long k = in.nextLong();
		long r = in.nextLong();
		long[] s = new long[n];
		for (int i = 0; i < n; ++i) {
			s[i] = in.nextLong();
		}
		if (k == 1) {
			out.println("Case #" + testNumber + ": 1/1");
			return;
		}
		long[] prevRep = new long[n];
		for (int i = 0; i < n; ++i) {
			prevRep[i] = Long.MAX_VALUE;
			for (int j = 0; j < n; ++j) {
				long delta = s[j] - s[i];
				if (delta < 0 || delta % n != 0) {
					continue;
				}
				delta /= n;
				if (delta == 0 && j >= i) {
					continue;
				}
				prevRep[i] = Math.min(prevRep[i], n - j + (delta - 1) * n + i);
			}
		}
		long res = 0;
		for (int start = 0; start < n; ++start) {
			long[] rotRep = new long[n];
			for (int i = 0; i < n; ++i) {
				rotRep[i] = prevRep[(start + i) % n];
			}
			long left = 0;
			long right = r + 10;
			if (getCount(rotRep, right) < k) {
				continue;
			}
			while (right - left > 1) {
				long middle = (left + right) / 2;
				if (getCount(rotRep, middle) < k) {
					left = middle;
				} else {
					right = middle;
				}
			}
			res += sumFrom(r - start - right + 2, n);
		}
		long p = 2 * res;
		long q = (r + 1) * (r + 1);
		long g = BigInteger.valueOf(p).gcd(BigInteger.valueOf(q)).longValue();
		out.println("Case #" + testNumber + ": " + p / g + "/" + q / g);
		System.err.println(System.currentTimeMillis() - startTime);
	}

	private long getCount(long[] prevRep, long cnt) {
		long n = prevRep.length;
		long res = 0;
		for (int i = 0; i < n; ++i) {
			long first = i;
			long last = cnt - 1;
			if (last < first) {
				continue;
			}
			last -= (last - i) % n;
			if (last % n != i) {
				throw new RuntimeException();
			}
			if (prevRep[i] < Long.MAX_VALUE) {
				last = Math.min(last, first + n * ((prevRep[i] - i + n - 1) / n - 1));
			}
			if (last < first) {
				continue;
			}
			res += (last - first) / n + 1;
		}
		return res;
	}

	private long sumFrom(long start, long n) {
		if (start <= 0) {
			return 0;
		}
		long cnt = start / n + 1;
		return (start + (start - (cnt - 1) * n)) * cnt / 2;
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

	public long nextLong() {
		return Long.parseLong(next());
	}

}

