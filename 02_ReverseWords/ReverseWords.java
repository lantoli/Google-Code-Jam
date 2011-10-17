package lantoli.codejam.reversewords;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class ReverseWords {

	public static void main(String[] args) throws Exception {

		//String inFile = "sample.in";
		//String outFile = "sample.out";
		//String inFile = "B-small-practice.in";
		//String outFile = "B-small-practice.out";
		String inFile = "B-large-practice.in";
		String outFile = "B-large-practice.out";
		
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		
		int tests = sc.nextInt();
		sc.nextLine();
		System.out.println("tests: " + tests);
		for (int test=1; test<=tests; test++) {
			writer.println("Case #" + test + ": " + reverse(sc.nextLine()));
		}		
		writer.close();
	}
	
	
	public static String reverse(String text) {
		String[] words = text.split(" ");
		StringBuilder builder = new  StringBuilder(text.length());
		for (int i=words.length-1; i>=0; i--) {
			builder.append(words[i]);
			builder.append(' ');
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
	
}
