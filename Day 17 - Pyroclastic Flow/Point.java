public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return ("(" + x + "," + y + ")");
    }

    public Point Shift(Point offset) {
        return new Point(this.x + offset.x, this.y + offset.y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Point))
            return false;
        Point compare = (Point) o;
        return Integer.compare(this.getX(), compare.getX()) == 0 && Integer.compare(this.getY(), compare.getY()) == 0;
    }
    
    @Override
public int hashCode() {
    return getX() ^ getY();
}
}