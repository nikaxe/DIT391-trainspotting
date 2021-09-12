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

  Map <Position, Sensor> sensors = new HashMap<>();

  public Lab1(int speed1, int speed2) {
    
    //putSensors();
    Thread train1 = new Train(false, 1, speed1, true);
    Thread train2 = new Train(true, 2, speed2, true);

    train1.start();
    train2.start();
  }
  /*
  public void putSensors(){
        // Sensors
    // 1
    sensors.put(new Position(14, 3), new Sensor(new SensorAction(){
      public void action(Train train)throws CommandException, InterruptedException {
        train.changeDirection();
      }
    }, null));
    // 2
    sensors.put(new Position(14, 5), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.changeDirection();
      }
    }, null));
    // 3
    sensors.put(new Position(9, 5), new Sensor(new SensorAction() {
      public void action(Train train) {
        blue.release();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.stopTrain();
        blue.acquire();
        train.startTrain();
      }
    }));
    // 4
    sensors.put(new Position(6, 6), new Sensor(new SensorAction() {
      public void action(Train train) {
        blue.release();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.stopTrain();
        blue.acquire();
        orange.acquire();
        train.startTrain();
      }
    }));
    // 5
    sensors.put(new Position(11, 7), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        train.stopTrain();
        blue.acquire();
        orange.release();
        train.startTrain();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        blue.release();
      }
    }));
    // 6
    sensors.put(new Position(11, 8), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        train.stopTrain();
        blue.acquire();
        train.startTrain();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        blue.release();
      }
    }));
    // 7
    sensors.put(new Position(14, 7), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        purple.release();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.stopTrain();
        purple.acquire();
        tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
        brown.release();
        orange.release();
        train.startTrain();
      }
    }));
    // 8
    sensors.put(new Position(14, 8), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        purple.release();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.stopTrain();
        purple.acquire();
        tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
        train.startTrain();
      }
    }));
    // 9
    sensors.put(new Position(19, 8), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        train.stopTrain();
        if(brown.tryAcquire(1)){
          if(orange.tryAcquire(1))
            tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
          else{
            brown.release();
            tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
          }
        }
        else 
          tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
        train.startTrain();
      }
    }, null));
    // 10
    sensors.put(new Position(18, 9), new Sensor(null, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.stopTrain();
        if(yellow.tryAcquire(1))
          tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
        else
          tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);
        train.startTrain();
      }
    }));
    // 11
    sensors.put(new Position(12, 9), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        train.stopTrain();
        purple.acquire();
        tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
        yellow.release();
        System.out.print("released yellow: " + yellow.availablePermits());
        train.startTrain();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        purple.release();
      }
    }));
    // 12
    sensors.put(new Position(12, 10), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        train.stopTrain();
        purple.acquire();
        tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);
        train.startTrain();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        purple.release();
      }
    }));
    // 13
    sensors.put(new Position(7, 9), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        red.release();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.stopTrain();
        red.acquire();
        tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
        if(pink.tryAcquire(1))
          tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
        else
          tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
        yellow.release();
        System.out.print("released yellow: " + yellow.availablePermits());
        train.startTrain();
      }
    }));
    // 14
    sensors.put(new Position(7, 10), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        red.release();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.stopTrain();
        red.acquire();
        tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
        if(pink.tryAcquire(1))
          tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
        else
          tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
        train.startTrain();
      }
    }));
    // 15
    sensors.put(new Position(6, 11), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        train.stopTrain();
        red.acquire();
        tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
        pink.release();
        if(yellow.tryAcquire(1))
          tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
        else
          tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
        train.startTrain();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        red.release();
      }
    }));
    // 16
    sensors.put(new Position(6, 13), new Sensor(new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException{
        train.stopTrain();
        red.acquire();
        tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
        if(yellow.tryAcquire(1))
          tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
        else
          tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
        train.startTrain();
      }
    }, new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        red.release();
      }
    }));
    // 17
    sensors.put(new Position(13, 11), new Sensor(null, 
    new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.changeDirection();
      }
    }));
    // 18
    sensors.put(new Position(13, 13), new Sensor(null, 
    new SensorAction() {
      public void action(Train train) throws CommandException, InterruptedException {
        train.changeDirection();
      }
    }));
  }
  */
  
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
          Sensor hitSensor = sensors.get(new Position(x, y));
          if(se.getStatus() == SensorEvent.ACTIVE)
            hitSensor.doAction(this, direction);

          
          if(se.getStatus() == SensorEvent.INACTIVE)
            continue; 
          Position sensorPos = new Position(x, y);
          // 1 or 2 or 15 or 16
          if(sensorPos.equals(new Position(14, 3)) || sensorPos.equals(new Position(14, 5))
          || sensorPos.equals(new Position(13, 11)) || sensorPos.equals(new Position(13, 13))) {
            if(direction)
              changeDirection();
          // 3 or 4
          } else if(sensorPos.equals(new Position(9, 5)) || sensorPos.equals(new Position(6, 6))) {
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
              if(onDefaultTrack){
                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                brown.release();
              }
              else 
                tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
              tryToAcquire(yellow, 15, 9, true);
              startTrain();
            }
          // 9 or 10
          } else if(sensorPos.equals(new Position(12, 9)) || sensorPos.equals(new Position(12, 10))){
            if(direction){
              stopTrain();
              purple.acquire();
              if(onDefaultTrack){
                tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
                yellow.release();
              }
              else 
                tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);
              tryToAcquire(brown, 17, 7, true);
              startTrain();
            }
            else
              purple.release();
          // 11 or 12
          } else if(sensorPos.equals(new Position(7, 9)) || sensorPos.equals(new Position(7, 10))){
            if(direction)
              red.release();
            else{
              stopTrain();
              red.acquire();
              if(onDefaultTrack){
                tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
                yellow.release();
              }
              else 
                tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
              tryToAcquire(pink, 3, 11, false);
              startTrain();
            }  
          // 13 or 14
          } else if(sensorPos.equals(new Position(6, 11)) || sensorPos.equals(new Position(6, 13))){
            if(direction){
              stopTrain();
              red.acquire();
              if(onDefaultTrack){
                tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                pink.release();
              }
              else 
                tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
              tryToAcquire(yellow, 4, 9, false);
              startTrain();
            }
            else
              red.release();
          }
        } 
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
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
    public void changeDirection() throws CommandException, InterruptedException {
      tsi.setSpeed(id, 0);
      Thread.sleep(3000);
      direction = !direction;
      maxSpeed = -maxSpeed;
      tsi.setSpeed(id, maxSpeed);
    }
    public void stopTrain() throws CommandException {
      tsi.setSpeed(id, 0);
    }
    public void startTrain() throws CommandException {
      tsi.setSpeed(id, maxSpeed);
    }
  }
  
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