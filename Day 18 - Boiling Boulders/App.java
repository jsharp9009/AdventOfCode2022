import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

public class App {
    public static final List<Cube> Adjacents = new LinkedList<>() {
        {
            add(new Cube(0, 0, 1));
            add(new Cube(0, 0, -1));
            add(new Cube(0, 1, 0));
            add(new Cube(0, -1, 0));
            add(new Cube(1, 0, 0));
            add(new Cube(-1, 0, 0));
        }
    };

    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var cubeInput = ParseInput(input);
        var sides = CountAllSides(cubeInput);
        System.out.println("Part 1 Total Sides: " + sides);

        var maxX = cubeInput.stream().mapToInt(x -> x.X()).max().getAsInt() + 1;
        var minX = cubeInput.stream().mapToInt(x -> x.X()).min().getAsInt() - 1;
        var maxY = cubeInput.stream().mapToInt(x -> x.Y()).max().getAsInt() + 1;
        var minY = cubeInput.stream().mapToInt(x -> x.Y()).min().getAsInt() - 1;
        var maxZ = cubeInput.stream().mapToInt(x -> x.Z()).max().getAsInt() + 1;
        var minZ = cubeInput.stream().mapToInt(x -> x.Z()).min().getAsInt() - 1;

        var minCube = new Cube(minX, minY, minZ);
        var maxCube = new Cube(maxX, maxY, maxZ);

        var area = TraversceSteam(minCube, minCube, maxCube, cubeInput);
        System.out.println("Part 2 Total Sides: " + area);
    }

    public static List<Cube> ParseInput(List<String> input) {
        var ret = new LinkedList<Cube>();
        for (String coord : input) {
            var coords = coord.split(",");
            ret.add(new Cube(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
        }
        return ret;
    }

    public static int CountAllSides(List<Cube> input) {
        var total = 0;
        for (Cube cube : input) {
            total += CountCubeSides(cube, input);
        }
        return total;
    }

    public static int CountCubeSides(Cube cube, List<Cube> input) {
        var count = 6;
        for (Cube offsetCube : Adjacents) {
            if (input.contains(
                    new Cube(cube.X() + offsetCube.X(), cube.Y() + offsetCube.Y(), cube.Z() + offsetCube.Z()))) {
                count--;
            }
        }
        return count;
    }

    public static int TraversceSteam(Cube start, Cube min, Cube max, List<Cube> droplet) {
        var toCheck = new ArrayDeque<Cube>();
        toCheck.push(start);
        var area = 0;
        List<Cube> visisted = new LinkedList<Cube>();

        while (!toCheck.isEmpty()) {
            var cube = toCheck.pop();
            visisted.add(cube);
            for (Cube ajacent : Adjacents) {
                var nextCube = cube.Add(ajacent);
                if (visisted.contains(nextCube))
                    continue;
                if (nextCube.X() < min.X() || nextCube.Y() < min.Y() || nextCube.Z() < min.Z())
                    continue;
                if (nextCube.X() > max.X() || nextCube.Y() > max.Y() || nextCube.Z() > max.Z())
                    continue;
                if (droplet.contains(nextCube))
                {
                    area++;
                    continue;
                }
                if (toCheck.contains(nextCube))
                    continue;

                toCheck.push(nextCube);
            }
        }
        return area;
    }

}
