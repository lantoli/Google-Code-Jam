import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;


public class StudiousStudent {
	
	public static void main(String[] args) throws FileNotFoundException {

	 	System.setIn(new FileInputStream("studious_student.in"));
	 	System.setOut(new PrintStream(new FileOutputStream("studious_student.out")));

	    Scanner in = new Scanner(System.in);
	    int tests = in.nextInt(); in.nextLine();
	    System.err.println("tests: " + tests);
	    for (long test=1; test<= tests; test++) {    	
	    	
	    	in.nextInt();
	    	String[] str = in.nextLine().split(" ");
	    	
	    	Arrays.sort(str);
	    	for (String s: str) {
	    		System.out.print(s);		    
	    	}
	    
	    	System.out.println();
	    }
	    
	}

}
