import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import java.awt.Point;

public class App {
    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var rocks = ParseInput(input);
        Print(rocks);
        SolvePartOne(rocks);
        SolvePartTwo(rocks);
    }

    static HashMap<Integer,ArrayList<Integer>> ParseInput(List<String> input) {
        var result = new HashMap<Integer,ArrayList<Integer>>();
        for (String line : input) {
            var indices = line.split(" -> ");
            Point previous = null;
            for (int i = 1; i < indices.length; i++) {
                if (previous == null)
                    previous = ReadPoint(indices[i - 1]);
                var current = ReadPoint(indices[i]);
                var points = GetPointsBetween(previous, current);
                for(var point : points){
                    if(!result.containsKey(point.y)){
                        result.put(point.y, new ArrayList<Integer>());
                    }
                    result.get(point.y).add(point.x);
                }

                previous = current;
            }
        }
        return result;
    }

    static void Print(HashMap<Integer,ArrayList<Integer>> rocks){
        var minX = Collections.min(flatten(rocks.values()).toList());
        var maxX = Collections.max(flatten(rocks.values()).toList());
        var maxY = Collections.max(rocks.keySet());
        for(int y = 0; y <= maxY; y++){
            for(int x = minX; x <= maxX; x++){
                if(rocks.containsKey(y) && rocks.get(y).contains(x)){
                    System.out.print("#");
                }
                else{
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
    }

    static Point ReadPoint(String s) {
        var parts = s.split(",");
        return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    static ArrayList<Point> GetPointsBetween(Point start, Point end) {
        var result = new ArrayList<Point>();

        if (start.x != end.x) {
            if (start.x > end.x) {
                var holder = start;
                start = end;
                end = holder;
            }
            for (int x = start.x; x <= end.x; x++) {
                result.add(new Point(x, start.y));
                //System.out.println("(" + x + ", " + start.y + ")");
            }
        }

        if (start.y != end.y) {
            if (start.y > end.y) {
                var holder = start;
                start = end;
                end = holder;
            }
            for (int y = start.y; y <= end.y; y++) {
                result.add(new Point(start.x, y));
                //System.out.println("(" + start.x + ", " + y + ")");
            }
        }

        return result;
    }

    static void SolvePartOne(HashMap<Integer,ArrayList<Integer>> rocks){
        var myRocks = new HashMap<>(rocks);
        var unitCount = 0;
        var largestY = Collections.max(myRocks.keySet());
        
        while(true){
            unitCount++;
            var breakout = false;
            var sand = new Point(500, 0);

            while(true){
                if(sand.y >= largestY) {
                    breakout = true;
                    break;
                }

                if(!myRocks.containsKey(sand.y + 1) || !myRocks.get(sand.y + 1).contains(sand.x)){
                    sand.y+=1;
                    continue;
                }
                if(!myRocks.containsKey(sand.y + 1) || !myRocks.get(sand.y + 1).contains(sand.x - 1)){
                    sand.y+=1;
                    sand.x-=1;
                    continue;
                }
                if(!myRocks.containsKey(sand.y + 1) || !myRocks.get(sand.y + 1).contains(sand.x + 1)){
                    sand.y+=1;
                    sand.x+=1;
                    continue;
                }
                break;
            }
            if(!myRocks.containsKey(sand.y))
                myRocks.put(sand.y, new ArrayList<Integer>());
            myRocks.get(sand.y).add(sand.x);
            //System.out.println(sand);
            if(breakout)
                break;
        }
        Print(myRocks);
        System.out.println("Part One Unit Count: " + (unitCount - 1));
    }

    static void SolvePartTwo(HashMap<Integer,ArrayList<Integer>> rocks){
        var myRocks = new HashMap<>(rocks);
        var unitCount = 0;
        var largestY = Collections.max(myRocks.keySet()) + 2;
        
        while(true){
            unitCount++;
            var sand = new Point(500, 0);

            while(true){
                if(sand.y == largestY){
                    break;
                }
                if(!myRocks.containsKey(sand.y + 1) || !myRocks.get(sand.y + 1).contains(sand.x)){
                    sand.y+=1;
                    continue;
                }
                if(!myRocks.containsKey(sand.y + 1) || !myRocks.get(sand.y + 1).contains(sand.x - 1)){
                    sand.y+=1;
                    sand.x-=1;
                    continue;
                }
                if(!myRocks.containsKey(sand.y + 1) || !myRocks.get(sand.y + 1).contains(sand.x + 1)){
                    sand.y+=1;
                    sand.x+=1;
                    continue;
                }
                break;
            }
            if(!myRocks.containsKey(sand.y))
                myRocks.put(sand.y, new ArrayList<Integer>());
            myRocks.get(sand.y).add(sand.x);
            //System.out.println(sand);
            if(sand.x == 500 && sand.y == 0)
                break;
        }
        Print(myRocks);
        System.out.println("Part Two Unit Count: " + (unitCount - 1));
    }

    public static<T> Stream<T> flatten(Collection<ArrayList<T>> values)
    {
        Stream<T> stream = values.stream()
                                .flatMap(x -> x.stream());
 
        return stream;
    }
 }