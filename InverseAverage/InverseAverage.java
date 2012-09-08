package kata;

import java.util.Map;
import java.util.TreeMap;

public class InverseAverage {

	public int[] getInverse(double avg) {
		double diff = 0;
		while (true) {
			Info info1 = avgInHundrethsToInfo.get((int) Math.round((avg + diff) * 100));
			Info info2 = avgInHundrethsToInfo.get((int) Math.round((avg - diff) * 100));
			if (info1 != null && info2 != null) {
				return info1.count <= info2.count ? info1.getArrayExample() : info2.getArrayExample();
			} else if (info1 != null) {
				return info1.getArrayExample();
			} else if (info2 != null) {
				return info2.getArrayExample();
			}
			diff += .01;
		}
	}

	// Cache elements on creation
	public InverseAverage() {
		for (int count = 10; count >= 1; count--) {
			for (int sum = count; sum <= 10 * count; sum++) {
				Info info = new Info(count, sum);
				avgInHundrethsToInfo.put(info.avgInHundreths, info);
			}
		}
	}

	// it'll just have 289 elements
	private Map<Integer, Info> avgInHundrethsToInfo = new TreeMap<>();

	public static class Info implements Comparable<Info> {
		public int count, sum, avgInHundreths, test;

		public Info(int count, int sum) {
			this.count = count;
			this.sum = sum;
			double avg = (double) sum / count;
			avgInHundreths = (int) Math.round(avg * 100);
		}

		public int[] getArrayExample() {
			int[] ret = new int[count];
			int acc = 0;
			for (int i = 0; i < count - 1; i++) {
				int inc = (sum - acc > 10 + (count - 1 - i)) ? 10 : 1;
				ret[i] += inc;
				acc += inc;
			}
			ret[count - 1] = sum - acc;
			return ret;
		}

		@Override
		public int compareTo(Info o) {
			return avgInHundreths - o.avgInHundreths;
		}
	}

}
