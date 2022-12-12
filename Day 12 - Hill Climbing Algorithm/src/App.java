import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.awt.Point;

public class App {

    static Point startPoint;
    static Point endPoint;
    static List<Point> movements = Arrays.asList(
            new Point(0, 1),
            new Point(1, 0),
            new Point(0, -1),
            new Point(-1, 0));

    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var map = parseInput(input);
        SolvePartOne(map);
        SolvePart2(map);
    }

    static void SolvePartOne(int[][] map){
        var pred = new HashMap<Point, Point>();
        var dist = new HashMap<Point, Integer>();

        TwoArgInterface<Point> validTest = (newPoint, currentPoint) ->{
            return (map[newPoint.y][newPoint.x] - map[currentPoint.y][currentPoint.x]) <= 1;
        };

        TwoArgInterface<Point> endTest = (newPoint, endPoint) ->{
            return newPoint.equals(endPoint);
        };

        var length = BFS(map, startPoint, pred, dist, validTest, endTest);
            System.out.println("Shortest Path: " + length + " steps");

    }

    static void SolvePart2(int[][] map){
        var pred = new HashMap<Point, Point>();
        var dist = new HashMap<Point, Integer>();

        TwoArgInterface<Point> validTest = (newPoint, currentPoint) ->{
            return (map[newPoint.y][newPoint.x] - map[currentPoint.y][currentPoint.x]) >= -1;
        };

        TwoArgInterface<Point> endTest = (newPoint, endPoint) ->{
            return map[newPoint.y][newPoint.x] == 0;
        };

        var length = BFS(map, endPoint, pred, dist, validTest, endTest);
        System.out.println("Shortest Path: " + length + " steps");
    }

    static int[][] parseInput(List<String> input) {
        int[][] result = new int[input.size()][input.get(0).length()];

        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
                result[y][x] = input.get(y).charAt(x) % 'a';
                if (result[y][x] == 'S'){
                    startPoint = new Point(x, y);
                    result[y][x] = 0;
                }
                if (result[y][x] == 'E'){
                    endPoint = new Point(x, y);
                    result[y][x] = 25;
                }
            }
        }

        return result;
    }

    static Integer BFS(int[][] map, Point start, HashMap<Point, Point> pred, HashMap<Point, Integer> Dist, TwoArgInterface<Point> validTest, TwoArgInterface<Point> endTest){
        ArrayList<Point> visited = new ArrayList<Point>();
        var queue = new LinkedList<Point>();
        queue.add(start);
        visited.add(start);
        Dist.put(start, 0);

        while(queue.size() > 0){

            var point = queue.poll();            
            for (Point move : movements){
                var newX = (int) (point.getX() + move.getX());
                var newY = (int) (point.getY() + move.getY());
                
                if (newX < 0 || newX >= map[0].length
                        || newY < 0 || newY >= map.length) continue;

                var newPoint = new Point(newX, newY);
                if(!visited.stream().anyMatch(p -> p.getX() == newX && p.getY() == newY)
                    && validTest.operation(newPoint, point)){
                    
                    visited.add(newPoint);  
                    Dist.put(newPoint, Dist.get(point) + 1);               
                    queue.add(newPoint);
                    pred.put(newPoint, point);

                    if(endTest.operation(newPoint, endPoint)){
                        return Dist.get(point) + 1;
                    }
                }
            }
        }

        return 0;
    }
}
