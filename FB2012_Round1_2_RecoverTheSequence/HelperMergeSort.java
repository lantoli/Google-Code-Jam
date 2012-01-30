import static java.util.Arrays.deepToString;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class HelperMergeSort {

	public static void main(String[] args) {

		//int[] elms = {2, 4, 3, 1};

		List<Element> list = Element.newList(10000);

		List<Element> sorted = merge_sort(list);

		System.err.println("SECUENCIA: " + seq.toString());

		System.err.println("ORIGINAL: ");
		//	debug(list);
		System.err.println("SORTED: ");
		//	debug(sorted);

	}

	static StringBuilder seq = new StringBuilder();

	static class Element {
		int value;
		private Element(int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "[value=" + value + "]";
		}

		static List<Element> newList(int[] elms) {
			List<Element> ret = new ArrayList<Element>();
			for (int e : elms) {
				ret.add(new Element(e));
			}
			return ret;
		}

		static List<Element> newList(int n) {
			List<Element> ret = new ArrayList<Element>();
			for (int i=0; i<n; i++) {
				ret.add(new Element(n-i));
			}
			return ret;
		}

	}

	static List<Element> merge_sort (List<Element> arr) {
		int	n = arr.size();
		if (n <= 1) {
			return arr;
		}
		int mid = n/2;
		List<Element> first_half = merge_sort(arr.subList(0, mid));
		List<Element> second_half = merge_sort(arr.subList(mid, n));
		return merge(first_half, second_half);
	}

	static List<Element> merge(List<Element> arr1, List<Element> arr2) {
		List<Element> result = new ArrayList<Element>();
		Queue<Element> q1 = new LinkedList<Element> (arr1);
		Queue<Element> q2 = new LinkedList<Element> (arr2);
		while (!q1.isEmpty() && !q2.isEmpty()) {
			if (q1.element().value < q2.element().value) {
				seq.append('1');
				result.add(q1.remove());
			} else {
				seq.append('2');
				result.add(q2.remove());
			}
		}
		result.addAll(q1);
		result.addAll(q2);
		return result;
	}

	static void debug(Object...os) {
		System.err.println(deepToString(os));
	}

}


