import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var dStructure = ParseInput(input);
        var result = SolvePartOne(dStructure);
        System.out.println("Part One: " + result);
        result = SolvePartTwo(dStructure);
        System.out.println("Part Two: " + result);
    }

    static pDirectory ParseInput(List<String> input){
        var start = new pDirectory(null, "");
        var currentDirectory = start;
        for(int i = 1; i < input.size(); i++){
            var in = input.get(i);
            if(in.equals("$ ls")) continue;

            if(in.contains("$ cd")){
                var split = in.split(" ");
                var movingTo = split[2];
                if(movingTo.equals(".."))
                    currentDirectory = currentDirectory.parent;
                else
                    currentDirectory = currentDirectory.Children.stream().filter(s -> s.getName().equals(movingTo)).findFirst().get();
                continue;
            }
            
            var split = in.split(" ");
            if(split[0].equals("dir")){
                currentDirectory.Children.add(new pDirectory(currentDirectory, split[1]));
            }
            else{
                currentDirectory.files.add(new pFile(split[1], Integer.parseInt(split[0])));
            }
        }

        return start;
    }

    static Integer SolvePartOne(pDirectory input){
        var sizes = new ArrayList<Integer>();
        GetDirectorySizes(input, sizes);
        return sizes.stream().filter(i -> i < 100000).mapToInt(i -> i).sum();
    }

    static Integer SolvePartTwo(pDirectory input){
        var sizes = new ArrayList<Integer>();
        GetDirectorySizes(input, sizes);
        Integer baseSize = sizes.stream().mapToInt(s -> s).max().getAsInt();
        var remaining = 70000000 - baseSize;
        var needed = 30000000 - remaining;

        return sizes.stream().filter(s -> s >= needed).mapToInt(t -> t).min().getAsInt();
    }

    static Integer GetDirectorySizes(pDirectory input, ArrayList<Integer> sizes){
        Integer totalSize = 0;
        for(var dir : input.Children){
            totalSize += GetDirectorySizes(dir, sizes);
        }

        for(var file : input.files){
            totalSize += file.getSize();
        }
        sizes.add(totalSize);
        return totalSize;
    }
}
