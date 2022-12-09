import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Point;

public class App {
    static Map<Character, Point> Moves = new HashMap<Character, Point>() {
        {
            put('U', new Point(0, -1));
            put('D', new Point(0, 1));
            put('L', new Point(-1, 0));
            put('R', new Point(1, 0));
        }
    };

    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var parsed = ParseInput(input);
        var result = SolvePartOne(parsed);
        System.out.println("Part One - Tail Visits " + result + " squares");
        result = SolvePartTwo(parsed);
        System.out.println("Part Two - Tail Visits " + result + " squares");
    }

    static ArrayList<Map.Entry<Character, Integer>> ParseInput(List<String> input) {
        var result = new ArrayList<Map.Entry<Character, Integer>>();
        for (String move : input) {
            var split = move.split(" ");
            result.add(new AbstractMap.SimpleImmutableEntry<Character, Integer>(split[0].charAt(0),
                    Integer.parseInt(split[1])));
        }
        return result;
    }

    static Integer SolvePartOne(ArrayList<Map.Entry<Character, Integer>> input) {
        var head = new Point(0, 0);
        var tail = new Point(0, 0);
        var tailPositions = new ArrayList<Point>();
        tailPositions.add(tail);

        for (var entry : input) {
            var move = Moves.get(entry.getKey());
            for (int i = 0; i < entry.getValue(); i++) {
                head.move(head.x + move.x, head.y + move.y);
                if (Math.abs(head.x - tail.x) > 1 || Math.abs(head.y - tail.y) > 1) {
                    var moveTowardsHead = CalculateMove(head, tail);
                    tail = new Point(tail.x + moveTowardsHead.x, tail.y + moveTowardsHead.y);
                    var tailX = tail.x;
                    var taily = tail.y;
                    if (!tailPositions.stream().anyMatch(p -> p.x == tailX && p.y == taily)) {
                        tailPositions.add(tail);
                    }
                }
            }
        }
        return tailPositions.size();
    }

    static Integer SolvePartTwo(ArrayList<Map.Entry<Character, Integer>> input) {
        var knots = new ArrayList<Point>();
        var tailPositions = new ArrayList<Point>();
        for (int i = 0; i < 10; i++) {
            knots.add(new Point(0, 0));
        }

        for (var entry : input) {
            var move = Moves.get(entry.getKey());
            for (int i = 0; i < entry.getValue(); i++) {
                var head = knots.get(0);
                knots.get(0).move(head.x + move.x, head.y + move.y);
                for (int n = 0; n < knots.size() - 1; n++) {
                    var cHead = knots.get(n);
                    var cTail = knots.get(n + 1);
                    if (Math.abs(cHead.x - cTail.x) > 1 || Math.abs(cHead.y - cTail.y) > 1) {
                        var moveTowardsHead = CalculateMove(cHead, cTail);
                        knots.set(n + 1, new Point(cTail.x + moveTowardsHead.x, cTail.y + moveTowardsHead.y));
                    }
                }
                var tail = knots.get(knots.size() - 1);
                var tailX = tail.x;
                var taily = tail.y;
                if (!tailPositions.stream().anyMatch(p -> p.x == tailX && p.y == taily)) {
                    tailPositions.add(tail);
                }
            }
        }
        return tailPositions.size();
    }

    static Point CalculateMove(Point head, Point tail) {
        var moveTowardsHead = new Point(head.x - tail.x, head.y - tail.y);
        if (moveTowardsHead.x != 0)
            moveTowardsHead.x = moveTowardsHead.x / Math.abs(moveTowardsHead.x);
        if (moveTowardsHead.y != 0)
            moveTowardsHead.y = moveTowardsHead.y / Math.abs(moveTowardsHead.y);
        return moveTowardsHead;
    }
}
