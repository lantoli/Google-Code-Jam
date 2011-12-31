package lantoli.codejam;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.max;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;


public class BotTrust {
	
	public static void main(String[] args) throws FileNotFoundException {

	 	System.setIn(new FileInputStream("A-large-practice.in"));
	 	System.setOut(new PrintStream(new FileOutputStream("A-large-practice.out")));

	    Scanner in = new Scanner(System.in);
	    int tests = in.nextInt(); in.nextLine();
	    for (long test=1; test<= tests; test++) {    	
	   
	    	int time = 0;
	    	int posOrange = 1, posBlue = 1;
	    	int timeOrange = 0, timeBlue = 0;
	    	int n = in.nextInt();
	    	for (int i=0; i<n; i++) {
	    		if ("O".equals(in.next())) {
	    	 		int button = in.nextInt();
	        		int add =  max(1 - timeOrange, abs(button-posOrange) + 1 - timeBlue);
	 	    		timeOrange += add;
		    		time += add;
	 	    		timeBlue = 0;
	 	    		posOrange = button;
	    		} else {
	    	 		int button = in.nextInt();
	        		int add =  max(1 - timeBlue, abs(button-posBlue) + 1 - timeOrange);
	 	    		timeBlue += add;
		    		time += add;
		    		timeOrange = 0;
	 	    		posBlue = button;
	    		}   	
	    	}
	    	System.out.println(String.format("Case #%d: %d", test, time));
	    }
	}

}
