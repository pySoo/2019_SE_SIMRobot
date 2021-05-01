package Control;
/**
 * SIM�� positioning sensor�� ����� �ؼ��ϴ� Ŭ����
 */
import Map.Point;

public class PositioningSensor {
	
	public PositioningSensor() {
		
	}
	
	void interpret(Point sensorResult, Point targetPosition) {
		/* �κ��� ����� ������ ���*/
		if(sensorResult.getRow()==targetPosition.getRow() && sensorResult.getCol()==targetPosition.getCol()) {
			return;
		} else if(sensorResult.getRow()==targetPosition.getRow()+1 || sensorResult.getRow()==targetPosition.getRow()-1
				|| sensorResult.getCol()==targetPosition.getCol()+1 || sensorResult.getCol()==targetPosition.getCol()-1) {
			/* �κ��� ��ĭ ������ ��� */
			PathManager hpm=PathManager.getInstance();
			hpm.decideWrongPosition(sensorResult);
		} else {
			/* �κ��� �������� ���� ���*/
			return;
		}
	}
}
