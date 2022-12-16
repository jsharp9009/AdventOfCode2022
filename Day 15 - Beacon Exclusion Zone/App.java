import java.awt.Point;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {
    static Pattern pattern = Pattern.compile("\\d+");

    public static void main(String[] args) throws Exception {
        var file = new File("input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var senors = ParseInput(input);
        SolvePartOne(senors, 2000000);
    }

    static ArrayList<Sensor> ParseInput(List<String> input) {
        var result = new ArrayList<Sensor>();
        for (String line : input) {
            var matcher = pattern.matcher(line);
            Integer sX = 0;
            Integer sY = 0;
            Integer bX = 0;
            Integer bY = 0;
            var count = 0;
            while (matcher.find()) {
                switch(count){
                    case 0: sX = Integer.parseInt(matcher.group()); count++; break;
                    case 1: sY = Integer.parseInt(matcher.group()); count++; break;
                    case 2: bX = Integer.parseInt(matcher.group()); count++; break;
                    case 3: bY = Integer.parseInt(matcher.group()); count++; break;
                }
            }
            result.add(new Sensor(sX, sY, bX, bY));
        }
        return result;
    }

    static void SolvePartOne(ArrayList<Sensor> sensors, Integer line){
        var canTouch = (Sensor[])sensors.stream().filter(p -> p.CanSee(p.x, line)).toArray(Sensor[]::new);
        var beacons = sensors.stream().filter(p -> p.beacon.y == line).distinct().collect(Collectors.toList());
        ArrayList<Point> checked = new ArrayList<Point>();
        for (Sensor sensor : canTouch) {
            if(!checked.stream().anyMatch(p -> p.x == sensor.x && p.y == line)
            && !beacons.stream().anyMatch(p -> p.beacon.x == sensor.x && p.beacon.y == line))
                checked.add(new Point(sensor.x, line));
            
            var x = sensor.x + 1;
            while(true){
                var checkX = x;
                if(sensor.CanSee(new Point(x, line)) 
                && !checked.stream().anyMatch(p -> p.x == checkX && p.y == line)
                && !beacons.stream().anyMatch(p -> p.beacon.x == checkX && p.beacon.y == line)){
                    checked.add(new Point(x, line));
                    x++;
                }
                else{
                    break;
                }
            }

            x = sensor.x - 1;
            while(true){
                var checkX = x;
                if(sensor.CanSee(new Point(x, line)) 
                && !checked.stream().anyMatch(p -> p.x == checkX && p.y == line)
                && !beacons.stream().anyMatch(p -> p.beacon.x == checkX && p.beacon.y == line)){
                    checked.add(new Point(x, line));
                    x--;
                }
                else{
                    break;
                }
            }
        }
        //checked.stream().forEach(s -> System.out.println(s));
        System.out.println("Part One - space scanned: " + checked.stream().distinct().count());
    }
}