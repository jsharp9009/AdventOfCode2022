import java.util.HashSet;

public class Board {
    public HashSet<Point> Rocks;
    public Piece Falling;
    public int HighestPoint;

    public Board(HashSet<Point> rocks, Piece falling, int highestPoint) {
        Rocks = rocks;
        Falling = falling;
        HighestPoint = highestPoint;
    }

    public boolean tryShift(Point offset) {
        Piece tryPiece = Falling.Shift(offset);
        if (tryPiece.Lowest() < 0 || tryPiece.Leftest() < 0 || tryPiece.Rightest() >= 7)
            return false;
        for (Point point : tryPiece.Rocks) {
            if (Rocks.contains(point)) {
                return false;
            }
        }
        this.Falling = tryPiece;
        return true;
    }

    public void SpawnPiece(Piece next) {
        int rowshift = HighestPoint + 4;
        int leftshift = next.Leftest() + 2;
        Falling = next.Shift(new Point(rowshift, leftshift));
    }

    public void SetPiece(Piece next) {
        Rocks.addAll(Falling.Rocks);
        HighestPoint = Math.max(HighestPoint, Falling.Highest());
        SpawnPiece(next);
    }

    public int GetTopRow() {
        int mask = 0;
        for (int i = 0; i < 7; i++) {
            if (Rocks.contains(new Point(HighestPoint, i))) {
                mask |= 1 << i;
            }
        }
        return mask;
    }

    public void PrintBoard() {
        int top = this.HighestPoint + 7;
        for (int row = top; row >= 0; row--) {
            System.out.print("|");
            for (int col = 0; col < 7; col++) {
                if (Rocks.contains(new Point(row, col))) {
                    System.out.print("#");
                } else if (Falling.Rocks.contains(new Point(row, col))) {
                    System.out.print("@");
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.print("‾‾‾‾‾‾‾‾‾");
    }
}
