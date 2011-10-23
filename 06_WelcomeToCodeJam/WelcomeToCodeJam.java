package lantoli.codejam;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This implementation is based on solution by contestant liszt1990
 */
public class WelcomeToCodeJam {

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
			writer.println("Case #" + test + ": " + String.format("%04d",welcome(sc.nextLine())));
		}		
		writer.close();
	}

	
	final static String welcome = "welcome to code jam";
	final static int welcomeLen = welcome.length();
	
	private static long welcome(String sentence) {
		int sentenceLen = sentence.length();
		long[] table = new long[welcomeLen];
		for (int i=0; i<sentenceLen; i++) {
			for (int j=0; j<welcomeLen; j++) {
				if (sentence.charAt(i) == welcome.charAt(j)) {
					if (j>0) {
						table[j] += table[j-1];
					} else {
						table[j]++;
					}
					table[j] %= 10000;
				}
			}
		}
		return table[welcomeLen-1];
	}
	
}
