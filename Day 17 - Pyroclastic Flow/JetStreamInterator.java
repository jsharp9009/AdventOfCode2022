import java.util.Iterator;

public class JetStreamInterator implements Iterator<Character> {

    private String input;
    int currentIndex = 0;

    public JetStreamInterator(String input) {
        this.input = input;
    }
    
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Character next() {
        char ch = input.charAt(currentIndex % input.length());
        currentIndex++;
        return ch;
    }

    public int CurrentID() {
        return (currentIndex - 1)  % input.length();
    }
}
