import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;

public class Tunnel extends LinkedList<BitSet> {
    public final BitSet AllRight = BitSet.valueOf(new long[] { 0, 0, 0, 0, 0, 0, 1 });
    public final BitSet AllLeft = BitSet.valueOf(new long[] { 1, 0, 0, 0, 0, 0, 0 });
    public final BitSet Zero = BitSet.valueOf(new long[] { 0, 0, 0, 0, 0, 0, 0 });

    HashMap<Integer, BitSet[]> Shapes;

    public Tunnel() {
        Shapes = new HashMap<>() {
            {
                put(0, Shape1);
                put(1, Shape2);
                put(2, Shape3);
                put(3, Shape4);
                put(4, Shape5);
            }
        };
    }

    public void AddAndDrop(int shape, Boolean gas) {
        for (int i = 0; i < 3; i++) {
            this.addFirst(new BitSet(7));
        }

        var originalShape = Shapes.get(shape).clone();
        var currentShape = originalShape.clone();
        for (int i = currentShape.length - 1; i >= 0; i--)
            this.addFirst(currentShape[i]);

        var bottomIndex = currentShape.length - 1;
        while (true) {
            Move(currentShape, gas);
            Move(currentShape, gas);

            // var index = this.indexOf(currentShape[currentShape.length-1]);
            if (bottomIndex == this.size() - 1)
                break;
            if (this.get(bottomIndex).intersects(this.get(bottomIndex + 1))) {
                break;
            }
            var movementBottom = bottomIndex - (currentShape.length - 1);
            for (int i = bottomIndex; i >= movementBottom; i--) {
                this.get(i + 1).or(originalShape[i - movementBottom]);
                this.get(i).xor(originalShape[i - movementBottom]);
            }
            // this.set(movementBottom, Zero);
            bottomIndex += 1;
        }

        while (this.get(0).equals(Zero))
            this.pop();
    }

    static void Move(BitSet[] currentShape, boolean right) {
        if (right) {
            if (!Arrays.stream(currentShape).anyMatch(a -> a.get(6))) {
                for (BitSet bitSet : currentShape) {
                    for (Integer i = 6; i > 0; i--) {
                        bitSet.set(i, bitSet.get(i - 1));
                    }
                    bitSet.set(0, 0);
                }
            }
        } else {
            if (!Arrays.stream(currentShape).anyMatch(a -> a.get(0))) {
                for (BitSet bitSet : currentShape) {
                    for (Integer i = 0; i < 6; i++) {
                        bitSet.set(i, bitSet.get(i + 1));
                    }
                    if (bitSet.length() == 7)
                        bitSet.set(6, 0);
                }
            }
        }
    }

    public final BitSet[] Shape1 = new BitSet[] {
            BitSet.valueOf(new long[] { 0, 0, 1, 1, 1, 0, 0 })
    };

    public final BitSet[] Shape2 = new BitSet[] {
            BitSet.valueOf(new long[] { 0, 0, 0, 1, 0, 0, 0 }),
            BitSet.valueOf(new long[] { 0, 0, 1, 1, 1, 0, 0 }),
            BitSet.valueOf(new long[] { 0, 0, 0, 1, 0, 0, 0 })
    };

    public final BitSet[] Shape3 = new BitSet[] {
            BitSet.valueOf(new long[] { 0, 0, 0, 0, 1, 0, 0 }),
            BitSet.valueOf(new long[] { 0, 0, 0, 0, 1, 0, 0 }),
            BitSet.valueOf(new long[] { 0, 0, 1, 1, 1, 0, 0 })
    };

    public final BitSet[] Shape4 = new BitSet[] {
            BitSet.valueOf(new long[] { 0, 0, 1, 0, 0, 0, 0 }),
            BitSet.valueOf(new long[] { 0, 0, 1, 0, 0, 0, 0 }),
            BitSet.valueOf(new long[] { 0, 0, 1, 0, 0, 0, 0 }),
            BitSet.valueOf(new long[] { 0, 0, 1, 0, 0, 0, 0 })
    };

    public final BitSet[] Shape5 = new BitSet[] {
            BitSet.valueOf(new long[] { 0, 0, 1, 1, 0, 0, 0 }),
            BitSet.valueOf(new long[] { 0, 0, 1, 1, 0, 0, 0 })
    };

}
