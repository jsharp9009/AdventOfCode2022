import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App {
    static Map<String, Moves> translations = Map.of(
        "A", Moves.Rock,
        "B", Moves.Paper,
        "C", Moves.Scissors,
        "X", Moves.Rock,
        "Y", Moves.Paper,
        "Z", Moves.Scissors
    );
    
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var allStrategies = ParseInput(input);
        
        var totalScore = allStrategies.stream()
                            .map(s -> s.GetRoundScorePart1())
                            .reduce((long)0, (a, b) -> a + b);
        System.out.println("Total score Part 1: " + totalScore);

        
        totalScore = allStrategies.stream()
                            .map(s -> s.GetRoundScorePart2())
                            .reduce((long)0, (a, b) -> a + b);

        System.out.println("Total score Part 2: " + totalScore);
    }

    private static ArrayList<RoundStrategy> ParseInput(List<String> input){
        var total = new ArrayList<RoundStrategy>();
        for (String round : input) {
            var choices = round.split(" ");
            var thisRoundStrategy = new App.RoundStrategy(App.translations.get(choices[1]), App.translations.get(choices[0]));
            total.add(thisRoundStrategy);
        }
        return total;
    }

    public static class RoundStrategy{
        Moves MyMove;
        Moves OpponentMove;

        public RoundStrategy(Moves myMove, Moves opponentMoves){
            MyMove = myMove;
            OpponentMove = opponentMoves;
        }
        
        public long GetRoundScorePart1(){
            var score = MyMove.Value;
            if(MyMove == Moves.Rock){
                if(OpponentMove == Moves.Rock) score += 3;
                if(OpponentMove == Moves.Scissors) score += 6;
            }

            if(MyMove == Moves.Paper){
                if(OpponentMove == Moves.Paper) score += 3;
                if(OpponentMove == Moves.Rock) score += 6;
            }

            if(MyMove == Moves.Scissors){
                if(OpponentMove == Moves.Scissors) score += 3;
                if(OpponentMove == Moves.Paper) score += 6;
            }

            return score;
        }

        public long GetRoundScorePart2(){
            var score = 0;
            switch(MyMove){
                case Paper: score = 3; break; //Paper means Draw
                case Rock: score = 0; break; // Rock means Lose
                case Scissors: score = 6; //Scissors means Win
            }
            
            if(MyMove == Moves.Rock){
                switch(OpponentMove){
                    case Paper: score += Moves.Rock.Value; break;//Paper means Draw
                    case Rock: score += Moves.Scissors.Value; break; // Rock means Lose
                    case Scissors: score += Moves.Paper.Value; //Scissors means Win
                }   
            }

            if(MyMove == Moves.Paper){
                score += OpponentMove.Value;
            }

            if(MyMove == Moves.Scissors){
                switch(OpponentMove){
                    case Paper: score += Moves.Scissors.Value; break;//Paper means Draw
                    case Rock: score += Moves.Paper.Value; break; // Rock means Lose
                    case Scissors: score += Moves.Rock.Value; //Scissors means Win
                }
            }

            return score;
        }

    }

    enum Moves{
        Rock(1),
        Scissors(3),
        Paper(2);

        public final int Value;
        private Moves(int value){
            this.Value = value;
        }
    }
}
