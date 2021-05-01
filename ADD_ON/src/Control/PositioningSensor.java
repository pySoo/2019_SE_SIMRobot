package Control;
/**
 * SIM의 positioning sensor의 결과를 해석하는 클래스
 */
import Map.Point;

public class PositioningSensor {
	
	public PositioningSensor() {
		
	}
	
	void interpret(Point sensorResult, Point targetPosition) {
		/* 로봇이 제대로 움직인 경우*/
		if(sensorResult.getRow()==targetPosition.getRow() && sensorResult.getCol()==targetPosition.getCol()) {
			return;
		} else if(sensorResult.getRow()==targetPosition.getRow()+1 || sensorResult.getRow()==targetPosition.getRow()-1
				|| sensorResult.getCol()==targetPosition.getCol()+1 || sensorResult.getCol()==targetPosition.getCol()-1) {
			/* 로봇이 두칸 움직인 경우 */
			PathManager hpm=PathManager.getInstance();
			hpm.decideWrongPosition(sensorResult);
		} else {
			/* 로봇이 움직이지 않은 경우*/
			return;
		}
	}
}
