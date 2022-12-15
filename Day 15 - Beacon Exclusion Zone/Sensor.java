import java.awt.Point;

public class Sensor extends Point {
    Point beacon;

    public Sensor(int x, int y, Integer beaconX, Integer beaconY) {
        super(x, y);
        this.beacon = new Point(beaconX, beaconY);
    }

    public Point getBeacon() {
        return beacon;
    }

    public void setBeacon(Point beacon) {
        this.beacon = beacon;
    }

    public Integer GetDistance(){
        return Math.abs(beacon.x - this.x) + Math.abs(beacon.y - this.y);
    }
}
