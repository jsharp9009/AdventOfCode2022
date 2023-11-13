import java.util.Iterator;

public class PiecesIterator implements Iterator<Piece> {

    int currentIndex = 0;
    Piece[] Pieces;

    public PiecesIterator(Piece[] pieces) {
        Pieces = pieces;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Piece next() {
        var piece = Pieces[currentIndex % Pieces.length];
        currentIndex++;
        return piece;
    }
    
    public Piece current() {
        return Pieces[(currentIndex - 1) % Pieces.length];
    }
}
