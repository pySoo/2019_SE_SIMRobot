package SIM;
/**
 * SIM을 구현한 클래스
 */
import java.util.*;
import Map.*;
import Map.Map;

public class SIM {

	char[][] grid; //로봇 내부의 좌표
	Point curPosition; // 로봇의 현재 위치
	public int direction; // 로봇이 바라보고 있는 방향, 0은 위, 1은 아래, 2는 왼쪽, 3은 오른쪽
	ArrayList<Point> sequenceSet = new ArrayList<>();

	/* Singleton Pattern */
	private static SIM instance = new SIM();

	private SIM() {

	}

	public static SIM getInstance() {
		return instance;
	}

	/* 초기 정보를 받아서 SIM의 Grid에 저장 */
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

	/* 위험지점 랜덤 생성 */
	void createRandomHazard() {
		int hazNum = (int) (Math.random() * 5) + 1; //개수는 1~5개 중 랜덤

		outer: for (int i = 0; i < hazNum; i++) {
			int hazRow = (int) (Math.random() * grid.length);
			int hazCol = (int) (Math.random() * grid.length);
			for (int j = 0; j < sequenceSet.size(); j++) {
				if (hazRow == sequenceSet.get(j).getRow() && hazCol == sequenceSet.get(j).getCol())
					continue outer; //탐색지점과 겹치는 경우 생성안함
			}
			if (grid[hazRow][hazCol] == '-') {
				//중요지점과 겹치지 않는 경우에만 생성
				grid[hazRow][hazCol] = '!';
			}
		}
	}

	/* 중요지점 랜덤 생성 */
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
					continue outer; //입력받은 위험지점과 겹치는 경우 생성안함

			}
			grid[blobRow][blobCol] = '*';
			blob = new Point(blobRow, blobCol);
			blobSet.add(blob);
			count++;
		} while (count != 3); //개수 3개 될때까지 반복
		map.saveBlob(blobSet); //map에 전달
	}

	/* 전방 한칸이 위험지점인지 검사 */
	public Point hazard_sensor() {
		int curRow = curPosition.getRow();
		int curCol = curPosition.getCol();
		Point hazPosition = new Point();
		switch (direction) {
		case 0: //위를 바라보고 있는 경우
			if ((curRow - 1) >= 0) {
				if (grid[curRow - 1][curCol] == '!') {
					hazPosition.setRow(curRow - 1);
					hazPosition.setCol(curCol);
					return hazPosition;
				} else
					return hazPosition;
			} else
				return hazPosition;
		case 1: //아래를 바라보고 있는 경우
			if ((curRow + 1) < grid.length) {
				if (grid[curRow + 1][curCol] == '!') {
					hazPosition.setRow(curRow + 1);
					hazPosition.setCol(curCol);
					return hazPosition;
				} else
					return hazPosition;
			} else
				return hazPosition;
		case 2: //왼쪽을 바라보고 있는 경우
			if ((curCol - 1) >= 0) {
				if (grid[curRow][curCol - 1] == '!') {
					hazPosition.setRow(curRow);
					hazPosition.setCol(curCol - 1);
					return hazPosition;
				} else
					return hazPosition;
			} else
				return hazPosition;
		case 3: //오른쪽을 바라보고 있는 경우
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
			// 방향 오류
			return hazPosition;
		}
	}
	
	/* 전후좌우 한칸이 중요지점인지 검사 */
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

	/* 로봇의 현재 위치 반환 */
	public Point positioning_sensor() {
		return curPosition;
	}

	public void move_forward() {
		int curRow = curPosition.getRow();
		int curCol = curPosition.getCol();

		switch (direction) {
		case 0: //위를 바라보고 있는 경우
			grid[curRow][curCol] = '-';
			curPosition.setRow(curRow - 1);
			grid[curPosition.getRow()][curPosition.getCol()] = 'R';
			break;
		case 1: //아래를 바라보고 있는 경우
			grid[curRow][curCol] = '-';
			curPosition.setRow(curRow + 1);
			grid[curPosition.getRow()][curPosition.getCol()] = 'R';
			break;
		case 2: //왼쪽을 바라보고 있는 경우
			grid[curRow][curCol] = '-';
			curPosition.setCol(curCol - 1);
			grid[curPosition.getRow()][curPosition.getCol()] = 'R';
			break;
		case 3: //오른쪽을 바라보고 있는 경우
			grid[curRow][curCol] = '-';
			curPosition.setCol(curCol + 1);
			grid[curPosition.getRow()][curPosition.getCol()] = 'R';
			break;
		default:
		}
	}

	/* 방향 전환 함수 */
	public void turnClockWise() {
		if (direction == 0)
			direction = 3; //위->오른쪽
		else if (direction == 1)
			direction = 2; //아래->왼쪽
		else if (direction == 2)
			direction = 0; //왼쪽->위
		else if (direction == 3)
			direction = 1; //오른쪽->아래
	}

	public int getDirection() {
		return direction;
	}
}
