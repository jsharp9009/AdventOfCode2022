public class State
{
    public int TopRow;
    Piece NextPiece;
    String moves;
    int Id;

    public State(int topRow, Piece nextPiece, String moves, int id) {
        this.TopRow = topRow;
        this.NextPiece = nextPiece;
        this.moves = moves;
        this.Id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (!(obj instanceof State))
            return false;

        var compareState = (State)obj;

        return compareState.TopRow == this.TopRow
                && compareState.NextPiece.equals(this.NextPiece)
                && this.moves.equals(compareState.moves)
                && this.Id == compareState.Id;
    }

    // @Override
    // public int hashCode() {
    //     return this.TopRow.BitMask() ^ NextPiece.hashCode() ^ moves.hashCode() ^ Id;        
    // }
}