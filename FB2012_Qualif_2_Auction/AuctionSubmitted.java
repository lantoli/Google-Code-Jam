import static java.lang.Math.min;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * This is the code I submitted. I think it's correct but inefficient for large inputs.
 */
public class AuctionSubmitted {

	public static void main(String[] args) throws FileNotFoundException {

		System.setIn(new FileInputStream("auction.in"));
		System.setOut(new PrintStream(new FileOutputStream("auction.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		System.err.println("tests: " + tests);
		for (long test=1; test<= tests; test++) {
			System.err.println("doing test: " + test);

			long n = in.nextLong();
			int p1 = in.nextInt(), w1 = in.nextInt();
			int m = in.nextInt(), k = in.nextInt();
			long a = in.nextLong(), b = in.nextLong(), c = in.nextLong(), d = in.nextLong();
			in.nextLine();

			p[0] = p1; w[0] = w1;
			int plen = 1, wlen = 1;
			for (int i=1; i<p.length; i++) {
				if ( (p[i] = (int) ((a * p[i-1] + b) % m) + 1) == p1) {
					plen = i;
					break;
				}
			}
			for (int i=1; i<w.length; i++) {
				if ( (w[i] = (int) ((c * w[i-1] + d) % k) + 1) == w1) {
					wlen = i;
					break;
				}
			}
			BigInteger plenBig = BigInteger.valueOf(plen), wlenBig = BigInteger.valueOf(wlen);
			long lcm = plenBig.multiply(wlenBig).divide(plenBig.gcd(wlenBig)).longValue();

			long times = n / lcm;
			long extra = n % lcm;
			long bargainExtra = 0, terribleExtra = 0;
			long bargain = 0, terrible = 0;
			long limit = min(n,lcm);
			for (long i=0; i<limit; i++) {
				boolean isBargain = true;
				boolean isTerrible = true;
				int ipmod = (int) (i % plen);
				int iwmod = (int) (i % wlen);
				int jpmod=0, jwmod = 0;
				for (long j=0; j<limit; j++, jpmod++, jwmod++) {
					if (jpmod == plen) {
						jpmod = 0;
					}
					if (jwmod == wlen) {
						jwmod = 0;
					}
					int signP = Integer.signum(p[ipmod] - p[jpmod]);
					int signW = Integer.signum(w[jwmod] - w[iwmod]);
					if (signP != signW) {
						if (signP <= 0 && signW >= 0) {
							isTerrible = false;
							if (isBargain == false) {
								break;
							}
						} else {
							isBargain = false;
							if (isTerrible == false) {
								break;
							}
						}
					}
				}
				if (isBargain) {
					bargain++;
					if (i<extra) {
						bargainExtra++;
					}
				}
				if (isTerrible) {
					terrible++;
					if (i<extra) {
						terribleExtra++;
					}
				}

			}

			System.out.println(String.format("Case #%d: %d %d", test, terrible*times + terribleExtra, bargain*times + bargainExtra));
			System.out.flush();
		}
	}


	static int [] p = new int[10000002];
	static int [] w = new int[10000002];

}
