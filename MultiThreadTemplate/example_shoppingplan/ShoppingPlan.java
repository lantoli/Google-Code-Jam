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
import java.util.Stack;

public class ShoppingPlan {


	public static void main(String[] args) throws FileNotFoundException {

		long now = System.nanoTime();
		System.setIn(new FileInputStream("shoppingplan.in"));
		System.setOut(new PrintStream(new FileOutputStream("shoppingplan.out_singlethread")));

		Scanner in = new Scanner(System.in);
		int tests = in.nextInt(); in.nextLine();
		for (long test=1; test<= tests; test++) {

			itemNum = in.nextInt();
			storeNum = in.nextInt();
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
			int[] x = new int[storeNum];
			int[] y = new int[storeNum];
			dist = new double[storeNum][storeNum];


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
				for (int j=0; j<i; j++) {
					dist[i][j] = dist[j][i] = Math.sqrt((x[i]-x[j])*(x[i]-x[j]) + (y[i]-y[j])*(y[i]-y[j]));
				}
			}

			Map<State,State> statesMap = new HashMap<State,State>();
			Stack<State> states = new Stack<State>();
			states.add(new State());
			double cost = Double.MAX_VALUE;
			long stackpops = 0;
			while (!states.empty()) {
				stackpops++;
				State state = states.pop();
				state.removed = true;

				if (state.cost >= cost) {
					continue;
				}
				for (int item=0; item<itemNum; item++) {
					if ((state.items & (1 << item)) == 0) {
						for (int i=0; i<itemsInStore[item].size(); i++) {
							State newState = State.newInstance(state, item, itemsInStore[item].get(i), pricesInStore[item].get(i));
							if (newState != null) {
								if (newState.itemNum == itemNum) {
									cost = min(cost, newState.cost);
								} else if (newState.cost < cost) {
									State s = statesMap.get(newState);
									if (s == null) {
										states.add(newState);
										statesMap.put(newState,newState);
									} else {
										if (s.removed) {
											if (newState.cost < s.cost) {
												states.add(newState);
												statesMap.put(newState,newState);
											}
										} else {
											s.setMinCost(newState);
										}
									}
								}
							}
						}
					}
				}

			}
			System.out.println(String.format("Case #%d: %.07f", test, cost));
			System.err.println(String.format("Case #%d: %.07f", test, cost));
		}
		System.err.println(String.format("TOTAL: %d ms" , (System.nanoTime() - now) / 1000000));
	}

	static int itemNum, storeNum;
	static int gasprice;
	static boolean[] perishable;
	static double[][] dist;

	static class State {
		int itemNum;
		long items;
		long storesReturn;
		long storesDont;
		double distance;
		int prices;
		int lastStore;
		double cost;
		boolean removed;

		@Override
		public String toString() {
			return "State [items=" + items + ", storesReturn=" + storesReturn + ", storesDont=" + storesDont
					+ ", distance=" + distance + ", prices=" + prices
					+ ", lastStore=" + lastStore + ", cost: " + cost + "]";
		}

		State() {
		}

		private State(State prev) {
			storesReturn = prev.storesReturn;
			storesDont = prev.storesDont;
			distance = prev.distance;
			prices = prev.prices;
			lastStore = prev.lastStore;
			itemNum = prev.itemNum;
			items = prev.items;
		}

		public static State newInstance(State prev, int newItem, int newStore, int newPrice) {
			State state = new State(prev);

			state.items |= 1 << newItem;
			state.itemNum++;
			state.prices += newPrice;

			if (perishable[newItem]) {
				if ((prev.storesDont & (1 << newStore)) != 0) {
					return null;
				} else if ((prev.storesReturn & (1 << newStore)) == 0) {
					state.storesReturn |= 1 << newStore;
					state.distance += dist[prev.lastStore][newStore] + dist[newStore][0];
					state.lastStore = 0;
				}

			} else { // not perishable
				if (((prev.storesDont | prev.storesReturn) & (1 << newStore)) == 0) {
					state.storesDont |= 1 << newStore;
					state.distance += dist[prev.lastStore][newStore];
					state.lastStore = newStore;
				}
			}
			state.cost = (state.distance + dist[state.lastStore][0]) * gasprice + state.prices;
			return state;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (items ^ (items >>> 32));
			result = prime * result + lastStore;
			result = prime * result + (int) (storesDont ^ (storesDont >>> 32));
			result = prime * result
					+ (int) (storesReturn ^ (storesReturn >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			State a = (State) obj;
			return items == a.items && storesReturn == a.storesReturn && storesDont == a.storesDont
					&& lastStore == a.lastStore;
		}

		public void setMinCost(State a) {
			if (a.cost < cost) {
				distance = a.distance;
				prices = a.prices;
				cost = a.cost;
			}
		}
	}

}
