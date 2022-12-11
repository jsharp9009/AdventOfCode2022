import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var commands = ParseInput(input);
        var result = FindSignalStrengths(commands, new Integer[]{20, 60, 100, 140, 180, 220});
        System.err.println("Part One Signal Strengths: " + result);
        DrawMessage(commands);
    }

    static ArrayList<Map.Entry<String, Integer>> ParseInput(List<String> input){
        var result = new ArrayList<Map.Entry<String, Integer>>();
        for(var cmd : input){
            var split = cmd.split(" ");
            var num = split.length > 1 ? Integer.parseInt(split[1]) : 0;
            var entry = new AbstractMap.SimpleEntry<String, Integer>(split[0], num);
            result.add(entry);           
        }
        return result;
    }

    static Integer FindSignalStrengths(ArrayList<Map.Entry<String, Integer>> input, Integer[] cyclesToRecord){
        var x = 1;
        var cycle = 0;
        var sum = 0;
        for(int i = 0; i < input.size(); i++){
            cycle++;
            var cmd = input.get(i);
            var cycle2 = cycle;
            if(Arrays.stream(cyclesToRecord).anyMatch(a -> a == cycle2)){
                sum += (x * cycle);
                if(cycle == 220) break;
            }

            if(cmd.getKey().equals("addx")){
                cycle++;
                var cycle3 = cycle;
                if(Arrays.stream(cyclesToRecord).anyMatch(a -> a == cycle3)){
                    sum += (x * cycle);
                    if(cycle == 220) break;
                }
                x += cmd.getValue();
            }
        }
        return sum;
    }

    static void DrawMessage(ArrayList<Map.Entry<String, Integer>> input){
        var x = 1;
        var cycle = 0;
        for(int i = 0; i < input.size(); i++){
            cycle++;
            if(cycle % 40 == 1) System.out.println("");
            if((cycle % 40) >= x && (cycle % 40) <= x + 2) System.out.print("█");
            else System.out.print(" ");

            var cmd = input.get(i);
            if(cmd.getKey().equals("addx")){
                cycle++;
                if(cycle % 40 == 1) System.out.println("");
                if(cycle % 40 >= x && cycle % 40 <= x + 2) System.out.print("█");
                else System.out.print(" ");
                x += cmd.getValue();
            }
        }
    }

}
