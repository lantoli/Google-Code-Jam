import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class Ivan21 implements Runnable {


	int[] a, rk;
	int[] count;
	int[][] res;

	int parent(int x) {
		if (x == a[x]) {
			return x;
		}
		a[x] = parent(a[x]);
		return a[x];
	}

	public void solve() throws Exception {
		int ntest = iread();
		for (int test = 1; test <= ntest; test++) {
			int N = iread(), D = iread();

			int[] u = new int[N], v = new int[N];
			a = new int[N];
			count = new int[N];
			rk = new int[N];
			for (int i = 0; i +1< N; i++) {
				u[i] = iread();
				v[i] = iread();
				u[i]--;
				v[i]--;

			}
			for (int i=0; i<N; i++)
			{
				a[i] = i;
				count[i] = 1;
			}

			final int inf = N + 1;

			res = new int[N][D + 1];
			for (int i = 0; i < N; i++) {
				Arrays.fill(res[i], inf);
				res[i][0] = 1;
			}

			for (int i = 0; i+1 < N; i++) {
				int x = parent(u[i]), y = parent(v[i]);
				int[] resX = res[x];
				int[] resY = res[y];
				int par = -1;
				if (rk[x] > rk[y]) {
					a[y] = x;
					par = x;
				} else if (rk[y] > rk[x]) {
					a[x] = y;
					par = y;
				} else {
					a[x] = y;
					par = y;
					rk[y]++;
				}
				int min = inf;
				int countX = count[x];
				int countY = count[y];

				count[par] = countX + countY;
				for (int j = Math.min(count[par] - 1, D); j >= 1; j--) {
					res[par][j] = inf;
					int t;

					if (j>1 || countX == 1)
					{
						t = Math.max(resX[j - 1], resY[0]+1);
						if (res[par][j] > t) {
							res[par][j] = t;
						}
					}

					if (j>1 || countY == 1)
					{
						t = Math.max(resY[j - 1], resX[0]+1);
						if (res[par][j] > t) {
							res[par][j] = t;
						}
					}

					if (min > res[par][j]) {
						min = res[par][j];
					}
				}
				res[par][0] = min;
			}

			int ans = res[parent(0)][0];
			out.write("Case #" + test + ": " + ans + "\n");

			System.out.println("test "+test+" passed");
		}
	}

	public void run() {
		try {
			//			in = new BufferedReader(new InputStreamReader(System.in));
			//			out = new BufferedWriter(new OutputStreamWriter(System.out));
			in = new BufferedReader(new FileReader("monopoly.in"));
			out = new BufferedWriter(new FileWriter("monopoly_21.out"));
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