
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import static java.math.BigInteger.*;

public class FirstOrLast {
	
	public static void main(String[] args) throws Exception {

	 	System.setIn(new FileInputStream("first_or_last.in"));
	 	System.setOut(new PrintStream(new FileOutputStream("first_or_last.out")));

	    Scanner in = new Scanner(System.in);
	    int tests = in.nextInt(); in.nextLine();
	    for (long test=1; test<= tests; test++) {    	
	    	int takeoversNum = in.nextInt() - 1;
	    	int turnNum = in.nextInt();
	    	 
	    	Turn[] turn = new Turn[turnNum];
	    	for (int i=0; i < turnNum; i++) {
	    		turn[i] = new Turn(in.nextBigInteger(), in.nextBigInteger());
	    	  }
	    	Arrays.sort(turn, Turn.comparator);
	    	
	    	Fraction res = new Fraction(ONE,ONE);
	    	for (int i=0; i<takeoversNum; i++) res.multSelf(turn[i].probOvertake());
	    	for (int i=takeoversNum; i<turnNum; i++) res.multSelf(turn[i].probNormal());
	    	
	    	System.out.println(res);
	    }
	}

	static class Turn {
		Turn(BigInteger overtake, BigInteger normal) {
			this.overtake = overtake;
			this.normal = normal;
			danger = new Fraction(overtake.subtract(normal), overtake.multiply(normal));
		}
		BigInteger overtake, normal;
		Fraction danger;
		
		Fraction probOvertake() {
			return new Fraction(overtake.subtract(ONE),overtake);
		}

		Fraction probNormal() {
			return new Fraction(normal.subtract(ONE),normal);
		}

		static Comparator<Turn> comparator = new Comparator<Turn>() {
			public int compare(Turn a, Turn b) {
				return -Fraction.compare(a.danger,b.danger);
			}
    	};
	}
	
	static class Fraction {
		Fraction(BigInteger n, BigInteger d) {
			num = n;
			den = d;
			reduce();
		}
		BigInteger num, den; 
		
		private void reduce() {
			BigInteger gcd = num.gcd(den);
			num = num.divide(gcd);
			den = den.divide(gcd);
		}
				
		public void multSelf(Fraction a) {
			num = num.multiply(a.num);
			den = den.multiply(a.den);
			reduce();
		}

		public String toString() {
			return num + "/" + den;
		}

		static int compare(Fraction a, Fraction b) {
			return a.num.multiply(b.den).compareTo(a.den.multiply(b.num));
		}
	}
	
}
