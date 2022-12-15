import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class App {
    static Pattern pattern = Pattern.compile("\\d+");

    public static void main(String[] args) throws Exception {
        var file = new File("input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var senors = ParseInput(input);
    }

    static ArrayList<Sensor> ParseInput(List<String> input) {
        var result = new ArrayList<Sensor>();
        for (String line : input) {
            var matcher = pattern.matcher(line);
            if (matcher.matches()) {
                var sX = Integer.parseInt(matcher.group(0));
                var sY = Integer.parseInt(matcher.group(1));
                var bX = Integer.parseInt(matcher.group(2));
                var bY = Integer.parseInt(matcher.group(3));
                result.add(new Sensor(sX, sY, bX, bY));
            }

        }
        return result;
    }
}