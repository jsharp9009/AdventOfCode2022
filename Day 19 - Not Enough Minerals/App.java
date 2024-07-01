import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/// Based off of https://github.com/ash42/adventofcode/blob/main/adventofcode2022/src/nl/michielgraat/adventofcode2022/day19/Day19.java
public class App {

    public static Pattern matchPatter = Pattern.compile("[0-9]+");
    private static Map<State, Integer> memory = new HashMap<>();

    public static void main(String[] args) throws IOException {
        var file = new File("input.txt");
        var input = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        var blueprints = GetBlueprints(input);

        var answer = 0;
        for (Blueprint blueprint : blueprints) {
            answer += QualityLevel(blueprint);
        }
        System.out.println("Part 1: " + answer);

        answer = 1;
        for (int i = 0; i < 3; i++) {
            answer *= MaxGeodes(blueprints.get(i), 32);
        }
        System.out.println("Part 2: " + answer);
    }

    public static List<Blueprint> GetBlueprints(List<String> input) {
        List<Blueprint> ret = new LinkedList<Blueprint>();

        for (String lineString : input) {
            ret.add(ParseLine(lineString));
        }
        return ret;
    }

    public static Blueprint ParseLine(String line) {
        var matcher = matchPatter.matcher(line);
        int counter = 0;
        int Id = 0, OreRobots = 0, ClayRobots = 0, ObsidionRobotsOre = 0, ObsidionRobotsClay = 0, GeodeRobotsOre = 0,
                GeodeRobotsOb = 0;
        while (matcher.find()) {
            if (matcher.start() == matcher.end())
                continue;

            var result = Integer.parseInt(line.substring(matcher.start(), matcher.end()));

            switch (counter) {
                case 0:
                    Id = result;
                case 1:
                    OreRobots = result;
                    break;
                case 2:
                    ClayRobots = result;
                    break;
                case 3:
                    ObsidionRobotsOre = result;
                    break;
                case 4:
                    ObsidionRobotsClay = result;
                case 5:
                    GeodeRobotsOre = result;
                    break;
                case 6:
                    GeodeRobotsOb = result;
                    break;
                default:
                    break;
            }
            counter++;
        }
        return new Blueprint(Id, OreRobots, ClayRobots, ObsidionRobotsOre, ObsidionRobotsClay, GeodeRobotsOre,
                GeodeRobotsOb);
    }

    static int MaxGeodesForType(final Blueprint blueprint, int minutesLeft, final Bot goal, int ore, int clay,
            int obsidion,
            int geode,
            final int oreBots, final int clayBots, final int obsidionBots, final int geodeBots) {
        if (minutesLeft == 0)
            return geode;

        final int maxOre = Math.max(blueprint.oreBotCost(), Math.max(blueprint.ClayBotCost(),
                Math.max(blueprint.ObsidionBotCostOre(), blueprint.GeodeBotCostObsidion())));

        if ((goal == Bot.ORE && ore >= maxOre) ||
                (goal == Bot.CLAY && clay >= blueprint.ObsidionBotCostClay())
                || (goal == Bot.Obsidion && (obsidion >= blueprint.GeodeBotCostObsidion() || clay == 0))
                || (goal == Bot.GEODE && obsidion == 0))
            return 0;

        State state = new State(ore, clay, obsidion, geode, oreBots, clayBots, obsidionBots, geodeBots, minutesLeft,
                goal.ordinal());

        if (memory.containsKey(state)) {
            return memory.get(state);
        }

        int max = 0;
        while (minutesLeft > 0) {
            if (goal == Bot.ORE && ore >= blueprint.oreBotCost()) {
                int tmpMax = 0;
                for (int i = 0; i < 4; i++) {
                    var nextGoal = Bot.values()[i];
                    tmpMax = Math.max(tmpMax,
                            MaxGeodesForType(blueprint, minutesLeft - 1, nextGoal,
                                    ore - blueprint.oreBotCost() + oreBots,
                                    clay + clayBots, obsidion + obsidionBots, geode + geodeBots, oreBots + 1, clayBots,
                                    obsidionBots, geodeBots));
                }
                max = Math.max(tmpMax, max);
                memory.put(state, max);
                return max;

            }
            if (goal == Bot.CLAY && ore >= blueprint.ClayBotCost()) {
                int tmpMax = 0;
                for (int i = 0; i < 4; i++) {
                    var nextGoal = Bot.values()[i];
                    tmpMax = Math.max(tmpMax,
                            MaxGeodesForType(blueprint, minutesLeft - 1, nextGoal,
                                    ore - blueprint.ClayBotCost() + oreBots,
                                    clay + clayBots, obsidion + obsidionBots, geode + geodeBots, oreBots, clayBots + 1,
                                    obsidionBots, geodeBots));
                }
                max = Math.max(tmpMax, max);
                memory.put(state, max);
                return max;

            }
            if (goal == Bot.Obsidion && ore >= blueprint.ObsidionBotCostOre()
                    && clay >= blueprint.ObsidionBotCostClay()) {
                int tmpMax = 0;
                for (int i = 0; i < 4; i++) {
                    var nextGoal = Bot.values()[i];
                    tmpMax = Math.max(tmpMax,
                            MaxGeodesForType(blueprint, minutesLeft - 1, nextGoal,
                                    ore - blueprint.ObsidionBotCostOre() + oreBots,
                                    clay - blueprint.ObsidionBotCostClay() + clayBots, obsidion + obsidionBots,
                                    geode + geodeBots, oreBots, clayBots,
                                    obsidionBots + 1, geodeBots));
                }
                max = Math.max(tmpMax, max);
                memory.put(state, max);
                return max;

            }
            if (goal == Bot.GEODE && ore >= blueprint.GeodeBotCostOre()
                    && obsidion >= blueprint.GeodeBotCostObsidion()) {
                int tmpMax = 0;
                for (int i = 0; i < 4; i++) {
                    var nextGoal = Bot.values()[i];
                    tmpMax = Math.max(tmpMax,
                            MaxGeodesForType(blueprint, minutesLeft - 1, nextGoal,
                                    ore - blueprint.GeodeBotCostOre() + oreBots,
                                    clay + clayBots, obsidion - blueprint.GeodeBotCostObsidion() + obsidionBots,
                                    geode + geodeBots, oreBots, clayBots,
                                    obsidionBots, geodeBots + 1));
                }
                max = Math.max(tmpMax, max);
                memory.put(state, max);
                return max;

            }

            minutesLeft--;
            ore += oreBots;
            clay += clayBots;
            obsidion += obsidionBots;
            geode += geodeBots;
            max = Math.max(max, geode);
        }
        memory.put(state, max);
        return max;
    }

    static int MaxGeodes(Blueprint blueprint, int minutes) {
        memory = new HashMap<>();
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = Math.max(result, MaxGeodesForType(blueprint, minutes, Bot.values()[i], 0, 0, 0, 0, 1, 0, 0, 0));
        }
        return result;
    }

    static int QualityLevel(Blueprint b) {
        return MaxGeodes(b, 24) * b.id();
    }

    enum Bot {
        ORE,
        CLAY,
        Obsidion,
        GEODE
    }
}
