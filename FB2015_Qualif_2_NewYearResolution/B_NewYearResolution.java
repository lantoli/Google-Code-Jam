import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.BitSet;
import java.util.Scanner;

// Facebook Hacker Cup 2015, qualif B: https://www.facebook.com/hackercup/problems.php?pid=1036037553088752&round=742632349177460

// VM arguments: -Xms4096M -Xmx4096M

public class B_NewYearResolution {

	static String FILENAME = "year";

	final static int MAX = 2 << 20 + 1;

	final static int[] stack = new int[MAX];
	final static int[] stackA = new int[MAX];
	final static int[] stackB = new int[MAX];
	final static int[] stackC = new int[MAX];

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();
			for (int test = 1; test <= tests; test++) {

				int a = in.nextInt();
				int b = in.nextInt();
				int c = in.nextInt();
				int n = in.nextInt();
				int[] aa = new int[n];
				int[] bb = new int[n];
				int[] cc = new int[n];
				for (int i = 0; i < n; i++) {
					aa[i] = in.nextInt();
					bb[i] = in.nextInt();
					cc[i] = in.nextInt();
				}
				int index = 1;
				boolean res = false;

				BitSet visited = new BitSet(MAX);

				outer: while (index-- > 0) {
					int curIndex = index;
					int cur = stack[curIndex];
					int curA = stackA[curIndex];
					int curB = stackB[curIndex];
					int curC = stackC[curIndex];

					for (int i = 0; i < n; i++) {
						if ((cur & (1 << i)) == 0) {

							int next = stack[curIndex] | (1 << i);
							if (visited.get(next))
								continue;
							visited.set(next);

							if (curA + aa[i] <= a && curB + bb[i] <= b
									&& curC + cc[i] <= c) {

								if (curA + aa[i] == a && curB + bb[i] == b
										&& curC + cc[i] == c) {
									res = true;
									break outer;
								}

								index++;
								stack[index] = next;
								stackA[index] = curA + aa[i];
								stackB[index] = curB + bb[i];
								stackC[index] = curC + cc[i];
							}
						}
					}

				}

				System.out.format("Case #%d: %s%n", test, res ? "yes" : "no");
			}
		}

	}
}
