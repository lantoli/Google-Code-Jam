import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class T10_CheckingMachine {

	// This is a good problem to use multi-threading as MD5 calculation is a fairly CPU-intensive task

	// VM arguments for hard problems: -Xms4096M -Xmx4096M -Xss1024m

	// Set FILENAME = null for standard input/output
	final static String FILENAME = null;
	// final static String FILENAME = "sample_checking2";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 5; // use 1 to solve them sequentially

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE
		// -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(String text) {
			this.text = text;
		}

		final private String text;

		@Override
		public String call() throws Exception {
			MessageDigest md = MessageDigest.getInstance("MD5");
			optimizeNodes(getNode(text)).updateMessageDiggest(md);
			return bytesToHex(md.digest());
		}

		private static Node getNode(String text) {

			if (!text.contains("["))
				return new TextNode(text);

			TreeNode tree = new TreeNode();

			int len = text.length();
			int pos = 0;
			while (pos < len) {

				// Search text node
				int posCurrent = pos;
				while (posCurrent < len && isNotSpecialChar(text.charAt(posCurrent))) {
					posCurrent++;
				}
				if (posCurrent - pos > 0) {
					tree.addNode(1, new TextNode(text.substring(pos, posCurrent)));
				}
				pos = posCurrent;

				// Search tree node
				long times = 0;
				while (posCurrent < len && isNumber(text.charAt(posCurrent))) {
					times *= 10;
					times += text.charAt(posCurrent) - '0';
					posCurrent++;
				}
				if (posCurrent - pos > 0 && times > 0) {
					assert text.charAt(posCurrent) == '[';

					pos = posCurrent;
					int balanced = 0;
					while (true) {
						char ch = text.charAt(posCurrent);
						if (ch == '[') {
							balanced++;
						} else {
							if (ch == ']') {
								balanced--;
								if (balanced == 0) {
									break;
								}
							}
						}
						posCurrent++;
					}
					String inside = text.substring(pos + 1, posCurrent);
					tree.addNode(times, getNode(inside));
					pos = posCurrent + 1; // remove trailing ]
				}

			}

			return tree;
		}

		private static boolean isNotSpecialChar(char ch) {
			return ch != '[' && ch != ']' && !isNumber(ch);
		}

		private static boolean isNumber(char ch) {
			return (ch >= '0' && ch <= '9');
		}

		static interface Node {
			void updateMessageDiggest(MessageDigest md);

			long byteCount();

			byte[] toBytes();
		}

		static class TextNode implements Node {

			final private byte[] chars;

			public TextNode(String text) {
				int len = text.length();
				chars = new byte[len];
				for (int i = 0; i < len; i++) {
					chars[i] = (byte) text.charAt(i);
				}
			}

			public TextNode(byte[] chars) {
				this.chars = chars;
			}

			@Override
			public void updateMessageDiggest(MessageDigest md) {
				md.update(chars);
			}

			@Override
			public long byteCount() {
				return chars.length;
			}

			@Override
			public byte[] toBytes() {
				return chars;
			}
		}

		static class TreeNode implements Node {

			private final List<Node> nodes = new ArrayList<>();
			private final List<Long> counts = new ArrayList<>();;

			public TreeNode() {
			}

			public TreeNode(long count, Node node) {
				addNode(count, node);
			}

			public void addNode(long count, Node node) {
				nodes.add(node);
				counts.add(count);
			}

			public int getChildLen() {
				return nodes.size();
			}

			public long getChildCount(int i) {
				return counts.get(i);
			}

			public Node getChildNode(int i) {
				return nodes.get(i);
			}

			public long getChildByteCount(int i) {
				return nodes.get(i).byteCount() * counts.get(i);
			}

			public byte[] getChildToBytes(int i) {
				byte[] nodeBytes = nodes.get(i).toBytes();
				int copyLen = nodeBytes.length;
				int count = (int) (long) counts.get(i);
				byte[] retBytes = new byte[copyLen * count];
				for (int n = 0; n < count; n++) {
					System.arraycopy(nodeBytes, 0, retBytes, n * copyLen, copyLen);
				}
				return retBytes;
			}

			@Override
			public void updateMessageDiggest(MessageDigest md) {
				int len = nodes.size();
				for (int i = 0; i < len; i++) {
					Node node = nodes.get(i);
					long count = counts.get(i);
					for (long j = 0; j < count; j++) {
						node.updateMessageDiggest(md);
					}
				}
			}

			@Override
			public long byteCount() {
				long ret = 0;
				int len = getChildLen();
				for (int i = 0; i < len; i++) {
					ret += getChildByteCount(i);
				}
				return ret;
			}

			@Override
			public byte[] toBytes() {
				int len = (int) byteCount();
				byte[] retBytes = new byte[len];
				int pos = 0;
				for (int i = 0; i < getChildLen(); i++) {
					System.arraycopy(getChildToBytes(i), 0, retBytes, pos, (int) getChildByteCount(i));
					pos += getChildByteCount(i);
				}
				return retBytes;
			}

			public void changeNode(int i, long newCount, Node newNode) {
				if (newCount != getChildCount(i) && newNode != getChildNode(i)) {
					nodes.set(i, newNode);
					counts.set(i, newCount);
				}

			}
		}

		final private static long OPTIMIZATION_THRESHOLD = 100_000;

		private static Node optimizeNodes(Node node) {
			if (node instanceof TreeNode) {
				TreeNode tree = (TreeNode) node;
				for (int i = 0; i < tree.getChildLen(); i++) {
					if (tree.getChildByteCount(i) <= OPTIMIZATION_THRESHOLD) {
						tree.changeNode(i, 1, new TextNode(tree.getChildToBytes(i)));
					} else {
						tree.changeNode(i, tree.getChildCount(i), optimizeNodes(tree.getChildNode(i)));
					}
				}
			}
			return node;
		}

		public static String bytesToHex(byte[] bytes) {
			final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			char[] hexChars = new char[bytes.length * 2];
			int v;
			for (int j = 0; j < bytes.length; j++) {
				v = bytes[j] & 0xFF;
				hexChars[j * 2] = hexArray[v >>> 4];
				hexChars[j * 2 + 1] = hexArray[v & 0x0F];
			}
			return new String(hexChars);
		}

		// PROBLEM SOLUTION ENDS HERE
		// -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

	}

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}
		Scanner in = new Scanner(System.in);
		List<Solver> solvers = new ArrayList<Solver>();
		int numTests = 0;
		while (in.hasNextLine()) {
			solvers.add(new Solver(in.nextLine()));
			numTests++;
		}
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		List<Future<String>> solutions = executor.invokeAll(solvers);
		for (int i = 0; i < numTests; i++) {
			try {
				System.out.println(solutions.get(i).get());
			} catch (Exception e) {
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!", i));
				e.printStackTrace(System.err);
			}
		}
		executor.shutdown();
	}
}