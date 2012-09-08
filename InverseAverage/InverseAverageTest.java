package kata;

import static org.junit.Assert.assertArrayEquals;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Ignore;
import org.junit.Test;

public class InverseAverageTest {

	InverseAverage service = new InverseAverage();

	@Test
	public void testKataList() throws Exception {
		assertInverseAverage(4., 4);
		assertInverseAverage(5.5, 1, 10);
		assertInverseAverage(4.25, 10, 1, 1, 5);
		assertInverseAverage(9.88, 10, 10, 10, 10, 10, 10, 10, 9);
		assertInverseAverage(3.75, 10, 1, 1, 3);
		assertInverseAverage(3.73, 10, 1, 1, 3);
	}

	@Test
	public void testNumberArrays() throws Exception {
		assertArrays(new InverseAverage.Info(1, 7).getArrayExample(), 7);
		assertArrays(new InverseAverage.Info(2, 13).getArrayExample(), 10, 3);
		assertArrays(new InverseAverage.Info(2, 10).getArrayExample(), 1, 9);
		assertArrays(new InverseAverage.Info(2, 11).getArrayExample(), 1, 10);
		assertArrays(new InverseAverage.Info(3, 11).getArrayExample(), 1, 1, 9);
		assertArrays(new InverseAverage.Info(4, 11).getArrayExample(), 1, 1, 1, 8);
		assertArrays(new InverseAverage.Info(4, 40).getArrayExample(), 10, 10, 10, 10);
	}

	private void assertArrays(int[] result, int... expected) throws Exception {
		assertArrayEquals(expected, result);
	}

	private void assertInverseAverage(double avg, int... expected) throws Exception {
		assertArrayEquals(expected, service.getInverse(avg));
	}

	public static class Info implements Comparable<Info> {
		public int count, sum, avgInHundreths, test;

		public Info(int count, int sum, int test) {
			this.count = count;
			this.sum = sum;
			this.test = test;
			double avg = (double) sum / count;
			avgInHundreths = (int) Math.round(avg * 100);
		}

		@Override
		public int compareTo(Info o) {
			return avgInHundreths - o.avgInHundreths;
		}
	}

	@Test
	@Ignore
	public void generateFile() throws Exception {
		int n = 0;
		SortedSet<Info> set = new TreeSet<Info>();
		for (int count = 10; count >= 1; count--) {
			for (int sum = count; sum <= 10 * count; sum++) {
				set.add(new Info(count, sum, ++n));
			}
		}
		for (Info info : set) {
			System.out.println(String.format("%d\t%d\t%d\t%d", info.test, info.avgInHundreths, info.count, info.sum));

		}
	}
}
