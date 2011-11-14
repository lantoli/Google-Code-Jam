package lantoli.codejam;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RopeIntranet {

	static class Wire {
		public int left, right;
		public Wire(int left, int right) {
			this.left = left;
			this.right = right;
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		//String inFile = "sample.in";
		//String outFile = "sample.out";
		//String inFile = "A-small-practice.in";
		//String outFile = "A-small-practice.out";
		String inFile = "A-large-practice.in";
		String outFile = "A-large-practice.out";
		
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		
		int numTest = sc.nextInt();
		sc.nextLine();
		System.out.println("tests: " + numTest);
		for (int test=1; test<=numTest; test++) {
			System.out.println("test: " + test);
			
			int numWire = sc.nextInt();
			Wire[] wire = new Wire[numWire];
			for (int i=0; i<numWire; i++) {
				wire[i] = new Wire(sc.nextInt(), sc.nextInt());
			}
					
			writer.println("Case #" + test + ": " + solve(wire));
		}		
		writer.close();
	}

	private static String solve(Wire[] wire) {
		int ret = 0;
		int numWire = wire.length;
		for (int i=0; i<numWire; i++) {
			for (int j=i; j<numWire; j++) {
				if (wire[i].left < wire[j].left != wire[i].right < wire[j].right) {
					ret++;
				}
			}
		}
		return Integer.toString(ret);
	}

}
