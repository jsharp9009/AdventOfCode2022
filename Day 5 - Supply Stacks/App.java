import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {
    static ArrayList<Integer> moves = new ArrayList<Integer>();
    static ArrayList<ArrayList<Integer>> buckets = new ArrayList<ArrayList<Integer>>();

    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        ParseInput(input);
        var part1Inputer = CopyBuckets(buckets);
        var result = RunPartOne(part1Inputer);
        System.out.println("Message from Part 1: " + result);

        result = RunPartTwo(buckets);
        System.out.println("Message from Part 2: " + result);
    }

    static void ParseInput(List<String> input) {
        var splitIndex = input.indexOf("");
        List<String> bucketsInput = input.stream().takeWhile(s -> (!s.isEmpty())).collect(Collectors.toList());
        var movesInput = input.stream().skip(splitIndex).collect(Collectors.toList());

        buckets = new ArrayList<ArrayList<Integer>>();
        var numOfBuckets = (bucketsInput.get(0).length() - 2) / 3;
        for (int i = 0; i < numOfBuckets; i++)
            buckets.add(new ArrayList<Integer>());

        for (String line : bucketsInput) {
            var index = 0;
            var chars = line.chars().toArray();
            for (int c : chars) {
                if (c >= 65 && c <= 90) {
                    buckets.get(index / 4).add(c);
                }
                index++;
            }
        }

        // for(var bucket : buckets){
        // var stack = new ArrayDeque<Integer>();

        // for(int i = bucket.size() -1; i >= 0; i--){
        // stack.push(bucket.get(i));
        // }
        // stackBuckets.add(stack);
        // }

        var pattern = Pattern.compile("([0-9]+)");

        for (var move : movesInput) {
            var matcher = pattern.matcher(move);
            while (matcher.find()) {
                moves.add(Integer.parseInt(move.substring(matcher.start(), matcher.end())));
            }
        }
    }

    static String RunPartOne(ArrayList<ArrayList<Integer>> myBuckets) {
        for (int i = 0; i < moves.size(); i += 3) {
            var toMove = moves.get(i);
            var from = moves.get(i + 1) - 1;
            var to = moves.get(i + 2) - 1;

            for (int n = 0; n < toMove; n++) {
                myBuckets.get(to).add(0, myBuckets.get(from).get(0));
                myBuckets.get(from).remove(0);
            }
        }
        String result = "";
        for (var bucket : myBuckets) {
            if (bucket.size() == 0) {
                result += " ";
                continue;
            }
            result += Character.toString(bucket.get(0));
        }
        return result;
    }

    static String RunPartTwo(ArrayList<ArrayList<Integer>> myBuckets) {
        for (int i = 0; i < moves.size(); i += 3) {
            var toMove = moves.get(i);
            var from = moves.get(i + 1) - 1;
            var to = moves.get(i + 2) - 1;

            var itemsToMove = myBuckets.get(from).subList(0, toMove);
            myBuckets.get(to).addAll(0, itemsToMove);
            myBuckets.get(from).subList(0, toMove).clear();
        }
        String result = "";
        for (var bucket : myBuckets) {
            if (bucket.size() == 0) {
                result += " ";
                continue;
            }
            result += Character.toString(bucket.get(0));
        }
        return result;
    }

    static ArrayList<ArrayList<Integer>> CopyBuckets(ArrayList<ArrayList<Integer>> buckets){
        var result = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < buckets.size(); i++){
            result.add(new ArrayList<Integer>(buckets.get(i)));
        }
        return result;
    }
}
