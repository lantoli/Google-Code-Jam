import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class T05_DungeonQuest {

	// VM arguments for hard problems: -ea -Xms4096M -Xmx4096M -Xss1024m

	// Will try to precalculate as much as possible to save precious cpu cycles later

	// Set FILENAME = null for standard input/output
	final static String FILENAME = null;
	// final static String FILENAME = "sample_quest2";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	static byte M, N, X, Y, Z;
	static int G;

	static byte[] grid;

	private static void initPrecalculatedData() {
		initMovsNext();
		initMaxMoney();
	}

	// For xy, dir, nextDir, returns -1 if not valid, otherwise coordinate for that movement
	static int[][][] movsNext;

	private static void initMovsNext() {
		movsNext = new int[M * N][Dirs.values().length][Dirs.values().length];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				int ij = toXY(i, j);
				for (Dirs dir : Dirs.values()) {
					Arrays.fill(movsNext[ij][dir.pos], -1);
					for (Dirs nextDir : Dirs.values()) {
						if (nextDir != Dirs.NONE && nextDir != dir.opposite()) {
							int nextX = i + nextDir.xinc;
							int nextY = j + nextDir.yinc;
							if (nextX >= 0 && nextY >= 0 && nextX < M && nextY < N) {
								movsNext[ij][dir.pos][nextDir.pos] = toXY(nextX, nextY);
							}
						}
					}
				}
			}
		}

	}

	// Max money which can be get from coordinate xy, moves remaining
	static int[][] maxMoney;

	static int MAXMONEY_MAX_MOVES;

	private static boolean canReachTarget(State state) {
		int remaining = Z - 1 - state.moves;
		if (remaining < MAXMONEY_MAX_MOVES)
			return state.money + maxMoney[state.xy][1] > moneyReturn;
		else
			return true;
	}

	private static void initMaxMoney() {
		MAXMONEY_MAX_MOVES = Math.min(7, G / 2);
		maxMoney = new int[M * N][MAXMONEY_MAX_MOVES];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				int ij = toXY(i, j);
				for (int moves = 0; moves < MAXMONEY_MAX_MOVES; moves++) {
					State ini = new State(ij);
					int money = ini.money;
					Queue<State> queue = new LinkedList<>();
					Set<State> cache = new HashSet<>();
					queue.add(ini);
					State state;
					while ((state = queue.poll()) != null) {
						money = Math.max(money, state.money);
						if (state.moves < moves + 1) {
							for (State add : state.returnsNextStates())
								if (!cache.contains(add)) {
									queue.add(add);
									cache.add(add);
								}
						}
					}
					maxMoney[ij][moves] = money - grid[ij];
				}
			}
		}
	}

	enum Dirs {
		NONE(0, 0, 0), UP(1, 0, -1), DOWN(2, 0, 1), LEFT(3, -1, 0), RIGHT(4, 1, 0);

		final int pos, xinc, yinc;

		Dirs(int pos, int xinc, int yinc) {
			this.pos = pos;
			this.xinc = xinc;
			this.yinc = yinc;
		}

		public Dirs opposite() {
			switch (this) {
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case RIGHT:
				return LEFT;
			case LEFT:
				return RIGHT;
			default:
				return NONE;
			}
		}
	};

	static int moneyReturn;

	private static int toXY(int x, int y) {
		return x * N + y;
	}

	private static void sortLastElm(int[] v) {
		int index = v.length - 1;
		while (index > 0 && v[index] < v[index - 1]) {
			int temp = v[index];
			v[index] = v[index - 1];
			v[index - 1] = temp;
			index--;
		}
	}

	private static int[] newIntArray(int[] src, int newVal) {
		int[] ret = Arrays.copyOf(src, src.length + 1);
		ret[src.length] = newVal;
		sortLastElm(ret);
		return ret;
	}

	static class State implements Comparable<State> {
		final private int xy;
		final private byte moves;
		final private int money;
		final private Dirs dir;
		final private int[] xygem; // visited gems

		public State(int xy) {
			this(xy, (byte) 0, 0, Dirs.NONE, new int[0]);
		}

		private State(int xy, byte moves, int money, Dirs dir, int[] xygem) {
			this.xy = xy;
			this.moves = moves;
			this.dir = dir;
			boolean visitedGem = Arrays.binarySearch(xygem, xy) >= 0;
			if (visitedGem || grid[xy] == 0) {
				this.xygem = xygem;
				this.money = money;
			} else {
				this.money = money + grid[xy];
				this.xygem = newIntArray(xygem, xy);
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + xy;
			result = prime * result + money;
			result = prime * result + dir.hashCode();
			result = prime * result + Arrays.hashCode(xygem);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			State other = (State) obj;
			if (xy != other.xy)
				return false;
			if (money != other.money)
				return false;
			if (dir != other.dir)
				return false;
			if (!Arrays.equals(xygem, other.xygem))
				return false;
			return true;
		}

		public State nextState(int nextXY, Dirs nextDir) {
			return new State(nextXY, (byte) (moves + 1), money, nextDir, xygem);
		}

		public List<State> returnsNextStates() {
			List<State> ret = new ArrayList<>();
			for (Dirs nextDir : Dirs.values()) {
				int nextXY = movsNext[xy][dir.pos][nextDir.pos];
				if (nextXY > 0) {
					State add = nextState(nextXY, nextDir);
					ret.add(add);
				}
			}
			return ret;
		}

		@Override
		public int compareTo(State o) {
			int ret = moves - o.moves;
			if (ret != 0)
				return ret;
			else
				return money - o.money;
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

				in.useDelimiter("[\\s,#]+");

				M = in.nextByte();
				N = in.nextByte();
				X = in.nextByte();
				Y = in.nextByte();
				Z = in.nextByte();
				G = in.nextInt();
				grid = new byte[M * N];

				for (int n = 0; n < G; n++) {
					byte i = in.nextByte();
					byte j = in.nextByte();
					byte k = in.nextByte();
					grid[toXY(i, j)] = k;
				}

				initPrecalculatedData();
				processQuest();
				System.out.println(moneyReturn);
			}
		}
	}

	private static void processQuest() {
		State ini = new State(toXY(X, Y));
		moneyReturn = ini.money;
		Queue<State> queue = new LinkedList<>();
		Set<State> cache = new HashSet<>();
		queue.add(ini);
		State state;
		while ((state = queue.poll()) != null) {
			if (moneyReturn < state.money) {
				moneyReturn = state.money;
			}
			if (state.moves < Z) {
				for (State add : state.returnsNextStates())
					if (!cache.contains(add)) {
						if (canReachTarget(add)) {
							queue.add(add);
							cache.add(add);
						}
					}
			}
		}
	}

}