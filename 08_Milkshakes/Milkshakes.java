package lantoli.codejam;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This implementation is based on solution by contestant Bohua
 */
public class Milkshakes {

	public static void main(String[] args) throws Exception {
		
		//String inFile = "sample.in";
		//String outFile = "sample.out";
		//String inFile = "B-small-practice.in";
		//String outFile = "B-small-practice.out";
		String inFile = "B-large-practice.in";
		String outFile = "B-large-practice.out";
				
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		
		int numTest = sc.nextInt();
		for (int test=1; test<=numTest; test++) {
			
			int numFlavor = sc.nextInt(); 
			int numCustomer = sc.nextInt();

			boolean[][] canUse = new boolean[numCustomer][numFlavor];
			int[] malted = new int[numCustomer];
			int[] numCanUse = new int[numCustomer];

			for (int customer=0; customer<numCustomer; customer++) {
				malted[customer] = -1;
				for (int flavor=0; flavor<numFlavor; flavor++) {
					canUse[customer][flavor] = false;
				}
				
				int numFlavorCustomer = sc.nextInt();
				for (int f=0; f<numFlavorCustomer; f++) {
					int flavor = sc.nextInt() - 1;
					if (sc.nextInt() == 1) {
						malted[customer] = flavor;
					} else {
						canUse[customer][flavor] = true;
						numCanUse[customer]++;
					}
				}
			}
			writer.println("Case #" + test + ":" + milkshake(malted, canUse, numCanUse));
		}		
		writer.close();
	}

	private static String milkshake(int[] malted, boolean[][] canUse, int[] numCanUse) {
		int numCustomer = canUse.length;
		int numFlavor = canUse[0].length;
		boolean[] choice = new boolean[numFlavor] ;
		boolean[] visited = new boolean[numCustomer];
		boolean possible = true;
		boolean found;
	
		do {
			found = false;
			for (int j = 0; j < numCustomer; j++) {
				if (numCanUse[j] == 0 && !visited[j]) {
					visited[j] = true;
					found = true;
					if (malted[j] == -1) {
						possible = false;
						break;
					} else {
						choice[malted[j]] = true;
						for (int k = 0; k < numCustomer; k++) {
							if (canUse[k][malted[j]]) {
								numCanUse[k]--;
								canUse[k][malted[j]] = false;
							}
						}
					}
				}
			}
		} while (found && possible);
		
		if (possible) {
			return arrayToString(choice);
		} else {
			return " IMPOSSIBLE";			
		}
	}

	
	private static String arrayToString(boolean[] res) {
		StringBuilder str = new StringBuilder();
		for (boolean r: res) {
			str.append(' ');
			str.append(r ? '1' : '0'); 
		}
		return str.toString();
	}

}
