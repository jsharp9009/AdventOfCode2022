import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));

        var priority = SolvePartOne(input);
        System.out.println("Priority Part 1: " + priority);
        priority = SolvePartTwo(input);
        System.out.println("Priority Part 2: " + priority);
    }

    public static int SolvePartOne(List<String> input){
        var priority = 0;
        for (String sack : input){
            var size = sack.length() / 2;
            var pocket1 = Arrays.copyOfRange(sack.chars().toArray(), 0, size);
            var pocket2 = Arrays.copyOfRange(sack.chars().toArray(), size, sack.length());

            var intersect = Arrays.stream(pocket1).distinct().filter(x -> Arrays.stream(pocket2).anyMatch(y -> y == x)).toArray();
            if(intersect[0] > 90)
                priority += intersect[0] - 96;
            else
                priority += intersect[0] - 38;
        }
        return priority;
    }

    public static int SolvePartTwo(List<String> input){
        var priority = 0;
        for (int i = 0; i < input.size(); i+=3){
            var string1 = input.get(i);
            var string2 = input.get(i + 1);
            var string3 = input.get(i + 2);

            var intersect = string1.chars().distinct()
                .filter(x -> string2.chars().anyMatch(y -> y == x))
                .filter(x -> string3.chars().anyMatch(y -> y == x)).toArray();
            if(intersect[0] > 90)
                priority += intersect[0] - 96;
            else
                priority += intersect[0] - 38;
        }
        return priority;
    }
}
