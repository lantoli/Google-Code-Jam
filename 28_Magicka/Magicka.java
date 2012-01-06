package lantoli.codejam;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Magicka {
	
	public static void main(String[] args) throws Exception {

	 	System.setIn(new FileInputStream("B-large-practice.in"));
	 	System.setOut(new PrintStream(new FileOutputStream("B-large-practice.out")));

	    Scanner in = new Scanner(System.in);
	    int tests = in.nextInt(); in.nextLine();
	    for (long test=1; test<= tests; test++) {    	
	   
	    	HashMap<String,Character> combiMap = new HashMap<String,Character>();
	    	int combiNum = in.nextInt();
	    	for (int i=0; i<combiNum; i++) {
	    		String com = in.next();
	    		combiMap.put(com.substring(0,2), com.charAt(2));
	    		combiMap.put(com.substring(1,2) + com.substring(0,1), com.charAt(2));
	    	}
	
	    	int oppoNum = in.nextInt();
	    	String[] oppoArray = new String[oppoNum*2];
	    	for (int i=0; i<oppoNum; i++) {
	    		oppoArray[i*2] = in.next();
	    		oppoArray[i*2+1] = new StringBuilder(oppoArray[i*2]).reverse().toString();
	    	}
	    	int elmNum = in.nextInt();
	    	String elm = in.next();
	    	assert(elmNum == elm.length());

	    	ArrayList<Character> res = new ArrayList<Character>();
	    	loop: 
	    	for (char c : elm.toCharArray()) {
	    		if (res.isEmpty()) {
	    			res.add(c);
	    			continue;
	    		}
	    		Character combiChar = combiMap.get("" + c + res.get(res.size()-1));
	    		if (combiChar != null) {
	    			res.remove(res.size()-1);
	    			res.add(combiChar);
	    			continue;
	    		} 
	    		for (String oppo : oppoArray) { 
	    			if (oppo.charAt(0) == c && res.contains(oppo.charAt(1))) {
	    				res.clear();
	    				continue loop;
	    			}	
	    		}
	    		res.add(c);
	    	}
	    
	    	System.out.println(String.format("Case #%d: %s", test,res));
	    }
	}
}
