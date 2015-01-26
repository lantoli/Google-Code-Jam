import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

// Facebook Hacker Cup 2015, round 1 A: https://www.facebook.com/hackercup/problems.php?pid=582396081891255&round=344496159068801

public class A_Homework {

	final static String FILENAME = "homework";

	private final static int MAX = 10000000;

	private final static int MAX_PRIMES = 664579;

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		int primeNums[] = new int[MAX_PRIMES];
		int maxPrimes = 0;
		outer: for (int i = 2; i < MAX; i++) {
			int sqrt = (int) Math.sqrt(i);
			for (int j = 0; j < maxPrimes; j++) {
				if (primeNums[j] > sqrt)
					break;
				if (i % primeNums[j] == 0)
					continue outer;
			}
			primeNums[maxPrimes++] = i;
		}

		int primacity[] = new int[MAX + 1];
		for (int i = 0; i < maxPrimes; i++) {
			int prime = primeNums[i];
			for (int j = prime; j <= MAX; j += prime) {
				primacity[j]++;
			}
		}

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();
			for (int test = 1; test <= tests; test++) {

				int A = in.nextInt();
				int B = in.nextInt();
				int K = in.nextInt();

				int res = 0;
				for (int i = A; i <= B; i++)
					if (primacity[i] == K)
						res++;

				System.out.format("Case #%d: %d%n", test, res);
			}
		}

	}
}
