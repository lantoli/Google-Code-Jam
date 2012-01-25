import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.TreeMap;

public class Auction_10_cBbn7i_462lin implements Runnable {

	final boolean debug = false;

	long A, B, C, D;
	int M, K;

	int next_P(int p) {
		return (int) ((A * (p + 1) + B) % M);
	}

	int next_W(int w) {
		return (int) ((C * (w + 1) + D) % K);
	}

	ArrayList<Interval> calc(int p0, int w0, long N, int period_p, int period_w) {
		ArrayList<Interval> ans = new ArrayList<Auction_10_cBbn7i_462lin.Interval>();

		int[] p = new int[period_p];
		for (int i = 0; i < period_p; i++) {
			p[i] = p0;
			p0 = next_P(p0);
		}
		int[] w = new int[period_w];
		for (int i = 0; i < period_w; i++) {
			w[i] = w0;
			w0 = next_W(w0);
		}

		long[] ones = new long[period_w];
		Arrays.fill(ones, 1);

		int[] ww = new int[period_w];
		long[] w1 = new long[period_w];

		int[] T = new int[period_w];
		long[] T1 = new long[period_w];

		Interval[] pairs = new Interval[period_p];
		for (int i = 0; i < period_p; i++) {
			pairs[i] = new Interval(p[i], 0, 0, 0, 0);
		}
		int i1 = (int) (N % period_p);
		for (int md = 0; md < 2; md++) {
			long n1 = N / period_p;
			calc_min(w, ones, ww, w1, period_p, n1, md == 1);
			for (int i = i1; i < period_p; i++) {
				if (md == 0) {
					pairs[i].min = ww[i % period_w];
					pairs[i].minCount = w1[i % period_w];
				} else {
					pairs[i].max = ww[i % period_w];
					pairs[i].maxCount = w1[i % period_w];
				}
			}
			min(w, ones, ww, w1, T, T1, period_p, md == 1);
			for (int i = 0; i < i1; i++) {
				if (md == 0) {
					pairs[i].min = T[i % period_w];
					pairs[i].minCount = T1[i % period_w];
				} else {
					pairs[i].max = T[i % period_w];
					pairs[i].maxCount = T1[i % period_w];
				}
			}
		}
		if (period_p <= N) {
			i1 = period_p;
		}
		for (int i = 0; i < i1; i++) {
			ans.add(pairs[i]);
		}
		return ans;
	}

	void calc_min(int[] A, long[] A1, int[] B, long[] B1, int shift, long N, boolean mode) {
		int n = A.length;

		int[] T_A = Arrays.copyOf(A, n);
		long[] T_A1 = Arrays.copyOf(A1, n);
		int[] T_B = new int[n];
		long[] T_B1 = new long[n];

		int[] T = new int[n];
		long[] T1 = new long[n];

		if (!mode) {
			Arrays.fill(T_B, K + 1);
		} else {
			Arrays.fill(T_B, -1);
		}
		Arrays.fill(T_B1, 0);
		while (N > 0) {
			if (N % 2 == 1) {
				min(T_A, T_A1, T_B, T_B1, T, T1, shift, mode);
				int[] X = T_B;
				T_B = T;
				T = X;
				long[] Y = T_B1;
				T_B1 = T1;
				T1 = Y;
			}
			min(T_A, T_A1, T_A, T_A1, T, T1, shift, mode);
			int[] X = T_A;
			T_A = T;
			T = X;
			long[] Y = T_A1;
			T_A1 = T1;
			T1 = Y;
			N /= 2;
			shift = (int) ((shift * 2) % n);
		}
		for (int i = 0; i < n; i++) {
			B[i] = T_B[i];
			B1[i] = T_B1[i];
		}

	}

	void min(int[] A, long[] A1, int[] B, long[] B1, int[] C, long[] C1, int shift, boolean mode) {
		int n = A.length;
		for (int i = 0; i < n; i++) {
			int j = (i + shift) % n;
			if (A[i] == B[j]) {
				C[i] = A[i];
				C1[i] = A1[i] + B1[j];
			} else if ((A[i] < B[j]) ^ mode) {
				C[i] = A[i];
				C1[i] = A1[i];
			} else {
				C[i] = B[j];
				C1[i] = B1[j];
			}
		}
	}

	public Interval[] getPairsSmart(long N, int p, int w) {
		int[] was_P = new int[M];
		int[] was_W = new int[K];
		Arrays.fill(was_P, -1);
		Arrays.fill(was_W, -1);

		TreeMap<Integer, Interval> first = new TreeMap<Integer, Auction_10_cBbn7i_462lin.Interval>();
		int pos = 0;
		while (pos < N && (was_P[p] == -1 || was_W[w] == -1)) {
			was_P[p] = pos;
			was_W[w] = pos;
			Interval pair = new Interval(p, w, w, 1, 1);
			if (first.containsKey(pair.x)) {
				first.get(pair.x).consume(pair);
			} else {
				first.put(pair.x, pair);
			}
			p = next_P(p);
			w = next_W(w);
			pos++;
		}
		// Interval[] pairs_ = first.values().toArray(new Interval[] {});
		// for (int i = 0; i < pairs_.length; i++) {
		// System.out.println(pairs_[i].x + " " + pairs_[i].min + " " +
		// pairs_[i].minCount + " " + pairs_[i].max + " " + pairs_[i].maxCount);
		// }
		// System.out.println("=");

		if (pos < N) {
			ArrayList<Interval> second = calc(p, w, N - pos, pos - was_P[p], pos - was_W[w]);
			for (Interval pair : second) {
				if (first.containsKey(pair.x)) {
					first.get(pair.x).consume(pair);
				} else {
					first.put(pair.x, pair);
				}
			}
		}
		return first.values().toArray(new Interval[] {});
	}

	public Interval[] getPairsDummy(long N, int p, int w) {
		TreeMap<Integer, Interval> first = new TreeMap<Integer, Auction_10_cBbn7i_462lin.Interval>();
		int pos = 0;
		while (pos < N) {
			Interval pair = new Interval(p, w, w, 1, 1);
			if (first.containsKey(pair.x)) {
				first.get(pair.x).consume(pair);
			} else {
				first.put(pair.x, pair);
			}
			p = next_P(p);
			w = next_W(w);
			pos++;
		}
		return first.values().toArray(new Interval[] {});
	}

	public long[] smart(long N, int p0, int w0, int M, int K, int A, int B, int C, int D) {
		this.M = M;
		this.K = K;
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;

		long ans1 = 0, ans2 = 0;

		int[] min = new int[M];
		int[] max = new int[M];
		long[] minCount = new long[M];
		long[] maxCount = new long[M];
		Arrays.fill(min, K + 1);
		Arrays.fill(max, -1);

		int[] was_P = new int[M];
		int[] was_W = new int[K];
		Arrays.fill(was_P, -1);
		Arrays.fill(was_W, -1);
		int pos = 0;
		while (pos < N && (was_P[p0] == -1 || was_W[w0] == -1)) {
			was_P[p0] = pos;
			was_W[w0] = pos;
			if (min[p0] > w0) {
				min[p0] = w0;
				minCount[p0] = 1;
			} else if (min[p0] == w0) {
				minCount[p0]++;
			}
			if (max[p0] < w0) {
				max[p0] = w0;
				maxCount[p0] = 1;
			} else if (max[p0] == w0) {
				maxCount[p0]++;
			}
			p0 = next_P(p0);
			w0 = next_W(w0);
			pos++;
		}

		int period_p = pos - was_P[p0];
		int period_w = pos - was_W[w0];
		N -= pos;
		if (N > 0) {

			int[] p = new int[period_p];
			for (int i = 0; i < period_p; i++) {
				p[i] = p0;
				p0 = next_P(p0);
			}
			int[] w = new int[period_w];
			for (int i = 0; i < period_w; i++) {
				w[i] = w0;
				w0 = next_W(w0);
			}

			long[] ones = new long[period_w];
			Arrays.fill(ones, 1);

			int[] ww = new int[period_w];
			long[] w1 = new long[period_w];

			int[] T = new int[period_w];
			long[] T1 = new long[period_w];

			int i1 = (int) (N % period_p);
			for (int md = 0; md < 2; md++) {
				long n1 = N / period_p;
				calc_min(w, ones, ww, w1, period_p, n1, md == 1);
				for (int i = i1; i < period_p; i++) {
					int j = p[i];
					if (md == 0) {
						if (min[j] > ww[i % period_w]) {
							min[j] = ww[i % period_w];
							minCount[j] = w1[i % period_w];
						} else if (min[j] == ww[i % period_w]) {
							minCount[j] += w1[i % period_w];
						}
					} else {
						if (max[j] < ww[i % period_w]) {
							max[j] = ww[i % period_w];
							maxCount[j] = w1[i % period_w];
						} else if (max[j] == ww[i % period_w]) {
							maxCount[j] += w1[i % period_w];
						}
					}
				}
				min(w, ones, ww, w1, T, T1, period_p, md == 1);
				for (int i = 0; i < i1; i++) {
					int j = p[i];
					if (md == 0) {
						if (min[j] > T[i % period_w]) {
							min[j] = T[i % period_w];
							minCount[j] = T1[i % period_w];
						} else if (min[j] == T[i % period_w]) {
							minCount[j] += T1[i % period_w];
						}
					} else {
						if (max[j] < T[i % period_w]) {
							max[j] = T[i % period_w];
							maxCount[j] = T1[i % period_w];
						} else if (max[j] == T[i % period_w]) {
							maxCount[j] += T1[i % period_w];
						}
					}
				}
			}
		}

		int m = K + 1;
		for (int i = 0; i < M; i++) {
			if (m > min[i]) {
				ans1 += minCount[i];
				m = min[i];
			}
		}
		int m2 = -1;
		for (int i = M - 1; i >= 0; i--) {
			if (m2 < max[i]) {
				ans2 += maxCount[i];
				m2 = max[i];
			}
		}

		return new long[] { ans1, ans2 };
	}

	public void solve() throws Exception {
		int tests = iread();
		for (int ntest = 1; ntest <= tests; ntest++) {
			long t = System.currentTimeMillis();
			{
				long N = lread();
				int p = iread(), w = iread();
				p--;
				w--;
				long[] ans = smart(N, p, w, iread(), iread(), iread(), iread(), iread(), iread());
				out.write("Case #" + ntest + ": " + ans[1] + " " + ans[0] + "\n");
			}
			System.out.println("Case #" + ntest + ": "+(System.currentTimeMillis() - t));
		}
	}

	public void stressTest() {
		Random r = new Random(32167);
		for (int i = 0; i < 1000; i++) {
			long N = r.nextInt(100);
			int M = r.nextInt(10) + 1;
			int K = r.nextInt(10) + 1;
			int p = r.nextInt(M);
			int w = r.nextInt(K);
			int A = r.nextInt(M);
			int B = r.nextInt(M);
			int C = r.nextInt(K);
			int D = r.nextInt(K);
			long[] ans = smart(N, p, w, M, K, A, B, C, D);
			System.out.println("Test " + i + " passed");
		}
	}

	class Interval implements Comparable<Interval> {
		int x, min, max;
		long minCount, maxCount;

		public Interval(int x, int min, int max, long minCount, long maxCount) {
			super();
			this.x = x;
			this.min = min;
			this.max = max;
			this.minCount = minCount;
			this.maxCount = maxCount;
		}

		public void consume(Interval x) {
			if (x.min == min) {
				minCount += x.minCount;
			} else if (x.min < min) {
				min = x.min;
				minCount = x.minCount;
			}

			if (x.max == max) {
				maxCount += x.maxCount;
			} else if (x.max > max) {
				max = x.max;
				maxCount = x.maxCount;
			}
		}

		@Override
		public int compareTo(Interval o) {
			return x - o.x;
		}
	}

	public void run() {
		if (debug) {
			stressTest();
		}
		try {
			// in = new BufferedReader(new InputStreamReader(System.in));
			// out = new BufferedWriter(new OutputStreamWriter(System.out));
			in = new BufferedReader(new FileReader("auction.in"));
			out = new BufferedWriter(new FileWriter("auction10.out"));
			solve();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public int iread() throws Exception {
		return Integer.parseInt(readword());
	}

	public double dread() throws Exception {
		return Double.parseDouble(readword());
	}

	public long lread() throws Exception {
		return Long.parseLong(readword());
	}

	BufferedReader in;

	BufferedWriter out;

	public String readword() throws IOException {
		StringBuilder b = new StringBuilder();
		int c;
		c = in.read();
		while (c >= 0 && c <= ' ') {
			c = in.read();
		}
		if (c < 0) {
			return "";
		}
		while (c > ' ') {
			b.append((char) c);
			c = in.read();
		}
		return b.toString();
	}

	public static void main(String[] args) {
		try {
			Locale.setDefault(Locale.US);
		} catch (Exception e) {

		}
		// new Thread(new Main()).start();
		new Thread(null, new Auction_10_cBbn7i_462lin(), "1", 1 << 25).start();
	}
}