import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.util.StringTokenizer;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream("trapezoids.in");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream("trapezoids.out");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Trapezoids solver = new Trapezoids();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
}

class Trapezoids {
    static final int INF = (int) 1e9;
    
    static class RNG {
        long prior;
        private long a;
        private long b;
        private long m;

        RNG(long X, long A, long B, long M) {
            this.prior = X;
            a = A;
            b = B;
            m = M;
        }
        
        long next() {
            prior = (a * prior + b) % m;
            return prior;
        }
    }
    
	public void solve(int testNumber, InputReader in, PrintWriter out) {
        int n = in.nextInt();
        int k = in.nextInt();
        long X = in.nextInt();
        long A = in.nextInt();
        long B = in.nextInt();
        long M = in.nextInt();
        long p = in.nextInt();
        long q = in.nextInt();
        long[] a = new long[n];
        long[] b = new long[n];
        long[] c = new long[n];
        long[] d = new long[n];
        for (int i = 0; i < k; ++i) {
            a[i] = in.nextInt();
            b[i] = in.nextInt();
            c[i] = in.nextInt();
            d[i] = in.nextInt();
        }
        RNG rng = new RNG(X, A, B, M);
        for (int i = k; i < n; ++i) {
            a [i] = a [i - k] + rng.next() % (2 * p) - p;
            b [i] = a [i] + 1 + rng.next() % (2 * (b [i % k] - a [i % k]));
            c [i] = c [i - k] + rng.next() % (2 * q) - q;
            d [i] = c [i] + 1 + rng.next() % (2 * (d [i % k] - c [i % k]));
        }
        for (int i = 0; i < n; ++i) {
            a[i] = a[i] * 1000000 + i;
            b[i] = b[i] * 1000000 + i;
            if (a[i] > b[i]) {
                long tmp = a[i];
                a[i] = b[i];
                b[i] = tmp;
            }
            c[i] = c[i] * 1000000 + i;
            d[i] = d[i] * 1000000 + i;
            if (c[i] > d[i]) {
                long tmp = c[i];
                c[i] = d[i];
                d[i] = tmp;
            }
        }
        int maxAB = compressCoordinates(a, b);
        int maxCD = compressCoordinates(c, d);
        int[] nextSameA = new int[n];
        int[] nextSameB = new int[n];
        int[] firstByA = new int[maxAB];
        int[] firstByB = new int[maxAB];
        Arrays.fill(firstByA, -1);
        Arrays.fill(firstByB, -1);
        for (int i = 0; i < n; ++i) {
            nextSameA[i] = firstByA[((int) a[i])];
            firstByA[((int) a[i])] = i;
            nextSameB[i] = firstByB[((int) b[i])];
            firstByB[((int) b[i])] = i;
        }
        int earliest = INF;
        IntervalTree tree = new IntervalTree(maxCD);
        int[] latestFor = new int[maxAB];
        int latest = -INF;
        for (int topSplitAfter = maxAB - 1; topSplitAfter >= 0; --topSplitAfter) {
            latestFor[topSplitAfter] = latest;
            int cur = firstByA[topSplitAfter];
            while (cur >= 0) {
                latest = Math.max(latest, (int) c[cur] - 1);
                tree.addTo((int) c[cur], maxCD - 1, 1);
                cur = nextSameA[cur];
            }
        }    
        int anyway = 0;
        int res = INF;
        for (int topSplitAfter = 0; topSplitAfter < maxAB; ++topSplitAfter) {
            latest = latestFor[topSplitAfter];
            int cur = firstByA[topSplitAfter];
            while (cur >= 0) {
                ++anyway;
                tree.addTo((int) c[cur], maxCD - 1, -1);
                cur = nextSameA[cur];
            }
            cur = firstByB[topSplitAfter];
            while (cur >= 0) {
                earliest = Math.min(earliest, (int) d[cur]);
                tree.addTo(0, (int) (d[cur] - 1), 1);
                --anyway;
                cur = nextSameB[cur];
            }
            res = Math.min(res, tree.getMin(earliest, latest) + anyway);
        }
        if (res >= INF / 2)
            res = -1;
        out.println("Case #" + testNumber + ": " + res);
	}
    
    static class IntervalTree {
        int[] delta;
        int[] min;
        int n;
        
        public IntervalTree(int n) {
            this.n = n;
            delta = new int[4 * n + 10];
            min = new int[4 * n + 10];
        }
        
        void addTo(int left, int right, int what) {
            internalAddTo(0, 0, n - 1, left, right, what);
        }

        private void internalAddTo(int root, int rl, int rr, int left, int right, int what) {
            if (left > right)
                return;
            if (rl == left && rr == right) {
                delta[root] += what;
                min[root] += what;
                return;
            }
            int rm = (rl + rr) / 2;
            internalAddTo(root * 2 + 1, rl, rm, left, Math.min(right, rm), what);
            internalAddTo(root * 2 + 2, rm + 1, rr, Math.max(rm + 1, left), right, what);
            min[root] = delta[root] + Math.min(min[root * 2 + 1], min[root * 2 + 2]);
        }
        
        private int getMin(int left, int right) {
            return internalGetMin(0, 0, n - 1, left, right);
        }

        private int internalGetMin(int root, int rl, int rr, int left, int right) {
            if (left > right) return INF;
            if (rl == left && rr == right)
                return min[root];
            int rm = (rl + rr) / 2;
            return delta[root] + Math.min(
                    internalGetMin(root * 2 + 1, rl, rm, left, Math.min(right, rm)),
                    internalGetMin(root * 2 + 2, rm + 1, rr, Math.max(rm + 1, left), right)
            );
        }
    }

    private int compressCoordinates(long[] a, long[] b) {
        long[] all = new long[a.length + b.length];
        System.arraycopy(a, 0, all, 0, a.length);
        System.arraycopy(b, 0, all, a.length, b.length);
        Arrays.sort(all);
        int cnt = 0;
        for (int i = 0; i < all.length; ++i) {
            if (i == 0 || all[i] > all[i - 1]) {
                ++cnt;
                all[cnt - 1] = all[i];
            }
        }
        for (int i = 0; i < a.length; ++i) {
            int left = -1;
            int right = cnt;
            while (true) {
                int middle = (left + right) / 2;
                if (middle == left || middle == right) throw new RuntimeException();
                if (all[middle] == a[i]) {
                    a[i] = middle;
                    break;
                } else if (all[middle] < a[i]) {
                    left = middle;
                } else {
                    right = middle;
                }
            }
        }
        for (int i = 0; i < b.length; ++i) {
            int left = -1;
            int right = cnt;
            while (true) {
                int middle = (left + right) / 2;
                if (middle == left || middle == right) throw new RuntimeException();
                if (all[middle] == b[i]) {
                    b[i] = middle;
                    break;
                } else if (all[middle] < b[i]) {
                    left = middle;
                } else {
                    right = middle;
                }
            }
        }
        return cnt;
    }
}

class InputReader {
    private BufferedReader reader;
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

