import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/*
 * Could not get large input and output file to test it
 */
public class PegGame {
	
	public static void main(String[] args) throws FileNotFoundException {

	 	System.setIn(new FileInputStream("sample2.in"));
	 	System.setOut(new PrintStream(new FileOutputStream("sample2.out")));

	    Scanner in = new Scanner(System.in);
	    int tests = in.nextInt(); in.nextLine();
	    for (long test=1; test<= tests; test++) {    	
	    	
	    	int rows = in.nextInt();
	    	int cols = in.nextInt();
	    	int colsTotal = cols*2 - 1;
	    	int colTarget = in.nextInt() * 2 + 1;
	    	
	    	
	    	boolean[][] peg = new boolean[rows+1][colsTotal];
	    	for (int i=0; i<rows; i++) {
	    		for (int j=i%2; j<colsTotal; j+=2) {
	    			peg[i][j] = true;
	    		}
	    	}
	    	int missingPegs = in.nextInt();
	    	for (int i=0; i<missingPegs; i++) {
	    		int r = in.nextInt();
	    		int c = in.nextInt();
	    		peg[r][c*2 + r%2] = false;
	    	}
	    
	    	String str = "";
	    	int colRes = -1;
	    	double probRes = 0;
	    	for (int posSource = 1; posSource<colsTotal; posSource+=2) {
	    		double[][] prob = new double[rows+1][colsTotal];
	    		prob[0][posSource] = 1;
	    		for (int row=1; row<=rows; row++) {
	    			for (int c=row%2; c < colsTotal - row%2; c++) {
	    				if (peg[row][c]) {
	    					if (c > row%2 && c < colsTotal - row%2 - 1) {
	    						prob[row][c-1] += prob[row-1][c] / 2;
	    						prob[row][c+1] += prob[row-1][c] / 2;
	    					} else if (c > row%2) {
	    						prob[row][c-1] += prob[row-1][c];
	    					} else {
	    						prob[row][c+1] += prob[row-1][c];	    						
	    					}
	    				} else {
	    					prob[row][c] += prob[row-1][c];
	    				}
	    			}
	    		}
	    		if (prob[rows][colTarget] > probRes) {
	    			probRes = prob[rows][colTarget];
	    			colRes = posSource;
	    		}
	    	}
	    	if (colRes == -1) {
	    		System.out.println("XXX");
	    	} else {
	    		System.out.println(String.format("%d %.6f", (colRes-1)/2, probRes));
	    	}
	    	
	    	
	    	
	    }
	}

}
