import static java.lang.Math.min;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.sort;

import java.io.*;
import java.util.*;

public class A {

	private static void solve() throws IOException {
		int tc = nextInt();
		for (int i = 1; i <= tc; i++) {
			out.print("Case #" + i + ": ");
			System.err.println("done "+i+"/"+tc);
			solveOneTest();
		}
	}

	private static void solveOneTest() throws IOException {
		int n = nextInt(), k = nextInt();
		int x = nextInt(), A = nextInt(), B = nextInt(), m = nextInt();
		int p = nextInt(), q = nextInt();
		prior = x;
		long[] aa = new long[n];
		long[] bb = new long[n];
		long[] cc = new long[n];
		long[] dd = new long[n];
		for (int i = 0; i < k; i++) {
			aa[i] = nextLong();
			bb[i] = nextLong();
			cc[i] = nextLong();
			dd[i] = nextLong();
		}
		for (int i = k; i < n; i++) {
			aa[i] = aa[i - k] + next(A, B, m) % (2 * p) - p;
			bb[i] = aa[i] + 1 + next(A, B, m) % (2 * (bb[i % k] - aa[i % k]));
			cc[i] = cc[i - k] + next(A, B, m) % (2 * q) - q;
			dd[i] = cc[i] + 1 + next(A, B, m) % (2 * (dd[i % k] - cc[i % k]));
		}
		long[] a = new long[n];
		long[] b = new long[n];
		long[] c = new long[n];
		long[] d = new long[n];
		for (int i = 0; i < n; i++) {
			a[i] = aa[i] * 1000000 + i;
			b[i] = bb[i] * 1000000 + i;
			c[i] = cc[i] * 1000000 + i;
			d[i] = dd[i] * 1000000 + i;
		}
		int ans = get(a, b, c, d);
		if (ans == Integer.MAX_VALUE) {
			out.println(-1);
		} else {
			out.println(ans);
		}
	}

	static int fixThem(long[] a, long[] b) {
		Set<Long> top = new HashSet<Long>();
		for (long i : a) {
			top.add(i);
		}
		for (long i : b) {
			top.add(i);
		}
		long[] array = toArray(top);
		sort(array);
		for (int i = 0; i < a.length; i++) {
			a[i] = binarySearch(array, a[i]);
		}
		for (int i = 0; i < b.length; i++) {
			b[i] = binarySearch(array, b[i]);
		}
		return array.length;
	}

	static int get(long[] a, long[] b, long[] c, long[] d) {
		int top = fixThem(a, b);
		int bottom = fixThem(c, d);
		int n = a.length;
		Trap[] traps = new Trap[n];
		for (int i = 0; i < n; i++) {
			traps[i] = new Trap((int) a[i], (int) b[i], (int) c[i], (int) d[i]);
		}
		SegmentTreeMinModify tree = new SegmentTreeMinModify(bottom - 1);
		List<Trap>[] trapsForLeft = new List[top];
		List<Trap>[] trapsForRight = new List[top];
		for (int i = 0; i < top; i++) {
			trapsForLeft[i] = new ArrayList<Trap>();
			trapsForRight[i] = new ArrayList<Trap>();
		}
		for (Trap t : traps) {
			trapsForLeft[t.a].add(t);
			trapsForRight[t.b].add(t);
		}
		Trap leftmost = traps[0], rightmost = traps[0];
		for (Trap t : traps) {
			if (t.b < leftmost.b) {
				leftmost = t;
			}
			if (t.a > rightmost.a) {
				rightmost = t;
			}
		}
		TreeSet<Integer> rightToCut = new TreeSet<Integer>();
		for (Trap t : traps) {
			rightToCut.add(t.c);
			tree.add(t.c, bottom - 1, 1);
		}
		TreeSet<Integer> leftToCut = new TreeSet<Integer>();
		int theAns = Integer.MAX_VALUE;
		int ansAdd = 0;
		for (int topCut = 0; topCut < top - 1; topCut++) {
			for (Trap t : trapsForLeft[topCut]) {
				tree.add(t.c, bottom - 1, -1);
				ansAdd++;
				rightToCut.remove(t.c);
			}
			for (Trap t : trapsForRight[topCut]) {
				tree.add(0, t.d, 1);
				ansAdd--;
				leftToCut.add(t.d);
			}
			if (!leftToCut.isEmpty() && !rightToCut.isEmpty()) {
				int cutLeft = leftToCut.first();
				int cutRight = rightToCut.last();
				int curAns = tree.get(cutLeft, cutRight) + ansAdd;
				theAns = min(theAns, curAns);
			}
		}
		if (theAns >= Integer.MAX_VALUE / 3) {
			theAns = Integer.MAX_VALUE;
		}
		return theAns;
	}

	static long[] toArray(Collection<Long> collection) {
		long[] res = new long[collection.size()];
		int i = 0;
		for (long element : collection) {
			res[i++] = element;
		}
		return res;
	}

	static long next(int a, int b, int m) {
		prior = (a * prior + b) % m;
		return prior;
	}

	static long prior;

	static class Trap {
		@Override
		public String toString() {
			return "Trap [a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + "]";
		}

		int a, b, c, d;

		private Trap(int a, int b, int c, int d) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}
	}

	public static class SegmentTreeMinModify {

		final static int INF = Integer.MAX_VALUE / 2;
		int h, n;
		int[] min;
		int[] add;

		SegmentTreeMinModify(int n) {
			this.n = n;
			while (1 << h < n)
				h++;
			min = new int[1 << h + 1];
			add = new int[1 << h + 1];
			min[0] = 0;
		}

		SegmentTreeMinModify(int[] x) {
			this(x.length);
			System.arraycopy(x, 0, min, 1 << h, n);
			for (int i = (1 << h) - 1; i > 0; i--) {
				min[i] = Math.min(min[2 * i], min[2 * i + 1]);
			}
		}

		void add(int l, int r, int val) {
			add1(l, r, 1, 0, 1 << h, val);
		}

		void add1(int l, int r, int i, int from, int to, int val) {
			if (from >= r || to <= l)
				return;
			if (from >= l && to <= r) {
				add[i] += val;
				min[i] += val;
			} else {
				int m = (from + to) / 2;
				add1(l, r, 2 * i, from, m, val);
				add1(l, r, 2 * i + 1, m, to, val);
				min[i] = Math.min(min[2 * i], min[2 * i + 1]) + add[i];
			}
		}

		int get(int l, int r) {
			return get1(l, r, 1, 0, 1 << h);
		}

		int get1(int l, int r, int i, int from, int to) {
			if (from >= r || to <= l)
				return INF;
			if (from >= l && to <= r) {
				return min[i];
			} else {
				int m = (from + to) / 2;
				int a = get1(l, r, 2 * i, from, m);
				int b = get1(l, r, 2 * i + 1, m, to);
				return Math.min(a, b) + add[i];
			}
		}
	}

	public static void main(String[] args) {
		try {
			br = new BufferedReader(new FileReader("A.in"));
			out = new PrintWriter("A.out");
			solve();
			out.close();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(239);
		}
	}

	static BufferedReader br;
	static StringTokenizer st;
	static PrintWriter out;

	static String nextToken() throws IOException {
		while (st == null || !st.hasMoreTokens()) {
			String line = br.readLine();
			if (line == null)
				return null;
			st = new StringTokenizer(line);
		}
		return st.nextToken();
	}

	static int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	static long nextLong() throws IOException {
		return Long.parseLong(nextToken());
	}

	static double nextDouble() throws IOException {
		return Double.parseDouble(nextToken());
	}
}