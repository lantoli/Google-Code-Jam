import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class T09_DefendersOfTheGalaxy {

	// Set FILENAME = null for standard input/output
	final static String FILENAME = null;
	// final static String FILENAME = "sample_galaxy";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	private static long W, H, S, C, G;

	public static void main(String[] args) throws Exception {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}

		try (Scanner in = new Scanner(System.in)) {

			int tests = in.nextInt();
			in.nextLine();
			for (int test = 0; test < tests; test++) {

				W = in.nextLong();
				H = in.nextLong();
				S = in.nextLong();
				C = in.nextLong();
				G = in.nextLong();
				in.nextLine();

				System.out.println(Long.toString(processGalaxy()));
			}
		}
	}

	private static long processGalaxy() {
		long maxSeconds = 0;

		long maxSoldiers = G / S;
		if (maxSoldiers >= W) // Have enough soldiers to keep Zorglings safe
			return -1;

		for (long soldiers = maxSoldiers; soldiers >= 0; soldiers--) {
			maxSeconds = Math.max(maxSeconds, processBattle(soldiers));
		}

		return maxSeconds;
	}

	private static long processBattle(long soldiers) {
		long crematoriums = (G - soldiers * S) / C;
		long zorglingsPerTurn = W - soldiers;
		long reachCityInSeconds = (W * (H - 1)) / zorglingsPerTurn + 1;
		return reachCityInSeconds * (crematoriums + 1);
	}
}