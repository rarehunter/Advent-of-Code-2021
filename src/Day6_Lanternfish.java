import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Day6_Lanternfish {
    public static void main(String[] args) {
        File file = new File("./inputs/day6/day6.txt");

        try {
            Scanner sc = new Scanner(file);

            String line = "";
            while(sc.hasNextLine()) {
                line = sc.nextLine();
            }

            List<Integer> fishAges = new ArrayList<>(); // List for tracking each individual fish, used in part 1
            String[] tokens = line.split(",");
            for (String age : tokens) {
                fishAges.add(Integer.parseInt(age));
            }

            Map<Integer, Long> fishTypes = new HashMap<>(); // Map for storing "types" of fish, used in part 2

            // Populate the map with 8 "types" of fish representing the numbers of days they have left.
            for (int i = 0; i <= 8; i++) {
                fishTypes.put(i, (long)0);
            }

            // Iterate through the starting input and count the number of fish per "type" (days they have left)
            for (String age: tokens) {
                int fishAge = Integer.parseInt(age);
                fishTypes.put(fishAge, fishTypes.get(fishAge) + 1);
            }

            int part1 = part1(fishAges, 80);
            System.out.println("Part 1: " + part1);

            long part2 = part2(fishTypes, 256);
            System.out.println("Part 2: " + part2);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: Starting with the list of ages,
    // returns the number of lantern fish present after a given number of days.
    // Appends new fish to the end of the list, thereby tracking each individual
    // fish as its days decrease and new fish are added.
    // Return the size of the list as that is the number of fish present after n days.
    private static int part1(List<Integer> ages, int day) {
        for (int i = 0; i < day; i++) {
            int newFish = 0;
            for (int f = 0; f < ages.size(); f++) {
                int currentAge = ages.get(f);
                // If a fish is already at age 0, we set it to 6 and add another fish with age 8
                if (currentAge == 0) {
                    ages.set(f, 6);
                    newFish++;
                } else {
                    ages.set(f, currentAge-1);
                }
            }

            for (int j = 0; j < newFish; j++) {
                ages.add(8);
            }
        }
        return ages.size();
    }

    // Part 2: Observe that each type of fish (fish with 0 days left, fish with 1 day left) will reproduce
    // in the exact same way, so you only need to simulate one of each every day. In other words,
    // there is no need to track each individual fish but rather, just the number of fishes per "type".
    // We create a dictionary mapping a fish with 0 day remaining, 1 day remaining, ... ,8 days remaining
    // to the number of fish in each category.
    private static long part2(Map<Integer, Long> fishTypes, int day) {
        for (int i = 0; i < day; i++) {
            long fishAtZero = fishTypes.get(0);

            for (int j = 1; j <= 8; j++) {
                fishTypes.put(j-1, fishTypes.get(j));
            }

            // Fishes that were at day 0, get respawned to day 6 in addition
            // to the fishes that were already at day 6
            long daySixFishes = fishTypes.get(6);
            fishTypes.put(6, fishAtZero + daySixFishes);

            // The same number of fishes that were at day 0 will now reproduce fishes at day 8
            fishTypes.put(8, fishAtZero);
        }

        // Sum up each type of fish
        long sum = 0;
        for (Integer fishType : fishTypes.keySet()) {
            sum += fishTypes.get(fishType);
        }

        return sum;
    }
}
