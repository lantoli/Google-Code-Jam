package lantoli.codejam;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MinimunScalarProduct {

	public static void main(String[] args) throws Exception {
		
		//String inFile = "sample.in";
		//String outFile = "sample.out";
		//String inFile = "A-small-practice.in";
		//String outFile = "A-small-practice.out";
		String inFile = "A-large-practice.in";
		String outFile = "A-large-practice.out";
		
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		
		int tests = sc.nextInt();
		sc.nextLine();
		System.out.println("tests: " + tests);
		for (int test=1; test<=tests; test++) {
			int size = sc.nextInt(); sc.nextLine();
			long v1[] = new long[size], v2[] = new long[size];
			for (int i=0; i<size; i++) {
				v1[i] = sc.nextInt();
			}
			sc.nextLine();
			for (int i=0; i<size; i++) {
				v2[i] = sc.nextInt();
			}
			sc.nextLine();
			System.out.println("test: " + test);
			writer.println("Case #" + test + ": " + minimunScalar(v1, v2));
		}		
		writer.close();
	}

	private static long minimunScalar(long[] v1, long[] v2) {
		int size = v1.length;
		long total = 0;
		Arrays.sort(v1);
		Arrays.sort(v2);
		for (int i=0; i<size; i++) {
			total += v1[i] * v2[size-i-1];
		}
		return total;
	}
	
}
