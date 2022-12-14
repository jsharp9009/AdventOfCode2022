import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ArrayListWithParent<T> extends ArrayList<T> implements Comparable<ArrayListWithParent<T>> {
    ArrayListWithParent<T> parent;

    public ArrayListWithParent(Collection<? extends T> c) {
        super(c);
    }

    public ArrayListWithParent() {
    }

    public ArrayListWithParent<T> getParent() {
        return parent;
    }

    public void setParent(ArrayListWithParent<T> parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(final ArrayListWithParent<T> Right) {
        var lastI = 0;
        for(int i = 0; i < this.size(); i++){
            lastI = i;
            var leftItem = this.get(i);
            if(Right.size() <= i)
                return 1;
            var rightItem = Right.get(i);

            if(leftItem instanceof Integer && rightItem instanceof Integer){
                var result = ((Integer)leftItem).compareTo((Integer)rightItem);
                if(result != 0) return result;
            }
            else if(leftItem instanceof ArrayListWithParent && rightItem instanceof Integer){
                var result = ((ArrayListWithParent<Object>)leftItem).compareTo(new ArrayListWithParent<Object>(Arrays.asList(rightItem)));
                if(result != 0) return result;

            }
            else if(leftItem instanceof Integer && rightItem instanceof ArrayListWithParent){
                var result = new ArrayListWithParent<Object>(Arrays.asList(leftItem)).compareTo((ArrayListWithParent<Object>)rightItem);
                if(result != 0) return result;
            }
            else{
                var result = ((ArrayListWithParent)leftItem).compareTo((ArrayListWithParent)rightItem);
                if(result != 0) return result;
            }
        }
        if(Right.size() == this.size())
            return 0;
        else return -1;
    }

    @Override
    public boolean equals(final Object obj) {
        return this.compareTo((ArrayListWithParent<T>)obj) == 0;
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();
        sb.append("[");
        for (T t : this) {
            sb.append(t.toString() + ",");
        }
        return sb.substring(0, sb.length() - 1) + "]";
    }
}
