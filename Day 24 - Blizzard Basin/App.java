import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class App {

    static ArrayList<Point> cardinalDirections = new ArrayList<Point>(Arrays.asList(
            new Point(0, -1),
            new Point(0, 1),
            new Point(-1, 0),
            new Point(1, 0)));

    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var input = ParseInput(lines);
        var map = (HashMap<Point, Boolean>) input.get(0);
        var blizards = (ArrayList<Point>) input.get(1);
        var blizzardMovements = (ArrayList<Integer>) input.get(2);

        // PrintMap(map, blizards, blizzardMovements);
        var maxY = map.keySet().stream().map(p -> p.Y).max(Comparator.naturalOrder()).get();
        var maxX = map.keySet().stream().map(p -> p.X).max(Comparator.naturalOrder()).get();
        var blizzardStates = GetBlizzardStates(blizards, blizzardMovements, maxX, maxY, map);

        var steps = Walk(map, blizzardStates, blizzardMovements);
        System.out.println("Part 1: " + steps);

    }

    static ArrayList<ArrayList<Point>> GetBlizzardStates(ArrayList<Point> blizzards, ArrayList<Integer> movements,
            int maxX, int maxY, HashMap<Point, Boolean> map) {
        var blizzardStates = new ArrayList<ArrayList<Blizard>>();
        var currBliz = new ArrayList<Blizard>();
        for (int i = 0; i < blizzards.size(); i++) {
            currBliz.add(new Blizard(blizzards.get(i), movements.get(i)));
        }
        blizzardStates.add(currBliz);
        while (true) {
            var nextState = MoveBlizzards(currBliz, maxX, maxY);

            if (blizzardStates.stream().findFirst().get().equals(nextState)) {

                break;
            }

            blizzardStates.add(nextState);
            currBliz = nextState;
        }
        return blizzardStates.stream()
                .map(b -> b.stream().map(Blizard::getPosition).collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static int Walk(HashMap<Point, Boolean> map, ArrayList<ArrayList<Point>> blizzardsStates,
            ArrayList<Integer> blizzardMovements) {
        var count = 0;
        var start = map.entrySet().stream().filter(e -> e.getKey().Y == 0 && e.getValue()).findFirst().get().getKey();
        var maxY = map.keySet().stream().map(p -> p.Y).max(Comparator.naturalOrder()).get();
        var maxX = map.keySet().stream().map(p -> p.X).max(Comparator.naturalOrder()).get();
        var end = map.entrySet().stream().filter(e -> e.getKey().Y == maxY && e.getValue()).findFirst().get().getKey();

        Queue<Point> currStates = new ArrayDeque<Point>();
        currStates.add(start);
        var bliz = 0;
        var memStates = new HashMap<State, ArrayList<Point>>();
        while (true) {
            count++;
            bliz = (bliz + 1) % blizzardsStates.size();
            var blizzards = blizzardsStates.get(bliz);
            // PrintMap(map, blizzards, blizzardMovements);

            var visited = new HashSet<Point>();
            Queue<Point> nextStates = new ArrayDeque<Point>();
            while (currStates.size() > 0) {
                var state = currStates.poll();
                var memState = new State(state, bliz);
                if (memStates.containsKey(memState)) {
                    // nextStates.removeAll(memStates.get(memState));
                    // nextStates.addAll(memStates.get(memState));
                    continue;
                }
                for (Point dirPoint : cardinalDirections) {
                    var check = Point.Add(state, dirPoint);
                    if (check.equals(end))
                            return ++count;
                    if (check.X > 0 && check.X < maxX
                            && check.Y > 0 && check.Y < maxY
                            && !blizzards.contains(check)
                            && !visited.contains(check)) {
                        nextStates.add(check);
                        visited.add(check);
                        if (memStates.containsKey(memState)) {
                            memStates.get(memState).add(check);
                        } else {
                            memStates.put(memState, new ArrayList<Point>(
                                    Arrays.asList(check)));
                        }
                    }
                }
                if (!visited.contains(state)) {
                    if (memStates.containsKey(memState)) {
                        memStates.get(memState).add(state);
                    } else {
                        memStates.put(memState, new ArrayList<Point>(
                                Arrays.asList(state)));
                    }

                    nextStates.add(state);
                    visited.add(state);
                }
            }
            currStates = nextStates;

        }
    }

    static void PrintMap(HashMap<Point, Boolean> map, ArrayList<Point> blizzards, ArrayList<Integer> movements) {
        var maxY = map.keySet().stream().map(p -> p.Y).max(Comparator.naturalOrder()).get();
        var maxX = map.keySet().stream().map(p -> p.X).max(Comparator.naturalOrder()).get();
        System.out.println("\r");
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                var p = new Point(x, y);
                if (!map.get(p))
                    System.out.print('#');
                else if (blizzards.contains(p)) {
                    var dir = cardinalDirections.get(movements.get(blizzards.indexOf(p)));
                    if (dir.X == 1)
                        System.out.print('>');
                    else if (dir.X == -1)
                        System.out.print('<');
                    else if (dir.Y == 1)
                        System.out.print('V');
                    else if (dir.Y == -1)
                        System.out.print('^');
                } else
                    System.out.print('.');
            }
            System.out.println();
        }
        System.out.println();
    }

    static ArrayList<Blizard> MoveBlizzards(ArrayList<Blizard> blizzards, int maxX,
            int maxY) {
        var newBlizards = new ArrayList<Blizard>();
        for (int i = 0; i < blizzards.size(); i++) {
            var newX = blizzards.get(i).postion.X + cardinalDirections.get(blizzards.get(i).direction).X;
            var newY = blizzards.get(i).postion.Y + cardinalDirections.get(blizzards.get(i).direction).Y;

            if (newX == maxX)
                newX = 1;
            if (newX == 0)
                newX = maxX - 1;
            if (newY == maxY)
                newY = 1;
            if (newY == 0)
                newY = maxY - 1;

            newBlizards.add(new Blizard(new Point(newX, newY), blizzards.get(i).direction));
        }
        return newBlizards;
    }

    static List<Object> ParseInput(List<String> input) {
        HashMap<Point, Boolean> map = new HashMap<>();
        ArrayList<Point> blizards = new ArrayList<>();
        ArrayList<Integer> movements = new ArrayList<>();
        for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
                var p = new Point(x, y);
                var c = input.get(y).charAt(x);
                map.put(p, c != '#');

                switch (c) {
                    case '>':
                        blizards.add(p);
                        movements.add(3);
                        break;
                    case '<':
                        blizards.add(p);
                        movements.add(2);
                        break;
                    case 'v':
                        blizards.add(p);
                        movements.add(1);
                        break;
                    case '^':
                        blizards.add(p);
                        movements.add(0);
                        break;

                    default:
                        break;
                }
            }
        }

        return Arrays.asList(map, blizards, movements);
    }

    record Point(int X, int Y) {
        public static Point Add(Point p1, Point p2) {
            return new Point(p1.X + p2.X, p1.Y + p2.Y);
        }
    }

    record Blizard(Point postion, Integer direction) {
        Point getPosition() {
            return postion;
        }
    }

    record State(Point location, int blizzardState) {
    }
}
