import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

//VM arguments: -ea -Xms4096M -Xmx8192M
// increase stack: -Xss515m

public class C_PriceCorrect {

	final static String FILENAME = "price";

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();
			for (int test = 1; test <= tests; test++) {
				int N = in.nextInt();
				int P = in.nextInt();
				in.nextLine();
				int B[] = new int[N];
				for (int i=0; i<N; i++) {
					B[i] = in.nextInt();
				}
				in.nextLine();
				long total = 0;
				
				for (int min=0; min<N; min++) {
					if (B[min] <= P) {
						int sum = B[min];
						int max = min;
						while (max+1 < N && sum + B[max+1] <= P) {
							max++;
							sum += B[max];
						}
						total += max - min + 1;
					}
				}
				
				
				System.out.format("Case #%d: %d%n", test, total);
				System.err.format("Case #%d: %d%n", test, total);
			}
		}

	}

}
