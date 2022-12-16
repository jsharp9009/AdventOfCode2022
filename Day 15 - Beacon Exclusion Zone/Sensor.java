import java.awt.Point;

public class Sensor extends Point {
    Point beacon;
    Integer distance;
    public Sensor(int x, int y, Integer beaconX, Integer beaconY) {
        super(x, y);
        this.beacon = new Point(beaconX, beaconY);
        this.distance = CalculateDistance(beacon.x, beacon.y, this.x, this.y);
    }

    public Point getBeacon() {
        return beacon;
    }

    public Integer GetDistance(){
        return distance;
    }

    public static Integer CalculateDistance(Integer p1x, Integer p1y, Integer p2x, Integer p2y){
        return Math.abs(p1x - p2x) + Math.abs(p1y - p2y);
    }

    public boolean CanSee(Point point){
        var dist = CalculateDistance(this.x, this.y, point.x, point.y);
        return dist <= distance;
    }

    public boolean CanSee(Integer p1x, Integer p1y){
        var dist = CalculateDistance(p1x, p1y, this.x, this.y);
        return dist < distance;
    }
}
