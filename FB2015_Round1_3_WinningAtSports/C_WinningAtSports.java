import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

// Facebook Hacker Cup 2015, round 1 C: https://www.facebook.com/hackercup/problems.php?pid=688426044611322&round=344496159068801

public class C_WinningAtSports {

	final static String FILENAME = "sports";

	public final static long MOD = 1000000007;

	public final static int MAX_POINTS = 2000;

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		long[][] stressfree = new long[MAX_POINTS + 1][MAX_POINTS + 1];
		for (int me = 1; me <= MAX_POINTS; me++) {
			stressfree[0][me] = 1;
		}
		for (int him = 1; him <= MAX_POINTS; him++) {
			for (int me = him + 1; me <= MAX_POINTS; me++) {
				stressfree[him][me] = (stressfree[him - 1][me] + stressfree[him][me - 1]) % MOD;
			}
		}

		long[][] stressfull = new long[MAX_POINTS + 1][MAX_POINTS + 1];
		for (int him = 1; him <= MAX_POINTS; him++) {
			stressfull[him][0] = 1;
		}
		for (int him = 1; him <= MAX_POINTS; him++) {
			for (int me = 1; me <= him; me++) {
				stressfull[him][me] = (stressfull[him - 1][me] + stressfull[him][me - 1]) % MOD;
			}
		}

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();

			for (int test = 1; test <= tests; test++) {

				String[] line = in.nextLine().split("-");
				int a = Integer.parseInt(line[0]);
				int b = Integer.parseInt(line[1]);

				System.out.format("Case #%d: %d %d%n", test, Math.max(1, stressfree[b][a]),
						Math.max(1, stressfull[b][b]));
			}
		}

	}
}
