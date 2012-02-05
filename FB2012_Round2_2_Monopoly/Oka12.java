import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.deepToString;
import static java.util.Arrays.fill;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Oka12 {
	public static void main(String[]args){
		new Oka12().run();
	}
	private static void debug(Object...os){
		System.err.println(deepToString(os));
	}
	private void run() {
		File inFile = new File("monopoly.in");
		File outFile = new File("monoply_12.out");
		try {
			System.setIn(new FileInputStream(inFile));
			System.setOut(new PrintStream(outFile));
		} catch (IOException e) {
		}
		Scanner sc = new Scanner(System.in);
		preCalc();
		int testCaseCount = sc.nextInt();
		for(int testCase=1;testCase<=testCaseCount;testCase++){
			System.out.printf("Case #%d: ",testCase);
			solve(sc);
		}
	}

	private void preCalc() {

	}
	int INF = 1<<28;
	private void solve(Scanner sc) {
		int N=sc.nextInt(),D=sc.nextInt();
		UnionFind uf = new UnionFind(N);
		int[][] dp=new int[N][D+1];
		for (int i = 0; i < N; i++) {
			dp[i][0]=1;
			for (int j = 1; j < D+1; j++) {
				dp[i][j] = INF;
			}
		}
		for (int i = 0; i < N-1; i++) {
			int a=sc.nextInt()-1,b=sc.nextInt()-1;
			int ra = uf.root(a),rb = uf.root(b);
			if(ra==rb) {
				throw new RuntimeException();
			}
			uf.union(ra, rb);
			int r = uf.root(ra);
			int minA=INF,minB=INF;
			for (int j = 0; j < D+1; j++) {
				minA = min(minA,dp[ra][j]);
				minB = min(minB,dp[rb][j]);
			}
			for (int j = D; j > 0; j--) {
				dp[r][j]=min(max(dp[ra][j-1],minB+1),max(dp[rb][j-1],minA+1));
				if(dp[r][j]>INF) {
					dp[r][j]=INF;
				}
			}
			dp[r][0]=INF;
		}
		int res = INF;
		int r = uf.root(0);
		for (int i = 0; i < D+1; i++) {
			res = min(res,dp[r][i]);
		}
		System.out.println(res);
	}

	ArrayList<Integer>[] es;
	class UnionFind{
		final int[] tree;

		public UnionFind(int n){
			this.tree=new int[n];
			fill(tree,-1);
		}
		void union(int x,int y){
			x=root(x);
			y=root(y);
			if(x!=y){
				if(tree[x]>tree[y]){
					int t=x;x=y;y=t;
				}
				tree[x]+=tree[y];
				tree[y]=x;
			}
		}

		boolean find(int x,int y){
			return root(x)==root(y);
		}

		int root(int x){
			return tree[x]<0?x:(tree[x]=root(tree[x]));
		}

		int size(int x){
			return -tree[root(x)];
		}
	}
}
