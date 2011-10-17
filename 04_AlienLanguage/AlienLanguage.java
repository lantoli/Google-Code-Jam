package lantoli.codejam.alienlanguage;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AlienLanguage {

	public static void main(String[] args) throws Exception {

		//String inFile = "sample.in";
		//String outFile = "sample.out";
		//String inFile = "A-small-practice.in";
		//String outFile = "A-small-practice.out";
		String inFile = "A-large-practice.in";
		String outFile = "A-large-practice.out";
		
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		

		int letters = sc.nextInt();
		int wordCount = sc.nextInt();
		int tests = sc.nextInt();
		sc.nextLine();

		System.out.println("tests: " + tests + ", letters: " + letters + ", words: " + wordCount);

		String[] words = new String[wordCount];
		for (int i=0; i<wordCount; i++) {
			words[i] = sc.nextLine();
		}
		Arrays.sort(words);
		
		for (int test=1; test<=tests; test++) {
			String pattern = sc.nextLine();
			pattern = pattern.replaceAll("\\(", "[").replaceAll("\\)", "]");
			Pattern p = Pattern.compile(pattern);
			int count = 0;
			for (int i=0; i<wordCount; i++) {
				if (p.matcher(words[i]).matches()) {
					count++;
				}
			}
			writer.println("Case #" + test + ": " + count);
		}
		
		writer.close();
	}
}
