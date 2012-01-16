
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;

/*
 * Using http://en.wikipedia.org/wiki/Derangement
 * Number of exactly having r hits y n-r fails in n glasses:
 * C(n,r) * subfact(n-r)
 * C(n,r) -> ways to separate in two sets
 * 1 -> only one way to have all the hits in the first set
 * subfact(n-r) -> ways to have all fails in the second set
 */
public class WineTastingSubfactorial {

	public static void main(String[] args) throws Exception {

		System.setIn(new FileInputStream("wine_tasting.in"));
		System.setOut(new PrintStream(new FileOutputStream("wine_tasting.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();

		for (long test=1; test<= tests; test++) {

			int glasses = in.nextInt();
			int drinks = in.nextInt();

			BigInteger res = BigInteger.ONE;
			for (int i=drinks; i<=glasses-2; i++) {
				res = res.add(comb(glasses,i).multiply(subfact[glasses-i]));
			}
			System.out.println(String.format("%d", res.mod(MOD).longValue()));
		}
	}

	final static BigInteger MOD = BigInteger.valueOf(1051962371);
	final static int MAX_N = 100;

	final static BigInteger[] fact = new BigInteger[MAX_N+1];
	static {
		fact[0] = BigInteger.ONE;
		for (int i=1; i<fact.length; i++) {
			fact[i] = fact[i-1].multiply(BigInteger.valueOf(i));
		}
	}

	// subfactorial is the number of all positions wrong
	final static BigInteger[] subfact = new BigInteger[MAX_N+1];
	static {
		subfact[0] = BigInteger.ONE;
		subfact[1] = BigInteger.ZERO;
		for (int i=2; i<fact.length; i++) {
			subfact[i] = BigInteger.valueOf(i-1).multiply(subfact[i-1].add(subfact[i-2]));
		}
	}

	static BigInteger comb(int n, int m) {
		return fact[n].divide(fact[m].multiply(fact[n-m]));
	}

}
