import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    static ArrayList<Point> fullDirections = new ArrayList<Point>(Arrays.asList(
            new Point(0, -1),
            new Point(0, 1),
            new Point(-1, 0),
            new Point(1, 0),

            new Point(1, 1),
            new Point(1, -1),
            new Point(-1, -1),
            new Point(-1, 1)));

    static ArrayList<Point> north = new ArrayList<Point>(Arrays.asList(
            new Point(0, -1),
            new Point(1, -1),
            new Point(-1, -1)));
    static ArrayList<Point> south = new ArrayList<Point>(Arrays.asList(
            new Point(0, 1),
            new Point(1, 1),
            new Point(-1, 1)));
    static ArrayList<Point> east = new ArrayList<Point>(Arrays.asList(
            new Point(1, 0),
            new Point(1, 1),
            new Point(1, -1)));
    static ArrayList<Point> west = new ArrayList<Point>(Arrays.asList(
            new Point(-1, 0),
            new Point(-1, -1),
            new Point(-1, 1)));

    static ArrayList<ArrayList<Point>> faces = new ArrayList<>(
            Arrays.asList(
                    north, south, west, east));

    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var elves = ParseInput(lines);

        var dir = 0;
        var count = 0;
        while(true) {

            var newElves = ElfProcess(elves, dir);
            dir = (dir + 1) % 4;
            //Print(elves);
            if(count == 10){
                PrintPart1Answer(elves);
            }
            count++;

            if(newElves.equals(elves)) break;
            elves = newElves;
        }

       System.err.println("Part 2: " + count);
    }

    static void PrintPart1Answer(HashSet<Point> elves){
         var maxX = elves.stream().map(c -> c.X).max(Comparator.naturalOrder()).get();
        var minX = elves.stream().map(c -> c.X).min(Comparator.naturalOrder()).get();

        var maxY = elves.stream().map(c -> c.Y).max(Comparator.naturalOrder()).get();
        var minY = elves.stream().map(c -> c.Y).min(Comparator.naturalOrder()).get();

        var area = (maxX - minX + 1) * (maxY - minY + 1);

        System.err.println("Part 1: " + (area - elves.size()));
    }
    
    static void Print(HashSet<Point> elves){
        var maxX = elves.stream().map(c -> c.X).max(Comparator.naturalOrder()).get();
        var minX = elves.stream().map(c -> c.X).min(Comparator.naturalOrder()).get();

        var maxY = elves.stream().map(c -> c.Y).max(Comparator.naturalOrder()).get();
        var minY = elves.stream().map(c -> c.Y).min(Comparator.naturalOrder()).get();

        for(int y = minY; y <= maxY; y++){
            for(int x = minX; x <= maxX; x++){
                var p = new Point(x, y);
                if(elves.contains(p)) System.out.print('#');
                else System.err.print('.');
            }
            System.err.println();
        }
    }

    static HashSet<Point> ElfProcess(HashSet<Point> elves, int direction) {
        HashMap<Point, Integer> pointCounts = new HashMap<Point, Integer>();
        var propsed = new HashMap<Point, Point>();
        for (var elf : elves) {
            var neighbors = CountNeighbors(elf, elves);

            if (neighbors.size() > 0) {
                var moved = false;
                for (int i = 0; i < 4; i++) {
                    var checkDir = (direction + i) % 4;
                    if (!neighbors.containsKey(fullDirections.get(checkDir))) {
                        moved = true;
                        var p = Point.Add(elf, fullDirections.get(checkDir));
                        propsed.put(elf, p);
                        if (pointCounts.containsKey(p))
                            pointCounts.put(p, pointCounts.get(p) + 1);
                        else
                            pointCounts.put(p, 1);
                        break;
                    }
                }

                if(!moved) propsed.put(elf, elf);

            }
            else{
                propsed.put(elf, elf);
                pointCounts.put(elf, 1);
            }
        }

        List<Point> mutliple = pointCounts.entrySet().stream().filter(e -> e.getValue() > 1).map(c -> c.getKey())
                .collect(Collectors.toList());

        var newElves = propsed.entrySet().stream().map(e -> mutliple.contains(e.getValue()) ? e.getKey() : e.getValue())
                .collect(Collectors.toCollection(HashSet::new));
        return newElves;
    }

    static HashMap<Point, Integer> CountNeighbors(Point elf, HashSet<Point> elves) {
        var neighbors = new HashMap<Point, Integer>();

        for (int face = 0; face < 4; face++) {
            var dirs = faces.get(face);
            for (int n = 0; n < dirs.size(); n++) {
                var dir = dirs.get(n);
                var check = Point.Add(elf, dir);
                if (elves.contains(check)) {
                    if (neighbors.containsKey(fullDirections.get(face)))
                        neighbors.put(fullDirections.get(face), neighbors.get(fullDirections.get(face)) + 1);
                    else
                        neighbors.put(fullDirections.get(face), 1);
                }
            }
        }

        return neighbors;
    }

    static HashSet<Point> ParseInput(List<String> input) {
        var elves = new HashSet<Point>();
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
                if (input.get(y).charAt(x) == '#') {
                    elves.add(new Point(x, y));
                }
            }
        }
        return elves;
    }

    record Point(int X, int Y) {
        public static Point Add(Point p1, Point p2) {
            return new Point(p1.X + p2.X, p1.Y + p2.Y);
        }
    }
}
