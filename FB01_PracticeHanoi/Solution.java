import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;


public class Solution {
	
	@SuppressWarnings("unchecked")
	static Stack<Integer>[] newStack(int size) {
		@SuppressWarnings("rawtypes")
		Stack[] stack = new Stack[size];
	    for (int i=0; i<size; i++) {
	    	stack[i] = new Stack<Integer>();
	    }
	    return stack;
	}
	
	static class Move{
		public Move(int source, int target) {
			this.source = source;
			this.target = target;
		}
		private int source, target;
	}
	
	static class Result {
		public Result(Stack<Integer>[] pegs) {
			this.pegs = pegs.clone();
			this.moves = new Move[0];
		}
		@SuppressWarnings("unchecked")
		public Result(Result res, int source, int target) {
			pegs = res.pegs.clone();
			for (int i=0; i<pegs.length; i++) {
				pegs[i] = (Stack<Integer>) pegs[i].clone();
			}
			pegs[target].push(pegs[source].pop());
			moves = Arrays.copyOf(res.moves, res.moves.length+1);
			moves[res.moves.length] = new Move(source,target);
		}
		public int numMoves() {
			return moves.length;
		}
		public boolean won(Stack<Integer>[] pegsEnd) {
			for (int i=0; i< pegs.length; i++) {
				if (!pegs[i].containsAll(pegsEnd[i]) || !pegsEnd[i].containsAll(pegs[i])) {
					return false;
				}
			}
			return true;
		}
		Stack<Integer>[] pegs;
		Move[] moves ;
	}
	
	public static void main(String[] args) throws FileNotFoundException {

	 	//System.setIn(new FileInputStream("sample1.in"));

	    Scanner in = new Scanner(System.in);
	    int n = in.nextInt();
	    int k = in.nextInt();
	    
	    Stack<Integer>[] pegsIni = newStack(k);	    
	    for (int i=0; i<n; i++) {
	    	pegsIni[in.nextInt()-1].push(new Integer(i));
	    }	   
	    Stack<Integer>[] pegsEnd = newStack(k);
	    for (int i=0; i<n; i++) {
	    	pegsEnd[in.nextInt()-1].push(new Integer(i));
	    }	  
	    for (int i=0; i<k; i++) {
		    Collections.reverse(pegsIni[i]);
		    Collections.reverse(pegsEnd[i]);
	    }
	    
	    Result won = null;
	    Stack<Result> results = new Stack<Result>();
	    results.add(new Result(pegsIni));
	    while (!results.empty()) {
	    	Result res = results.pop();
	    	if (res.won(pegsEnd)) {
	    		if (won == null || res.numMoves() < won.numMoves()) {
	    			won = res;
	    		}
	    	} else{
		    	if (res.numMoves() < 7) {
			    	for (int i=0; i<k; i++) if (!res.pegs[i].isEmpty()) {
			    		for (int j=0; j<k; j++) {
			    			if (i!=j) {
			    				if (res.pegs[j].isEmpty() || res.pegs[i].peek() < res.pegs[j].peek()) {
			    					results.add(new Result(res, i, j));
			    				}
			    			}
			    		}
			    	}
			    }
	    	}
	    }
	    
	    System.out.println(won.numMoves());
	    for (Move m : won.moves) {
	    	System.out.println(String.format("%d %d", m.source+1, m.target+1));
	    }
	}

}
