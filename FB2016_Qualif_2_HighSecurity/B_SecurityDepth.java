import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//VM arguments: -ea -Xms4096M -Xmx8192M
// increase stack: -Xss515m

// NOT GOOD FOR INPUT SIZE

public class B_SecurityDepth {

	final static String FILENAME = "security_sample";

	private static BitSet empty;
	private static long total;
	private static int N;

	public static void main(String[] args) throws Exception {
		System.setIn(new FileInputStream(FILENAME + ".in"));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME + ".out")));

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();
			for (int test = 1; test <= tests; test++) {
				N = in.nextInt();
				in.nextLine();
				memoCover.clear();
				empty = new BitSet(N*2);
				String line1 = in.nextLine();
				String line2 = in.nextLine();
				for (int i=0; i<N; i++) {
					if (line1.charAt(i) == '.') empty.set(i);
					if (line2.charAt(i) == '.') empty.set(i+N);
				}
				total = empty.cardinality(); // max number of guards
				
				solve(new BitSet());
				
				System.out.format("Case #%d: %d%n", test, total);
				System.err.format("Case #%d: %d%n", test, total);
			}
		}

	}

	private static void solve(BitSet guards) {
		int count = guards.cardinality();
		if (count >= total) return;
		BitSet cover = getCover(guards);
		int coverCount = cover.cardinality();
		if (cover.equals(empty)) {
			total = count;
			return;
		}
		int firstGuard = guards.nextSetBit(0);
		for (int i = empty.nextSetBit(firstGuard+1); i >= 0; i = empty.nextSetBit(i + 1)) {
			BitSet newGuards = (BitSet) guards.clone();
			newGuards.set(i);
			if (getCover(newGuards).cardinality() > coverCount) {
				solve(newGuards);
			}
		}
	}

	static Map<BitSet, BitSet> memoCover = new HashMap<>();
	
	private static BitSet getCover(BitSet guards) {
		BitSet memo = memoCover.get(guards);
		if (memo != null) return memo;
		BitSet covered = (BitSet) guards.clone();
		for (int i = guards.nextSetBit(0); i >= 0; i = guards.nextSetBit(i + 1)) {
		    if (i < N) {
		    	for (int j=i-1; j>0 && empty.get(j); j--) covered.set(j);
		    	for (int j=i+1; j<N && empty.get(j); j++) covered.set(j);
		    	if (empty.get(i+N)) covered.set(i+N);
		    } else {
		    	for (int j=i-1; j>N && empty.get(j); j--) covered.set(j);
		    	for (int j=i+1; j<2*N && empty.get(j); j++) covered.set(j);
		    	if (empty.get(i-N)) covered.set(i-N);
		    }
		}
		memoCover.put(guards, covered);
		return covered;
	}
}
