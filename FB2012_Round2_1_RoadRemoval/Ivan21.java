import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class Ivan21 implements Runnable {

	int K;

	public void solve() throws Exception {
		int ntest = iread();
		for (int test = 1; test <= ntest; test++) {
			int N = iread(), M = iread();
			K = iread();
			first = new Edge[N];
			int[] u = new int[M];
			int[] v = new int[M];

			for (int i = 0; i < M; i++) {
				int x = iread(), y = iread();
				u[i] = x;
				v[i] = y;

				new Edge(x, y);
				new Edge(y, x);
			}

			color = new int[N];

			Arrays.fill(color, -1);
			int S = 0;
			int L = 0;
			for (int i = 0; i < N; i++) {
				if (color[i] == -1) {
					dfs(i, i);
					S++;
				}
			}
			int P = 0;
			for (int i = K; i < N; i++) {
				if (color[i] < N) {
					dfs2(i, i + N);
					P++;
				}
			}
			for (int i=0; i<M; i++)
			{
				int x = u[i], y = v[i];
				if (x>=K && y>=K && color[x]==color[y]) {
					L++;
				}
			}

			L-=(N-K-P);

			int ans = M-L-(N-S);
			out.write("Case #" + test + ": " + ans + "\n");

			System.out.println("N="+N+" M="+M+" K="+K+" L="+L+" S="+S+" P="+P);
		}
	}

	int[] color, q;

	void dfs(int x, int clr) {
		if (color[x] == clr) {
			return;
		}
		color[x] = clr;
		for (Edge e = first[x]; e != null; e = e.next) {
			dfs(e.to, clr);
		}
	}

	void dfs2(int x, int clr) {
		if (color[x] == clr) {
			return;
		}
		color[x] = clr;
		for (Edge e = first[x]; e != null; e = e.next) {
			if (e.to >= K) {
				dfs2(e.to, clr);
			}
		}
	}

	Edge[] first;

	class Edge {
		int from, to;
		Edge next;

		public Edge(int x, int y) {
			from = x;
			to = y;
			add();
		}

		public void add() {
			next = first[from];
			first[from] = this;
		}
	}

	public void run() {
		try {
			//			in = new BufferedReader(new InputStreamReader(System.in));
			//			out = new BufferedWriter(new OutputStreamWriter(System.out));
			in = new BufferedReader(new FileReader("road_removal.in"));
			out = new BufferedWriter(new FileWriter("road_removal_21.out"));
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