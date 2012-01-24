package lantoli.codejam;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;


public class AlwaysTurnLeft {

	public static void main(String[] args) throws FileNotFoundException {

		System.setIn(new FileInputStream("B-large-practice.in"));
		System.setOut(new PrintStream(new FileOutputStream("B-large-practice.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		for (long test=1; test<= tests; test++) {

			String[] paths = in.nextLine().split(" ");
			String pathIn = paths[0], pathOut = paths[1];

			minCol = Integer.MAX_VALUE;
			maxCol = maxRow = 0;
			walkBothWays(0, pathIn, pathOut, listenerSize);

			maze = new byte[maxRow + 1][maxCol - minCol + 1];
			walkBothWays(-minCol, pathIn, pathOut, listenerMap);

			System.out.println(String.format("Case #%d:", test));
			for (int i=0; i<maze.length; i++) {
				for (int j=0; j<maze[0].length; j++) {
					System.out.print(Integer.toHexString(maze[i][j]));
				}
				System.out.println();
			}
		}
	}

	private static void walkBothWays(int colStart, String pathIn, String pathOut, MazeListener listener) {
		row = 0;
		col = colStart;
		dir = SOUTH;
		listener.afterwalk();
		walkMaze(pathIn, listener);
		listener.prewalk();
		dir = turn(dir,2);
		walkMaze(pathOut, listener);
	}

	private static void walkMaze(String path, MazeListener listener) {
		for (int i=1; i<path.length()-1; i++) {
			char type = path.charAt(i);
			if (type == 'L') {
				dir = turn(dir, -1);
			} else  if (type == 'R') {
				dir = turn(dir, 1);
			} else {
				listener.prewalk();
				row += rowinc[dir];
				col += colinc[dir];
				listener.afterwalk();
			}
		}
	}

	static MazeListener listenerMap = new MazeListener() {
		public void prewalk() {
			maze[row][col] |= dir;
		}
		public void afterwalk() {
			maze[row][col] |= opp[dir];
		}
	};

	static MazeListener listenerSize = new MazeListener() {
		public void prewalk() {  }
		public void afterwalk() {
			maxRow = max(maxRow, row);
			maxCol = max(maxCol, col);
			minCol = min(minCol, col);
		}
	};

	interface MazeListener {
		void prewalk();
		void afterwalk();
	}

	static int row, col;
	static int maxRow, minCol, maxCol;
	static byte dir;
	static byte[][] maze;
	static final byte NORTH = 1;
	static final byte SOUTH = 2;
	static final byte WEST = 4;
	static final byte EAST = 8;

	static final int[] rowinc = { 0, -1, 1, 0,  0, 0, 0, 0, 0  };
	static final int[] colinc = { 0,  0, 0, 0, -1, 0, 0, 0, 1 };

	static final byte[] opp = { 0, SOUTH, NORTH, 0, EAST, 0, 0, 0, WEST };

	static byte turn(int dir, int turn) {
		final int[] dirTodeg = { -1, 0, 2, -1, 3, -1, -1, -1, 1 };
		final byte[] degTodir = { NORTH, EAST, SOUTH, WEST };

		return  degTodir[(dirTodeg[dir] + turn + 4) % 4];
	}


}
