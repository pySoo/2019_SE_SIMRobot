package SIM;
/**
 * SIM�� ������ Ŭ����
 */
import java.util.*;
import Map.*;
import Map.Map;

public class SIM {

	char[][] grid; //�κ� ������ ��ǥ
	Point curPosition; // �κ��� ���� ��ġ
	public int direction; // �κ��� �ٶ󺸰� �ִ� ����, 0�� ��, 1�� �Ʒ�, 2�� ����, 3�� ������
	ArrayList<Point> sequenceSet = new ArrayList<>();

	/* Singleton Pattern */
	private static SIM instance = new SIM();

	private SIM() {

	}

	public static SIM getInstance() {
		return instance;
	}

	/* �ʱ� ������ �޾Ƽ� SIM�� Grid�� ���� */
	public void getInitialData(Point startingPoint, int gridSize, ArrayList<Point> sequenceSet, ArrayList<Point> hazardSet) {
		curPosition = startingPoint;
		direction = 0;

		grid = new char[gridSize][gridSize];
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (i == curPosition.getRow() && j == curPosition.getCol()) {
					grid[i][j] = 'R';
				} else {
					grid[i][j] = '-';
				}
			}
		}

		this.sequenceSet = sequenceSet;
		createColorBlob(hazardSet);
		createRandomHazard();
	}

	/* �������� ���� ���� */
	void createRandomHazard() {
		int hazNum = (int) (Math.random() * 5) + 1; //������ 1~5�� �� ����

		outer: for (int i = 0; i < hazNum; i++) {
			int hazRow = (int) (Math.random() * grid.length);
			int hazCol = (int) (Math.random() * grid.length);
			for (int j = 0; j < sequenceSet.size(); j++) {
				if (hazRow == sequenceSet.get(j).getRow() && hazCol == sequenceSet.get(j).getCol())
					continue outer; //Ž�������� ��ġ�� ��� ��������
			}
			if (grid[hazRow][hazCol] == '-') {
				//�߿������� ��ġ�� �ʴ� ��쿡�� ����
				grid[hazRow][hazCol] = '!';
			}
		}
	}

	/* �߿����� ���� ���� */
	void createColorBlob(ArrayList<Point> hazardSet) {
		int count = 0;
		Map map = Map.getInstance();

		Point blob;
		ArrayList<Point> blobSet = new ArrayList<>();
		outer: do {
			int blobRow = (int) (Math.random() * grid.length);
			int blobCol = (int) (Math.random() * grid.length);
			for (int i = 0; i < hazardSet.size(); i++) {
				if (blobRow == hazardSet.get(i).getRow() && blobCol == hazardSet.get(i).getCol())
					continue outer; //�Է¹��� ���������� ��ġ�� ��� ��������

			}
			grid[blobRow][blobCol] = '*';
			blob = new Point(blobRow, blobCol);
			blobSet.add(blob);
			count++;
		} while (count != 3); //���� 3�� �ɶ����� �ݺ�
		map.saveBlob(blobSet); //map�� ����
	}

	/* ���� ��ĭ�� ������������ �˻� */
	public Point hazard_sensor() {
		int curRow = curPosition.getRow();
		int curCol = curPosition.getCol();
		Point hazPosition = new Point();
		switch (direction) {
		case 0: //���� �ٶ󺸰� �ִ� ���
			if ((curRow - 1) >= 0) {
				if (grid[curRow - 1][curCol] == '!') {
					hazPosition.setRow(curRow - 1);
					hazPosition.setCol(curCol);
					return hazPosition;
				} else
					return hazPosition;
			} else
				return hazPosition;
		case 1: //�Ʒ��� �ٶ󺸰� �ִ� ���
			if ((curRow + 1) < grid.length) {
				if (grid[curRow + 1][curCol] == '!') {
					hazPosition.setRow(curRow + 1);
					hazPosition.setCol(curCol);
					return hazPosition;
				} else
					return hazPosition;
			} else
				return hazPosition;
		case 2: //������ �ٶ󺸰� �ִ� ���
			if ((curCol - 1) >= 0) {
				if (grid[curRow][curCol - 1] == '!') {
					hazPosition.setRow(curRow);
					hazPosition.setCol(curCol - 1);
					return hazPosition;
				} else
					return hazPosition;
			} else
				return hazPosition;
		case 3: //�������� �ٶ󺸰� �ִ� ���
			if ((curCol + 1) < grid.length) {
				if (grid[curRow][curCol + 1] == '!') {
					hazPosition.setRow(curRow);
					hazPosition.setCol(curCol + 1);
					return hazPosition;
				} else
					return hazPosition;
			} else
				return hazPosition;
		default:
			// ���� ����
			return hazPosition;
		}
	}
	
	/* �����¿� ��ĭ�� �߿��������� �˻� */
	public ArrayList<Point> color_blob_sensor() {
		int curRow = curPosition.getRow();
		int curCol = curPosition.getCol();
		ArrayList<Point> blobPosition = new ArrayList<>();
		Point colorBlob;
		int blobRow, blobCol;
		if ((curRow - 1) >= 0) {
			if (grid[curRow - 1][curCol] == '*') {
				blobRow = curRow - 1;
				blobCol = curCol;
				colorBlob = new Point(blobRow, blobCol);
				blobPosition.add(colorBlob);
			}
		}
		if ((curRow + 1) < grid.length) {
			if (grid[curRow + 1][curCol] == '*') {
				blobRow = curRow + 1;
				blobCol = curCol;
				colorBlob = new Point(blobRow, blobCol);
				blobPosition.add(colorBlob);
			}
		}
		if ((curCol - 1) >= 0) {
			if (grid[curRow][curCol - 1] == '*') {
				blobRow = curRow;
				blobCol = curCol - 1;
				colorBlob = new Point(blobRow, blobCol);
				blobPosition.add(colorBlob);
			}
		}
		if ((curCol + 1) < grid.length) {
			if (grid[curRow][curCol + 1] == '*') {
				blobRow = curRow;
				blobCol = curCol + 1;
				colorBlob = new Point(blobRow, blobCol);
				blobPosition.add(colorBlob);
			}
		}
		return blobPosition;
	}

	/* �κ��� ���� ��ġ ��ȯ */
	public Point positioning_sensor() {
		return curPosition;
	}

	public void move_forward() {
		int curRow = curPosition.getRow();
		int curCol = curPosition.getCol();

		switch (direction) {
		case 0: //���� �ٶ󺸰� �ִ� ���
			grid[curRow][curCol] = '-';
			curPosition.setRow(curRow - 1);
			grid[curPosition.getRow()][curPosition.getCol()] = 'R';
			break;
		case 1: //�Ʒ��� �ٶ󺸰� �ִ� ���
			grid[curRow][curCol] = '-';
			curPosition.setRow(curRow + 1);
			grid[curPosition.getRow()][curPosition.getCol()] = 'R';
			break;
		case 2: //������ �ٶ󺸰� �ִ� ���
			grid[curRow][curCol] = '-';
			curPosition.setCol(curCol - 1);
			grid[curPosition.getRow()][curPosition.getCol()] = 'R';
			break;
		case 3: //�������� �ٶ󺸰� �ִ� ���
			grid[curRow][curCol] = '-';
			curPosition.setCol(curCol + 1);
			grid[curPosition.getRow()][curPosition.getCol()] = 'R';
			break;
		default:
		}
	}

	/* ���� ��ȯ �Լ� */
	public void turnClockWise() {
		if (direction == 0)
			direction = 3; //��->������
		else if (direction == 1)
			direction = 2; //�Ʒ�->����
		else if (direction == 2)
			direction = 0; //����->��
		else if (direction == 3)
			direction = 1; //������->�Ʒ�
	}

	public int getDirection() {
		return direction;
	}
}
