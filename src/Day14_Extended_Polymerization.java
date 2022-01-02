import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day14_Extended_Polymerization {
    public static void main(String[] args) {
        File file = new File("./inputs/day14/day14.txt");

        try {
            Scanner sc = new Scanner(file);
            String template = "";
            Map<String, String> rules = new HashMap<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("")) continue;

                if (!line.contains("->")) {
                    template = line;
                    continue;
                }

                String[] tokens = line.split(" ");
                rules.put(tokens[0], tokens[2]);
            }

            Map<String, Integer> pairCount = new HashMap<>();
            Map<String, Long> pairCountLong = new HashMap<>();
            for (int i = 1; i < template.length(); i++) {
                String sub = template.substring(i-1, i+1);
                pairCount.put(sub, 1);
                pairCountLong.put(sub, (long)1);
            }

            int part1 = part1(rules, pairCount, 10);
            System.out.println("Part 1 is: " + part1);

            long part2 = part2(rules, pairCountLong, 40);
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Given a map and an element, adds the element to the map if it doesn't already exist
    // or increments its current count.
    private static void addToDictionary(Map<String, Integer> map, String element) {
        if (map.containsKey(element)) {
            int count = map.get(element);
            map.put(element, count+1);
        } else {
            map.put(element, 1);
        }
    }

    // Given a map and an element, decrements its count in the map
    // or removes it from the dictionary if after decrementing, the count would be 0 or negative.
    private static void removeFromDictionary(Map<String, Integer> map, String element) {
        if (map.containsKey(element)) {
            int count = map.get(element);

            if (count <= 1) {
                map.remove(element);
            } else {
                map.put(element, count-1);
            }
        }
    }

    // Given a map of pairs to their count, counts the number of individual characters
    // and returns a map of that information.
    private static Map<String, Integer> countCharacters(Map<String, Integer> pairCount) {
        Map<String, Integer> charCount = new HashMap<>();

        for (String pair : pairCount.keySet()) {
            int count = pairCount.get(pair);

            if (count <= 0) continue;

            for (int i = 1; i <= count; i++) {
                addToDictionary(charCount, pair.substring(0, 1));
                addToDictionary(charCount, pair.substring(1));
            }
        }

        // Because the pairs are overlapping, we have to divide
        // the count by 2 (and take the ceiling) due to over-counting
        for (String character : charCount.keySet()) {
            int currCount = charCount.get(character);
            double halved = currCount / 2.0;
            charCount.put(character, (int)Math.ceil(halved));
        }

        return charCount;
    }

    // Part 1: Keep a dictionary of every pair of characters mapped to its frequency.
    // For every step, look up how each pair of characters is transformed, adding the two pairs
    // created and removing the original pair. In this implementation, I had helper functions for
    // adding and removing from dictionary which was called numerous times in for loops.
    private static int part1(Map<String, String> rules, Map<String, Integer> pairCount, int steps) {
        // For each step cycle, iterate through the pairs in pairCount. This represents the state
        // of the polymer. Each pair generates two pairs that we can increment the count for to our pairCount
        // Remove the original pair from pairCount if it's ever 0.

        for (int i = 1; i <= steps; i++) {
            List<String> pairsToRemove = new ArrayList<>();
            List<String> pairsToAdd = new ArrayList<>();

            for (String pair : pairCount.keySet()) {
                String ruleOutput = rules.get(pair);
                Integer count = pairCount.get(pair);

                // Generate new pairs: if a rule is AB -> C
                // Then the new pairs are: AC and CB
                String newPair1 = pair.substring(0, 1) + ruleOutput;
                String newPair2 = ruleOutput + pair.substring(1);

                for (int j = 1; j <= count; j++) {
                    pairsToAdd.add(newPair1);
                    pairsToAdd.add(newPair2);
                    pairsToRemove.add(pair);
                }
            }

            // Increment the values of the new pairs in the map
            // or add it if it doesn't exist.
            for (String pairToAdd : pairsToAdd) {
                addToDictionary(pairCount, pairToAdd);
            }

            // Finally, decrement the value of the original rule pair
            for (String pairToRemove : pairsToRemove) {
                removeFromDictionary(pairCount, pairToRemove);
            }
        }

        Map<String, Integer> charCount = countCharacters(pairCount);
        int max = 0;
        int min = Integer.MAX_VALUE;
        for (Integer i : charCount.values()) {
            if (i > max) max = i;
            if (i < min) min = i;
        }

        return max - min;
    }

    private static Map<String, Long> countCharactersLong(Map<String, Long> pairCount) {
        Map<String, Long> charCount = new HashMap<>();

        for (String pair : pairCount.keySet()) {
            long count = pairCount.get(pair);

            if (count <= 0) continue;

            String firstChar = pair.substring(0, 1);
            String secondChar = pair.substring(1);

            if (charCount.containsKey(firstChar)) {
                charCount.put(firstChar, charCount.get(firstChar) + count);
            } else {
                charCount.put(firstChar, count);
            }

            if (charCount.containsKey(secondChar)) {
                charCount.put(secondChar, charCount.get(secondChar) + count);
            } else {
                charCount.put(secondChar, count);
            }
        }

        // Because the pairs are overlapping, we have to divide
        // the count by 2 (and take the ceiling) due to over-counting
        for (String character : charCount.keySet()) {
            long currCount = charCount.get(character);
            double halved = currCount / 2.0;
            charCount.put(character, (long)Math.ceil(halved));
        }

        return charCount;
    }


    // Part 2: Same concept as part 1 but it turns out that looping through helper functions for dictionary
    // operations were slow as the number of steps increased. Instead, kept another two dictionaries
    // for determining the exact number of pairs to add or remove instead of looping through it.
    private static long part2(Map<String, String> rules, Map<String, Long> pairCount, int steps) {
        // For each step cycle, iterate through the pairs stored in the rules
        // map. Each pair generates two pairs that we can increment the count for to our pairCount
        // Remove the original pair from pairCount

        for (int i = 1; i <= steps; i++) {
            Map<String, Long> pairsToRemove = new HashMap<>();
            Map<String, Long> pairsToAdd = new HashMap<>();

            for (String pair : pairCount.keySet()) {
                String ruleOutput = rules.get(pair);
                Long count = pairCount.get(pair);

                // Generate new pairs: if a rule is AB -> C
                // Then the new pairs are: AC and CB
                String newPair1 = pair.substring(0, 1) + ruleOutput;
                String newPair2 = ruleOutput + pair.substring(1);

                if (pairsToAdd.containsKey(newPair1)) {
                    pairsToAdd.put(newPair1, pairsToAdd.get(newPair1) + count);
                } else {
                    pairsToAdd.put(newPair1, count);
                }

                if (pairsToAdd.containsKey(newPair2)) {
                    pairsToAdd.put(newPair2, pairsToAdd.get(newPair2) + count);
                } else {
                    pairsToAdd.put(newPair2, count);
                }

                if (pairsToRemove.containsKey(pair)) {
                    pairsToRemove.put(pair, pairsToRemove.get(pair) + count);
                } else {
                    pairsToRemove.put(pair, count);
                }
            }

            // Increment the values of the new pairs in the map
            // or add it if it doesn't exist.
            for (String pairToAdd : pairsToAdd.keySet()) {
                long numToAdd = pairsToAdd.get(pairToAdd);

                if (pairCount.containsKey(pairToAdd)) {
                    long currentNum = pairCount.get(pairToAdd);
                    pairCount.put(pairToAdd, currentNum + numToAdd);
                } else {
                    pairCount.put(pairToAdd, numToAdd);
                }
            }

            // Finally, decrement the value of the original rule pair
            for (String pairToRemove : pairsToRemove.keySet()) {
                long numToRemove = pairsToRemove.get(pairToRemove);
                long currentNum = pairCount.get(pairToRemove);
                long result = currentNum - numToRemove;

                if (result <= 0) {
                    pairCount.remove(pairToRemove);
                } else {
                    pairCount.put(pairToRemove, result);
                }
            }

        }

        Map<String, Long> charCount = countCharactersLong(pairCount);

        long max = 0;
        long min = Long.MAX_VALUE;
        for (Long i : charCount.values()) {
            if (i > max) max = i;
            if (i < min) min = i;
        }

        return max - min;
    }
}
