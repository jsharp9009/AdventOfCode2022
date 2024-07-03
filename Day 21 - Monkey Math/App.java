import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class App {
    static Pattern EquationMonkey = Pattern.compile("([a-z]+): ([a-z]+) ([\\+|\\-|*|\\/]) ([a-z]+)");
    static Pattern ConstantMonkey = Pattern.compile("([a-z]+): ([0-9]+)");
    static String TestPattern = ".*[\\+|\\-|*|\\/].*";

    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var monkeys = ParseInput(lines);
        var answer = GetMonkeyShout("root", monkeys);
        System.out.println("Answer 1: " + answer);

        answer = GetHumanNumber(monkeys);
        System.out.println("Answer 2: " + answer);
    }

    public static Map<String, Monkey> ParseInput(List<String> input) {
        var result = new HashMap<String, Monkey>();
        for (String m : input) {
            if (m.matches(TestPattern)) {
                var matcher = EquationMonkey.matcher(m);
                var monkey = new Monkey();
                monkey.IsConstant = false;
                if (matcher.find()) {
                    monkey.Name = matcher.group(1);
                    monkey.Coefficient1 = matcher.group(2);
                    monkey.Operator = matcher.group(3).charAt(0);
                    monkey.Coefficient2 = matcher.group(4);
                    monkey.CachedAnswer = null;
                    result.put(monkey.Name, monkey);
                }
            } else {
                var matcher = ConstantMonkey.matcher(m);
                var monkey = new Monkey();
                monkey.IsConstant = true;
                if (matcher.find()) {
                    monkey.Name = matcher.group(1);
                    monkey.Constant = Long.parseLong(matcher.group(2));
                    result.put(monkey.Name, monkey);
                }
            }
        }
        return result;
    }

    static long GetMonkeyShout(String m, Map<String, Monkey> monkeys) {
        var monkey = monkeys.get(m);
        if (monkey.IsConstant) {
            return monkey.Constant;
        } else if (monkey.CachedAnswer != null) {
            return monkey.CachedAnswer;
        } else {
            var const1 = GetMonkeyShout(monkey.Coefficient1, monkeys);
            var const2 = GetMonkeyShout(monkey.Coefficient2, monkeys);
            switch (monkey.Operator) {
                case '+':
                    return const1 + const2;
                case '-':
                    return const1 - const2;
                case '*':
                    return const1 * const2;
                case '/':
                    return const1 / const2;
                default:
                    break;
            }
        }
        return 0;
    }

    static long GetHumanNumber(Map<String, Monkey> monkeys) {
        var root = monkeys.get("root");
        var target = GetMonkeyShout(root.Coefficient2, monkeys);

        var humn = monkeys.get("humn");
        List<String> humnChildren = new ArrayList<String>();
        var current = humn;
        while (true) {
            var parent = GetParent(current, monkeys);
            if (parent.Name.equals("root"))
                break;
            humnChildren.add(0, parent.Name);
            current = parent;
        }
        for (String m : humnChildren) {
            var monkey = monkeys.get(m);
            boolean inverse = true;
            var num = 0L;
            if (humnChildren.contains(monkey.Coefficient1) || monkey.Coefficient1.equals("humn")) {
                num = GetMonkeyShout(monkey.Coefficient2, monkeys);
            }
            else if (humnChildren.contains(monkey.Coefficient2) || monkey.Coefficient2.equals("humn")) {
                num = GetMonkeyShout(monkey.Coefficient1, monkeys);
                inverse = false;
            }

            switch (monkey.Operator) {
                case '+':
                    target -= num;
                    break;
                case '-':
                    if (inverse)
                        target += num;
                    else
                        target = num - target;
                    break;
                case '*':
                    target /= num;
                    break;
                case '/':
                    if (inverse)
                        target *= num;
                    else
                        target = num / target;
                    break;
                default:
                    break;
            }
        }
        return target;
    }

    static Monkey GetParent(Monkey m, Map<String, Monkey> monkeys) {
        return monkeys.values().stream()
                .filter(n -> !n.IsConstant && (n.Coefficient1.equals(m.Name) || n.Coefficient2.equals(m.Name)))
                .findFirst().get();
    }
}
