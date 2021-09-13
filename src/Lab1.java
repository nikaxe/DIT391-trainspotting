import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import TSim.*;


public class Lab1 {
  // Semaphores
  Semaphore brown = new Semaphore(0, true);
  Semaphore blue = new Semaphore(1, true);
  Semaphore orange = new Semaphore(1, true);
  Semaphore purple = new Semaphore(1, true);
  Semaphore yellow = new Semaphore(1, true);
  Semaphore red = new Semaphore(1, true);
  Semaphore pink = new Semaphore(0, true);

  //Map <Position, Sensor> sensors = new HashMap<>();

  public Lab1(int speed1, int speed2) {
    
    //putSensors();
    Thread train1 = new Train(false, 1, speed1, true);
    Thread train2 = new Train(true, 2, speed2, true);

    train1.start();
    train2.start();
  }
  
  private class Train extends Thread {
    private int id;
    private int maxSpeed;
    private boolean direction;
    private boolean onDefaultTrack;
    TSimInterface tsi;

    public Train(boolean dir, int id, int maxSpeed, boolean onDefaultTrack) {
      direction = dir;
      this.id = id;
      this.maxSpeed = maxSpeed;
      this.onDefaultTrack = onDefaultTrack;
      tsi = TSimInterface.getInstance();
    }

    @Override
    public void run() {
      try {
        tsi.setSpeed(id, maxSpeed);
        while(true) {
          SensorEvent se = tsi.getSensor(id);
          int x = se.getXpos();
          int y = se.getYpos();
          /*
          Sensor hitSensor = sensors.get(new Position(x, y));
          if(se.getStatus() == SensorEvent.ACTIVE)
            hitSensor.doAction(this, direction);
          */
          
          if(se.getStatus() == SensorEvent.INACTIVE)
            continue; 
          Position sensorPos = new Position(x, y);
          
          // 1 or 2 
          if(sensorPos.equals(new Position(13, 3)) || sensorPos.equals(new Position(13, 5))) {
            if(direction)
              changeDirection();
          // 3 or 4
          }  else if(sensorPos.equals(new Position(9, 5)) || sensorPos.equals(new Position(6, 6))) {
            if(direction)
              blue.release();
            else {
              stopTrain();
              blue.acquire();
              startTrain();
            }
          // 5 or 6
          } else if(sensorPos.equals(new Position(11, 7)) || sensorPos.equals(new Position(11, 8))) {
            if(direction) {
              stopTrain();
              blue.acquire();
              startTrain();
            }
            else 
              blue.release();
          // 7 or 8
          } else if(sensorPos.equals(new Position(14, 7)) || sensorPos.equals(new Position(14, 8))){
            if(direction)
              purple.release();
            else{
              stopTrain();
              purple.acquire();
              releaseIfDefaultTrack(brown, 17, 7, true);
              tryToAcquire(yellow, 15, 9, true);
              startTrain();
            }
          // 9 or 10
          } else if(sensorPos.equals(new Position(11, 9)) || sensorPos.equals(new Position(11, 10))){
            if(direction){
              stopTrain();
              purple.acquire();
              releaseIfDefaultTrack(yellow, 15, 9, true);
              tryToAcquire(brown, 17, 7, true);
              startTrain();
            }
            else
              purple.release();
          // 11 or 12
          } else if(sensorPos.equals(new Position(8, 9)) || sensorPos.equals(new Position(8, 10))){
            if(direction)
              red.release();
            else{
              stopTrain();
              red.acquire();
              releaseIfDefaultTrack(yellow, 4, 9, false);
              tryToAcquire(pink, 3, 11, false);
              startTrain();
            }  
          // 13 or 14
          } else if(sensorPos.equals(new Position(6, 11)) || sensorPos.equals(new Position(6, 13))){
            if(direction){
              stopTrain();
              red.acquire();
              releaseIfDefaultTrack(pink, 3, 11, false);
              tryToAcquire(yellow, 4, 9, false);
              startTrain();
            }
            else
              red.release();
          // 15 or 16
          } else if(sensorPos.equals(new Position(13, 11)) || sensorPos.equals(new Position(13, 13))) {
            if(!direction)
              changeDirection();
          }
        } 
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    private void releaseIfDefaultTrack(Semaphore toRelease, int x, int y, boolean releaseRight) throws CommandException {
      if(onDefaultTrack){
        if(releaseRight)
          tsi.setSwitch(x, y, TSimInterface.SWITCH_RIGHT);
        else
          tsi.setSwitch(x, y, TSimInterface.SWITCH_LEFT);
        toRelease.release();
        return;
      }
      if(releaseRight)
        tsi.setSwitch(x, y, TSimInterface.SWITCH_LEFT);
      else
        tsi.setSwitch(x, y, TSimInterface.SWITCH_RIGHT);
    }
    private void tryToAcquire(Semaphore sem, int switchX, int switchY
    , boolean switchRightOnAcquire)throws CommandException {
      if(sem.tryAcquire(1)){
        onDefaultTrack = true;
        if(switchRightOnAcquire)
          tsi.setSwitch(switchX, switchY, TSimInterface.SWITCH_RIGHT);
        else
          tsi.setSwitch(switchX, switchY, TSimInterface.SWITCH_LEFT);
      }
      else{
        onDefaultTrack = false;
        if(switchRightOnAcquire)
          tsi.setSwitch(switchX, switchY, TSimInterface.SWITCH_LEFT);
        else
          tsi.setSwitch(switchX, switchY, TSimInterface.SWITCH_RIGHT);
      }
    }
    private void changeDirection() throws CommandException, InterruptedException {
      tsi.setSpeed(id, 0);
      Thread.sleep(3000);
      direction = !direction;
      maxSpeed = -maxSpeed;
      tsi.setSpeed(id, maxSpeed);
    }
    private void stopTrain() throws CommandException {
      tsi.setSpeed(id, 0);
    }
    private void startTrain() throws CommandException {
      tsi.setSpeed(id, maxSpeed);
    }
  }
  /*
  private interface SensorAction {
    public void action(Train train) throws CommandException, InterruptedException;
  }

  private class Sensor {
    private SensorAction upAction;
    private SensorAction downAction;
    public Sensor(SensorAction up, SensorAction down) {
      upAction = up;
      downAction = down;
    }
    public void doAction(Train train, boolean direction) throws CommandException, InterruptedException{
      if(direction)
        doUp(train);
      else 
        doDown(train);
    }
    private void doUp(Train train) throws CommandException, InterruptedException{
      if(upAction != null)
        upAction.action(train);
    }
    private void doDown(Train train) throws CommandException, InterruptedException{
      if(downAction != null)
        downAction.action(train);
    }
  }
  */
  private class Position {
    private final int x;
    private final int y;
    public Position(int x, int y) {
      this.x = x;
      this.y = y;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    
    @Override
    public boolean equals(Object obj) {
      if(obj == null || getClass() != obj.getClass())
        return false;
      Position other = (Position) obj;
      return x == other.getX() && y == other.getY();
    }
    @Override
    public int hashCode() {
      int h = 17;
      h += x * 7 + y * 23;
      return h;
    }
  }
}

// Ask ta if sensor map counts as shared variable 
// good/bad idea to use bool default track in train

/*
CommandException - Exception in TSim
Thrown when a command to TSim fails.
CommandException(String) - Constructor for exception TSim.CommandException
Constructs an CommandException with the specified detailed error message.
*/


/*
direction == true is the upwards direction
1:
  direction: 
    stop train
    wait 2s
    start train in the other direction
  !direction: 
    do nothing
2:
  direction: 
    stop train
    wait 2s
    start train in the other direction
  !direction:
    do nothing
3: 
  direction:
    release blue
  !direction:
    stop train
    acquire blue
    start train
4: 
  direction:
    release blue
  !direction:
    stop train
    acquire blue
    acquire orange
    start train
5:
  direction:
    stop train
    acquire blue
    release orange
    start train
  !direction:
    release blue
6: 
  direction: 
    stop train
    acquire blue
    start train
  !direction:
    release blue
7:
  direction:
    release purple
  !direction:
    stop train
    acquire purple
    set switch 1 right
    release orange
    start train
8:
  direction:
    release purple
  !direction:
    stop train
    acquire purple
    set switch 1 left
    start train
9:
  direction:
    stop train
    if(tryacquire orange)
      set switch 1 right
    else
      set switch 1 left
    start train
  !direction:
    do nothing
10:
  direction:
    release yellow
  !direction:
    stop train
    if(tryacquire yellow)
      set switch 2 right
    else
      set switch 2 left
    start train
11:
  direction:
    stop train
    acquire purple
    set switch 2 right
    start train
  !direction:
    release purple
12:
  direction:
    stop train
    acquire purple
    set switch 2 left
    start train
  !direction:
    release purple
13:
  direction:
    release red
  !direction:
    stop train
    acquire red
    set switch 3 left
    if(tryacquire pink)
      set switch 4 left
    else
      set switch 4 right
    start train
14:
  direction:
    release red
  !direction:
    stop train
    acquire red
    set switch 3 right
    if(tryacquire pink)
      set switch 4 left
    else
      set switch 4 right
    start train
15:
  direction:
    stop train
    acquire red
    set switch 4 left
    if(tryacquire yellow)
      set switch 3 left
    else
      set switch 3 right
    start train
  !direction:
    release red
16:
  direction:
    stop train
    acquire red 
    set switch 4 right
    if(tryacquire yellow)
      set switch 3 left
    else
      set switch 3 right
  !direction:
    release red
17:
  direction:
    do nothing
  !direction:
    stop train 
    wait 3s
    start train in other direction
18:
  direction:
    do nothing
  !direction:
    stop train 
    wait 3s
    start train in other direction
*/