import java.util.HashSet;

public class Piece {
    public HashSet<Point> Rocks;

    public Piece(HashSet<Point> rocks) {
        Rocks = rocks;
    }

    public static Piece H = new Piece(
            new HashSet<Point>() {
                {
                    add(new Point(0, 0));
                    add(new Point(0, 1));
                    add(new Point(0, 2));
                    add(new Point(0, 3));
                }
            });

    public static Piece X = new Piece(
            new HashSet<Point>() {
                {
                    add(new Point(0, 1));
                    add(new Point(1, 0));
                    add(new Point(1, 1));
                    add(new Point(1, 2));
                    add(new Point(2, 1));
                }
            });

    public static Piece J = new Piece(
            new HashSet<Point>() {
                {
                    add(new Point(0, 2));
                    add(new Point(1, 2));
                    add(new Point(0, 1));
                    add(new Point(2, 2));
                    add(new Point(0, 0));
                }
            });

    public static Piece V = new Piece(
            new HashSet<Point>() {
                {
                    add(new Point(0, 0));
                    add(new Point(1, 0));
                    add(new Point(2, 0));
                    add(new Point(3, 0));
                }
            }

    );

    public static Piece O = new Piece(
            new HashSet<Point>() {
                {
                    add(new Point(0, 0));
                    add(new Point(1, 0));
                    add(new Point(0, 1));
                    add(new Point(1, 1));
                }
            }

    );

    public Piece Shift(Point offset) {
        HashSet<Point> rocks = new HashSet<Point>();
        for (var r : Rocks) {
            rocks.add(r.Shift(offset));
        }
        return new Piece(rocks);
    }

    public int Highest() {
        return Rocks.stream()
                .mapToInt(p -> p.getX())
                .max().getAsInt();
    }

    public int Lowest() {
        return Rocks.stream()
                .mapToInt(p -> p.getX())
                .min().getAsInt();
    }
    
    public int Leftest() {
        return Rocks.stream()
                .mapToInt(p -> p.getY())
                .min().getAsInt();
    }
    
    public int Rightest() {
        return Rocks.stream()
        .mapToInt(p -> p.getY())
        .max().getAsInt();
    }
}
