import java.awt.Point;

public class Sensor extends Point {
    Integer range;
    public Sensor(int x, int y, Integer range) {
        super(x, y);
        this.range = range;
    }

    public Integer GetRange(){
        return range;
    }

    public static Integer CalculateDistance(Integer p1x, Integer p1y, Integer p2x, Integer p2y){
        return Math.abs(p1x - p2x) + Math.abs(p1y - p2y);
    }

    public boolean CanSee(Point point){
        var dist = CalculateDistance(this.x, this.y, point.x, point.y);
        return dist <= range;
    }

    public boolean CanSee(Integer p1x, Integer p1y){
        var dist = CalculateDistance(p1x, p1y, this.x, this.y);
        return dist < range;
    }
}
