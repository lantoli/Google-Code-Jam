import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Pavel Mavrin
 */
public class Pavel15 {

	BufferedReader br;
	StringTokenizer st;
	PrintWriter out;
	private int nn;
	private int vv;
	private int[] p;

	public Pavel15() throws FileNotFoundException {
		br = new BufferedReader(new FileReader("road_removal.in"));
		out = new PrintWriter("road_removal_15.out");
	}

	String next() throws IOException {
		while (st == null || !st.hasMoreTokens()) {
			st = new StringTokenizer(br.readLine());
		}
		return st.nextToken();
	}

	int nextInt() throws IOException {
		return Integer.parseInt(next());
	}

	public static void main(String[] args) throws IOException {
		new Pavel15().solve();
	}

	private void solve() throws IOException {
		int n = nextInt();
		for (int i = 0; i < n; i++) {
			String s = solveCase();
			out.println("Case #" + (i + 1) + ": " + s);
			System.out.println("Case #" + (i + 1) + ": " + s);
		}
		out.close();
	}

	class Node {
		int id;
		List<Edge> e = new ArrayList<Edge>();
		public boolean z;
		boolean important;
	}

	class Edge {
		Node dst;
		Edge rev;
	}

	Node[] v;

	private String solveCase() throws IOException {
		int n = nextInt();
		int m = nextInt();
		int k = nextInt();
		v = new Node[n];
		for (int i = 0; i < n; i++) {
			v[i] = new Node();
			v[i].id = i;
			v[i].important = (i < k);
		}
		p = new int[n];
		for (int i = 0; i < n; i++) {
			p[i] = i;
		}
		for (int i = 0; i < m; i++) {
			int x = nextInt();
			int y = nextInt();
			addEdge(x, y);
			if (x >= k && y >= k) {
				union(x, y);
			}
		}

		int result = 0;
		for (int i = 0; i < n; i++) {
			if (!v[i].z) {
				nn = 0;
				vv = 0;
				dfs(v[i]);
				vv /= 2;
				result += (vv - nn + 1);
			}
		}
		return "" + result;
	}

	private int get(int i) {
		if (p[i] == i) {
			return i;
		}
		p[i] = get(p[i]);
		return p[i];
	}

	private void union(int x, int y) {
		x = get(x);
		y = get(y);
		p[x] = y;
	}

	private void dfs(Node node) {
		if (node.z) {
			return;
		}
		node.z = true;
		if (p[node.id] == node.id) {
			nn++;
		}
		for (Edge edge : node.e) {
			if (node.important || edge.dst.important) {
				vv++;
			}
			dfs(edge.dst);
		}
	}


	private void addEdge(int x, int y) {
		Edge e = new Edge();
		Edge e2 = new Edge();
		e.dst = v[y];
		e2.dst = v[x];
		v[x].e.add(e);
		v[y].e.add(e2);
		e.rev = e2;
		e2.rev = e;
	}
}
