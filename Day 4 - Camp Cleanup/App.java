import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var numbers = ParseInput(input);
        var result = SolvePartOne(numbers);
        System.out.println("Result: " + result);

        result = SolvePartTwo(numbers);
        System.out.println("Result Part 2: " + result);
    }

    static ArrayList<Integer> ParseInput(List<String> input){
        var result = new ArrayList<Integer>();
        for(String line : input){
            var elves = line.split(",");
            for(String elf : elves){
                var parts = elf.split("-");
                result.add(Integer.parseInt(parts[0]));
                result.add(Integer.parseInt(parts[1]));
            }
        }
        return result;
    }

    static int SolvePartOne(List<Integer> input){
        var result = 0;
        for(int i = 0; i < input.size(); i += 4){
            if((input.get(i) <= input.get(i+2)
             && input.get(i+1) >= input.get(i+3))
             || (input.get(i+2) <= input.get(i)
             && input.get(i+3) >= input.get(i+1)))
                result++;
        }
        return result;
    }

    static int SolvePartTwo(List<Integer> input){
        var result = 0;
        for(int i = 0; i < input.size(); i += 4){
            if(input.get(i + 2) > input.get(i + 1)
                || input.get(i + 3) < input.get(i))
                result++;
        }
        return (input.size() / 4) - result;
    }
}
