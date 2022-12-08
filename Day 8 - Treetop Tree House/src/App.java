import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var intInput = ParseInput(input);
        var result = SolvePartOne(intInput);
        System.out.println("Part One: " + result);
        result = SolvePartTwo(intInput);
        System.out.println("Part Two: " + result);
    }

    static int[][] ParseInput(List<String> input) {
        var result = new int[input.get(0).length()][input.size()];
        for (int i = 0; i < input.get(0).length(); i++) {
            for (int n = 0; n < input.size(); n++) {
                result[i][n] = Character.valueOf(input.get(i).charAt(n)) - 48;
            }
        }
        return result;
    }

    static int SolvePartOne(int[][] input) {
        var visible = new ArrayList<Integer>();
        for (int i = 0; i < input.length; i++) {
            var largestN = -1;
            for (int n = 0; n < input[i].length; n++) {
                var height = input[i][n];
                if (height > largestN) {
                    largestN = height;
                    var value = (i * 100000) + n;
                    if (!visible.contains(value))
                        visible.add(value);
                }
            }
        }

        for (int i = input.length - 1; i >= 0; i--) {
            var largestN = -1;
            for (int n = input[i].length - 1; n >= 0; n--) {
                var height = input[i][n];
                if (height > largestN) {
                    largestN = height;
                    var value = (i * 100000) + n;
                    if (!visible.contains(value))
                        visible.add(value);
                }
            }
        }

        for (int i = 0; i < input[0].length; i++) {
            var largestN = -1;
            for (int n = 0; n < input.length; n++) {
                var height = input[n][i];
                if (height > largestN) {
                    largestN = height;
                    var value = (n * 100000) + i;
                    if (!visible.contains(value))
                        visible.add(value);
                }
            }
        }

        for (int i = input[0].length - 1; i >= 0; i--) {
            var largestN = -1;
            for (int n = input.length - 1; n >= 0; n--) {
                var height = input[n][i];
                if (height > largestN) {
                    largestN = height;
                    var value = (n * 100000) + i;
                    if (!visible.contains(value))
                        visible.add(value);
                }
            }
        }

        return visible.size();
    }

    static int SolvePartTwo(int[][] input) {
        var scenicScore = 0;
        for (int i = 0; i < input.length; i++) {
            for (int n = 0; n < input[i].length; n++) {
                var currentScore = CalculateScenicScore(i, n, input);
                if(currentScore > scenicScore)
                    scenicScore = currentScore;
            }
        }
        return scenicScore;
    }

    static int CalculateScenicScore(int x, int y, int[][] input){
        var score = 0;
        var myHieght = input[x][y];
        //Look Up
        for(int i = y - 1; i >= 0; i--){
            var currentHeight = input[x][i];
            if(currentHeight <= myHieght)
                score++;
            if(currentHeight >= myHieght)
                break;
        }

        //Look Down
        var tmpScore = 0;
        for(int i = y + 1; i < input.length; i++){
            var currentHeight = input[x][i];
            if(currentHeight <= myHieght)
                tmpScore++;
            if(currentHeight >= myHieght)
                break;
        }
        score *= tmpScore;

        //Look left
        tmpScore = 0;
        for(int i = x - 1; i >= 0; i--){
            var currentHeight = input[i][y];
            if(currentHeight <= myHieght)
                tmpScore++;
            if(currentHeight >= myHieght)
                break;
        }
        score *= tmpScore;

        //Look right
        tmpScore = 0;
        for(int i = x + 1; i < input.length; i++){
            var currentHeight = input[i][y];
            if(currentHeight <= myHieght)
                tmpScore++;
            if(currentHeight >= myHieght)
                break;
        }
        score *= tmpScore;

        return score;
    }
}
