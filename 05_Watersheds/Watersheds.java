package lantoli.codejam.watersheds;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.omg.PortableServer.CurrentHelper;

public class Watersheds {

	final static private int HIGHEST_ALTITUDE = 100000;
	
	public static void main(String[] args) throws Exception {

		//String inFile = "sample.in";
		//String outFile = "sample.out";
		//String inFile = "B-small-practice.in";
		//String outFile = "B-small-practice.out";
		String inFile = "B-large-practice.in";
		String outFile = "B-large-practice.out";
		
		Scanner sc = new Scanner(new File(inFile));
		PrintWriter writer = new PrintWriter(outFile);		

		int tests = sc.nextInt();
		sc.nextLine();

		System.out.println("tests: " + tests);
		
		for (int test=1; test<=tests; test++) {
			
			int height = sc.nextInt();
			int width = sc.nextInt();
			sc.nextLine();

			System.out.println("test: " + test + ", height: " + height + ", weight: " + width);

			int[][] map = new int[height+2][width+2];
			for (int i=0; i<=height+1; i++) 
				for (int j=0; j<=width+1; j++) {
					if (i==0 || j==0 || i==height+1 || j==width+1) {
						map[i][j] = HIGHEST_ALTITUDE;
					} else {
						map[i][j] = sc.nextInt();
					}
				}
			sc.nextLine();
			char[][] sinks = getSinks(map, height, width);
			writer.println("Case #" + test + ":");
			for (int i=1; i<=height; i++) {
				for (int j=1; j<=width; j++) {
					writer.print(sinks[i][j]);
					writer.print(' ');
				}
				writer.println();
			}
		}
		
		writer.close();
	}

	
	private static char[][] getSinks(int[][] map, int height, int width) {

		char[][] sinks = new char[height+2][width+2];

		char lastLetter = 'a';
		int lettersToUpdate = height*width;
		
		while (lettersToUpdate > 0) {
			outerLoop: 
			for (int i=1; i<=height; i++) {
				for (int j=1; j<=width; j++) {
					if (sinks[i][j] == 0) {
						sinks[i][j] = lastLetter++;
						lettersToUpdate--;
						break outerLoop;
					}
				}
			} 
			int oldLettersToUpdate;
			do  {
				oldLettersToUpdate = lettersToUpdate;
				for (int i=1; i<=height; i++) {
					for (int j=1; j<=width; j++) {
						int value = map[i][j];
						
						int min = value;
						int ii=0, jj=0;
						
						if ((map[i-1][j] < min)) {  ii=i-1; jj=j; min = map[ii][jj]; } // North
						if ((map[i][j-1] < min)) {  ii=i; jj=j-1; min = map[ii][jj]; } // West
						if ((map[i][j+1] < min)) {  ii=i; jj=j+1; min = map[ii][jj]; } // East
						if ((map[i+1][j] < min)) {  ii=i+1; jj=j; min = map[ii][jj]; } // South
		
						if (min != value && (sinks[i][j] != 0 || sinks[ii][jj] != 0)) {
							if (sinks[i][j] == 0 && sinks[ii][jj] != 0 ) {
								sinks[i][j] = sinks[ii][jj];
								lettersToUpdate--;
							} else if (sinks[ii][jj] == 0 && sinks[i][j] != 0 ) {
								sinks[ii][jj] = sinks[i][j];
								lettersToUpdate--;
							}
						}
					}
				}
			} while (oldLettersToUpdate != lettersToUpdate);
		}		
		return sinks;
	}
}
