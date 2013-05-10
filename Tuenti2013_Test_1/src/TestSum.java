import java.util.Scanner;

public class TestSum {

	public static void main(String[] args) {
		try (Scanner in = new Scanner(System.in)) {
			while (in.hasNextLine()) {
				long total = 0;
				for (String num : in.nextLine().split(" ")) {
					try {
						total += Long.parseLong(num);
					} catch (Exception e) {
					}
				}
				System.out.println(total);
			}
		}
	}

}