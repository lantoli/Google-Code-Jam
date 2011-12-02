package lantoli.codejam;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MakingChessBoards {

	public static void main(String[] args) throws Exception {
		
		//String inFile = "sample.in";
		//String outFile = "sample.out";
		//String inFile = "C-small-practice.in";
		//String outFile = "C-small-practice.out";
		String inFile = "C-large-practice.in";
		String outFile = "C-large-practice.out";
		
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		
		int tests = sc.nextInt();
		sc.nextLine();
		System.out.println("tests: " + tests);
		for (int test=1; test<=tests; test++) {
			int rows = sc.nextInt();
			int cols = sc.nextInt();
			sc.nextLine();
			assert(cols % 4 == 0);
			boolean[][] board = new boolean[rows][cols];
			for (int i=0; i<rows; i++) {
				String rowStr = sc.nextLine();
				for (int j=0; j<cols/4; j++) {
					char c = rowStr.charAt(j);
					int dec = (c >= '0' && c<= '9') ? c-'0' : c-'A' + 10;
						board[i][j*4+0] = (dec & 8) == 8;
						board[i][j*4+1] = (dec & 4) == 4;
						board[i][j*4+2] = (dec & 2) == 2;
						board[i][j*4+3] = (dec & 1) == 1;	
				}
			}
			System.out.println("test: " + test);
			writer.println("Case #" + test + ": " + doit(board));
		}		
		writer.close();
	}

	final static int MAX_SIZE_BOARDS = 515;
	
	private static String doit(boolean[][] board) {
		int rows = board.length;
		int cols = board[0].length;
		boolean[][] visited = new boolean[rows][cols];
		int[] boardSizes = new int[MAX_SIZE_BOARDS];
		int bestSize, lastBestSize = 0;
		do {
			bestSize=0;
			loop:
			for (int i=0;i<rows; i++) {
				for (int j=0; j<cols; j++) {
					int size = getBoardSize(board,i,j, visited);
					if (size > bestSize) {
						bestSize = size;
						if (bestSize == lastBestSize-1 ) {
							break loop;
						}
					}
				}
			}
			if (bestSize > 0) {
				for (int i=0;i<rows; i++) {
					for (int j=0; j<cols; j++) {
						int size = getBoardSize(board,i,j, visited);
						if (size==bestSize) {
							boardSizes[bestSize] ++;
							for (int ii=i; ii<i+bestSize; ii++) {
								for (int jj=j; jj<j+bestSize; jj++) {
									visited[ii][jj] = true;
								}
							}
						}
					}
				}
				lastBestSize = bestSize;
			}
		} while (bestSize>0);
		int numSizes = 0;
		for (int s : boardSizes) if (s>0) numSizes++;
		StringBuilder strAux = new StringBuilder();
		strAux.append(numSizes);
		strAux.append('\n');
		for (int i=boardSizes.length-1; i>=0; i--) if (boardSizes[i] > 0) {
			strAux.append(i);
			strAux.append(' ');
			strAux.append(boardSizes[i]);
			strAux.append('\n');
		}
		return strAux.toString();
	}

	private static int getBoardSize(boolean[][] board, int row, int col,boolean[][] visited) {
		int size = 0;
		boolean initColor = board[row][col];
		int rows = board.length;
		int cols = board[0].length;
		outerloop:
		while (row+size<rows && col+size<cols && !visited[row+size][col+size] && board[row+size][col+size] == initColor) {
			for (int i=row+size-1; i>=row; i--) {
				if (visited[i][col+size] || board[i][col+size] == board[i+1][col+size]) {
					break outerloop;
				}
			}
			for (int j=col+size-1; j>=col; j--) {
				if (visited[row+size][j] || board[row+size][j] == board[row+size][j+1]) {
					break outerloop;
				}
			}
			size++;
		}
		
		return size;
	}

}
