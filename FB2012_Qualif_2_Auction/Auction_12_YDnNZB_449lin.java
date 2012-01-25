import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Auction_12_YDnNZB_449lin {

	public static void main(String[] args) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream("auction.in");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream("auction12.out");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Auction_12_YDnNZB_449lin solver = new Auction_12_YDnNZB_449lin();
		solver.solve(1, in, out);
		out.close();
	}

	static class SeqDescription {
		int[] preperiod;
		int[] period;
		long periodItems;
		long[] firstPos;

		public void precalc(int max) {
			firstPos = new long[max];
			Arrays.fill(firstPos, INF);
			for (int i = 0; i < preperiod.length; ++i) {
				if (firstPos[preperiod[i]] != INF) {
					throw new RuntimeException();
				}
				firstPos[preperiod[i]] = i;
			}
			if (periodItems > 0) {
				if (periodItems < period.length) {
					throw new RuntimeException();
				}
				for (int i = 0; i < period.length; ++i) {
					if (firstPos[period[i]] != INF) {
						throw new RuntimeException();
					}
					firstPos[period[i]] = preperiod.length + i;
				}
			}
		}

		public int getItem(long pos) {
			if (pos < preperiod.length) {
				return preperiod[((int) pos)];
			}
			pos -= preperiod.length;
			if (pos < periodItems) {
				return period[((int) (pos % period.length))];
			}
			throw new RuntimeException();
		}
	}

	static class ExtendedGcdResult {
		long gcd;
		long mulA;
		long mulB;
	}

	static final int INF = (int) 1e9;

	static class SyncRes {
		String[] res;

		SyncRes(String[] res) {
			this.res = res;
		}

		synchronized void store(int at, String what) {
			res[at] = what;
			System.err.println("Done test " + (at + 1));
		}

		synchronized String get(int at) {
			return res[at];
		}
	}

	public void solve(int testNumber, InputReader in, PrintWriter out) {
		int numTests = in.nextInt();
		ExecutorService executor = Executors.newFixedThreadPool(1);
		final SyncRes res = new SyncRes(new String[numTests]);
		for (int testId = 0; testId < numTests; ++testId) {
			final long n = in.nextLong();
			final long p1 = in.nextLong();
			final long w1 = in.nextLong();
			final long m = in.nextLong();
			final long k = in.nextLong();
			final long a = in.nextLong();
			final long b = in.nextLong();
			final long c = in.nextLong();
			final long d = in.nextLong();
			final int curTest = testId;
			executor.submit(new Runnable() {
				public void run() {
					try {
						res.store(curTest, doit(n, p1, w1, m, k, a, b, c, d));
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
			});
		}
		executor.shutdown();
		try {
			executor.awaitTermination(1000, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		for (int testId = 0; testId < numTests; ++testId) {
			out.println("Case #" + (testId + 1) + ": " + res.get(testId));
		}
	}

	private String doit(long n, long p1, long w1, long m, long k, long a, long b, long c, long d) {
		SeqDescription xSeq = buildSeq(n, p1, a, b, m);
		SeqDescription ySeq = buildSeq(n, w1, c, d, k);
		ExtendedGcdResult gcdRes = extendedGcd(xSeq.period.length, ySeq.period.length);
		int maxX = (int) (m + 1);
		int maxY = (int) (k + 1);
		xSeq.precalc(maxX);
		ySeq.precalc(maxY);
		int[] minThisX = new int[maxX];
		Arrays.fill(minThisX, INF);
		int[] maxThisX = new int[maxX];
		Arrays.fill(maxThisX, -INF);
		long badPart = Math.max(xSeq.preperiod.length, ySeq.preperiod.length);
		if (badPart + Math.max(xSeq.period.length, ySeq.period.length) >= n) {
			badPart = (int) n;
		}
		if (badPart > n) {
			throw new RuntimeException();
		}
		for (int i = 0; i < badPart; ++i) {
			int x = xSeq.getItem(i);
			int y = ySeq.getItem(i);
			minThisX[x] = Math.min(minThisX[x], y);
			maxThisX[x] = Math.max(maxThisX[x], y);
		}
		if (badPart < n) {
			int[] xp = new int[xSeq.period.length];
			for (int i = 0; i < xp.length; ++i) {
				xp[i] = xSeq.getItem(badPart + i);
			}
			int[] yp = new int[ySeq.period.length];
			for (int i = 0; i < yp.length; ++i) {
				yp[i] = ySeq.getItem(badPart + i);
			}
			periodicSolution(xp, yp, n - badPart, minThisX, maxThisX);
		}
		int curMax = -INF;
		long numTerribleDeals = 0;
		for (int x = maxX - 1; x >= 0; --x) {
			if (maxThisX[x] > curMax) {
				numTerribleDeals += countOccurrences(x, maxThisX[x], xSeq, ySeq, n, gcdRes);
				curMax = maxThisX[x];
			}
		}
		int curMin = INF;
		long numBargains = 0;
		for (int x = 0; x < maxX; ++x) {
			if (minThisX[x] < curMin) {
				numBargains += countOccurrences(x, minThisX[x], xSeq, ySeq, n, gcdRes);
				curMin = minThisX[x];
			}
		}
		return numTerribleDeals + " " + numBargains;
	}

	private ExtendedGcdResult extendedGcd(long a, long b) {
		long p = a;
		long pa = 1;
		long pb = 0;
		long q = b;
		long qa = 0;
		long qb = 1;
		while (q > 0) {
			long d = p / q;
			p -= d * q;
			pa -= d * qa;
			pb -= d * qb;
			long tmp = p;
			p = q;
			q = tmp;
			tmp = pa;
			pa = qa;
			qa = tmp;
			tmp = pb;
			pb = qb;
			qb = tmp;
		}
		ExtendedGcdResult res = new ExtendedGcdResult();
		res.gcd = p;
		res.mulA = ((pa % (b / p)) + (b / p)) % (b / p);
		res.mulB = ((pb % (a / p)) + (a / p)) % (a / p);
		return res;
	}

	private long countOccurrences(int x, int y, SeqDescription xSeq, SeqDescription ySeq, long n, ExtendedGcdResult gcdRes) {
		long firstX = xSeq.firstPos[x];
		long firstY = ySeq.firstPos[y];
		if (firstX == INF || firstY == INF) {
			throw new RuntimeException();
		}
		boolean repeatX = firstX >= xSeq.preperiod.length;
		boolean repeatY = firstY >= ySeq.preperiod.length;
		if (repeatX) {
			if (repeatY) {
				long start = Math.max(firstX, firstY);
				long needX = (xSeq.period.length - (start - firstX) % xSeq.period.length) % xSeq.period.length;
				long needY = (ySeq.period.length - (start - firstY) % ySeq.period.length) % ySeq.period.length;
				if (needX % gcdRes.gcd != 0 || needY % gcdRes.gcd != 0) {
					throw new RuntimeException();
				}
				long offset;
				if (needX == 0) {
					long by = (gcdRes.mulA * (needY / gcdRes.gcd)) % (ySeq.period.length / gcdRes.gcd);
					offset = xSeq.period.length * by;
				} else if (needY == 0) {
					long by = (gcdRes.mulB * (needX / gcdRes.gcd)) % (xSeq.period.length / gcdRes.gcd);
					offset = ySeq.period.length * by;
				} else {
					throw new RuntimeException();
				}
				start += offset;
				if ((start - firstX) % xSeq.period.length != 0) {
					throw new RuntimeException();
				}
				if ((start - firstY) % ySeq.period.length != 0) {
					throw new RuntimeException();
				}
				if (start >= n) {
					throw new RuntimeException();
				}
				long each = xSeq.period.length * (long) ySeq.period.length / gcdRes.gcd;
				return (n - 1 - start) / each + 1;
			} else {
				long delta = firstY - firstX;
				if (delta >= 0 && delta % xSeq.period.length == 0) {
					return 1;
				} else {
					return 0;
				}
			}
		} else if (repeatY) {
			long delta = firstX - firstY;
			if (delta >= 0 && delta % ySeq.period.length == 0) {
				return 1;
			} else {
				return 0;
			}
		} else {
			if (firstX == firstY) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private void periodicSolution(int[] xp, int[] yp, long n, int[] minThisX, int[] maxThisX) {
		doOneDirection(xp, yp, n, minThisX, new Operation() {
			public int getNeutral() {
				return INF;
			}

			public int apply(int first, int second) {
				return Math.min(first, second);
			}
		});
		doOneDirection(xp, yp, n, maxThisX, new Operation() {
			public int getNeutral() {
				return -INF;
			}

			public int apply(int first, int second) {
				return Math.max(first, second);
			}
		});
	}

	static interface Operation {
		int getNeutral();
		int apply(int first, int second);
	}

	private void doOneDirection(int[] xp, int[] yp, long n, int[] opThisX, Operation op) {
		long averageNum = n / xp.length - 1;
		boolean[] mark = new boolean[yp.length];
		int[] seq = new int[2 * yp.length];
		int[] opAverage = new int[yp.length];
		if (averageNum == 0) {
			Arrays.fill(opAverage, op.getNeutral());
		} else {
			for (int i = 0; i < yp.length; ++i) {
				if (!mark[i]) {
					int seqPos = 0;
					int j = i;
					int globalRes = op.getNeutral();
					while (!mark[j]) {
						mark[j] = true;
						seq[seqPos++] = j;
						globalRes = op.apply(globalRes, yp[j]);
						j = (j + xp.length) % yp.length;
					}
					if (averageNum >= seqPos) {
						for (int k = 0; k < seqPos; ++k) {
							opAverage[seq[k]] = globalRes;
						}
					} else {
						System.arraycopy(seq, 0, seq, seqPos, seqPos);
						int[] resLeft = new int[2 * seqPos];
						int[] resRight = new int[2 * seqPos];
						for (int k = 0; k < resLeft.length; ++k) {
							resLeft[k] = yp[seq[k]];
							if (k % averageNum != 0) {
								resLeft[k] = op.apply(resLeft[k], resLeft[k - 1]);
							}
						}
						for (int k = resRight.length - 1; k >= 0; --k) {
							resRight[k] = yp[seq[k]];
							if (k + 1 < resRight.length && (k % averageNum != averageNum - 1)) {
								resRight[k] = op.apply(resRight[k], resRight[k + 1]);
							}
						}
						for (int k = 0; k < seqPos; ++k) {
							opAverage[seq[k]] = op.apply(resRight[k], resLeft[((int) (k + averageNum - 1))]);
						}
					}
				}
			}
		}
		for (int ix = 0; ix < xp.length; ++ix) {
			long cnt = (n - 1 - ix) / xp.length + 1;
			if (cnt <= 0) {
				throw new RuntimeException();
			}
			if (ix + (cnt - 1) * xp.length >= n) {
				throw new RuntimeException();
			}
			if (ix + cnt * xp.length < n) {
				throw new RuntimeException();
			}
			int pos = ix % yp.length;
			int res = op.getNeutral();
			if (cnt < averageNum) {
				throw new RuntimeException();
			}
			while (cnt > averageNum) {
				res = op.apply(res, yp[pos]);
				pos = (pos + xp.length) % yp.length;
				--cnt;
			}
			res = op.apply(res, opAverage[pos]);
			int x = xp[ix];
			opThisX[x] = op.apply(opThisX[x], res);
		}
	}

	private SeqDescription buildSeq(long n, long first, long a, long b, long m) {
		int[] seenAt = new int[(int) (m + 1)];
		Arrays.fill(seenAt, -1);
		seenAt[((int) first)] = 0;
		int[] seq = new int[(int) (m + 1)];
		int seqPos = 1;
		seq[0] = (int) first;
		SeqDescription res = new SeqDescription();
		for (int i = 1; i < n; ++i) {
			long nxt = (((long) seq[i - 1]) * a + b) % m + 1;
			if (seenAt[((int) nxt)] >= 0) {
				int offset = seenAt[((int) nxt)];
				res.preperiod = new int[offset];
				System.arraycopy(seq, 0, res.preperiod, 0, offset);
				res.period = new int[seqPos - offset];
				System.arraycopy(seq, offset, res.period, 0, seqPos - offset);
				res.periodItems = n - offset;
				return res;
			}
			seenAt[((int) nxt)] = seqPos;
			seq[seqPos] = (int) nxt;
			++seqPos;
		}
		if (seqPos != n) {
			throw new RuntimeException();
		}
		res.preperiod = new int[seqPos];
		System.arraycopy(seq, 0, res.preperiod, 0, seqPos);
		res.period = new int[]{-1};
		res.periodItems = 0;
		return res;
	}
}

class InputReader {
	private final BufferedReader reader;
	private StringTokenizer tokenizer;

	public InputReader(InputStream stream) {
		reader = new BufferedReader(new InputStreamReader(stream));
		tokenizer = null;
	}

	public String next() {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(reader.readLine());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return tokenizer.nextToken();
	}

	public int nextInt() {
		return Integer.parseInt(next());
	}

	public long nextLong() {
		return Long.parseLong(next());
	}

}

