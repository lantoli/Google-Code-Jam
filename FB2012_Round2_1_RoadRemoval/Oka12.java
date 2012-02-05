import static java.util.Arrays.deepToString;
import static java.util.Arrays.fill;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Oka12 {
	public static void main(String[]args){
		new Oka12().run();
	}
	private static void debug(Object...os){
		System.err.println(deepToString(os));
	}

	private void run() {
		File inFile = new File("road_removal.in");
		File outFile = new File("road_removal_12.out");
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

	private void solve(Scanner sc) {
		int N=sc.nextInt(),M=sc.nextInt(),K=sc.nextInt();
		int[]a=new int[M],b=new int[M];
		UnionFind uf = new UnionFind(N);
		int used=0;
		for (int i = 0; i < M; i++) {
			a[i]=sc.nextInt();b[i]=sc.nextInt();
			if(a[i]>=K && b[i]>=K){
				used++;
				uf.union(a[i], b[i]);
			}
		}
		for (int i = 0; i < M; i++) {
			if(a[i]<K || b[i]<K){
				if(!uf.find(a[i], b[i])){
					used++;
					uf.union(a[i], b[i]);
				}
			}
		}
		System.out.println(M-used);
	}

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
