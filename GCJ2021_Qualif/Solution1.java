import java.io.FileInputStream;
import java.util.Scanner;

import static java.util.Arrays.sort;

public class Solution1 {

    final static String FILENAME = "sample1";
    final static String FILENAME_IN = FILENAME + ".in";

    public static void main(String[] args) throws Exception {
        System.setIn(new FileInputStream(FILENAME_IN));

        Scanner in = new Scanner(System.in);
        int numTests = in.nextInt();
        in.nextLine();
        for (int test = 1; test <= numTests; test++) {
            int[] nums = new int[in.nextInt()];
            for (int j = 0; j < nums.length; j++) {
                nums[j] = in.nextInt();                
            }
            System.out.println(String.format("Case #%d: %d", test, execute(nums)));
        }
    }

    private static int execute(int[] nums) {
        int ret = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            int j = i;
            for (int z = i + 1; z < nums.length; z++) {
                if (nums[z] < nums[j]) {
                    j = z;
                }
            }
            reverse(nums, i, j);
            ret += j - i + 1;
        }
        return ret;
    }

    public static void reverse(int[] arr, int l, int r)
    {
        int d = (r-l+1)/2;
        for(int i=0;i<d;i++)
        {
            int t = arr[l+i];
            arr[l+i] = arr[r-i];
            arr[r-i] = t;
        }
        // print array here
    }

}
