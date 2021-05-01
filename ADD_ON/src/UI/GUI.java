package UI;

/**
 * 지도를 GUI를 통해 화면에 출력하는 class
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import SIM.SIM;
import Map.*;

/* 초기 정보 입력 프레임 */
class GUI extends JFrame {
	private static GUI instance = new GUI();

	public static GUI getInstance() {
		return instance;
	}
	
	public void paintComponent(Graphics g) {
		Image image = new ImageIcon(getClass().getResource("/images/grass.png")).getImage();
		int x = (getWidth() - image.getWidth(null)) / 2;
	    int y = (getHeight() - image.getHeight(null)) / 2;
	    g.drawImage(image, x, y, null);
	}

	Container c = getContentPane();

	private GUI() {
		setTitle("ADD-ON");
		setSize(550, 400);
		Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dimen1 = getSize();

		int xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2);
		int ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2);
		setLocation(xpos, ypos);

		c.setLayout(null);
		c.setBackground(Color.WHITE);
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel sizeLb = new JLabel("지도 크기");
		JLabel startingLb = new JLabel("시작 지점");
		JLabel hazardLb = new JLabel("위험 지점");
		JLabel sequenceLb = new JLabel("탐색 지점");
		JLabel info = new JLabel("");

		JTextField sizeFd = new JTextField();
		JTextField startingFd = new JTextField();
		JTextField hazardFd = new JTextField();
		JTextField sequenceFd = new JTextField();
		JButton inputButton = new JButton("확인");

		sizeLb.setBounds(100, 50, 100, 35);
		setFont(sizeLb);

		sizeFd.setBounds(200, 50, 180, 40);
		setFont(sizeFd);

		startingLb.setBounds(100, 100, 100, 35);
		setFont(startingLb);

		startingFd.setBounds(200, 100, 180, 40);
		setFont(startingFd);

		hazardLb.setBounds(100, 150, 100, 35);
		setFont(hazardLb);

		hazardFd.setBounds(200, 150, 180, 40);
		setFont(hazardFd);

		sequenceLb.setBounds(100, 200, 100, 35);
		setFont(sequenceLb);

		sequenceFd.setBounds(200, 200, 180, 40);
		setFont(sequenceFd);

		inputButton.setSize(80, 30);
		inputButton.setLocation(430, 300);
		c.add(inputButton);

		setVisible(true);

		inputButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				InputDataUI idf = InputDataUI.getInstance();

				int size;
				String startstr, seqstr, hazstr;

				size = Integer.parseInt(sizeFd.getText());
				startstr = startingFd.getText();
				seqstr = sequenceFd.getText();
				hazstr = hazardFd.getText();

				idf.inputMap(size, hazstr);
				idf.inputData(startstr, seqstr);
				dispose();
			}
		});

	}
	private void setFont(JLabel label) {
		label.setFont(new Font("함초롬돋움", Font.BOLD, 20));
		c.add(label);
	}
	
	private void setFont(JTextField field) {
		field.setFont(new Font("함초롬돋움", Font.BOLD, 20));
		c.add(field);
	}

}

/* 지도 출력 패널 */
class MapPanel extends JPanel {

	public MapPanel() {
		setLayout(null);
	}
	
	public void paint(Graphics g) {
		Map map = Map.getInstance();
		SIM sim = SIM.getInstance();

		Graphics2D g2 = (Graphics2D) g;
		int mapSize = map.getMapSize();
		int size = 80;

		// 지도 그리기
		for (int i = 0; i < mapSize; i++) {
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.WHITE);
			g2.drawLine(size, size + size * i, size * mapSize, size + size * i);
			g2.drawLine(size + size * i, size, size + size * i, size * mapSize);
		}

		Image u_robot = getImage("/images/u_robot.png");
		Image d_robot = getImage("/images/d_robot.png");
		Image l_robot = getImage("/images/l_robot.png");
		Image r_robot = getImage("/images/r_robot.png");
		Image hazard = getImage("/images/danger.png");
		Image sequence = getImage("/images/yellowFlag.png");
		Image visitSq = getImage("/images/redFlag.png");
		Image blob = getImage("/images/blob.png");
		Image blurblob = getImage("/images/blurblob.png");
		Image up = getImage("/images/up.png");
		Image down = getImage("/images/down.png");
		Image left = getImage("/images/left.png");
		Image right = getImage("/images/right.png");

		// 위험 지역 위치
		for (int i = 0; i < map.hazardSet.size(); i++) {
			int hazRow = map.hazardSet.get(i).getRow();
			int hazCol = map.hazardSet.get(i).getCol();
			g.drawImage(hazard, hazCol * size + size - 35, hazRow * size + size - 35, 70, 70, null);
		}

		// random 생성된 중요 지점 위치
		for (int i = 0; i < map.hiddenBlob.size(); i++) {
			int blobRow = map.hiddenBlob.get(i).getRow();
			int blobCol = map.hiddenBlob.get(i).getCol();
			g.drawImage(blurblob, size + blobCol * size - 35, blobRow * size + size - 35, 70, 70, null);
		}

		// colorblob 찾았을 때
		try {
			for (int i = 0; i < map.detectedBlob.size(); i++) {
				int dblobRow = map.detectedBlob.get(i).getRow();
				int dblobCol = map.detectedBlob.get(i).getCol();
				g.drawImage(blob, size + dblobCol * size - 35, dblobRow * size + size - 35, 70, 70, null);
			}
		} catch (NullPointerException e) {

		}

		// 탐색 지점 위치
		for (int i = 0; i < map.sequenceSet.size(); i++) {
			int sqRow = map.sequenceSet.get(i).getRow();
			int sqCol = map.sequenceSet.get(i).getCol();
			g.drawImage(sequence, sqCol * size + size - 5, sqRow * size + size - 55, 70, 70, null);
		}
		
		// 탐색 끝난 위치
		try {
			for (int i = 0; i < map.visitSqSet.size(); i++) {
				int visitRow = map.visitSqSet.get(i).getRow();
				int visitCol = map.visitSqSet.get(i).getCol();
				g.drawImage(visitSq, visitCol * size + size - 15, visitRow * size + size - 65, 80, 80, null);
			}
		} catch (NullPointerException e) {

		}

		// 현재 로봇 위치
		int row = map.path.get(0).getRow();
		int col = map.path.get(0).getCol();
		int direction = sim.direction;
		switch (direction) {
		case 0:
			g.drawImage(u_robot, col * size + size - 35, row * size + size - 30, 80, 90, null);
			break;
		case 1:
			g.drawImage(d_robot, col * size + size - 35, row * size + size - 30, 80, 90, null);
			break;
		case 2:
			g.drawImage(l_robot, col * size + size - 35, row * size + size - 30, 90, 80, null);
			break;
		case 3:
			g.drawImage(r_robot, col * size + size - 35, row * size + size - 30, 90, 80, null);
			break;
		}

		// path를 나타내는 화살표
		try {
			for (int i = 0; i < map.path.size() - 1; i++) {
				int currow = map.path.get(i).getRow();
				int curcol = map.path.get(i).getCol();

				int nextrow = map.path.get(i + 1).getRow();
				int nextcol = map.path.get(i + 1).getCol();

				if (nextrow == currow && nextcol == curcol + 1) {
					// 오른
					g.drawImage(right, size + (curcol + 1) * size - (size / 2 + 15),
							size + (currow) * size - 40, 30, 30, null);
				} else if (nextrow == currow && nextcol == curcol - 1) {
					// 왼
					g.drawImage(left, size + (curcol) * size - (size / 2 + 10), size + (currow) * size - 40,
							30, 30, null);

				} else if (nextrow == currow + 1 && nextcol == curcol) {
					// 아래
					g.drawImage(down, size * (curcol + 1) + 15, size * (currow + 2) - 60, 30, 30, null);
				} else if (nextrow == currow - 1 && nextcol == curcol) {
					// 위
					g.drawImage(up, size * (curcol + 1) + 10, size * (currow + 1) - 60, 30, 30, null);
				} else {
					continue;
				}
			}
		} catch (NullPointerException e) {

		}
	}
	
	private Image getImage(String path) {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
		return imageIcon.getImage();
	}

}