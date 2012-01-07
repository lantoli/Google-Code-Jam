package codejam;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static java.lang.Math.min;

public class CandySplitting {
	
	public static void main(String[] args) throws Exception {

	 	System.setIn(new FileInputStream("C-large-practice.in"));
	 	System.setOut(new PrintStream(new FileOutputStream("C-large-practice.out")));

	    Scanner in = new Scanner(System.in);
	    int tests = in.nextInt(); in.nextLine();
	    for (long test=1; test<= tests; test++) {    	
	   
	    	int xor = 0, sum = 0, min = Integer.MAX_VALUE;
	    	int n = in.nextInt();
	    	for (int i=0; i<n; i++) {
	    		int candy = in.nextInt();
	    		xor ^= candy;
	    		sum += candy;
	    		min = min(min,candy);
	    	}
	    	String res = xor == 0 ? Integer.toString(sum-min) : "NO"; 
	    	System.out.println(String.format("Case #%d: %s", test, res));
	    }
	}
		
}
