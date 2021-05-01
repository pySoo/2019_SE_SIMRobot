package Control;
/**
 * SIM�� hazard sensor ����� �ؼ��ϴ� Ŭ����
 */
import Map.Point;

public class HazardSensor {

	public HazardSensor() {
		
	}
	
	boolean interpret(Point sensorResult) {
		//���������� �߰ߵ� ���
		if(sensorResult.getRow()!=-1 && sensorResult.getCol()!=-1) { 
			MapManager mapManager=MapManager.getInstance();
			mapManager.getHazardPosition(sensorResult);
			PathManager pathManager=PathManager.getInstance();
			pathManager.makePath(); //��� �����
			return true;
		} else {
			//���������� �߰ߵ��� ���� ���
			return false;
		}
	}

}
