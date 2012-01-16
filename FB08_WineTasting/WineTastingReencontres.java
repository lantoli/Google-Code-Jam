
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;

/*
 * Using http://en.wikipedia.org/wiki/Rencontres_numbers
 */
public class WineTastingReencontres {

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
				res = res.add(reencontres(glasses,i));
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

	final static BigInteger[] reencontres = new BigInteger[MAX_N + 1];

	static{
		reencontres[0] = BigInteger.ONE;
		reencontres[1] = BigInteger.ZERO;
		for (int i=2; i<reencontres.length; i++) {
			reencontres[i] = (BigInteger.valueOf(i-1).multiply(reencontres[i-1].add(reencontres[i-2])));
		}
	}

	static BigInteger reencontres(int n, int m) {
		return reencontres[n-m].multiply(fact[n]).divide(fact[m]).divide(fact[n-m]);
	}

}
