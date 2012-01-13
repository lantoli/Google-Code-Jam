package lantoli.codejam;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;


public class WelcomeToCodeJamDP  {


	public static void main(String[] args) throws Exception {

		System.setIn(new FileInputStream("C-large-practice.in"));
		System.setOut(new PrintStream(new FileOutputStream("C-large-practice.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		for (long test=1; test<= tests; test++) {

			String str = in.nextLine();
			int strLen = str.length();

			long[][] dp = new long[strLen+1][welcomeLen+1];
			dp[0][0] = 1;
			for (int i=1; i <= strLen; i++) {
				dp[i][0] = dp[i-1][0];
				for (int j=1; j <= welcomeLen; j++){
					dp[i][j] = dp[i-1][j];
					if (str.charAt(i-1) == welcome.charAt(j-1)) {
						dp[i][j] += dp[i-1][j-1];
					}
					dp[i][j] %= MOD;
				}
			}

			System.out.println(String.format("Case #%d: %04d", test, dp[strLen][welcomeLen]));
		}
	}

	private final static long MOD = 10000;
	private final static String welcome = "welcome to code jam";
	private final static int welcomeLen = welcome.length();

}
