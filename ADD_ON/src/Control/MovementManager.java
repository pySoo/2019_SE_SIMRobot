package Control;
/**
 * 3���� Sensor�κ��� ������� �޾Ƽ� ������ �����ϴ� class
 */
import java.util.*;
import Map.*;
import SIM.*;
import UI.PrintMap;

public class MovementManager {
   PathManager pathManager=PathManager.getInstance();
   ArrayList<Point> path;
   SIM sim=SIM.getInstance();
   HazardSensor hazSensor=new HazardSensor();
   ColorBlobSensor cbSensor=new ColorBlobSensor();
   PositioningSensor posSensor=new PositioningSensor();
   PrintMap mapPrint = PrintMap.getInstance();
   
   /* Singleton Pattern */
   private static MovementManager instance = new MovementManager();

   public static MovementManager getInstance() {
      return instance;
   }

   private MovementManager() {
      
   };
   
   /* �κ��� ������ ����ϴ� �޼��� */
   void move(ArrayList<Point> path) {
	  //�����̰� ���� �׻� path�� update�ϱ� ������ ���� ��ġ�� path�� ù���� ����, �̵��ؾ��� ��ġ�� path�� �ι��� �����̴�.
      Point curPos=path.get(0);
      int curDir=sim.getDirection();
      Point nextPos=path.get(1);
      
      //�̵��ؾ��� ����� ���� �κ��� ������ ��ġ�ϴ��� �˻�
      if(((nextPos.getRow()==curPos.getRow() && nextPos.getCol()==curPos.getCol()+1) && curDir==3) ||
            ((nextPos.getRow()==curPos.getRow() && nextPos.getCol()==curPos.getCol()-1) && curDir==2) ||
            ((nextPos.getRow()==curPos.getRow()+1 && nextPos.getCol()==curPos.getCol()) && curDir==1) ||
            ((nextPos.getRow()==curPos.getRow()-1 && nextPos.getCol()==curPos.getCol()) && curDir==0)) {
         
    	 Point hazResult=sim.hazard_sensor(); //�����̱� �� ������������ �˻�
         boolean isHazard=hazSensor.interpret(hazResult);
         if(isHazard)
            return; //���������̾��� ��� path�� ������Ǳ� ������ return
         
         ArrayList<Point> blobResult=sim.color_blob_sensor(); //�߿����� �ִ��� �˻�
         cbSensor.interpret(blobResult);
         
         sim.move_forward();
         
         Point posResult=sim.positioning_sensor();
         posSensor.interpret(posResult, nextPos); //�κ��� �߸��� �������� �ߴ��� �˻�
         
         pathManager.setCurPosition(posResult);
         pathManager.updatePath(); //�����̰� ���� �̵���� update(�����̱� �� ���� ����)
         mapPrint.updateMap(); 
         
         try {
        	//�κ��� �������� GUI�� ����ϱ� ���� sleep
            Thread.sleep(700);
         }catch(InterruptedException e) {
            e.printStackTrace();
         }
         
         return;
      } else {
    	 //������ ��ġ���� �ʴ� ��� ȸ��
         sim.turnClockWise();
         mapPrint.updateMap();
         
         try {
        	 //�κ��� �������� GUI�� ����ϱ� ���� sleep
            Thread.sleep(700); 
         }catch(InterruptedException e) {
            e.printStackTrace();
         }
         return;
      }
   }
   
}