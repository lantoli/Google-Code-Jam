import java.io.FileInputStream;
import java.util.Scanner;

public class Solution3 {

    final static String FILENAME = "sample3";
    final static String FILENAME_IN = FILENAME + ".in";

    public static void main(String[] args) throws Exception {
        System.setIn(new FileInputStream(FILENAME_IN));

        Scanner in = new Scanner(System.in);
        int numTests = in.nextInt();
        in.nextLine();
        for (int test = 1; test <= numTests; test++) {
            System.out.println(String.format("Case #%d: %s", test, stringfy(execute(in.nextInt(), in.nextInt()))));
        }
    }

    private static int[] execute(int n, int c) {
        c -= n-1;
        if (c < 0) return null;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i+1;
        }
        return arr;
    }

    private static String stringfy(int[] arr) {
        if (arr == null) return "IMPOSSIBLE";
        StringBuilder str = new StringBuilder();
        for (var elm : arr) {
            str.append(elm);
            str.append(' ');
        }
        return str.toString();
    }

}
