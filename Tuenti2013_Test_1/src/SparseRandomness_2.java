import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

public class SparseRandomness_2 {

	// VM arguments for hard problems: -Xms4096M -Xmx4096M -Xss1024m

	// Set FILENAME = null for standard input/output
	// final static String FILENAME = null;
	final static String FILENAME = "sample_randommal";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	private static final int MAX_SIZE = 1_000_002;

	private static int N, M;

	// Just one big array which is reused so we don't need to create many of them.
	private static int[] counts = new int[MAX_SIZE];
	private static int[] starts = new int[MAX_SIZE];
	private static int[] ends = new int[MAX_SIZE];

	public static void main(String[] args) throws Exception {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			// System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}

		try (Scanner in = new Scanner(System.in)) {

			int tests = in.nextInt();
			in.nextLine();
			for (int test = 1; test <= tests; test++) {

				System.out.println("Test case #" + test);

				N = in.nextInt();
				M = in.nextInt();

				int pos = 0;
				counts[0] = starts[0] = 1;
				int num = in.nextInt();
				int lastNumber = num;
				for (int i = 1; i < N; i++) {
					num = in.nextInt();
					if (num == lastNumber) {
						counts[pos]++;
					} else {
						lastNumber = num;
						ends[pos] = starts[pos] + counts[pos] - 1;
						pos++;
						counts[pos] = 1;
						starts[pos] = ends[pos - 1] + 1;
					}
				}
				ends[pos] = starts[pos] + counts[pos] - 1;
				int size = pos + 1;

				for (int i = 0; i < M; i++) {
					int start = in.nextInt();
					int end = in.nextInt();

					int max = 0;

					int posStart = Arrays.binarySearch(starts, 0, size, start);
					int posEnd = Arrays.binarySearch(ends, 0, size, end);

					int posStartInsert = -posStart - 2;
					int posEndInsert = -posEnd - 1;

					if (posStart < 0 && posEnd < 0 && posStartInsert == posEndInsert) {
						max = end - start + 1; // in the same chunk
					} else {

						if (posStart < 0) { // in the middle of a chunk in the beginning
							max = Math.max(max, ends[posStartInsert] - start + 1);
							posStart = posStartInsert + 1;
						}

						if (posEnd < 0) { // in the middle of a chunk at the end
							max = Math.max(max, end - starts[posEndInsert] + 1);
							posEnd = posEndInsert - 1;
						}

						if (posStart >= 0 && posEnd >= 0) {
							max = maxRepeats(max, posStart, posEnd);
						}
					}
					System.out.println(Integer.toString(max));
				}

			}
		}
	}

	private static int maxRepeats(int currentMax, int start, int end) {
		int max = currentMax;
		for (int i = start; i <= end; i++) {
			max = Math.max(max, counts[i]);
		}
		return max;
	}
}