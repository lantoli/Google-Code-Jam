package lantoli.codejam;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
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
	    		boolean orange = "O".equals(in.next());
	    		int button = in.nextInt();
	    		
	    		int steps;
	    		if (orange) {
	 	    		steps = Math.abs(button-posOrange) + 1;
	 	    		posOrange = button;
	 	    		timeOrange += steps;
		    		time += steps;
	 	    		int substract = Math.min(timeOrange-1, timeBlue);
	 	    		time -= substract;
	 	    		timeOrange -= substract;
	 	    		timeBlue = 0;

	    		} else {
	 	    		steps = Math.abs(button-posBlue) + 1;    			
	 	    		posBlue = button;
	 	    		timeBlue += steps;
		    		time += steps;
	 	    		int substract = Math.min(timeBlue-1, timeOrange);
	 	    		time -= substract;
	 	   			timeBlue -= substract;
	 	    		timeOrange = 0;
	    		}   	
	    	}
	    	System.out.println(String.format("Case #%d: %d", test, time));
	    }
	}

}
