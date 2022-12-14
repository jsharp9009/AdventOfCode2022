import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class App {
    static final Pattern pattern = Pattern.compile("[\\[]|\\d+|[\\]]");
    public static void main(String[] args) throws Exception {
        var file = new File("input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var arrays = ParseInput(input);
        SolvePartOne(arrays);
        SolvePartTwo(arrays);
    }

    static void SolvePartOne(ArrayListWithParent<ArrayListWithParent<Object>> input){
        var sum = 0;
        for(int i = 0; i < input.size(); i+=2){
            var left = input.get(i);
            var right = input.get(i+1);

            if(left.compareTo(right) < 0){
                //System.out.println("Line " + ((i/2)+1));
                sum += (i / 2) + 1;
            }
        }

        System.out.println("Part One Sum: " + sum);
    }

    static void SolvePartTwo(ArrayListWithParent<ArrayListWithParent<Object>> input){
        var add1 = ParseLine("[[2]]");
        var add2 = ParseLine("[[6]]");

        input.add(add1);
        input.add(add2);

        var sorted = input.stream().sorted().toList();
        var index1 = sorted.indexOf(add1) + 1;
        var index2 = sorted.indexOf(add2) + 1;

        System.out.println("Part 2 Answer: " + (index1 * index2));
    }

    static ArrayListWithParent<ArrayListWithParent<Object>> ParseInput(List<String> input){
        var result = new ArrayListWithParent<ArrayListWithParent<Object>>();

        for (String inpuString : input) {
            if(inpuString.equals("")) continue;
            result.add(ParseLine(inpuString));
        }

        return result;
    }

    static ArrayListWithParent<Object> ParseLine(String line){
        ArrayListWithParent<Object> current = null;

        var matcher = pattern.matcher(line);
        while(matcher.find()){
            var found = matcher.group();
            if(found.equals("[")){
                if(current == null)
                    current = new ArrayListWithParent<Object>();
                else{
                    var newList = new ArrayListWithParent<Object>();
                    newList.parent = current;
                    current.add(newList);
                    current = newList;
                }
            }
            else if(found.equals("]")){
                if(current.parent != null)
                    current = current.parent;
            }
            else{
                current.add(Integer.parseInt(found));
            }

        }
        return current;
    }

}