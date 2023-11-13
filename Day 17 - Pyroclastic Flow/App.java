import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var input = Files.readString(Paths.get(file.getAbsolutePath()));
        //SolvePart1(input);
        SolvePart2(input);
    }
    
    static void SolvePart1(String input) {
        var jetStreamInterator = new JetStreamInterator(input);
        var piecesIterator = new PiecesIterator(new Piece[] { Piece.H, Piece.X, Piece.J, Piece.V, Piece.O });
        var simulator = new Simulator(jetStreamInterator, piecesIterator);
        while (simulator.SetPieces < 2022) {
            //clearScreen();
            simulator.Step();
            //simulator.Board.PrintBoard();
        }
        System.out.println("Part 1: " + (simulator.Board.HighestPoint + 1));
    }

    static void SolvePart2(String input) {
        var jetStreamInterator = new JetStreamInterator(input);
        var piecesIterator = new PiecesIterator(new Piece[] { Piece.H, Piece.X, Piece.J, Piece.V, Piece.O });
        var simulator = new Simulator(jetStreamInterator, piecesIterator);
        while (simulator.Step()){};
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
       }
}
