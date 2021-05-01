package Control;
/**
 * path�� �����ϰ� �����ϴ� class
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import Map.*;
import SIM.SIM;

public class PathManager {
	char[][] map;
	int mapSize;

	ArrayList<Point> hazardSet;
	ArrayList<Point> sequenceSet;
	ArrayList<Point> visitSqSet = new ArrayList<Point>();
	ArrayList<Point> path = new ArrayList<Point>();
	Point startingPoint;
	Point curPosition; // ������ġ
	int[][] visit;

	/* Singleton Pattern */
	private static PathManager instance = new PathManager();
	
	public static PathManager getInstance() {
		return instance;
	}

	private PathManager() {}
	
	/* Map Ŭ�����κ��� ���� ���� */
	public void setMap(char[][] map) {
		this.map = map;
		mapSize = map.length;
	}

	ArrayList<Point> getPath() {
		return path;
	}

	/* InputDataUI Ŭ�����κ��� ���� �ʱ� ���� */
	public void getInitialData(Point startingPoint, ArrayList<Point> hazardSet, ArrayList<Point> sequenceSet) {
		this.startingPoint = startingPoint;
		this.hazardSet = hazardSet;
		this.sequenceSet = sequenceSet;

		curPosition = new Point(startingPoint.row, startingPoint.col);
		makePath(); //�ʱ� ������ �޾Ƽ� ó�� ��� ����
	}

	/* path ���� �˰��� �ʿ��� ����Ŭ���� */
	public static class Node {
		int x; // row
		int y; // col

		Node prev;

		Node(int x, int y, Node prev) {
			this.x = x;
			this.y = y;
			this.prev = prev;
		}

		// �̵���� string���� ����� �� �ʿ�
		@Override
		public String toString() {
			return String.format("(%d, %d)", x, y);
		}
	}

	/* ��� ���� �޼��� */
	void makePath() {
		// Ž�� �Ϸ�
		if (sequenceSet.size() == 0) {
			return;
		}
		if (path.size() != 0) {
			path.clear(); // ���� �̵���ΰ� �ִ� ��� �� ����� ó������ �ٽ� ����
		}

		Map mapInstance = Map.getInstance();
		MovementManager mm = MovementManager.getInstance();
		SIM sim = SIM.getInstance();

		this.map = mapInstance.getMap();
		Queue<Node> queue = new LinkedList<>();

		int row = map.length;
		int col = map[0].length;

		// ���� ����� ���������� ���� ������ ������ �ʴ� ��θ� ã�� ����
		// ���� (x,y)��ġ���� ��->��->��->�� ���� Ž���ϱ����� �Ÿ� �迭 ����
		int dist_x[] = { 1, 0, -1, 0 };
		int dist_y[] = { 0, 1, 0, -1 };

		// ���� ��ġ���� ����
		queue.offer(new Node(curPosition.row, curPosition.col, null));
		for (int i = 0; i < sequenceSet.size(); i++) {

			while (!queue.isEmpty()) {
				// ���� ť�� �ִ� ���� ������.
				Node node = queue.poll();

				// node�� Ž���������� �Դ��� üũ
				if (node.x == sequenceSet.get(i).row && node.y == sequenceSet.get(i).col) {
					Node s = node;

					// ���� loop�� ���� ������ ������ Ž�������� �ȴ�
					queue.clear();
					queue.offer(new Node(s.x, s.y, null));

					// Ž�������� ���������� path�� �����ϱ� ����
					// �Ųٷ� prev ��带 �ö󰡸鼭 list �� �߰��ϰ� Ž���� ������ �Ѵ�.
					int index = path.size();
					while (s != null) {
						Point p = new Point(s.x, s.y);
						path.add(index, p);
						s = s.prev;
					}
					break;
				}

				// Ž�� ������ �������� �ʾ����� �� ��忡�� ������ ��带
				// �� -> �� -> �� -> �� ������ �˻��Ѵ�
				for (int j = 0; j < dist_x.length; j++) {
					int vx = node.x + dist_x[j];
					int vy = node.y + dist_y[j];

					// ������ ��尡 �湮�Ǿ��ų� ������������ �˻�
					if (is_path(vx, vy, map, row, col)) {
						queue.offer(new Node(vx, vy, node));
					}
				}
			}
		}

		// �ߺ��� �� ����
		for (int i = 0; i < (path.size() - sequenceSet.size()); i++) {
			if (path.get(i).getRow() == path.get(i + 1).getRow() && path.get(i).getCol() == path.get(i + 1).getCol())
				path.remove(i);
		}

		mapInstance.savePath(path);
		
		//������ �̵���θ� ���� �̵�
		while (sequenceSet.size() != 0) {
			mm.move(path);
			Point curPosition = sim.positioning_sensor();
			if(sequenceSet.size()==0) break;
			int curRow = curPosition.getRow();
			int curCol = curPosition.getCol();
			if (curRow == sequenceSet.get(0).getRow()
					&& curCol == sequenceSet.get(0).getCol()) {
				Point markSq = new Point(curRow, curCol);
				mapInstance.markSequence(markSq);
				sequenceSet.remove(0);
			}
		}
	}

	//�̵���ο� ������ �� �ִ��� ������ �˻��ϴ� �Լ�
	public static boolean is_path(int x, int y, char[][] map, int row, int col) {
		// map ������ ����� �ȵ�.
		if (x < 0 || y < 0 || x > col - 1 || y > row - 1) {
			return false;
		}

		// ���������� �� �� ����.
		if (map[x][y] == '!') {
			return false;
		}

		return true;
	}

	void setCurPosition(Point curPosition) {
		this.curPosition = curPosition;
	}

	/* ��� �� ������ ������ ���� */
	void updatePath() {
		path.remove(0);
		Map map = Map.getInstance();
		map.savePath(path);
	}

	/* �κ��� �߸��� �������� �� ��� ��� ������� ���� ȣ��Ǵ� �Լ� */
	void decideWrongPosition(Point curPosition) {
		this.curPosition = curPosition;
		makePath();
	}

}