import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

// Facebook Hacker Cup 2015, qualif A: https://www.facebook.com/hackercup/problems.php?pid=582062045257424&round=742632349177460

public class A_CookingBooks {

	final static String FILENAME = "cooking";

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();
			for (int test = 1; test <= tests; test++) {
				long num = in.nextLong();
				long min = num, max = num;
				char[] ch = Long.toString(num).toCharArray();

				for (int i = 1; i < Long.toString(num).length(); i++) {
					for (int j = 0; j < i; j++) {
						if (ch[i] == ch[j])
							continue;
						char[] temp = Long.toString(num).toCharArray();
						temp[i] = ch[j];
						temp[j] = ch[i];
						if (temp[0] == '0')
							continue;
						long newNum = Long.parseLong(new String(temp));

						min = Math.min(min, newNum);
						max = Math.max(max, newNum);
					}
				}

				System.out.format("Case #%d: %d %d%n", test, min, max);
			}
		}

	}

}
