import static java.lang.Math.min;
import static java.util.Arrays.deepToString;

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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadShoppingPlan  {

	final static String FILENAME = "shoppingplan";
	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static int THREADS = 10; // use 1 to solve them sequentially

	// VM arguments: -Xms2048M -Xmx2048M

	static class Solver implements Callable<String> {

		// PROBLEM SOLUTION STARTS HERE -----------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		Solver(Scanner in, int testId) {
			this.testId = testId;

			// TODO: Read input

			itemNum = in.nextInt();
			storeNum = in.nextInt();
			gasprice = in.nextInt(); in.nextLine();
			String[] itemNames = in.nextLine().split(" ");
			assert itemNum == itemNames.length;
			perishable = new boolean[itemNum];
			itemsInStore = new ArrayList[itemNum];
			pricesInStore = new ArrayList[itemNum];
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
		}


		// TODO: Define input variables




		@Override
		public String call() throws Exception {
			long now = System.nanoTime();

			// TODO: Solve problem here and return result as a string

			Map<State,State> statesMap = new HashMap<State,State>();
			Stack<State> states = new Stack<State>();
			states.add(new State());
			double cost = Double.MAX_VALUE;
			while (!states.empty()) {
				State state = states.pop();
				state.removed = true;

				if (state.cost >= cost) {
					continue;
				}
				for (int item=0; item<itemNum; item++) {
					if ((state.items & (1 << item)) == 0) {
						for (int i=0; i<itemsInStore[item].size(); i++) {
							State newState = new State(state, item, itemsInStore[item].get(i), pricesInStore[item].get(i));
							if (!newState.invalid) {
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

			String res = String.format("Case #%d: %.07f", testId, cost);

			System.err.println(String.format("%4d ms %s" , (System.nanoTime() - now) / 1000000, res));
			return res;
		}


		int itemNum, storeNum;
		List<Integer>[] itemsInStore;
		List<Integer>[] pricesInStore;
		int gasprice;
		boolean[] perishable;
		double[][] dist;

		class State {
			int itemNum;
			long items;
			long storesReturn;
			long storesDont;
			double distance;
			int prices;
			int lastStore;
			double cost;
			boolean removed;
			boolean invalid;

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

			State (State prev, int newItem, int newStore, int newPrice) {
				this(prev);
				items |= 1 << newItem;
				itemNum++;
				prices += newPrice;

				if (perishable[newItem]) {
					if ((prev.storesDont & (1 << newStore)) != 0) {
						invalid = true;
						return;
					} else if ((prev.storesReturn & (1 << newStore)) == 0) {
						storesReturn |= 1 << newStore;
						distance += dist[prev.lastStore][newStore] + dist[newStore][0];
						lastStore = 0;
					}

				} else { // not perishable
					if (((prev.storesDont | prev.storesReturn) & (1 << newStore)) == 0) {
						storesDont |= 1 << newStore;
						distance += dist[prev.lastStore][newStore];
						lastStore = newStore;
					}
				}
				cost = (distance + dist[lastStore][0]) * gasprice + prices;
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

		// PROBLEM SOLUTION ENDS HERE -------------------------------------------------------------
		// ----------------------------------------------------------------------------------------

		int testId;
	}


	static void debug(Object...os) {
		System.err.println(deepToString(os));
	}
	;

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		long now = System.nanoTime();
		System.setIn(new FileInputStream(FILENAME_IN));
		System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		Scanner in = new Scanner(System.in);
		int numTests = in.nextInt(); in.nextLine();
		System.err.println(String.format("TESTS: %d" , numTests));
		List<Solver> solvers = new ArrayList<Solver>();
		for (int i=0; i<numTests; i++) {
			solvers.add(new Solver(in, i+1));
		}
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);
		List<Future<String>> solutions = executor.invokeAll(solvers);
		for (int i = 0; i < numTests; i++) {
			try {
				System.out.println(solutions.get(i).get());
			} catch (Exception e) {
				System.out.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				System.err.println(String.format("Case #%d: EXCEPTION !!!!!", solvers.get(i).testId));
				e.printStackTrace(System.err);
			}
		}
		executor.shutdown();
		System.err.println(String.format("TOTAL: %d ms" , (System.nanoTime() - now) / 1000000));
	}


}