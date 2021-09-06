import java.util.concurrent.Semaphore;

import TSim.*;

public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();

    try {
      tsi.setSpeed(1,speed1);
    }
    catch (CommandException e) {
      e.printStackTrace();    // or only e.getMessage() for the error
      System.exit(1);
    }
  }
}

class Train implements Runnable {
  private TSimInterface tsi;
  private int id;

  public Train(TSimInterface tsi, int id) {
    this.tsi = tsi;
    this.id = id;
  }

  @Override
  public void run() {
    try {
      while(true) {
        SensorEvent se = tsi.getSensor(id);

      }  
    } catch (CommandException e) {

    } catch(InterruptedException e) {

    } finally{}

  }
  
}

class Sensor {
  private Position pos;
  private Semaphore semaphore;

  public Sensor(Semaphore sem, int x, int y) {
    semaphore = sem;
    pos = new Position(x, y);
  }

  public Sensor(Semaphore sem, Position pos) {
    semaphore = sem;
    this.pos = pos;
  }

  public boolean ticketAvailable() {
    return semaphore.availablePermits() > 0;
  }

}

class Position {
  private int x;
  private int y;
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }
  public int getX() { return x; }
  public int getY() { return y; }
  
  public boolean equals(Position pos) {
    return x == pos.getX() && y == pos.getY();
  }
}