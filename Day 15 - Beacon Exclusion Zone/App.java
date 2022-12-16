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
        var parts = ParseInput(input);
        var sensors = (ArrayList<Sensor>)parts[0];
        var beacons = (ArrayList<Point>)parts[1];
        SolvePartOne(sensors, beacons, 2000000);
    }

    static Object[] ParseInput(List<String> input) {
        var senors = new ArrayList<Sensor>();
        var beacons = new ArrayList<Point>();
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
            senors.add(new Sensor(sX, sY, Sensor.CalculateDistance(sX, sY, bX, bY)));
            var checkX = bX;
            var checky= bY;
            if(!beacons.stream().anyMatch(p -> p.getX() == checkX && p.getY() == checky))
                beacons.add(new Point(bX, bY));
        }
        return new Object[]{senors, beacons};
    }

    static void SolvePartOne(ArrayList<Sensor> sensors, ArrayList<Point> beacons, Integer line){
        var canTouch = (Sensor[])sensors.stream().filter(p -> p.CanSee(p.x, line)).toArray(Sensor[]::new);        
        var maxX = 0;
        var minX = Integer.MAX_VALUE;

        for (Sensor sensor : canTouch) {
            var xRange = sensor.range - (Math.abs(sensor.y - line));            
            minX = Math.min(sensor.x - xRange, minX);
            maxX = Math.max(sensor.x + xRange, maxX);
        }
        int notBeacons = (maxX + 1) - minX;
        notBeacons -= beacons.stream().filter(p -> p.getY() == line).distinct().count();
        System.out.println("Part One - space scanned: " + notBeacons);
    }
}