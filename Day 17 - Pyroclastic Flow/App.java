import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var input = Files.readString(Paths.get(file.getAbsolutePath()));
        var tunnel = new Tunnel();
        for(int i = 0; i < 2022; i++){
            var gas = input.charAt(i % input.length()) == '>'; 
            var shape = i % 5;

            tunnel.AddAndDrop(shape, gas);
        }
        System.out.println(tunnel.size());
    }   
}
