import java.util.Scanner;

public class Solution2 {

    final static String FILENAME = "sample2";
    final static String FILENAME_IN = FILENAME + ".in";

    public static void main(String[] args) throws Exception {
 //       System.setIn(new FileInputStream(FILENAME_IN));

        Scanner in = new Scanner(System.in);
        int numTests = in.nextInt();
        in.nextLine();
        for (int test = 1; test <= numTests; test++) {
            System.out.println(String.format("Case #%d: %d", test, execute(in.nextInt(), in.nextInt(), in.next())));
            in.nextLine();
        }
    }

    private static int execute(int x, int y, String s) {
        int ret = 0;
        char cur = '?';
        for (char next : s.toCharArray()) {
            if (next != '?') {
                if (cur == 'C' && next == 'J') {
                    ret += x;
                } else if (cur == 'J' && next == 'C') {
                    ret += y;
                }
                cur = next;
            }
        }
        return ret;
    }
}
