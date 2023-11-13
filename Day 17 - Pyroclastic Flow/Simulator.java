import java.io.Console;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Simulator {
    public JetStreamInterator jetStreamInterator;
    public PiecesIterator piecesIterator;
    public Board Board;
    public int SetPieces = 0;
    public State PieceState;
    public State LastPieceState;
    public HashMap<State, State> Edges = new HashMap<State,State>();

    public Simulator(JetStreamInterator jetStreamInterator, PiecesIterator piecesIterator) {
        this.jetStreamInterator = jetStreamInterator;
        this.piecesIterator = piecesIterator;
        this.Board = InitBoard();
        this.PieceState = InitPieceState();
    }

    private Board InitBoard() {
        var piece = piecesIterator.next();
        piece = piece.Shift(new Point(3, 2));
        return new Board(new java.util.HashSet<Point>(), piece, 0);
    }

    private State InitPieceState() {
        return new State(Board.GetTopRow(), Board.Falling, "", 0);
    }

    public boolean Step() {
        Character nextJet = jetStreamInterator.next();
        var offset = nextJet == '>' ? new Point(0, 1) : new Point(0, -1);
        PieceState = new State(PieceState.TopRow, PieceState.NextPiece, PieceState.moves + nextJet, PieceState.Id);
        Board.tryShift(offset);

        if (!Board.tryShift(new Point(-1, 0))) {
            var nextPiece = piecesIterator.next();

            if (CheckCycles(nextPiece)) {
                Board.SetPiece(nextPiece);
                SetPieces++;
            } else {
                return false;
            }

        }
        return true;
    }
    
    public boolean CheckCycles(Piece current) {
        if (Edges.containsKey(PieceState)) {
            var cycle = FindCycle(PieceState);
            int heightBeforeCycle = Board.HighestPoint;
            DropPieces(cycle);
            int heightAfterCycle = Board.HighestPoint;
            int cycleHeight = heightAfterCycle - heightBeforeCycle;

            // System.out.println("Cycle has height " + cycleHeight);
            // System.out.println("Cycle found with " + cycle.size() + " rocks.");
            // System.out.println("Height before cycle was " + heightBeforeCycle);
            // System.out.println("Rocks before cycle was " + SetPieces);

            long rocksToDrop = 1_000_000_000_000L - (long) SetPieces;
            long cycles = rocksToDrop / (long) cycle.size();
            int leftover = (int) (rocksToDrop % (long) cycle.size());
            List<State> extraRocks = cycle.stream().limit(leftover).toList();
            DropPieces(extraRocks);
            int extraHeight = Board.HighestPoint - heightAfterCycle;
            long height = (cycles * cycleHeight) + (extraHeight + heightBeforeCycle);
            System.out.println("Height Part 2: " + (height + 1));
            return false;
        }
        else if (LastPieceState != null) {
            Edges.put(LastPieceState, PieceState);
        }
        LastPieceState = PieceState;
        PieceState = new State(Board.GetTopRow(), current, "", jetStreamInterator.CurrentID());
        return true;
    }
    
    public List<State> FindCycle(State start) {
        List<State> cycle = new LinkedList<State>() {
            {
                add(start);
            }};
        State next = Edges.get(start);
        while (next == null || !next.equals(start)) {
            cycle.add(next);
            next = Edges.get(next);
        }
        return cycle;
    }

    public void DropPieces(List<State> pieces) {
        for (State state : pieces) {
            DropPiece(state);
        }
    }

    public void DropPiece(State state) {
        Board.SpawnPiece(state.NextPiece);
        for (int ch : state.moves.chars().toArray()) {
            var offset = ((char) ch) == '>' ? new Point(0, 1) : new Point(0, -1);
            Board.tryShift(offset);
            Board.tryShift(new Point(-1, 0));
        }
        Board.SetPiece(Piece.J);
    }
}
