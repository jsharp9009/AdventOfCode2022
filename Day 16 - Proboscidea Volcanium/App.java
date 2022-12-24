import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {
    static Pattern firstPattern = Pattern.compile("Valve ([A-z][A-z]) has flow rate=(\\d+)");
    static Pattern secondPattern = Pattern.compile("([A-Z][A-Z])");

    public static void main(String[] args) throws Exception {
        var file = new File("input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var valves = parseInput(input).stream()
            .sorted((p1, p2) -> p1.getFlowRate().compareTo(p2.getFlowRate()))
            .collect(Collectors.toList());

        var start = valves.stream().filter(v -> v.valveName.equals("AA")).findFirst().get();

        var maxPressure = ReleaseValves(valves, start, 30, 0, false, new HashMap<State, Integer>());
        System.out.println("Max Pressure Part One: " + maxPressure);
        var cache = new HashMap<State, Integer>();
        var maxPressure2 = ReleaseValves(valves, start, 26, 0, true, cache);
        System.out.println("Max Pressure Part Two: " + (maxPressure2));
    }

    static ArrayList<Valve> parseInput(List<String> input) {
        var result = new ArrayList<Valve>();
        for (String line : input) {
            var matcher = firstPattern.matcher(line);
            if (matcher.find())
                result.add(new Valve(matcher.group(1), Integer.parseInt(matcher.group(2))));
        }

        for (String line : input) {
            var matcher = secondPattern.matcher(line);
            var firstMatch = true;
            var addingTo = result.get(0);
            var connected = new ArrayList<Valve>();
            while (matcher.find()) {
                if (firstMatch) {
                    addingTo = result.stream().filter(r -> r.getValveName().equals(matcher.group())).findFirst().get();
                    firstMatch = false;
                    continue;
                }
                connected.add(result.stream().filter(r -> r.getValveName().equals(matcher.group())).findFirst().get());
            }
            addingTo.setLeadsTo(connected.toArray(Valve[]::new));
        }
        return result;
    }

    static HashMap<Integer, Integer> elephantCache = new HashMap<Integer, Integer>();
    static Integer ReleaseValves(List<Valve> valves, Valve currentValve, Integer minute, Integer openedValves, Boolean bovineAsistant, HashMap<State, Integer> _cache) {
        if (minute == 0){
            if(!bovineAsistant)
                return 0;
            if(elephantCache.keySet().contains(openedValves))
                return elephantCache.get(openedValves);
            var start = valves.stream().filter(v -> v.valveName.equals("AA")).findFirst().get();
            var elephantResult = ReleaseValves(valves, start, 26, openedValves, false, new HashMap<State, Integer>());
            elephantCache.put(openedValves, elephantResult);
            return elephantResult;
        }
        final State state = new State(minute, openedValves, currentValve);
        if (_cache.keySet().contains(state))
            return _cache.get(state);

        Integer max = 0;
        if (currentValve.flowRate > 0
                && (openedValves & KeyValue(valves, currentValve)) == 0) {
            openedValves |= KeyValue(valves, currentValve);
            var val = ((minute - 1) * currentValve.flowRate)
                    + ReleaseValves(valves, currentValve, minute - 1, openedValves, bovineAsistant, _cache);
            openedValves &= ~KeyValue(valves, currentValve);
            max = val;
        }
        for (Valve valve : currentValve.leadsTo) {
            max = Math.max(max, ReleaseValves(valves, valve, minute - 1, openedValves, bovineAsistant, _cache));
        }
        _cache.put(state, max);
        return max;
    }

    static HashMap<Valve, Integer> valveCache = new HashMap<Valve, Integer>();

    static Integer KeyValue(List<Valve> valves, Valve valve) {
        if (!valveCache.containsKey(valve))
            valveCache.put(valve, 1 << (valves.indexOf(valve)));
        return valveCache.get(valve);
    }    
}