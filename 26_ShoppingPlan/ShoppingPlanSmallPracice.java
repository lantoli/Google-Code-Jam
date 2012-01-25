package lantoli.codejam;
import static java.lang.Math.min;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class ShoppingPlanSmallPracice {

	public static void main(String[] args) throws FileNotFoundException {

		System.setIn(new FileInputStream("D-large-practice.in"));
		System.setOut(new PrintStream(new FileOutputStream("D-large-practice.out")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		for (long test=1; test<= tests; test++) {

			int itemNum = in.nextInt();
			int storeNum = in.nextInt();
			gasprice = in.nextInt(); in.nextLine();
			String[] itemNames = in.nextLine().split(" ");
			assert itemNum == itemNames.length;
			perishable = new boolean[itemNum];
			@SuppressWarnings("unchecked")
			List<Integer>[] itemsInStore = new ArrayList[itemNum];
			@SuppressWarnings("unchecked")
			List<Integer>[] pricesInStore = new ArrayList[itemNum];
			Map<String,Integer> mapItemNames = new HashMap<String,Integer>();
			for (int i=0; i<itemNum; i++) {
				if (itemNames[i].charAt(itemNames[i].length()-1) == '!') {
					perishable[i] = true;
					itemNames[i] = itemNames[i].substring(0,itemNames[i].length()-1);
				}
				mapItemNames.put(itemNames[i] , i);
				itemsInStore[i] = new ArrayList<Integer>();
				pricesInStore[i] = new ArrayList<Integer>();
			}
			// store 0 is home
			storeNum++;
			x = new int[storeNum];
			y = new int[storeNum];
			for (int i=1; i< storeNum; i++) {
				x[i] = in.nextInt();
				y[i] = in.nextInt();
				String[] tempItems = in.nextLine().trim().split(" ");
				for (int j=0; j<tempItems.length; j++) {
					String[] tempPair = tempItems[j].split(":");
					int itemPos = mapItemNames.get(tempPair[0]);
					itemsInStore[itemPos].add(i);
					pricesInStore[itemPos].add(Integer.parseInt(tempPair[1]));
				}
			}

			Stack<State> states = new Stack<State>();
			states.add(new State());
			double cost = Double.MAX_VALUE;
			while (!states.isEmpty()) {
				State state = states.pop();
				for (int item=0; item<itemNum; item++) {
					if (!state.items.contains(item)) {
						for (int i=0; i<itemsInStore[item].size(); i++) {
							State newState = State.newInstance(state, item, itemsInStore[item].get(i), pricesInStore[item].get(i));
							if (newState != null) {
								if (newState.items.size() == itemNum) {
									cost = min(cost, newState.cost());
								} else if (newState.cost() < cost) {
									states.push(newState);
								}
							}
						}
					}
				}

			}
			System.out.println(String.format("Case #%d: %.07f", test, cost));
		}
	}

	static int[] x;
	static int[] y;
	static int gasprice;
	static boolean[] perishable;

	static class State {
		Set<Integer> items;
		Set<Integer> storesReturn;
		Set<Integer> storesDont;
		double distance;
		int prices;
		int lastStore;

		@Override
		public String toString() {
			return "State [items=" + items + ", storesReturn=" + storesReturn + ", storesDont=" + storesDont
					+ ", distance=" + distance + ", prices=" + prices
					+ ", lastStore=" + lastStore + ", cost: " + cost() + "]";
		}

		State() {
			items = new TreeSet<Integer>();
			storesReturn = new TreeSet<Integer>();
			storesDont = new TreeSet<Integer>();
		}

		private State(State prev) {
			items = new TreeSet<Integer>(prev.items);
			storesReturn = new TreeSet<Integer>(prev.storesReturn);
			storesDont = new TreeSet<Integer>(prev.storesDont);
			distance = prev.distance;
			prices = prev.prices;
			lastStore = prev.lastStore;
		}

		public static State newInstance(State prev, int newItem, int newStore, int newPrice) {
			State state = new State(prev);
			state.items.add(newItem);
			state.prices += newPrice;

			if (perishable[newItem]) {
				if (prev.storesDont.contains(newStore)) {
					return null;
				} else if (!prev.storesReturn.contains(newStore)) {
					state.storesReturn.add(newStore);
					state.distance += distance(prev.lastStore, newStore) + distance(newStore,0);
					state.lastStore = 0;
				}

			} else { // not perishable
				if (!prev.storesDont.contains(newStore) && !prev.storesReturn.contains(newStore)) {
					state.storesDont.add(newStore);
					state.distance += distance(prev.lastStore, newStore);
					state.lastStore = newStore;
				}
			}
			return state;
		}

		public double cost() {
			return (distance + distance(lastStore,0)) * gasprice + prices;
		}

		private static double distance(int store1, int store2) {
			return Math.sqrt((x[store1]-x[store2])*(x[store1]-x[store2]) + (y[store1]-y[store2])*(y[store1]-y[store2]));
		}
	}

}
