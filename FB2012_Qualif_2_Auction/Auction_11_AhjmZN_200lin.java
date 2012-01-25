import static java.lang.Math.min;
import static java.util.Arrays.deepToString;
import static java.util.Arrays.fill;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Auction_11_AhjmZN_200lin {
	Scanner sc = new Scanner(System.in);

	// This problem can be solve in O(M+K) by using deque, but I use segtree for simplicity.

	class Seg {
		int[] vs;
		int[] minID;
		int N;
		Seg(int[] a) {
			int n = a.length;
			N = Integer.highestOneBit(n) << 1;
			this.vs = a;
			minID = new int[N * 2];
			for (int i = 0; i < N; i++) {
				minID[N + i] = i;
			}
			for (int i = N - 1; i > 0; i--) {
				int l = minID[i * 2], r = minID[i * 2 + 1];
				if (r >= vs.length || vs[l] < vs[r]) {
					minID[i] = l;
				} else {
					minID[i] = r;
				}
			}
		}
		int min(int s, int t) {
			int id = -1;
			while (0 < s && s + (s & -s) <= t) {
				int i = minID[(N + s) / (s & -s)];
				if (id < 0 || vs[id] > vs[i]) {
					id = i;
				}
				s += s & -s;
			}
			while (s < t) {
				int i = minID[(N + t) / (t & -t) - 1];
				if (id < 0 || vs[id] > vs[i]) {
					id = i;
				}
				t -= t & -t;
			}
			return id;
		}
	}

	int[] gen(int a0, int A, int B, int M) {
		int[] id = new int[M + 1];
		fill(id, -1);
		int[] a = new int[M + 1];
		a[0] = a0;
		id[a0] = 0;
		for (int i = 1; ; i++) {
			a[i] = (int)(((long)A * a[i - 1] + B) % M) + 1;
			if (id[a[i]] >= 0) {
				a[i] = id[a[i]];
				int[] tmp = new int[i + 1];
				for (int j = 0; j < tmp.length; j++) {
					tmp[j] = a[j];
				}
				return tmp;
			}
			id[a[i]] = i;
		}
	}

	int gcd(int x, int y) {
		int t;
		while (y != 0) {
			t = x % y; x = y; y = t;
		}
		return x;
	}

	void solve() {
		long N = sc.nextLong();
		int P1 = sc.nextInt(), W1 = sc.nextInt(), M = sc.nextInt(), K = sc.nextInt();
		int A = sc.nextInt(), B = sc.nextInt(), C = sc.nextInt(), D = sc.nextInt();
		int[] P = gen(P1, A, B, M);
		int[] W = gen(W1, C, D, K);
		int p0 = P[P.length - 1], pn = P.length - 1 - p0, w0 = W[W.length - 1], wn = W.length - 1 - w0;
		if (p0 < w0) {
			int[] P2 = new int[w0 + pn];
			for (int i = 0; i < P2.length; i++) {
				P2[i] = (i < p0 ? P[i] : P[p0 + (i - p0) % pn]);
			}
			P = P2;
			p0 = w0;
		} else if (w0 < p0) {
			int[] W2 = new int[p0 + wn];
			for (int i = 0; i < W2.length; i++) {
				W2[i] = (i < w0 ? W[i] : W[w0 + (i - w0) % wn]);
			}
			W = W2;
			w0 = p0;
		}
		int g = gcd(pn, wn);
		long L = (long)pn * wn / g;
		debug(wn, pn);
		debug(wn / g);
		long[] res = new long[2];
		for (int it = 0; it < 2; it++) {
			int[] min = new int[M + 1];
			fill(min, K + 1);
			long[] minN = new long[M + 1];
			for (int i = 0; i < p0 && i < N; i++) {
				int p = P[i], w = W[i];
				if (min[p] > w) {
					min[p] = w;
					minN[p] = 1;
				} else if (min[p] == w) {
					minN[p]++;
				}
			}
			for (int d = 0; d < g; d++) {
				int[] ws = new int[wn / g];
				for (int i = 0, j = d; i < ws.length; i++, j = (j + pn) % wn) {
					ws[i] = W[w0 + j];
				}
				int e = 0;
				while (W[w0 + (d + g) % wn] != ws[e]) {
					e++;
				}
				Seg seg = new Seg(ws);
				for (int i = d, j = 0; i < pn; i += g, j = (j + e) % ws.length) {
					if (p0 + i < N) {
						int p = P[p0 + i];
						int m = (int)min(ws.length, (N - (p0 + i) + pn - 1) / pn);
						int k = seg.min(j, min(ws.length, j + m));
						if (j + m > ws.length) {
							int k2 = seg.min(0, j + m - ws.length);
							if (ws[k] > ws[k2]) {
								k = k2;
							}
						}
						int w = ws[k];
						long a = p0 + i + (k - j + ws.length) % ws.length * (long)pn;
						long n = (N - a + L - 1) / L;
						if (min[p] > w) {
							min[p] = w;
							minN[p] = n;
						} else if (min[p] == w) {
							minN[p] += n;
						}
					}
				}
			}
			int[] minL = new int[M + 1];
			minL[0] = K + 1;
			for (int i = 1; i <= M; i++) {
				minL[i] = minL[i - 1];
				if (minN[i] > 0 && minL[i] > min[i]) {
					minL[i] = min[i];
				}
			}
			for (int i = 1; i <= M; i++) {
				if (minN[i] > 0) {
					if (min[i] < minL[i - 1]) {
						res[it] += minN[i];
					}
				}
			}
			for (int i = 0; i < p0 + pn; i++) {
				P[i] = M - P[i] + 1;
			}
			for (int i = 0; i < w0 + wn; i++) {
				W[i] = K - W[i] + 1;
			}
		}
		System.out.printf("%d %d%n", res[1], res[0]);
	}

	void run() {
		int caseN = sc.nextInt();
		for (int caseID = 1; caseID <= caseN; caseID++) {
			System.out.printf("Case #%d: ", caseID);
			solve();
		}
	}

	void debug(Object...os) {
		System.err.println(deepToString(os));
	}

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream("auction.in"));
		System.setOut(new PrintStream(new FileOutputStream("auction11.out")));


		new Auction_11_AhjmZN_200lin().run();
	}
}
