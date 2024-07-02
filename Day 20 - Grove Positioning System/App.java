import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        List<Entry> input = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            input.add(new Entry(Integer.parseInt(lines.get(i)), i));
        }
        var output = Mix(input, 1);
        var zeroIndex = output.indexOf(0L);
        long answer = FindValueAtIndex(zeroIndex, 1000, output) + FindValueAtIndex(zeroIndex, 2000, output)
                + FindValueAtIndex(zeroIndex, 3000, output);
        System.out.println("Part 1: " + answer);

        var input2 = input.stream().map(c -> new Entry(c.value * 811589153L, c.originalIndex))
                .collect(Collectors.toList());
        output = Mix(input2, 10);
        zeroIndex = output.indexOf(0L);
        answer = FindValueAtIndex(zeroIndex, 1000, output) + FindValueAtIndex(zeroIndex, 2000, output)
                + FindValueAtIndex(zeroIndex, 3000, output);
        System.out.println("Part 2: " + answer);
    }

    static List<Long> Mix(List<Entry> input, int times) {
        List<Entry> output = new ArrayList<>(input);
        for (int i = 0; i < times; i++) {
            for (Entry e : input) {
                var start = output.indexOf(e);
                var end = (start + e.value) % (input.size() - 1);
                if (end < 0)
                    end += (input.size() - 1);
                output.remove(start);
                output.add((int)end, e);
            }
        }
        return output.stream().map(e -> e.value).collect(Collectors.toList());
    }

    static long FindValueAtIndex(int zeroIndex, int toFind, List<Long> input) {
        var index = (zeroIndex + toFind) % input.size();
        return input.get(index); 
    }

    record Entry(long value, int originalIndex){}
}
