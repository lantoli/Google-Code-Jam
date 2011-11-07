package lantoli.codejam;

import java.awt.datatransfer.FlavorTable;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MilkshakesSmallPractice {

	public static void main(String[] args) throws Exception {
		
		//String inFile = "sample.in";
		//String outFile = "sample.out";
		String inFile = "B-small-practice.in";
		String outFile = "B-small-practice.out";
		//String inFile = "B-large-practice.in";
		//String outFile = "B-large-practice.out";
		
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		
		int tests = sc.nextInt();
		System.out.println("tests: " + tests);
		for (int test=1; test<=tests; test++) {
			
			int flavorCount = sc.nextInt(); 
			int customerCount = sc.nextInt();
			
			int unmalted[][] = new int[customerCount][];
			int malted[] = new int[customerCount];
			Arrays.fill(malted, -1);

			System.out.println("test: " + test + ", flavorCount: " + flavorCount + ", customerCount: " + customerCount);
			
			for (int customer=0; customer<customerCount; customer++) {
				int customerFlavorCount = sc.nextInt();
				int[] unmaltedTemp = new int[customerFlavorCount];
				int posUnmaltedTemp = 0;
				for (int i=0; i<customerFlavorCount; i++) {
					int flavor = sc.nextInt() - 1;
					if (sc.nextInt() == 1) {
						malted[customer] = flavor;
					} else {
						unmaltedTemp[posUnmaltedTemp++] = flavor;
					}
				}
				unmalted[customer] = Arrays.copyOf(unmaltedTemp, posUnmaltedTemp);
			}
			writer.println("Case #" + test + ": " + milkshake(unmalted, malted, flavorCount));
		}		
		writer.close();
	}

	private static String milkshake(int[][] unmalted, int[] malted, int flavorCount) {
		int customerCount = unmalted.length;
		
		boolean[] res = new boolean[flavorCount];
				
		do  {
			if (isGood(res, unmalted, malted)) {
				return arrayToString(res);
			}
		} while (nextRes(res));
		return "IMPOSSIBLE";
	}

	private static boolean nextRes(boolean[] res) {
		int flavorCount = res.length;
		int pos = flavorCount;
		do {
			if (--pos<0) {
				return false;
			}
			res[pos] = ! res[pos];
		} while (!res[pos] && pos>=0);
		return true;
	}

	private static boolean isGood(boolean[] res, int[][] unmalted, int[] malted) {
		int customerCount = unmalted.length;
		outerloop:
		for (int i=0; i<customerCount; i++) {
			if (malted[i] >= 0 && res[malted[i]]) {
				continue;
			}
			for (int j=0; j<unmalted[i].length; j++) {		
				if (!res[unmalted[i][j]]) {
					continue outerloop;
				}
			}
			return false;
		}
		return true;
	}
	
	private static String arrayToString(boolean[] res) {
		StringBuilder str = new StringBuilder();
		for (boolean r: res) {
			str.append(r ? '1' : '0'); 
			str.append(' ');
		}
		return str.toString();
	}



}
