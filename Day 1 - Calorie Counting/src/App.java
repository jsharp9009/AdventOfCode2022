import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class App {
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));

        var calories = new ArrayList<Integer>();
        calories.add(0);
        for(String line : input){
            if(line.length() == 0){
                calories.add(0);   
                continue;
            }
            
            var newCalores = calories.get(calories.size() - 1) + Integer.parseInt(line);
            calories.set(calories.size() - 1, newCalores); 
        }

        Collections.sort(calories);
        Collections.reverse(calories);
        System.out.println("Max Calories: " + calories.get(0));
        System.out.println("Calories of Top 3: " + (calories.get(0) + calories.get(1) + calories.get(2)));

    }
}
