package Control;
/**
 * SIM�� colorblob sensor�� ����� �ؼ��ϴ� Ŭ����
 */
import java.util.*;
import Map.Point;

public class ColorBlobSensor {

	public ColorBlobSensor() {
		
	}
	
	void interpret(ArrayList<Point> sensorResult) {
		//�߰ߵ� �߿������� ���� ���
		if(sensorResult.size()==0) 
			return;
		else { 
			//�߰ߵ� �߿������� �ִ� ���
			MapManager mapManager=MapManager.getInstance();
			for(int i=0;i<sensorResult.size();i++) {
				int blobRow=sensorResult.get(i).getRow();
				int blobCol=sensorResult.get(i).getCol();
				Point blobPosition=new Point(blobRow, blobCol);
				mapManager.getColorBlobPosition(blobPosition);
			}
		}
	}

}
