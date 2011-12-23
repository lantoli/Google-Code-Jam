import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;


public class Solution {
	
	public static void main(String[] args) throws FileNotFoundException {

	 	System.setIn(new FileInputStream("double_squares.in"));
	 	System.setOut(new PrintStream(new FileOutputStream("double_squares.out")));

	    Scanner in = new Scanner(System.in);
	
	    int tests = in.nextInt(); in.nextLine();
	    
	    for (long test=1; test<= tests; test++) {    	
	    	int num = in.nextInt();
	    	int total = 0;
	    	for (int i=0; i<=num; i++) {
	    		int j = (int) Math.sqrt(num-i*i);
	    		if (j<i) break;
	    		if (num == i*i + j*j) total++;
		    }
	    	System.out.println(total);
	    }
	}

}
