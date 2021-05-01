package Control;
/**
 * 3개의 Sensor로부터 결과값을 받아서 동작을 결정하는 class
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
   
   /* 로봇의 동작을 담당하는 메서드 */
   void move(ArrayList<Point> path) {
	  //움직이고 나서 항상 path를 update하기 때문에 현재 위치는 path의 첫번쨰 지점, 이동해야할 위치는 path의 두번쨰 지점이다.
      Point curPos=path.get(0);
      int curDir=sim.getDirection();
      Point nextPos=path.get(1);
      
      //이동해야할 방향과 현재 로봇의 방향이 일치하는지 검사
      if(((nextPos.getRow()==curPos.getRow() && nextPos.getCol()==curPos.getCol()+1) && curDir==3) ||
            ((nextPos.getRow()==curPos.getRow() && nextPos.getCol()==curPos.getCol()-1) && curDir==2) ||
            ((nextPos.getRow()==curPos.getRow()+1 && nextPos.getCol()==curPos.getCol()) && curDir==1) ||
            ((nextPos.getRow()==curPos.getRow()-1 && nextPos.getCol()==curPos.getCol()) && curDir==0)) {
         
    	 Point hazResult=sim.hazard_sensor(); //움직이기 전 위험지점인지 검사
         boolean isHazard=hazSensor.interpret(hazResult);
         if(isHazard)
            return; //위험지점이었을 경우 path가 재생성되기 때문에 return
         
         ArrayList<Point> blobResult=sim.color_blob_sensor(); //중요지점 있는지 검사
         cbSensor.interpret(blobResult);
         
         sim.move_forward();
         
         Point posResult=sim.positioning_sensor();
         posSensor.interpret(posResult, nextPos); //로봇이 잘못된 움직임을 했는지 검사
         
         pathManager.setCurPosition(posResult);
         pathManager.updatePath(); //움직이고 나서 이동경로 update(움직이기 전 지점 제거)
         mapPrint.updateMap(); 
         
         try {
        	//로봇의 움직임을 GUI에 출력하기 위해 sleep
            Thread.sleep(700);
         }catch(InterruptedException e) {
            e.printStackTrace();
         }
         
         return;
      } else {
    	 //방향이 일치하지 않는 경우 회전
         sim.turnClockWise();
         mapPrint.updateMap();
         
         try {
        	 //로봇의 움직임을 GUI에 출력하기 위해 sleep
            Thread.sleep(700); 
         }catch(InterruptedException e) {
            e.printStackTrace();
         }
         return;
      }
   }
   
}