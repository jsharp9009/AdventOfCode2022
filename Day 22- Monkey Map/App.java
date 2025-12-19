import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class App {

    static Pattern CommandPattern = Pattern.compile("(\\d+|L|R)");

    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var input = ParseInput(lines);
        var tiles = (HashMap<Point, Character>) input[0];
        var commands = (List<String>) input[1];
        var xMaxMins = (HashMap<Integer, HighLow>) input[2];
        var yMaxMins = (HashMap<Integer, HighLow>) input[3];

        Point start = tiles.keySet().stream().filter((key) -> key.Y == 0)
                .sorted(Comparator.comparing(Point::X)).findFirst().get();

        var part1 = Part1(start, tiles, commands, xMaxMins, yMaxMins);
        System.out.println("Part 1: " + part1);

        var part2 = Part2(start, tiles, 50, commands, xMaxMins, yMaxMins);
        System.out.println("Part 2: " + part2);
    }

    static int Part2(Point start, HashMap<Point, Character> tiles, int blockLength, List<String> commands,
            HashMap<Integer, HighLow> xMaxMins, HashMap<Integer, HighLow> yMaxMins) {
        var neighbors = new HashMap<Face, List<Face>>();
        neighbors.put(Face.front, new ArrayList<>(Arrays.asList(Face.right, Face.bottom, Face.left, Face.top)));
        neighbors.put(Face.back, new ArrayList<>(Arrays.asList(Face.left, Face.bottom, Face.right, Face.top)));
        neighbors.put(Face.left, new ArrayList<>(Arrays.asList(Face.front, Face.bottom, Face.back, Face.top)));
        neighbors.put(Face.right, new ArrayList<>(Arrays.asList(Face.back, Face.bottom, Face.front, Face.top)));
        neighbors.put(Face.top, new ArrayList<>(Arrays.asList(Face.right, Face.front, Face.left, Face.back)));
        neighbors.put(Face.bottom, new ArrayList<>(Arrays.asList(Face.right, Face.back, Face.left, Face.front)));

        var directions = new ArrayList<Point>(Arrays.asList(

                new Point(1, 0),
                new Point(0, 1),
                new Point(-1, 0),
                new Point(0, -1)));

        var offsets = new HashMap<Face, Integer>();
        var faceSegments = new HashMap<Face, Point>();
        HashMap<Point, HashMap<Point, Boolean>> segements = new LinkedHashMap<Point, HashMap<Point, Boolean>>();

        Integer yMax = yMaxMins.values().stream().map(h -> h.High).max(Integer::compare).get() + 1;

        for (int yBlock = 0; yBlock < yMax / blockLength; yBlock++) {
            var yFactor = yBlock * blockLength;
            for (int xBlock = 0; xBlock <= xMaxMins.get(yFactor).High / blockLength; xBlock++) {
                var factor2 = xBlock * blockLength;
                var segment = new Point(xBlock, yBlock);
                segements.put(segment, new HashMap<Point, Boolean>());

                for (int y = 0; y < blockLength; y++) {
                    for (int x = 0; x < blockLength; x++) {
                        var p = new Point(x, y);
                        var factorP = new Point(x + factor2, y + yFactor);
                        if (tiles.containsKey(factorP)) {
                            segements.get(segment).put(p, tiles.get(factorP) == '.');
                        }
                    }
                }
            }
        }

        segements.entrySet().removeIf(e -> e.getValue().size() == 0);

        var stateQueue = new LinkedList<State>();
        var visted = new HashSet<Point>();
        visted.add(segements.keySet().stream().findFirst().get());
        stateQueue.offer(new State(visted.stream().findFirst().get(), Face.front, 1, Face.top));

        while (!stateQueue.isEmpty()) {
            var current = stateQueue.poll();
            faceSegments.put(current.face, current.segment);
            var relativeFrom = current.fromDirection + 2 % 4;
            var offset = (4 + relativeFrom - neighbors.get(current.face).indexOf(current.fromFace)) % 4;
            offsets.put(current.face, offset);

            for (int i = 0; i < 4; i++) {
                var dir = directions.get(i);
                var segment = Point.Add(current.segment, dir);
                if (segements.containsKey(segment) && !visted.contains(segment)) {
                    visted.add(segment);
                    stateQueue.offer(
                            new State(segment, neighbors.get(current.face).get((4 + i - offset) % 4), i, current.face));
                }
            }
        }
        var direction = 0;
        var curPositoin = new Point(0, 0);
        var curFace = Face.front;
        for (var cmd : commands) {
            if (Character.isAlphabetic(cmd.charAt(0))) {
                if (cmd.equals("L")) {
                    direction = (direction + 3) % 4;
                } else {
                    direction = (direction + 1) % 4;
                }
            } else {
                var steps = Integer.parseInt(cmd);

                for (int step = 0; step < steps; step++) {
                    var dir = directions.get(direction);
                    var newPos = Point.Add(curPositoin, dir);
                    var newDirIndex = direction;
                    var newFace = curFace;
                    var valid = false;
                    var contains = segements.get(faceSegments.get(curFace)).containsKey(newPos);
                    if (!contains) {
                        newFace = neighbors.get(curFace).get(((4 + direction - offsets.get(curFace)) % 4));
                        newPos = curPositoin;
                        var from = (direction + 2) % 4;
                        var positionOffset = ((4 + neighbors.get(newFace).indexOf(curFace) - from)) % 4;
                        var offset = offsets.get(newFace);

                        var rotations = (positionOffset + offset) % 4;

                        for (int r = 0; r < rotations; r++) {
                            newDirIndex++;
                            newDirIndex %= 4;
                            newPos = new Point(blockLength - 1 - newPos.Y, newPos.X);
                        }

                        switch (newDirIndex) {
                            case 0:
                                newPos = new Point(0, newPos.Y);
                                break;
                            case 1:
                                newPos = new Point(newPos.X, 0);
                                break;
                            case 2:
                                newPos = new Point(blockLength - 1, newPos.Y);
                                break;
                            case 3:
                                newPos = new Point(newPos.X, blockLength - 1);
                                break;
                            default:
                                break;
                        }

                        valid = segements.get(faceSegments.get(newFace)).get(newPos);

                    } else {
                        valid = segements.get(faceSegments.get(curFace)).get(newPos);
                    }

                    if (!valid)
                        break;
                    curPositoin = newPos;
                    curFace = newFace;
                    direction = newDirIndex;
                }
            }
        }

        var fin = faceSegments.get(curFace);
        var column = fin.X * blockLength + curPositoin.X + 1;
        var row = fin.Y * blockLength + curPositoin.Y + 1;
        return (1000 * row) + (4 * column) + direction;
    }

    static int Part1(Point start, HashMap<Point, Character> tiles, List<String> commands,
            HashMap<Integer, HighLow> xMaxMins, HashMap<Integer, HighLow> yMaxMins) {
        var direction = new Point(1, 0);

        var currPoint = start;
        for (String cmd : commands) {
            if (Character.isAlphabetic(cmd.charAt(0))) {
                if (cmd.equals("L")) {
                    direction = new Point(direction.Y, direction.X * -1);
                } else {
                    direction = new Point(direction.Y * -1, direction.X);
                }
            } else {
                int length = Integer.parseInt(cmd);
                if (direction.X != 0) {
                    for (int i = 0; i < length; i++) {
                        var newX = currPoint.X + direction.X;
                        var highLow = xMaxMins.get(currPoint.Y);
                        if (newX > highLow.High) {
                            newX = highLow.Low;
                        }

                        if (newX < highLow.Low) {
                            newX = highLow.High;
                        }

                        var next = new Point(newX, currPoint.Y);
                        if (tiles.get(next) == '#')
                            break;
                        currPoint = next;
                    }
                } else if (direction.Y != 0) {
                    for (int i = 0; i < length; i++) {
                        var newY = currPoint.Y + direction.Y;
                        var highLow = yMaxMins.get(currPoint.X);
                        if (newY > highLow.High) {
                            newY = highLow.Low;
                        }

                        if (newY < highLow.Low) {
                            newY = highLow.High;
                        }

                        var next = new Point(currPoint.X, newY);
                        if (tiles.get(next) == '#')
                            break;
                        currPoint = next;
                    }
                }
            }
        }
        var dirValue = 0;
        if (direction.X == -1) {
            dirValue = 2;
        } else if (direction.Y == 1) {
            dirValue = 1;
        } else if (direction.Y == -1) {
            dirValue = 3;
        }
        return (1000 * (currPoint.Y + 1)) + (4 * (currPoint.X + 1)) + dirValue;
    }

    public static Object[] ParseInput(List<String> lines) {
        HashMap<Point, Character> tiles = new HashMap<Point, Character>();
        List<String> commands = new ArrayList<String>();

        var xMaxMins = new HashMap<Integer, HighLow>();
        var yMaxMins = new HashMap<Integer, HighLow>();

        var isCommand = false;
        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            if (line.isEmpty()) {
                isCommand = true;
            } else if (isCommand) {
                var m = CommandPattern.matcher((CharSequence) line);
                while (m.find())
                    commands.add(m.group());
                break;
            } else {
                for (int x = 0; x < line.length(); x++) {
                    if (Character.isWhitespace(line.charAt(x)))
                        continue;
                    if (!xMaxMins.containsKey(i)) {
                        xMaxMins.put(i, new HighLow(x, x));
                    } else {
                        xMaxMins.get(i).High = x;
                    }

                    if (!yMaxMins.containsKey(x)) {
                        yMaxMins.put(x, new HighLow(i, i));
                    } else {
                        yMaxMins.get(x).High = i;
                    }
                    tiles.put(new Point(x, i), Character.valueOf(line.charAt(x)));
                }
            }
        }

        return new Object[] { tiles, commands, xMaxMins, yMaxMins };
    }

    record Point(int X, int Y) {
        public static Point Add(Point p1, Point p2) {
            return new Point(p1.X + p2.X, p1.Y + p2.Y);
        }
    }

    static class HighLow {
        public int High;
        public int Low = 10000;

        public HighLow(int high, int low) {
            High = high;
            Low = low;
        }
    }

    record State(Point segment, Face face, Integer fromDirection, Face fromFace) {
    }

    enum Face {
        front,
        back,
        left,
        right,
        top,
        bottom
    }
}
