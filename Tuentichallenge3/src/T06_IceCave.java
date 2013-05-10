import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class T06_IceCave {

	// VM arguments for hard problems: -ea -Xms4096M -Xmx4096M -Xss1024m

	// Set FILENAME = null for standard input/output
	// final static String FILENAME = null;
	final static String FILENAME = "sample_ice2";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	static int W, H, S, T;

	// let's try bitset instead of typical boolean[][] just for fun, no real need to optimize memory usage in this
	// problem
	static BitSet obstacles;

	static int start, exit;

	static int toXY(int x, int y) {
		return x * H + y;
	}

	static class State {
		final int xy;
		final int moves;
		final int stops;
		final double time;
		final boolean isExit;

		public State(int xy) {
			this.xy = xy;
			this.moves = 0;
			this.stops = 1; // before start
			this.isExit = false;
			this.time = 0;
		}

		public State(State parent, int xyinc, int incmoves, boolean isExit) {
			this.xy = parent.xy + xyinc;
			this.moves = parent.moves + incmoves;
			this.isExit = isExit;
			this.stops = isExit ? parent.stops : parent.stops + 1;
			this.time = (double) moves / S + stops * T;

		}

		public int getTimeRounded() {
			return (int) (time + .5);
		}

		public List<State> returnsNextStates() {
			List<State> ret = new ArrayList<>();
			int[] incmoves = { 1, -1, H, -H }; // all possible moves
			for (int inc : incmoves) {
				int count = 0;
				boolean foundExit = false;
				while (!obstacles.get(xy + (count + 1) * inc)) {
					if (xy + count * inc == exit) {
						foundExit = true;
						break;
					}
					count++;
				}
				if (count > 0) {
					ret.add(new State(this, count * inc, count, foundExit));
				}
			}
			return ret;
		}

		@Override
		public int hashCode() {
			return xy;
		}

		@Override
		public boolean equals(Object obj) {
			return xy == ((State) obj).xy; // only interested in the best time for this position
		}

	}

	public static void main(String[] args) throws Exception {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}

		try (Scanner in = new Scanner(System.in)) {
			int tests = in.nextInt();
			in.nextLine();
			for (int test = 0; test < tests; test++) {

				W = in.nextInt();
				H = in.nextInt();
				S = in.nextInt();
				T = in.nextInt();
				in.nextLine();

				obstacles = new BitSet(W * H);
				for (int y = 0; y < H; y++) {
					String line = in.nextLine();
					assert line.length() == W;
					for (int x = 0; x < W; x++) {
						switch (line.charAt(x)) {
						case '#':
							obstacles.set(toXY(x, y));
							break;
						case 'X':
							start = toXY(x, y);
							break;
						case 'O':
							exit = toXY(x, y);
							break;
						}
					}
				}

				int res = processIceCave();

				System.out.println(Integer.toString(res));
			}
		}
	}

	private static int processIceCave() {
		Map<State, State> cache = new HashMap<>();
		Queue<State> queue = new LinkedList<>();
		State ini = new State(start);
		queue.add(ini);
		cache.put(ini, ini);
		int time = Integer.MAX_VALUE;
		State state;
		while ((state = queue.poll()) != null) {
			for (State next : state.returnsNextStates()) {
				if (next.isExit) {
					time = Math.min(time, next.getTimeRounded());
				} else {
					State prev = cache.get(next);
					// keeps the best for this position
					if (prev == null || next.time < prev.time) {
						queue.add(next);
						cache.put(next, next);
					}
				}
			}
		}
		return time;
	}

}