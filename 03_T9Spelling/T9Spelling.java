package lantoli.codejam.t9spelling;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class T9Spelling {

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
			writer.println("Case #" + test + ": " + spelling(sc.nextLine()));
		}		
		writer.close();
	}
	
	public static char CHAR_NUMBER[] = { '2','2','2', '3','3','3', '4','4','4', '5','5','5', '6','6','6', '7','7','7','7', '8','8','8', '9','9','9','9' };
	public static int CHAR_TIMES[] = { 1,2,3, 1,2,3, 1,2,3, 1,2,3, 1,2,3, 1,2,3,4, 1,2,3, 1,2,3,4 };
	
	public static String spelling(String message) {
		String oldKeys = "$";
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<message.length(); i++) {
			String newKeys = getkeys(message.charAt(i));
			if (newKeys.charAt(0) == oldKeys.charAt(0)) {
				builder.append(' ');
			}
			oldKeys = newKeys;
			builder.append(newKeys);
		}
		return builder.toString();
	}
	
	public static String getkeys(char letra) {
		if (letra == ' ') {
			return "0";
		} else {
			int pos = letra-'a';
			return createString(CHAR_TIMES[pos], CHAR_NUMBER[pos]);		
		}	
	}
	
	public static String createString(int length, char ch) {
	    char[] chars = new char[length];
	    Arrays.fill(chars, ch);
	    return new String(chars);
	}
}
