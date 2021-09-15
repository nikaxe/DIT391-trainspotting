import java.util.concurrent.Semaphore;

import TSim.*;

public class Lab1 {
  // Semaphores
  private final Semaphore brown = new Semaphore(0, true);
  private final Semaphore blue = new Semaphore(1, true);
  private final Semaphore purple = new Semaphore(1, true);
  private final Semaphore yellow = new Semaphore(1, true);
  private final Semaphore red = new Semaphore(1, true);
  private final Semaphore pink = new Semaphore(0, true);

  public Lab1(int speed1, int speed2) {
    Thread train1 = new Train(false, 1, speed1, true);
    Thread train2 = new Train(true, 2, speed2, true);

    train1.start();
    train2.start();
  }
  
  /**
   * Representation of the trains in the simulator. 
   */
  private class Train extends Thread {
    private final int id;
    private int maxSpeed;
    private boolean direction;
    private boolean onDefaultTrack;
    private final TSimInterface tsi;

    public Train(boolean dir, int id, int maxSpeed, boolean onDefaultTrack) {
      direction = dir;
      this.id = id;
      this.maxSpeed = maxSpeed;
      this.onDefaultTrack = onDefaultTrack;
      tsi = TSimInterface.getInstance();
    }

    // run() is run when start() is called on the Train instance.
    @Override
    public void run() {
      try {
        tsi.setSpeed(id, maxSpeed);
        while(true) {
          SensorEvent se = tsi.getSensor(id);
          int x = se.getXpos();
          int y = se.getYpos();
          
          if(se.getStatus() == SensorEvent.INACTIVE)
            continue; 
          Position sensorPos = new Position(x, y);
          
          // Sensor cases
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
      // Catch CommandException or InterruptedException if any 
      // methods throws
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(1);
      }
    }

    /**
     * Used by the train when leaving a parallel track.
     * Releases the semaphore if on a default track and sets switch
     * @param toRelease the semaphore to release
     * @param x X position of switch
     * @param y Y position of switch
     * @param releaseRight Set switch in the right/left position if on default track
     * @throws CommandException
     */
    private void releaseIfDefaultTrack(Semaphore toRelease, int x, int y, boolean releaseRight) throws CommandException {
      if(onDefaultTrack){
        // if on default track switch goes in given direction
        if(releaseRight)
          tsi.setSwitch(x, y, TSimInterface.SWITCH_RIGHT);
        else
          tsi.setSwitch(x, y, TSimInterface.SWITCH_LEFT);
        toRelease.release();
        return;
      }
      // if not on default track switch goes in the opposite direction 
      if(releaseRight)
        tsi.setSwitch(x, y, TSimInterface.SWITCH_LEFT);
      else
        tsi.setSwitch(x, y, TSimInterface.SWITCH_RIGHT);
    }

    /**
     * Used when a train needs to decide if it can use the default
     * track on parallel sections. Acquires semaphore and sets switch if it can.
     * @param sem The semaphore to try to acquire.
     * @param switchX X position of the switch before the semaphore.
     * @param switchY Y position of the switch before the semaphore.
     * @param switchRightOnAcquire True will set the switch to be in the right position 
     * if the acquire succeds and in the left position if it fails. False does the reversed.
     * @throws CommandException
     */
    private void tryToAcquire(Semaphore sem, int switchX, int switchY
    , boolean switchRightOnAcquire)throws CommandException {
      if(sem.tryAcquire(1)){
        onDefaultTrack = true;
        // if acquire succeds switch goes in the given direction
        if(switchRightOnAcquire)
          tsi.setSwitch(switchX, switchY, TSimInterface.SWITCH_RIGHT);
        else
          tsi.setSwitch(switchX, switchY, TSimInterface.SWITCH_LEFT);
      }
      else{
        onDefaultTrack = false;
        // if acquire failed switch goes in the other direction
        if(switchRightOnAcquire)
          tsi.setSwitch(switchX, switchY, TSimInterface.SWITCH_LEFT);
        else
          tsi.setSwitch(switchX, switchY, TSimInterface.SWITCH_RIGHT);
      }
    }

    /**
     * Stops the train and wait 3 seconds, 
     * then start the train but in the opposite direction.
     * @throws CommandException
     * @throws InterruptedException
     */
    private void changeDirection() throws CommandException, InterruptedException {
      tsi.setSpeed(id, 0);
      Thread.sleep(3000);
      direction = !direction;
      maxSpeed = -maxSpeed;
      tsi.setSpeed(id, maxSpeed);
    }

    /**
     * Set train speed to 0.
     * @throws CommandException
     */
    private void stopTrain() throws CommandException {
      tsi.setSpeed(id, 0);
    }

    /**
     * Set train speed to maximum.
     * @throws CommandException
     */
    private void startTrain() throws CommandException {
      tsi.setSpeed(id, maxSpeed);
    }
  }

  /**
   * Used to compare 2D positions  
   */
  private class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
    @Override
    public boolean equals(Object obj) {
      if(obj == null || getClass() != obj.getClass())
        return false;
      Position other = (Position) obj;
      return x == other.x && y == other.y;
    }
    @Override
    public int hashCode() {
      int h = 17;
      h += x * 7 + y * 23;
      return h;
    }
  }
}
