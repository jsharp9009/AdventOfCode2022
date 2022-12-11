import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class App {
    static final Pattern pattern = Pattern.compile("[0-9].");
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var monkies = ParseInput(input);
        var result = SolvePartOne(monkies);
        System.out.println("Part One Monkey Business: " + result);

        monkies = ParseInput(input);
        var modulo = ComputeModulo(monkies);
        result = SolvePartTwo(monkies, modulo);
        System.out.println("Part Two Monkey Business: " + result);
    }

    static ArrayList<Monkey> ParseInput(List<String> input){
        var result = new ArrayList<Monkey>();

        ArrayList<Long> Items = null;
        String Operation = "";
        Integer Test = 0;
        Integer TestTrue = 0;
        Integer TestFalse = 0;
        
        for(int i = 0; i < input.size(); i++){
            var line = i % 7;
            switch (line) {
                case 0: continue;
                case 1: Items = GetStartingItems(input.get(i)); break;
                case 2: {
                    Operation = input.get(i).replace("  Operation: new = ", "");
                } break;
                case 3: {
                    var split = input.get(i).split(" ");
                    Test = Integer.parseInt(split[split.length-1]);
                } break;
                case 4: {
                    var split = input.get(i).split(" ");
                    TestTrue = Integer.parseInt(split[split.length-1]);
                } break;
                case 5: {
                    var split = input.get(i).split(" ");
                    TestFalse = Integer.parseInt(split[split.length-1]);
                } break;
                case 6: {
                    result.add(new Monkey(Items, Operation, Test, TestTrue, TestFalse));
                }
            }
        }
        result.add(new Monkey(Items, Operation, Test, TestTrue, TestFalse));

        return result;
    }

    static Long SolvePartOne(ArrayList<Monkey> monkies){
        for(int i = 0; i < 20; i++){
            AroundOfMonkeying(monkies, 3);
        }
        var largest = 0;
        var secondLargest = 0;
        for (Monkey monkey : monkies) {
            if(monkey.inspected > largest){
                secondLargest = largest;
                largest = monkey.inspected;
                continue;
            }
            if(monkey.inspected > secondLargest){
                secondLargest = monkey.inspected;
            }
        }        
        return largest * Long.valueOf(secondLargest);
    }

    static Long SolvePartTwo(ArrayList<Monkey> monkies, Integer Modulo){
        for(int i = 0; i < 10000; i++){
            AroundOfMonkeying(monkies, 1, Modulo);
        }
        var largest = 0;
        var secondLargest = 0;
        for (Monkey monkey : monkies) {
            if(monkey.inspected > largest){
                secondLargest = largest;
                largest = monkey.inspected;
                continue;
            }
            if(monkey.inspected > secondLargest){
                secondLargest = monkey.inspected;
            }
        }        
        return largest * Long.valueOf(secondLargest) ;
    }

    static ArrayList<Long> GetStartingItems(String line){
        var result = new ArrayList<Long>();
        var matcher = pattern.matcher(line);
        while(matcher.find()){
            result.add(Long.parseLong(matcher.group()));
        }
        return result;
    }

    static void InspectItems(Monkey monkey, ArrayList<Monkey> monkies, Integer worryReduction, Integer Modulo) {
        var item = monkey.GetItem();
        while(item != null){
            monkey.inspected++;
            var operation = monkey.Operation.replaceAll("old", item.toString());
            item = evalExpression(operation);
            item = Math.floorDiv(item, worryReduction);
            item = item % Modulo;
            if(item % monkey.Test == 0){
                monkies.get(monkey.TestTrue).CatchItem(item);;
            }
            else{
                monkies.get(monkey.TestFalse).CatchItem(item);
            }

            item = monkey.GetItem();
        }
    }

    static void InspectItems(Monkey monkey, ArrayList<Monkey> monkies, Integer worryReduction) {
        var item = monkey.GetItem();
        while(item != null){
            monkey.inspected++;
            var operation = monkey.Operation.replaceAll("old", item.toString());
            item = evalExpression(operation);
            item = Math.floorDiv(item, worryReduction);
            if(item % monkey.Test == 0){
                monkies.get(monkey.TestTrue).CatchItem(item);;
            }
            else{
                monkies.get(monkey.TestFalse).CatchItem(item);
            }

            item = monkey.GetItem();
        }
    }

    static void AroundOfMonkeying(ArrayList<Monkey> monkies, Integer worryReduction, Integer Modulo){
        for (Monkey monkey : monkies) {
                InspectItems(monkey, monkies, worryReduction, Modulo);
        }
    }

    static void AroundOfMonkeying(ArrayList<Monkey> monkies, Integer worryReduction){
        for (Monkey monkey : monkies) {
                InspectItems(monkey, monkies, worryReduction);
        }
    }

    static Long evalExpression(String expression){
        var split = expression.split(" ");
        switch (split[1]){
            case "*": return Long.parseLong(split[0]) * Long.parseLong(split[2]);
            case "+": return Long.parseLong(split[0]) + Long.parseLong(split[2]);
            case "-": return Long.parseLong(split[0]) - Long.parseLong(split[2]);
            case "/": return Long.parseLong(split[0]) / Long.parseLong(split[2]);
        }
        return 0L;
    }

    static Integer ComputeModulo(ArrayList<Monkey> monkies){
        var result = 1;
        for(Monkey m : monkies){
            result *= m.Test;
        }
        return result;
    }
}
