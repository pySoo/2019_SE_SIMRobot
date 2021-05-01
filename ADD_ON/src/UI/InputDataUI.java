package UI;
/**
 * ����ڷκ��� �ʱ� ������ �Է¹޴� class
 */
import java.util.ArrayList;
import Map.*;
import SIM.SIM;
import Control.*;

public class InputDataUI {
	char[][] map;
	int mapSize;
	ArrayList<Point> hazardSet = new ArrayList<>();
	ArrayList<Point> blobSet = new ArrayList<>();
	Point startingPoint;
	ArrayList<Point> sequenceSet = new ArrayList<>();
	SIM sim = SIM.getInstance();

	/* ���� �Լ� */
	public static void main(String[] args) {
		instance.showFrame();
	}

	/* Singleton Pattern */
	private static InputDataUI instance = new InputDataUI();

	private InputDataUI() {
	}

	public static InputDataUI getInstance() {
		return instance;
	}

	void showFrame() {
		GUI gui = GUI.getInstance();
	}

	/* �糭���� �� �Է� */
	void inputMap(int size, String hazstr) {
		/* ���� ũ�� �Է� */
		mapSize = size;
		map = new char[mapSize][mapSize];
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				map[i][j] = '-'; // ������ ��� ĭ�� '-'�� �ʱ�ȭ
			}
		}

		/* �������� �Է� */
		String str3 = hazstr;
		String[] hazInput = str3.split(" ");
		int row3 = 0, col3 = 0;
		for (int i = 0; i < hazInput.length; i++) {
			String[] hazPoint = hazInput[i].split(",");
			row3 = Integer.parseInt(hazPoint[0]);
			col3 = Integer.parseInt(hazPoint[1]);
			Point hazard = new Point(row3, col3);
			hazardSet.add(hazard);
		}

		for (int i = 0; i < hazardSet.size(); i++) {
			Point hazard = hazardSet.get(i);
			int hazRow = hazard.getRow();
			int hazCol = hazard.getCol();
			map[hazRow][hazCol] = '!'; // �������� '!'�� ǥ��

		}
	}

	/* �κ��� ������� Ž�������� �Է� */
	void inputData(String startstr, String seqstr) {
		// ������� �Է�
		String str = startstr;
		String[] startInput = str.split(",");
		int row = Integer.parseInt(startInput[0]);
		int col = Integer.parseInt(startInput[1]);
		startingPoint = new Point(row, col);

		// Ž������ �Է�
		String str2 = seqstr;
		String[] explInput = str2.split(" ");
		int row2 = 0, col2 = 0;
		for (int i = 0; i < explInput.length; i++) {
			String[] explPoint = explInput[i].split(",");
			row2 = Integer.parseInt(explPoint[0]);
			col2 = Integer.parseInt(explPoint[1]);
			Point exploration = new Point(row2, col2);
			sequenceSet.add(exploration);
		}
		saveMap();
	}

	void saveMap() {
		Map m = Map.getInstance();
		SIM s = SIM.getInstance();
		PathManager pathManager = PathManager.getInstance();
		m.saveMap(map);
		
		m.getInitionialData(hazardSet, sequenceSet);
		s.getInitialData(startingPoint, mapSize, sequenceSet, hazardSet);
		pathManager.getInitialData(startingPoint, hazardSet, sequenceSet);
	}
	
	Point getStartingPoint() {
		return startingPoint;
	}

	int getMapSize() {
		return mapSize;
	}

	ArrayList<Point> getSequenceSet() {
		return sequenceSet;
	}
}
