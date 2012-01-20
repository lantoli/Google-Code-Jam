import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Couldn't find official input file
 */
public class AfterTheDanceBattle {

	public static void main(String[] args) throws FileNotFoundException {

		System.setIn(new FileInputStream("sample.in"));
		System.setOut(new PrintStream(new FileOutputStream("sample.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		System.err.println("tests: " + tests);
		for (long test=1; test<= tests; test++) {

			int rows = in.nextInt();
			int cols = in.nextInt(); in.nextLine();
			StringBuilder str = new StringBuilder();
			for (int i=0; i<rows; i++) {
				str.append(in.nextLine());
			}
			String map = str.toString();
			assert  cols * rows == map.length();

			int[] cache = new int[rows*cols];
			Arrays.fill(cache, -1);
			LinkedList<Integer> list = new LinkedList<Integer>();
			for (int j=0; j<cols; j++) {
				if (map.charAt(j) == 'S') {
					cache[j] = 0;
					list.add(j);
					break;
				}
			}
			@SuppressWarnings("unchecked")
			LinkedList<Integer>[] numbers = new LinkedList[9];
			for (int i=0; i<9; i++) {
				numbers[i] = new LinkedList<Integer>();
			}
			for (int pos=0; pos<cols*rows; pos++) {
				char type = map.charAt(pos);
				if (type >= '1' && type <= '9') {
					numbers[type-'1'].add(pos);
				}
			}

			int res = -1;
			int[] diffRow = { 1, -1, 0, 0 };
			int[] diffCol = { 0, 0, 1, -1 };

			while (!list.isEmpty()) {
				int pos = list.removeFirst();
				char type = map.charAt(pos);
				if (type == 'E') {
					res = cache[pos];
					break;
				}
				for (int i=0; i<4; i++) {
					int row = pos / cols + diffRow[i];
					int col = pos % cols + diffCol[i];
					int dest = row*cols + col;
					if (row>=0 && row<=rows-1 && col>=0 && col<=cols-1 && cache[dest] == -1 && map.charAt(dest) != 'W') {
						cache[dest] = cache[pos] + 1;
						list.add(dest);
					}
				}
				if (type >= '1' && type <= '9') {
					while (!numbers[type-'1'].isEmpty()) {
						int dest = numbers[type-'1'].removeFirst();
						if (cache[dest] == -1) {
							cache[dest] = cache[pos] + 1;
							list.add(dest);
						}
					}
				}
			}
			System.out.println(String.format("Case #%d: %d", test, res));
		}

	}
}
