package Control;
/**
 * SIM의 hazard sensor 결과를 해석하는 클래스
 */
import Map.Point;

public class HazardSensor {

	public HazardSensor() {
		
	}
	
	boolean interpret(Point sensorResult) {
		//위험지점이 발견된 경우
		if(sensorResult.getRow()!=-1 && sensorResult.getCol()!=-1) { 
			MapManager mapManager=MapManager.getInstance();
			mapManager.getHazardPosition(sensorResult);
			PathManager pathManager=PathManager.getInstance();
			pathManager.makePath(); //경로 재생성
			return true;
		} else {
			//위험지점이 발견되지 않은 경우
			return false;
		}
	}

}
