import static java.lang.Math.min;
import static java.util.Arrays.deepToString;
import static java.util.Arrays.fill;
import static java.util.Arrays.sort;

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
		File inFile = new File("sequence_slicing.in");
		File outFile = new File("sequence_slicing_12.out");
		try {
			System.setIn(new FileInputStream(inFile));
			System.setOut(new PrintStream(outFile));
		} catch (IOException e) {
		}
		Scanner sc = new Scanner(System.in);
		int testCaseCount = sc.nextInt();
		for(int testCase=1;testCase<=testCaseCount;testCase++){
			System.out.printf("Case #%d: ",testCase);
			String res = solve(sc);
			System.out.println(res);
		}
	}

	int N;
	long K,R;
	long[] ls;
	private String solve(Scanner sc) {
		N=sc.nextInt();K=sc.nextLong();R=sc.nextLong();
		ls=new long[N];
		for (int i = 0; i < N; i++) {
			ls[i]=sc.nextLong();
		}
		return solve(N, K, R, ls);
	}
	private String solve(int N,long K,long R,long[] ls){
		this.N=N;this.K=K;this.R=R;this.ls=ls;
		if(K==1){
			return "1/1";
		}
		long num=0,den=(R+1)*(R+1);
		for (int left = 0; left < N && left<=R; left++) {
			//debug(left);
			long lb=left-1,ub=R+1;
			do{
				long md=(lb+ub)/2;
				long cnt = count(left,md);
				if(cnt>=K) {
					ub=md;
				} else {
					lb=md;
				}
			}while(ub-lb>1);
			if(ub==R+1) {
				continue;
			}
			long to = R-ub+1;
			long from = to%N;
			num += sum(from,to,N);
		}
		num*=2;
		long gcd=gcd(num,den);
		num/=gcd;
		den/=gcd;
		return String.format("%d/%d", num,den);
	}
	private long sum(long from, long to, long add) {
		long num = (to-from+add)/add;
		return (from+to)*num/2;
	}

	long gcd(long x,long y){
		if(y==0) {
			return x;
		}
		return gcd(y,x%y);
	}

	private long count(long left, long right) {
		if(left>right) {
			return 0;
		}
		I[] is=new I[N];
		for (int i = 0; i < N; i++) {
			long l = left/N + (i < left%N ? 1 : 0);
			long r = right/N - (right%N < i ? 1 : 0);
			if(l<=r){
				is[i] = new I(ls[i] + l*N, ls[i] + r*N);
			}else{
				is[i] = new I(-1, -1);
			}
		}
		sort(is);
		long res = 0;
		long[] mx=new long[N];
		fill(mx,Integer.MIN_VALUE);
		for (int i = 0; i < N; i++) {
			if(is[i].left==-1) {
				continue;
			}
			int md = (int)(is[i].left%N);
			long cmx = mx[md];
			if(cmx<is[i].right){
				res += min((is[i].right-is[i].left+N)/N , (is[i].right-cmx)/N);
				mx[md]=is[i].right;
			}
		}
		return res;
	}
	class I implements Comparable<I>{
		long left,right;// [left,right]

		public I(long left, long right) {
			this.left = left;
			this.right = right;
		}
		public int compareTo(I o) {
			return Long.valueOf(left).compareTo(o.left);
		}
	}
}
