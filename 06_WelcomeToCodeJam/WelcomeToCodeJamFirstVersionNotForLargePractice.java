package lantoli.codejam;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This was my first attempt to solve this problem. It works fine for small practice but
 * it's very inefficient for the large practice.
 *
 */
public class WelcomeToCodeJamFirstVersionNotForLargePractice {

	public static void main(String[] args) throws Exception {

		//String inFile = "sample.in";
		//String outFile = "sample.out";
		//String inFile = "C-small-practice.in";
		//String outFile = "C-small-practice.out";
		String inFile = "C-large-practice.in";
		String outFile = "C-large-practice.out";
		
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		
		int tests = sc.nextInt();
		sc.nextLine();
		System.out.println("tests: " + tests);
		for (int test=1; test<=tests; test++) {
			System.out.println("test: " + test);
			writer.println("Case #" + test + ": " + welcome(sc.nextLine()));
		}		
		writer.close();
	}

	final static String welcome = "welcome to code jam";
	
	
	private static String welcome(String sentence) {
		int len = welcome.length();
		
		int[] index = new int[len];
		Arrays.fill(index, -1);
		
		long count = 0;
		int i = 0;
		while (true) {
			int lastIndex = (i==0) ? 0 : index[i-1];
			if (index[i] != -1) {
				lastIndex = index[i] + 1 ;
			}
			int nextIndex = sentence.indexOf(welcome.charAt(i), lastIndex); 
			if (nextIndex != -1) {
				index[i] = nextIndex;
				if (i<len-1) {
					i++;
				} else {
					count++;
				}
			} else {
				index[i] = -1;
				i--;
				if (i<0) {
					break;
				}
			}
		}
		return numberToString4digits(count);
	}

	private static String numberToString4digits(long matches) {
		String str = Long.toString(matches);
		if (str.length() > 4) {
			return str.substring(str.length()-4);
		} 
		while (str.length() < 4) {
			str = "0" + str;
		}
		return str;
	}

}
