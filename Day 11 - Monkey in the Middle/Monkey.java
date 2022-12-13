import java.util.LinkedList;
import java.util.List;

public class Monkey {
    LinkedList<Long> Items;
    String Operation;
    public Integer inspected = 0;
    public String getOperation() {
        return Operation;
    }

    Integer Test;
    public Integer getTest() {
        return Test;
    }

    Integer TestTrue;
    public Integer getTestTrue() {
        return TestTrue;
    }

    Integer TestFalse;
    public Monkey(List<Long> items, String operation, Integer test,
            Integer testTrue, Integer testFalse) {
        Items = new LinkedList<>(items);
        Test = test;
        Operation = operation;
        TestTrue = testTrue;
        TestFalse = testFalse;
    }

    public Integer getTestFalse() {
        return TestFalse;
    }

    public Long GetItem() {
        return Items.poll();
    }

    public void CatchItem(Long item){
        Items.add(item);
    }
}
