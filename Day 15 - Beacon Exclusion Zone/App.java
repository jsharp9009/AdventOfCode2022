import java.awt.Point;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class App {
    static Pattern pattern = Pattern.compile("-?\\d+");

    public static void main(String[] args) throws Exception {
        var file = new File("input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var parts = ParseInput(input);
        var sensors = (ArrayList<Sensor>)parts[0];
        var beacons = (ArrayList<Point>)parts[1];
        SolvePartOne(sensors, beacons, 2000000);
        SolvePartTwo(sensors, 4000000);
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

    static void SolvePartTwo(ArrayList<Sensor> sensors, Integer maxLine){
        Point p = null;
        for(int i = 0; i < maxLine; i++){
            p = ScanLine(sensors, i, maxLine);
            if(p != null)
                break;
        }
        var frequency = (long)((p.getX() * 4000000) + p.getY());
        System.out.println(p);
        System.out.println("Emergency tunning frequency: " + frequency);
    }

    static Point ScanLine(ArrayList<Sensor> sensors, Integer line, Integer MaxX){
        var canTouch = (Sensor[])sensors.stream().filter(p -> p.CanSee(p.x, line)).sorted((p1, p2) -> Double.compare(p1.getX(), p2.getX())).toArray(Sensor[]::new);        
        ArrayList<Range> ranges = new ArrayList<Range>();
        for (Sensor sensor : canTouch) {
            var xRange = sensor.range - (Math.abs(sensor.y - line));

            var minX = sensor.x - xRange;
            var maxX = sensor.x + xRange;

            if(minX < 0)
                minX = 0;

            if(maxX > MaxX)
                maxX = MaxX;
            
            var newRange = new Range(minX, maxX);
            var newRanges = new ArrayList<Range>();
            for (Range range : ranges) {
                if(!newRange.Merge(range))
                    newRanges.add(range);
            }
            newRanges.add(newRange);
            ranges = newRanges;
        }
        if(ranges.size() > 1) {
            var x = ((ranges.get(1).low - ranges.get(0).high) - 1) + ranges.get(0).high;
                var testPoint = new Point(x, line);
                if(sensors.stream().allMatch(s -> !s.CanSee(testPoint.x, testPoint.y)))
                    return testPoint;
        }

        return null;
    }
}