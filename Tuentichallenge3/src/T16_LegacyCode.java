import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Scanner;

public class T16_LegacyCode {

	// VM arguments for hard problems: -Xms4096M -Xmx4096M -Xss1024m

	// Set FILENAME = null for standard input/output
	final static String FILENAME = null;
	// final static String FILENAME = "sample_turing2";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	public static void main(String[] args) throws Exception {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}

		try (Scanner in = new Scanner(System.in)) {
			while (in.hasNextLine()) {
				String input = in.nextLine();
				System.out.println(processNumbers(input));
			}

		}
	}

	// Multiply first number times the sum of the rest of numbers
	private static String processNumbers(String line) {
		String[] strNumbers = line.substring(1).split("#");
		int lenOutput = strNumbers[0].length();
		int len = strNumbers.length;
		BigInteger[] numbers = new BigInteger[len];
		for (int i = 0; i < len; i++) {
			numbers[i] = new BigInteger(strNumbers[i], 2);
		}

		BigInteger ret = BigInteger.ZERO;
		for (int i = 1; i < len; i++) {
			ret = ret.add(numbers[i]);
		}
		ret = ret.multiply(numbers[0]);
		String result = ret.toString(2);
		while (result.length() < lenOutput) {
			result = "0" + result;
		}
		return "#" + result + "#";
	}

	// This was the implementation of Turing machine, hard-wired instead of state machine to make it faster
	// I'm leaving it just for fun, it's not used anymore

	private static BitSet bstape0, bstape1, bstapeHash, bstapeDollar, bstapePercen;

	private static void clear() {
		bstape0.clear(pos);
		bstape1.clear(pos);
		bstapeHash.clear(pos);
		bstapeDollar.clear(pos);
		bstapePercen.clear(pos);
	}

	private static void tape0() {
		clear();
		bstape0.set(pos);
	}

	private static void tape1() {
		clear();
		bstape1.set(pos);
	}

	private static void tapeHash() {
		clear();
		bstapeHash.set(pos);
	}

	private static void tapeDollar() {
		clear();
		bstapeDollar.set(pos);
	}

	private static void tapePercen() {
		clear();
		bstapePercen.set(pos);
	}

	private static boolean get01() {
		return bstape0.get(pos) || bstape1.get(pos);
	}

	private static boolean blank() {
		return !bstape0.get(pos) && !bstape1.get(pos) && !bstapeHash.get(pos) && !bstapeDollar.get(pos)
				&& !bstapePercen.get(pos);
	}

	private static int state, pos;

	private static String processTuringMachine(String input) {

		initMachine(input);

		// Start
		assert bstapeHash.get(0);
		pos = 0;
		tapePercen();
		pos = 1;
		state = 0;

		while (state != -1) {
			switch (state) {
			case 0:
				if (bstapeHash.get(pos)) {
					state = 1;
				} else {
					pos++;
				}
				break;
			case 1:
				if (bstape1.get(pos)) {
					tape0();
					pos++;
					state = 2;
				} else if (bstape0.get(pos)) {
					tape1();
					pos--;
				} else {
					pos--;
				}
				break;
			case 2:
				if (get01()) {
					pos++;
				} else {
					pos--;
					state = 3;
				}
				break;
			case 3:
				if (bstape0.get(pos)) {
					pos--;
				} else if (bstape1.get(pos)) {
					pos++;
					state = 4;
				} else {
					pos++;
					state = 5;
				}
				break;

			case 4:
				if (get01()) {
					pos++;
				} else {
					state = 7;
				}
				break;
			case 5:
				if (get01()) {
					pos++;
				} else {
					state = 6;
				}
				break;
			case 6:
				if (blank()) {
					state = 21;
					pos--;
				} else {
					if (bstapeDollar.get(pos)) {
						tapeHash();
					}
					pos++;
				}
				break;
			case 7:
				if (bstapeHash.get(pos)) {
					tapeDollar();
					state = 8;
				} else {
					state = 9;
				}
				pos++;
				break;

			case 8:
				if (blank()) {
					state = 10;
					pos--;
				} else {
					pos++;
				}
				break;
			case 9:
				if (bstapeDollar.get(pos)) {
					state = 15;
				} else {
					if (bstape1.get(pos)) {
						state = 12;
					} else if (bstape0.get(pos)) {
						state = 13;
					} else {
						state = 14;
					}
					clear();
					pos++;
				}
				break;
			case 10:
				tapeDollar();
				pos--;
				state = 11;
				break;
			case 11:
				if (bstapeDollar.get(pos)) {
					pos++;
					state = 9;
				} else {
					pos--;
				}
				break;
			case 12:
				if (blank()) {
					tape1();
					pos--;
					state = 16;
				} else {
					pos++;
				}
				break;
			case 13:
				if (blank()) {
					tape0();
					pos--;
					state = 17;
				} else {
					pos++;
				}
				break;
			case 14:
				if (blank()) {
					tapeHash();
					pos--;
					state = 18;
				} else {
					pos++;
				}
				break;
			case 15:
				if (blank()) {
					tapeHash();
					pos--;
					state = 19;
				} else {
					pos++;
				}
				break;
			case 16:
				if (blank()) {
					tape1();
					pos++;
					state = 9;
				} else {
					pos--;
				}
				break;
			case 17:
				if (blank()) {
					tape0();
					pos++;
					state = 9;
				} else {
					pos--;
				}
				break;
			case 18:
				if (blank()) {
					tapeHash();
					pos++;
					state = 9;
				} else {
					pos--;
				}
				break;
			case 19:
				if (bstapeDollar.get(pos)) {
					state = 20;
				}
				pos--;
				break;
			case 20:
				if (bstapeDollar.get(pos)) {
					state = 1;
				} else {
					pos--;
				}
				break;
			case 21:
				state = 22;
				pos--;
				break;
			case 22:
				if (bstapePercen.get(pos)) {
					tapeHash();
					state = -1;
				} else {
					if (bstapeHash.get(pos)) {
						state = 23;
					}
					pos--;
				}
				break;
			case 23:
				if (blank()) {
					state = 24;
					pos--;
				} else {
					pos++;
				}
				break;
			case 24:
				pos--;
				state = 25;
				break;
			case 25:
				if (bstape0.get(pos)) {
					pos--;
				} else {
					if (bstape1.get(pos)) {
						state = 26;
					} else {
						state = 27;
					}
					pos++;
				}
				break;
			case 26:
				if (bstapeHash.get(pos)) {
					pos--;
					state = 29;
				} else {
					pos++;
				}
				break;
			case 27:
				if (bstapeHash.get(pos)) {
					clear();
					pos--;
					state = 28;
				} else {
					pos++;
				}
				break;
			case 28:
				if (bstapeHash.get(pos)) {
					state = 21;
				} else {
					clear();
					pos--;
				}
				break;
			case 29:
				if (bstape1.get(pos)) {
					tape0();
					state = 30;
				} else {
					tape1();
				}
				pos--;
				break;
			case 30:
				if (bstapeHash.get(pos)) {
					state = 31;
				}
				pos--;
				break;

			case 31:
				if (bstape1.get(pos)) {
					tape0();
					pos--;
				} else {
					tape1();
					state = 32;
					pos++;
				}
				break;
			case 32:
				if (blank()) {
					pos--;
					state = 21;
					break;
				} else {
					pos++;
				}
				break;
			}
		}
		return cleanTape();

	}

	private static String cleanTape() {
		StringBuilder str = new StringBuilder();
		int max1 = Math.max(bstape0.length(), bstape1.length());
		int max2 = Math.max(bstapeHash.length(), bstapePercen.length());
		int max = Math.max(max1, max2);
		for (int i = 0; i < max; i++) {
			if (bstape0.get(i)) {
				str.append('0');
			} else if (bstape1.get(i)) {
				str.append('1');
			} else if (bstapeHash.get(i)) {
				str.append('#');
			} else if (bstapePercen.get(i)) {
				str.append('%');
			}
		}
		return str.toString();
	}

	private static void initMachine(String input) {
		bstape0 = new BitSet();
		bstape1 = new BitSet();
		bstapeHash = new BitSet();
		bstapeDollar = new BitSet();
		bstapePercen = new BitSet();
		for (int i = 0; i < input.length(); i++) {
			switch (input.charAt(i)) {
			case '0':
				bstape0.set(i);
				break;
			case '1':
				bstape1.set(i);
				break;
			case '#':
				bstapeHash.set(i);
				break;
			case '$':
				bstapeDollar.set(i);
				break;
			case '%':
				bstapePercen.set(i);
				break;

			}
		}
	}
}