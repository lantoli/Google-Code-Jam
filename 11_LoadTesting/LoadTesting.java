package lantoli.codejam;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Solved after looking at the contest analysis
 */
public class LoadTesting {

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
			int least = sc.nextInt();
			int participant = sc.nextInt();
			int cfactor = sc.nextInt();
			final int res = doit(least, participant, cfactor);
			System.out.println(String.format("test: %d, least: %d, particpants: %d, cfactor: %d, res: %d", test, least, participant, cfactor, res));
			writer.println(String.format("Case #%d: %d", test, res));
			writer.flush();
		}		
		writer.close();
	}

	private static int doit(long least, long participant, long cfactor) {
		int n = 0;
		while (least * intpower(cfactor, intpower(2, n)) < participant) {
			n++;
		}
		return n;
	}

	private static long intpower(long number, long exp) {
		long ret = 1;
		for (int i=0; i<exp; i++) {
			ret *= number;
		}
		return ret;
	}

	
}
