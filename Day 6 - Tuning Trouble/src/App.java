import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        var file = new File("bin/input.txt");
        var input = Files.readString(Paths.get(file.getAbsolutePath()));
        var result = FindPacketStart(input, 4);
        System.out.println("Packet starts at: " + result);
        
        result = FindPacketStart(input, 14);
        System.out.println("Message starts at: " + result);
    }

    static int FindPacketStart(String input, int length){
        var i = 0;
        while(i < input.length()){
            var collected = new ArrayList<>(length);
            for(int n = i; n < i + length; n++){
                var charToTest = input.charAt(n);
                if(collected.contains(charToTest)){
                    i = input.indexOf(charToTest, i);
                    break;
                }

                collected.add(charToTest);
            }
            if(collected.size() == length)
                return i + length;
            i++;
        }
        return -1;
    }
}
