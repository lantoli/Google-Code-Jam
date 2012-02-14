import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * @author Pavel Mavrin
 */
public class C {

    BufferedReader br;
    StringTokenizer st;
    PrintWriter out;
    private int n;
    private int m;
    private ArrayList<Integer>[] pl;
    private int[][] lists;
    private int mm;
    private int[] k;
    private int[] x;
    private int[] pr;
    private int[] opr;

    public C() throws FileNotFoundException {
        br = new BufferedReader(new FileReader("input.txt"));
        out = new PrintWriter("output.txt");
    }

    String next() throws IOException {
        while (st == null || !st.hasMoreTokens()) {
            st = new StringTokenizer(br.readLine());
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(next());
    }

    public static void main(String[] args) throws IOException {
        new C().solve();
    }

    private void solve() throws IOException {
        int n = nextInt();
        for (int i = 0; i < n; i++) {
            String s = solveCase();
            out.println("Case #" + (i + 1) + ": " + s);
            System.out.println("Case #" + (i + 1) + ": " + s);
        }
        out.close();
    }

    private String solveCase() throws IOException {
        n = nextInt();
        m = nextInt();
        x = new int[n];
        x[0] = nextInt();
        int a = nextInt();
        int b = nextInt();
        int p = nextInt();
        for (int i = 1; i < n; i++) {
            x[i] = (int) ((x[i - 1] * 1L * a + b) % p);
        }
        Integer[] pp = new Integer[n];
        for (int i = 0; i < n; i++) {
            pp[i] = i;
        }
        Arrays.sort(pp, new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                if (x[a] < x[b]) return -1;
                if (x[a] > x[b]) return 1;
                return 0;
            }
        });
        pr = new int[n];
        for (int i = 0; i < n; i++) pr[i] = pp[i];
        opr = new int[n];
        for (int i = 0; i < n; i++) opr[pr[i]] = i;


        pl = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            pl[i] = new ArrayList<Integer>();
        }
        lists = new int[m][];
        for (int i = 0; i < m; i++) {
            int size = nextInt();
            lists[i] = new int[size];
            lists[i][0] = nextInt();
            a = nextInt();
            b = nextInt();
            for (int j = 1; j < size; j++) {
                lists[i][j] = (int) ((lists[i][j - 1] * 1L * a + b) % n);
            }
            for (int j = 0; j < size; j++) {
                pl[lists[i][j]].add(i);
            }
        }
        int l = 0;
        int r = (1 << 30) + 1;
        k = new int[n];
//        System.out.println(Arrays.toString(x));
//        System.out.println(x[pr[n - 1]] - x[pr[0]]);
        while (r > l + 1) {
            mm = (int) ((0l + l + r) / 2);
            Arrays.fill(k, -1);
            boolean ok = true;
            for (int i = 0; i < n; i++) {
                if (k[i] == -1) {
                    if (dfs(i, 0) || dfs(i, 1)) {
                        //ok
                    } else {
//                        System.out.println(i);
                        ok = false;
                        break;
                    }
                }
            }
//            System.out.println(mm + " " + ok);
            if (ok) {
                l = mm;
            } else {
                r = mm;
            }
//            System.out.println(l + " " + r);
        }

        return "" + l;
    }

    private boolean dfs(int i, int val) {
        if (k[i] == val) return true;
        if (k[i] == 1 - val) return false;
        k[i] = val;
        if (val == 1) {
            if (pl[i].size() == 0) {
                k[i] = -1;
                return false;
            }
            for (int j : pl[i]) {
                for (int k : lists[j]) {
                    if (k != i) {
                        if (!dfs(k, 0)) {
                            this.k[i] = -1;
                            return false;
                        }
                    }
                }
            }
        } else {
//            for (int j = 0; j < n; j++) {
//                if (Math.abs(x[i] - x[j]) < mm && j != i)
//                    if (!dfs(j, 1)) {
//                        k[i] = -1;
//                        return false;
//                    }
//            }
            int p = opr[i];
            for (int j = p + 1; j < n && x[pr[j]] < 0l + x[i] + mm; j++) {
                if (!dfs(pr[j], 1)) {
                    k[i] = -1;
                    return false;
                }
            }
            for (int j = p - 1; j >= 0 && x[pr[j]] > 0l + x[i] - mm; j--) {
                if (!dfs(pr[j], 1)) {
                    k[i] = -1;
                    return false;
                }
            }
        }
        return true;
    }
}
