package Control;
/**
 * SIM의 colorblob sensor의 결과를 해석하는 클래스
 */
import java.util.*;
import Map.Point;

public class ColorBlobSensor {

	public ColorBlobSensor() {
		
	}
	
	void interpret(ArrayList<Point> sensorResult) {
		//발견된 중요지점이 없는 경우
		if(sensorResult.size()==0) 
			return;
		else { 
			//발견된 중요지점이 있는 경우
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
