import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;

public class T02_DidYouMean {

	// Guava library must be added to classpath, current ver. 14.0.1
	// VM arguments for hard problems: -ea -Xms4096M -Xmx4096M -Xss1024m

	// Set FILENAME = null for standard input/output
	final static String FILENAME = null;
	// final static String FILENAME = "sample_dym";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	public static void main(String[] args) throws Exception {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}

		try (Scanner in = new Scanner(System.in)) {
			in.nextLine(); // comment: Dictionary file
			String dictName = in.nextLine();
			in.nextLine(); // comment: Suggestion numbers
			int wordCount = in.nextInt();
			in.nextLine();
			in.nextLine(); // comment: Find the suggestions
			Set<String> findWords = new HashSet<>(); // Canonical words to find
			BitSet findLen = new BitSet(); // Word lengths to find
			String[] words = new String[wordCount];
			for (int i = 0; i < wordCount; i++) {
				words[i] = in.nextLine();
				findLen.set(words[i].length());
				findWords.add(canonical(words[i]));
			}

			ArrayListMultimap<String, String> data = ArrayListMultimap.create();
			Charset charset = Charset.forName("US-ASCII");
			try (BufferedReader reader = Files.newBufferedReader(
					Paths.get(dictName), charset)) {
				String word = null;

				// THIS IS THE IMPORTANT LOOP TO BE OPTIMISED
				while ((word = reader.readLine()) != null) {
					if (findLen.get(word.length())) {
						String canonical = canonical(word);
						if (findWords.contains(canonical)) {
							data.put(canonical, word);
						}
					}
				}
			}

			for (String w : words) {
				System.out.print(w + " ->");
				for (String suggestion : data.get(canonical(w))) {
					if (!w.equals(suggestion)) {
						System.out.print(" " + suggestion);
					}
				}
				System.out.println();
			}
		}
	}

	private static String canonical(String text) {
		char[] ch = text.toCharArray();
		Arrays.sort(ch);
		return new String(ch);
	}
}