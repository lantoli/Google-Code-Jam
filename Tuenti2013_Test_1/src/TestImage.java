import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TestImage {

	public static void main(String[] args) throws Exception {

		boolean[][] board = new boolean[][] { { true, false, false, true }, { false, false, false, false },
				{ true, true, true, true }, { false, false, false, false } };

		paintArray(board, "prueba");
	}

	final private static int DOTSIZE = 10;

	private static void paintArray(boolean[][] board, String pngFilename) throws IOException {
		int len = board.length;

		BufferedImage img = new BufferedImage(len * DOTSIZE, len * DOTSIZE, BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g2 = img.createGraphics();

		for (int y = 0; y < len; y++) {
			for (int x = 0; x < len; x++)
				if (board[y][x]) {
					g2.fillRect(x * DOTSIZE, y * DOTSIZE, (x + 1) * DOTSIZE, (y + 1) * DOTSIZE);
				}
		}
		File outputfile = new File(pngFilename + ".png");
		ImageIO.write(img, "png", outputfile);
	}

}
