package lantoli.codejam;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BribeThePrisionersDP  {


	public static void main(String[] args) throws Exception {

		System.setIn(new FileInputStream("C-large-practice.in"));
		System.setOut(new PrintStream(new FileOutputStream("C-large-practice.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		for (long test=1; test<= tests; test++) {

			int prisionerNum = in.nextInt();
			int releaseNum = in.nextInt();
			release = new int[releaseNum];
			cache = new HashMap<String, Long>();
			for (int i=0; i<releaseNum; i++) {
				release[i] = in.nextInt() - 1;
			}

			System.out.println(String.format("Case #%d: %d", test, dp(0,prisionerNum-1)));
		}
	}

	private static long dp(int start, int end) {
		String key = start + "#" + end;
		Long cachedRes = cache.get(key);
		if (cachedRes != null) {
			return cachedRes;
		}
		long res = 0;
		for (int pos : release) {
			if (pos >= start && pos <= end) {
				long candidate = end-start + dp(start, pos-1) + dp(pos+1, end);
				if (res == 0 || candidate < res) {
					res = candidate;
				}
			}
		}
		cache.put(key, res);
		return res;
	}

	static int[] release;
	static Map<String,Long> cache;
}
