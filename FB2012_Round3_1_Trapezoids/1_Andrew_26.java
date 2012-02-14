import java.util.*;
import java.io.*;

class A_as {
    Scanner in;
    PrintWriter out;

    long x, A, B, M, p, q;

    long prior;

    long next() {
        prior = (A * prior + B) % M;
        return prior;
    }

    class Event implements Comparable<Event> {
        long pos;
        int trap;
        boolean start;

        Event(long pos, int trap, boolean start) {
            this.pos = pos;
            this.trap = trap;
            this.start = start;
        }

        public int compareTo(Event o) {
            return pos < o.pos ? -1 : pos > o.pos ? 1 : 0;
        }
    }

    public void solve() throws IOException {
        int testNo = in.nextInt();
        for (int test = 1; test <= testNo; test++) {
            long time = System.currentTimeMillis();
            int n = in.nextInt();
            int k = in.nextInt();
            x = in.nextInt();
            A = in.nextInt();
            B = in.nextInt();
            M = in.nextInt();
            p = in.nextInt();
            q = in.nextInt();
            prior = x;

            long[] aa = new long[n];
            long[] bb = new long[n];
            long[] cc = new long[n];
            long[] dd = new long[n];
            for (int i = 0; i < k; i++) {
                aa[i] = in.nextInt();
                bb[i] = in.nextInt();
                cc[i] = in.nextInt();
                dd[i] = in.nextInt();
            }

            for (int i = k; i < n; i++) {
                aa[i] = aa[i - k] + next() % (2 * p) - p;
                bb[i] = aa[i] + 1 + next() % (2 * (bb[i % k] - aa[i % k]));
                cc[i] = cc[i - k] + next() % (2 * q) - q;
                dd[i] = cc[i] + 1 + next() % (2 * (dd[i % k] - cc[i % k]));
            }

            long[] a = new long[n];
            long[] b = new long[n];
            long[] c = new long[n];
            long[] d = new long[n];

            for (int i = 0; i < n; i++) {
                a[i] = aa[i] * 1000000 + i;
                b[i] = bb[i] * 1000000 + i;
                c[i] = cc[i] * 1000000 + i;
                d[i] = dd[i] * 1000000 + i;
            }

            Event[] top = new Event[2 * n];
            Event[] bot = new Event[2 * n];
            for (int i = 0; i < n; i++) {
                top[2 * i] = new Event(a[i], i, true);
                top[2 * i + 1] = new Event(b[i], i, false);
                bot[2 * i] = new Event(c[i], i, true);
                bot[2 * i + 1] = new Event(d[i], i, false);
            }

            Arrays.sort(top);
            Arrays.sort(bot);

            int[] tv = new int[2 * n];
            tv[0] = 0;
            for (int i = 1; i < 2 * n; i++) {
                tv[i] = tv[i - 1];
                if (top[i - 1].start) {
                    tv[i]++;
                }
            }

            RMQ rmq = new RMQ();
            rmq.init(tv);

            int[] topS = new int[n];
            int[] topF = new int[n];
            for (int i = 0; i < 2 * n; i++) {
                if (top[i].start) {
                    topS[top[i].trap] = i;
                } else {
                    topF[top[i].trap] = i;
                }
            }

            int[] lft = new int[2 * n];
            int[] rgt = new int[2 * n];

            int minLeft = 2 * n + 1;

            for (int i = 0; i < 2 * n; i++) {
                if (!bot[i].start) {
                    if (topF[bot[i].trap] < minLeft) {
                        minLeft = topF[bot[i].trap];
                    }
                }
                lft[i] = minLeft;
            }

            int maxRight = -1;
            for (int i = 2 * n - 1; i >= 0; i--) {
                if (bot[i].start) {
                    if (topS[bot[i].trap] > maxRight) {
                        maxRight = topS[bot[i].trap];
                    }
                }
                rgt[i] = maxRight;
            }


            int ans = Integer.MAX_VALUE;
            for (int i = 0; i < 2 * n; i++) {
                if (!bot[i].start) {
                    rmq.add(topF[bot[i].trap], 2 * n, -1);
                }

                if (lft[i] <= rgt[i]) {
                    int v = rmq.min(lft[i], rgt[i] + 1);
                    if (v < ans) {
                        ans = v;
                    }
                }

                if (bot[i].start) {
                    rmq.add(0, topS[bot[i].trap] + 1, 1);
                }
            }

            if (ans == Integer.MAX_VALUE) {
                ans = -1;
            }

            out.println("Case #" + test + ": " + ans);
            System.err.println("Test " + test + " in " + (System.currentTimeMillis() - time));
        }
    }

    class RMQ {
        int n;
        int[] v;
        int[] d;

        void init(int p, int L, int R, int[] a) {
            if (L == R - 1) {
                v[p] = a[L];
            } else {
                int m = (L + R) / 2;
                init(2 * p + 1, L, m, a);
                init(2 * p + 2, m, R, a);
                v[p] = Math.min(v[2 * p + 1], v[2 * p + 2]);
            }
        }

        void init(int[] a) {
            n = a.length;
            v = new int[a.length * 4];
            d = new int[a.length * 4];
            init(0, 0, a.length, a);
        }

        int min(int p, int L, int R, int a, int b) {
            if (b <= L || R <= a) {
                return Integer.MAX_VALUE;
            } else if (a <= L && R <= b) {
                return v[p];
            }

            d[2 * p + 1] += d[p];
            v[2 * p + 1] += d[p];
            d[2 * p + 2] += d[p];
            v[2 * p + 2] += d[p];
            d[p] = 0;

            int m = (L + R) / 2;
            return Math.min(min(2 * p + 1, L, m, a, b), min(2 * p + 2, m, R, a, b));
        }

        void add(int p, int L, int R, int a, int b, int z) {
            if (b <= L || R <= a) {
                return;
            } else if (a <= L && R <= b) {
                d[p] += z;
                v[p] += z;
                return;
            }

            d[2 * p + 1] += d[p];
            v[2 * p + 1] += d[p];
            d[2 * p + 2] += d[p];
            v[2 * p + 2] += d[p];
            d[p] = 0;

            int m = (L + R) / 2;
            add(2 * p + 1, L, m, a, b, z);
            add(2 * p + 2, m, R, a, b, z);
            v[p] = Math.min(v[2 * p + 1], v[2 * p + 2]);

        }

        int min(int l, int r) {
            return min(0, 0, n, l, r);
        }

        void add(int l, int r, int z) {
            add(0, 0, n, l, r, z);
        }
    }

    public void run() {
        try {
            in = new Scanner(new File("trapezoids.txt"));
            out = new PrintWriter(new File("A.out"));

            solve();

            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] arg) {
        new A_as().run();
    }
}