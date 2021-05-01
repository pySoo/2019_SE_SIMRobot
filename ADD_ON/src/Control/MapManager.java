package Control;
/**
 * Map�� ���������� �߿������� ǥ���ϴ� ������ class
 */
import Map.*;

public class MapManager {
	
	Point blobPosition;
	Point hazardPosition;

	Map map = Map.getInstance();

	/* Singleton Pattern */
	private static MapManager instance = new MapManager();

	public static MapManager getInstance() {
		return instance;
	}

	private MapManager() {
		
	};

	/* Sensor�κ��� position �޾Ƽ� Map�� ���� */
	public void getColorBlobPosition(Point blobPosition) {
		this.blobPosition = blobPosition;
		map.markColorblob(blobPosition);
	}

	/* Sensor�κ��� ���������� position �޾Ƽ� Map�� ���� */
	void getHazardPosition(Point hazardPosition) {
		this.hazardPosition = hazardPosition;
		map.markHazard(hazardPosition);
	}
	
}
