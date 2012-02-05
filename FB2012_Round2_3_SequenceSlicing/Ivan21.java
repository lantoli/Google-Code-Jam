import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Ivan21 implements Runnable {

	long calc(int N, int R, int left, int right) {
		long count = (R - right) / N + 1;
		long ans = count * ((R - right) % N + 1) + (count * (count - 1) / 2) * N;
		return ans;
	}

	long doit(int N, int K, int R, int[] a) {
		if (K == 1) {
			return (R * R);
		}

		int MAX1 = 0;
		for (int i = 0; i < N; i++) {
			MAX1 = Math.max(MAX1, a[i]);
		}

		int[] fav = new int[3 * N];
		int[] S = new int[3 * N];
		int[] num = new int[N];
		Arrays.fill(num, -1);
		for (int i = 0; i < N; i++) {
			int k = a[i] % N;
			if (num[k] == -1 || a[num[k]] < a[i]) {
				num[k] = i;
			}
		}
		for (int i = 0; i < 3 * N; i++) {
			int j = i % N;
			fav[i] = (num[a[j] % N] == j) ? 1 : 0;
		}
		for (int i = 0; i + 1 < 3 * N; i++) {
			S[i + 1] = fav[i] + S[i];
		}

		int M = N * 3 + (MAX1 / N) * N;
		int[] b = new int[M];
		int MAX2 = 0;
		for (int i = 0; i < M; i++) {
			b[i] = a[i % N] + N * (i / N);
			MAX2 = Math.max(MAX2, b[i]);
		}

		final int inf = R + 2;
		boolean[] was = new boolean[MAX2 + 1];

		long ans = 0;
		for (int left = 0; left < N && left<R; left++) {
			Arrays.fill(was, false);

			int k = 0;
			int right = inf;
			for (int j = left; j < M && j < R; j++) {
				if (was[b[j]]) {
					continue;
				}
				was[b[j]] = true;
				k++;
				if (k == K) {
					right = j + 1;
					break;
				}
			}
			if (right == inf) {
				long pos = M;

				long per = S[N];
				long count = (K - k - 1) / per;
				pos += N * count;
				k += per * count;
				if (pos < R) {
					while (pos < R && k < K) {
						k += fav[(int) pos % N];
						pos++;
					}
					if (k == K) {
						right = (int) pos;
					}
				}
			}

			long t = 0;
			if (right!=inf) {
				t = calc(N, R, left, right);
			}

			ans += t;
		}
		ans *= 2;
		return ans;
	}

	long dummy(int N, int K, int R, int[] a) {
		if (K == 1) {
			return R * R;
		}
		int[] b = new int[R];
		int MAX1 = 0;
		for (int i = 0; i < R; i++) {
			b[i] = a[i % N] + N * (i / N);
			MAX1 = Math.max(MAX1, b[i]);
		}

		boolean[] was = new boolean[MAX1 + 1];
		int ans = 0;
		for (int x = 0; x < R; x++) {
			int k = 0;
			Arrays.fill(was, false);
			for (int i = x; i < R; i++) {
				if (was[b[i]]) {
					continue;
				}
				was[b[i]] = true;
				k++;
				if (k == K) {
					ans += R - i;
					break;
				}
			}
		}
		return ans * 2;
	}

	public void solve() throws Exception {
		int ntest = iread();
		for (int test = 1; test <= ntest; test++) {
			int N = iread(), K = iread(), R = iread();
			R++;
			int[] a = new int[N];
			for (int i = 0; i < N; i++) {
				a[i] = iread();
			}
			long ans = doit(N, K, R, a);

			BigInteger d = BigInteger.valueOf(ans);
			BigInteger n = BigInteger.valueOf(R).pow(2);
			BigInteger gcd = d.gcd(n);
			d = d.divide(gcd);
			n = n.divide(gcd);

			out.write("Case #" + test + ": " + d + "/" + n + "\n");

			System.out.println("test " + test + " passed");
		}
	}

	public void test() throws Exception {
		Random r = new Random(32167);
		for (int tst=0;;tst++) {
			int N = r.nextInt(6) + 3;
			int[] a = new int[N];
			for (int i = 0; i < N; i++) {
				a[i] = r.nextInt(20);
			}
			int R = r.nextInt(50)+10;
			int K = r.nextInt(R)+1;
			long ans1 = dummy(N, K, R, a);
			long ans2 = doit(N, K, R, a);
			if (ans1!=ans2)
			{
				doit(N, K, R, a);
				System.out.println(N+" "+K+" "+R+" fails");
				break;
			}
			System.out.println("test "+tst+" passed");
		}
	}

	public void run() {
		try {
			//			test();
			// in = new BufferedReader(new InputStreamReader(System.in));
			// out = new BufferedWriter(new OutputStreamWriter(System.out));
			in = new BufferedReader(new FileReader("sequence_slicing.in"));
			out = new BufferedWriter(new FileWriter("sequence_slicing_21.out"));
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
		new Thread(null, new Ivan21(), "1", 1 << 25).start();
	}
}