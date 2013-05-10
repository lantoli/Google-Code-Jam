import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.BitSet;
import java.util.Scanner;

public class T04_MissingNumbers {

	// VM arguments for hard problems: -ea -Xms4096M -Xmx4096M -Xss1024m

	// Set FILENAME = null for standard input/output
	// final static String FILENAME = null;
	final static String FILENAME = "sample_numbers";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static String FILENAME_INTEGERS = "integers";

	private static final int BUFFER_INTS = 1000;
	private static final int MAX_N = 100;

	public static void main(String[] args) throws Exception {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}

		BitSet bits = new BitSet(Integer.MAX_VALUE);
		try (FileInputStream is = new FileInputStream(FILENAME_INTEGERS); FileChannel fc = is.getChannel()) {
			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_INTS * 4);
			while (fc.read(buffer) != -1) {
				buffer.rewind();
				IntBuffer ints = buffer.asIntBuffer();
				while (ints.hasRemaining()) {
					bits.set(little2big(ints.get()));
				}
			}
		}

		int[] res = new int[MAX_N];
		int index = 0;
		for (int i = 0; i < res.length; i++) {
			int nextIndex = bits.nextClearBit(index);
			res[i] = nextIndex;
			index = nextIndex + 1;
		}

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			for (int i = 0; i < tests; i++) {
				int missing = in.nextInt();
				System.out.println(res[missing - 1]);
			}
		}

	}

	private static int little2big(int i) {
		return ((i & 0xff) << 24) + ((i & 0xff00) << 8) + ((i & 0xff0000) >> 8) + ((i >> 24) & 0xff);
	}

}