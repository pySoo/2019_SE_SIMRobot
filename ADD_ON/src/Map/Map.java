package Map;
/**
 * Map ������ �����ϴ� class
 */
import java.util.ArrayList;

import Control.*;


public class Map {
	char[][] map;
	int mapSize;
	public ArrayList<Point> path;
	public ArrayList<Point> hazardSet=new ArrayList<>();
	public ArrayList<Point> hiddenBlob=new ArrayList<>();
	public ArrayList<Point> detectedBlob=new ArrayList<>();
	public ArrayList<Point> sequenceSet=new ArrayList<>();
	public ArrayList<Point> visitSqSet=new ArrayList<>();

	PathManager pathManager = PathManager.getInstance();

	private Map() {
		
	};

	/* Singleton Pattern */
	private static Map instance = new Map();

	public static Map getInstance() {
		return instance;
	}

	public char[][] getMap() {
		return map;
	}
	
	public int getMapSize() {
		return mapSize;
	}

	public void getInitionialData(ArrayList<Point> hazard, ArrayList<Point> sequence) {
		hazardSet = hazard;
		sequenceSet = sequence;
	}

	public void saveMap(char[][] map) {
		this.map = map;
		mapSize = map.length;
		pathManager.setMap(map);
	}

	// SIM�� ���� ������ hidden blob
	public void saveBlob(ArrayList<Point> blobPosition) {
		try {
		hiddenBlob = blobPosition;
		}catch(NullPointerException e) {
			return;
		}
	}
	
	//��� �ٲ�� GUI�� update
	public void savePath(ArrayList<Point> path) {
		this.path = path;
	}

	//blob sensor�� ���� �߰��� �߿����� ǥ��
	public void markColorblob(Point blobPosition) {
		try {
		int blobRow = blobPosition.getRow();
		int blobCol = blobPosition.getCol();
		map[blobRow][blobCol] = '*';
		Point colorblob = new Point(blobRow, blobCol);
		detectedBlob.add(colorblob);
		}catch(NullPointerException e) {
			return;
		}

	}

	//hazard sensor�� ���� �߰��� �������� ǥ��
	public void markHazard(Point hazardPosition) {
		hazardSet.add(hazardPosition);
		int hazRow = hazardPosition.getRow();
		int hazCol = hazardPosition.getCol();
		map[hazRow][hazCol] = '!';
	}
	
	//�湮�� Ž������ ǥ��
	public void markSequence(Point visitSqPosition) {
		visitSqSet.add(visitSqPosition);
		int visitRow = visitSqPosition.getRow();
		int visitCol = visitSqPosition.getCol();
	}
	
}
