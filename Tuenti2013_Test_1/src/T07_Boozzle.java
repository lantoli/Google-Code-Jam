import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class T07_Boozzle {

	// VM arguments for hard problems: -ea -Xms4096M -Xmx4096M -Xss1024m

	// Guava library must be added to classpath, current ver. 14.0.1

	// Set FILENAME = null for standard input/output
	final static String FILENAME = null;
	// final static String FILENAME = "sample_boozle2";

	final static String FILENAME_IN = FILENAME + ".in";
	final static String FILENAME_OUT = FILENAME + ".out";

	final static String dictName = "boozzle-dict.txt";

	final static int WORDSECONDS_MIN = 2 + 1; // min word size in this dictionay + 1 sec submit time
	final static int WORDSECONDS_MAX = 15 + 1; // max word size in this dictionay + 1 sec submit time
	final static int WORDSECONDS_ARRAY = WORDSECONDS_MAX - WORDSECONDS_MIN + 1;

	final static Set<String> dictSet = new HashSet<>();
	final static List<String> dictList = new ArrayList<>();
	final static CharNode dictNode = new CharNode((char) 0);

	final static int NUM_LETTERS = 'Z' - 'A' + 1;

	static int W, n, m;

	static int scores[];

	static byte[] board; // letters, A is 0, B is 1, ...
	static byte[] boardCharMod;
	static byte[] boardWordMod;

	static int toXY(int x, int y) {
		return x * n + y;
	}

	static int toX(int xy) {
		return xy / n;
	}

	static int toY(int xy) {
		return xy % n;
	}

	// Read dictionary and create associated data structures, just once for all problems
	static {
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(dictName), charset)) {
			String word = null;
			while ((word = reader.readLine()) != null) {
				dictList.add(word);
				dictSet.add(word);
				dictNodeAdd(word);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static class CharNode {

		char ch;
		List<CharNode> children;

		public CharNode(char ch) {
			this.ch = ch;
		}

		public CharNode getChild(char ch) {
			if (children == null)
				return null;
			for (CharNode child : children) {
				if (child.ch == ch)
					return child;
			}
			return null;
		}

		public CharNode getOrCreateChild(char ch) {
			CharNode child = getChild(ch);
			if (child == null) {
				if (children == null) {
					children = new ArrayList<>();
				}
				child = new CharNode(ch);
				children.add(child);
			}
			return child;
		}
	};

	static void dictNodeAdd(String word) {
		CharNode cur = dictNode;
		for (char ch : word.toCharArray()) {
			cur = cur.getOrCreateChild(ch);
		}
	}

	// Returns true if some words in the dictionary start with this word, so we could eventually get those words from
	// this
	static boolean wordHasChances(String word) {
		CharNode cur = dictNode;
		int index = 0;

		while (cur != null && index < word.length()) {
			cur = cur.getChild(word.charAt(index));
			index++;
		}
		return cur != null;
	}

	public static void main(String[] args) throws Exception {
		if (FILENAME != null) {
			System.setIn(new FileInputStream(FILENAME_IN));
			System.setOut(new PrintStream(new FileOutputStream(FILENAME_OUT)));
		}

		try (Scanner in = new Scanner(System.in)) {

			in.useDelimiter("[\\s\\{\\}\\'\\:,]+");

			int tests = in.nextInt();
			in.nextLine();
			for (int test = 0; test < tests; test++) {

				scores = new int[NUM_LETTERS];
				for (int i = 0; i < NUM_LETTERS; i++) {
					String letter = in.next();
					assert letter.length() == 1;
					char ch = letter.charAt(0);
					assert ch >= 'A' && ch <= 'Z';
					scores[ch - 'A'] = in.nextInt();
				}

				W = in.nextInt();
				n = in.nextInt();
				m = in.nextInt();

				board = new byte[n * m];
				boardCharMod = new byte[n * m];
				boardWordMod = new byte[n * m];

				for (int y = 0; y < n; y++) {
					for (int x = 0; x < m; x++) {
						int xy = toXY(x, y);
						String cell = in.next();
						assert cell.length() == 3;
						board[xy] = (byte) (cell.charAt(0) - 'A');
						byte mod = (byte) (cell.charAt(2) - '0');
						if (cell.charAt(1) == '1') { // CM
							boardCharMod[xy] = mod;
							boardWordMod[xy] = 1;
						} else { // WM
							boardCharMod[xy] = 1;
							boardWordMod[xy] = mod;
						}
					}
				}

				int res = processBoozzle();

				System.out.println(Integer.toString(res));
			}
		}
	}

	static class WordState {

		private final int xy;
		private final String word;

		// We could use an ArrayList but this is more correct because it won't change
		private final ImmutableList<Integer> visited;

		public WordState(int xy) {
			this(xy, null);
		}

		public WordState(int xy, WordState parent) {
			this.xy = xy;

			Builder<Integer> builder = ImmutableList.<Integer> builder();
			String wordParent;
			if (parent == null) {
				wordParent = "";
			} else {
				builder.addAll(parent.visited);
				wordParent = parent.word;
			}
			this.word = wordParent + (char) (board[xy] + 'A');
			this.visited = builder.add(xy).build();
		}

		public List<WordState> returnsNextStates() {
			List<WordState> ret = new ArrayList<>();
			int xold = toX(xy);
			int yold = toY(xy);
			for (int x = xold - 1; x <= xold + 1; x++) {
				for (int y = yold - 1; y <= yold + 1; y++)
					if (x >= 0 && y >= 0 && x < m && y < n) {
						int newxy = toXY(x, y);
						if (!visited.contains(newxy)) {
							WordState newState = new WordState(newxy, this);
							if (wordHasChances(newState.word)) {
								ret.add(newState);
							}
						}
					}

			}
			return ret;
		}

		public int getScore() {
			int score = 0;
			int maxWM = 0;
			for (int xy : visited) {
				score += scores[board[xy]] * boardCharMod[xy];
				maxWM = Math.max(maxWM, boardWordMod[xy]);
			}
			return score * maxWM + visited.size();
		}

	}

	static Map<String, Integer> wordScoreMap;

	static List<WordInfo> wordInfo;

	static class WordInfo {
		int score, secs;
		double roi; // "roi" (return on investment) as the quality of the word, it's the score per second

		public WordInfo(int score, int secs) {
			this.score = score;
			this.secs = secs;
			roi = (double) score / secs;
		}
	}

	private static void initWordScore() {
		wordScoreMap = new HashMap<>();
		Queue<WordState> queue = new LinkedList<>();
		for (int xy = 0; xy < n * m; xy++) {
			WordState ini = new WordState(xy);
			queue.add(ini);
		}
		WordState state;
		while ((state = queue.poll()) != null) {
			String word = state.word;
			if (dictSet.contains(word)) {

				Integer oldScore = wordScoreMap.get(word);
				int score = state.getScore();
				if (oldScore == null || score > oldScore) {
					wordScoreMap.put(word, state.getScore());
				}
			}

			for (WordState next : state.returnsNextStates()) {
				queue.add(next);
			}
		}

		wordInfo = new ArrayList<>();
		for (Entry<String, Integer> entry : wordScoreMap.entrySet()) {
			wordInfo.add(new WordInfo(entry.getValue(), entry.getKey().length() + 1));
		}
		Collections.sort(wordInfo, comparatorByRoi);

	}

	final static Comparator<WordInfo> comparatorByRoi = new Comparator<WordInfo>() {
		@Override
		public int compare(WordInfo o1, WordInfo o2) {
			return Double.compare(o2.roi, o1.roi);
		}
	};

	private static int processBoozzle() {
		initWordScore();
		int maxScore = 0;

		// Get the result following best roi. Try several possibilities in case the secs are not exactly W
		for (int i = 0; i <= wordInfo.size(); i++) {
			int score = 0;
			int secs = 0;
			List<WordInfo> list = new ArrayList<>(wordInfo);
			if (i < wordInfo.size()) {
				list.remove(i);
			}
			for (WordInfo info : list) {
				if (info.secs + secs <= W) {
					score += info.score;
					secs += info.secs;
				}
			}
			maxScore = Math.max(maxScore, score);
		}
		return maxScore;
	}
}