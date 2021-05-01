package Control;
/**
 * Map에 위험지점과 중요지점을 표시하는 역할의 class
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

	/* Sensor로부터 position 받아서 Map에 저장 */
	public void getColorBlobPosition(Point blobPosition) {
		this.blobPosition = blobPosition;
		map.markColorblob(blobPosition);
	}

	/* Sensor로부터 위험지점의 position 받아서 Map에 저장 */
	void getHazardPosition(Point hazardPosition) {
		this.hazardPosition = hazardPosition;
		map.markHazard(hazardPosition);
	}
	
}
