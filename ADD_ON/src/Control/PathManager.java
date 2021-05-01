package Control;
/**
 * path를 생성하고 관리하는 class
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
	Point curPosition; // 현재위치
	int[][] visit;

	/* Singleton Pattern */
	private static PathManager instance = new PathManager();
	
	public static PathManager getInstance() {
		return instance;
	}

	private PathManager() {}
	
	/* Map 클래스로부터 받은 지도 */
	public void setMap(char[][] map) {
		this.map = map;
		mapSize = map.length;
	}

	ArrayList<Point> getPath() {
		return path;
	}

	/* InputDataUI 클래스로부터 받은 초기 정보 */
	public void getInitialData(Point startingPoint, ArrayList<Point> hazardSet, ArrayList<Point> sequenceSet) {
		this.startingPoint = startingPoint;
		this.hazardSet = hazardSet;
		this.sequenceSet = sequenceSet;

		curPosition = new Point(startingPoint.row, startingPoint.col);
		makePath(); //초기 정보를 받아서 처음 경로 생성
	}

	/* path 생성 알고리즘에 필요한 내부클래스 */
	public static class Node {
		int x; // row
		int y; // col

		Node prev;

		Node(int x, int y, Node prev) {
			this.x = x;
			this.y = y;
			this.prev = prev;
		}

		// 이동경로 string으로 출력할 때 필요
		@Override
		public String toString() {
			return String.format("(%d, %d)", x, y);
		}
	}

	/* 경로 생성 메서드 */
	void makePath() {
		// 탐색 완료
		if (sequenceSet.size() == 0) {
			return;
		}
		if (path.size() != 0) {
			path.clear(); // 기존 이동경로가 있는 경우 다 지우고 처음부터 다시 생성
		}

		Map mapInstance = Map.getInstance();
		MovementManager mm = MovementManager.getInstance();
		SIM sim = SIM.getInstance();

		this.map = mapInstance.getMap();
		Queue<Node> queue = new LinkedList<>();

		int row = map.length;
		int col = map[0].length;

		// 현재 노드의 인접노드들중 위험 지점을 지나지 않는 경로를 찾기 위해
		// 현재 (x,y)위치에서 서->남->동->북 으로 탐색하기위한 거리 배열 변수
		int dist_x[] = { 1, 0, -1, 0 };
		int dist_y[] = { 0, 1, 0, -1 };

		// 현재 위치부터 시작
		queue.offer(new Node(curPosition.row, curPosition.col, null));
		for (int i = 0; i < sequenceSet.size(); i++) {

			while (!queue.isEmpty()) {
				// 현재 큐에 있는 것을 꺼낸다.
				Node node = queue.poll();

				// node가 탐색지점까지 왔는지 체크
				if (node.x == sequenceSet.get(i).row && node.y == sequenceSet.get(i).col) {
					Node s = node;

					// 다음 loop의 시작 지점은 마지막 탐색지점이 된다
					queue.clear();
					queue.offer(new Node(s.x, s.y, null));

					// 탐색지점에 도착했으면 path를 저장하기 위해
					// 거꾸로 prev 노드를 올라가면서 list 에 추가하고 탐색을 마무리 한다.
					int index = path.size();
					while (s != null) {
						Point p = new Point(s.x, s.y);
						path.add(index, p);
						s = s.prev;
					}
					break;
				}

				// 탐색 지점에 도착하지 않았으면 현 노드에서 인접한 노드를
				// 서 -> 남 -> 동 -> 북 순으로 검사한다
				for (int j = 0; j < dist_x.length; j++) {
					int vx = node.x + dist_x[j];
					int vy = node.y + dist_y[j];

					// 인접한 노드가 방문되었거나 위험지점인지 검사
					if (is_path(vx, vy, map, row, col)) {
						queue.offer(new Node(vx, vy, node));
					}
				}
			}
		}

		// 중복된 값 제거
		for (int i = 0; i < (path.size() - sequenceSet.size()); i++) {
			if (path.get(i).getRow() == path.get(i + 1).getRow() && path.get(i).getCol() == path.get(i + 1).getCol())
				path.remove(i);
		}

		mapInstance.savePath(path);
		
		//생성된 이동경로를 따라 이동
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

	//이동경로에 포함할 수 있는지 조건을 검사하는 함수
	public static boolean is_path(int x, int y, char[][] map, int row, int col) {
		// map 영역을 벗어나면 안됨.
		if (x < 0 || y < 0 || x > col - 1 || y > row - 1) {
			return false;
		}

		// 위험지역은 갈 수 없음.
		if (map[x][y] == '!') {
			return false;
		}

		return true;
	}

	void setCurPosition(Point curPosition) {
		this.curPosition = curPosition;
	}

	/* 경로 중 지나간 지점은 삭제 */
	void updatePath() {
		path.remove(0);
		Map map = Map.getInstance();
		map.savePath(path);
	}

	/* 로봇이 잘못된 움직임을 한 경우 경로 재생성을 위해 호출되는 함수 */
	void decideWrongPosition(Point curPosition) {
		this.curPosition = curPosition;
		makePath();
	}

}