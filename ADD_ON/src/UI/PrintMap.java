package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/* 지도 출력 프레임 */
public class PrintMap extends JFrame {
	private static PrintMap instance = new PrintMap();

	public static PrintMap getInstance() {
		return instance;
	}

	Image img_buffer;
	Graphics dbg;
	MapPanel mappl = new MapPanel();

	public void paint(Graphics g) {
		img_buffer = createImage(1500, 900);
		dbg = img_buffer.getGraphics();
		paintComponent(dbg);
		g.drawImage(img_buffer, 0, 0, this);
	}

	public void paintComponent(Graphics g) {
		Image image = new ImageIcon(getClass().getResource("/images/grass.png")).getImage();
		int x = (getWidth() - image.getWidth(null)) / 2;
	    int y = (getHeight() - image.getHeight(null)) / 2;
	    g.drawImage(image, x, y, null);
		mappl.revalidate();
		mappl.update(g);
	}

	private PrintMap() {
		setTitle("ADD-ON");
		setSize(700, 700);
		Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dimen1 = getSize();

		int xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2);
		int ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2);
		setLocation(xpos, ypos);
		setBackground(Color.WHITE);

		getContentPane().add(mappl);

		setVisible(true);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	public void updateMap() {
		paint(getGraphics());
	}
}