package lantoli.codejam;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Done after looking at contest analysis
 */
public class Numbers {

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
		System.out.println("tests: " + tests);
		for (int test=1; test<=tests; test++) {
			int n = sc.nextInt();

			int [][] matrix = matrix_exp(A, n);
			int res = (2 * matrix[0][0] - 1 + MOD) % MOD;

			writer.println(String.format("Case #%d: %03d", test, res));
		}
		writer.close();
	}

	final static int[][] A = { {3, 5}, {1, 3} };

	final static int MOD = 1000;

	static int[][] matrix_exp(int[][] A, int exp) {
		if (exp == 1) {
			return A;
		} else if (exp % 2 == 0) {
			int[][] Ahalf = matrix_exp(A, exp/2);
			return matrix_multiply(Ahalf, Ahalf);
		} else {
			return matrix_multiply(matrix_exp(A, exp-1), A);
		}
	}

	static int[][] matrix_multiply(int[][] A, int[][] B) {
		int[][] res = new int[2][2];
		res[0][0] = (A[0][0] * B[0][0] + A[0][1] * B[1][0]) % 1000;
		res[0][1] = (A[0][0] * B[0][1] + A[0][1] * B[1][1]) % 1000;
		res[1][0] = (A[1][0] * B[0][0] + A[1][1] * B[1][0]) % 1000;
		res[1][1] = (A[1][0] * B[0][1] + A[1][1] * B[1][1]) % 1000;
		return res;
	}

	// THE SOLUTION ENDS HERE --------------------


	void matrix3Example() {
		int[] matrix = new int[4];
		int a = matrix[0], b = matrix[1], c = matrix[2], d = matrix[3];
		int aa = 1, bb = 1,  cc = 1, dd = 1;
		matrix[0] = (a * aa + b * cc) % 1000;
		matrix[1] = (a * bb + b * dd) % 1000;
		matrix[2] = (c * aa + d * cc) % 1000;
		matrix[3] = (c * bb + d * dd) % 1000;
	}

	static int[][] matrix_multiply_generic(int[][] A, int[][] B) {
		assert A[0].length == B.length;
		int[][] res = new int[A.length][B[0].length];
		for (int i=0; i<A.length; i++) {
			for (int j=0; j<B[0].length; j++) {
				for (int k=0; k<B.length; k++) {
					res[i][j] = (res[i][j] + A[i][k] * B[k][j]) % MOD;
				}
			}
		}
		return res;
	}


}
