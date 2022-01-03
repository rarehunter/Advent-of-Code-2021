import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day1_Sonar_Sweep {
    public static void main(String[] args) {
        File file = new File("./inputs/day1/day1.txt");

        try {
            Scanner sc = new Scanner(file);

            List<Integer> depths = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                depths.add(Integer.parseInt(line));
            }

            int part1 = part1(depths);
            System.out.println("Part 1: " + part1);

            int part2 = part2(depths);
            System.out.println("Part 2: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: Count the number of times a depth measurement increases from the previous measurement.
    // There is no measurement before the first measurement.
    // Iterate through every depth in the list, storing the previous depth seen. If the current depth
    // is greater than the previous depth, increase our count.
    private static int part1(List<Integer> depths) {
        int depthMeasurementIncreases = 0;
        int previousDepth = Integer.MAX_VALUE;

        for (Integer depth : depths) {
            if (depth > previousDepth) {
                depthMeasurementIncreases++;
            }

            previousDepth = depth;
        }

        return depthMeasurementIncreases;
    }

    // Part 2: Using a three-depth sliding window sum, count how many sums are larger than the previous sum.
    // Iterate through consecutive triplet of depths, summing them up and storing it. If the current sum
    // is greater than the previous sum, increase our count.
    private static int part2(List<Integer> depths) {
        int depthMeasurementIncreases = 0;
        int previousSlidingWindowSum = Integer.MAX_VALUE;

        for (int i = 2; i < depths.size(); i++) {
            int slidingWindowSum = depths.get(i) + depths.get(i-1) + depths.get(i-2);

            if (slidingWindowSum > previousSlidingWindowSum) {
                depthMeasurementIncreases++;
            }

            previousSlidingWindowSum = slidingWindowSum;
        }

        return depthMeasurementIncreases;
    }
}
