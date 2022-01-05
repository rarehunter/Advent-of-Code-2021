import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day7_Treachery_of_Whales {
    public static void main(String[] args) {
        File file = new File("./inputs/day7/day7.txt");

        try {
            Scanner sc = new Scanner(file);
            List<Integer> crabPositions = new ArrayList<>();

            String line = "";
            while(sc.hasNextLine()) {
                line = sc.nextLine();
            }

            String[] tokens = line.trim().split(",");
            for(String token : tokens) {
                crabPositions.add(Integer.parseInt(token));
            }

            int part1 = part1(crabPositions);
            System.out.println("Part 1: " + part1);

            int part2 = part2(crabPositions);
            System.out.println("Part 2: "  + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: Given a list of horizontal positions, find the horizontal position that minimizes
    // the distances that all the crabs have to travel and return the total fuel consumption to
    // travel that minimal distance. Each movement costs one fuel.
    // This essentially boils down to finding the median of the list of crab positions because
    // the distance between the median and the rest of the values is less than the distance from any other point.
    private static int part1(List<Integer> crabPositions) {
        Collections.sort(crabPositions);

        // Calculate the median
        int median = 0;
        int size = crabPositions.size();
        if (size % 2 == 1) {
            median = crabPositions.get((size - 1) / 2);
        } else {
            median = (crabPositions.get(size / 2) + crabPositions.get((size / 2) - 1))/2;
        }

        // Find the total fuel consumption needed for each crab to move to the median.
        int minFuel = 0;
        for (Integer pos : crabPositions) {
            minFuel += Math.abs(pos - median);
        }

        return minFuel;
    }


    // Part 2: Given a list of horizontal positions, find the horizontal position that minimizes the
    // distances that all crabs have to travel and return the total fuel consumption to travel that minimal distance.
    // Each change of 1 step in horizontal position costs 1 more unit of fuel than the last.
    // The intuition here leads us to consider the mean as outliers in the positions can have greater impact
    // on total fuel cost (than in part 1). Finding the mean will nearly minimize the sum of square distances,
    // and the fuel functions are triangular which is O(n^2) (a la Gauss).
    // However, the exact mean is not entirely sufficient. Since we are finding the point at which the derivative
    // of the triangular number is equal 0 of which finding the mean gets us close, there is another term which must
    // be accounted for and therefore, we can find the true solution by not blindly considering the mean but two
    // points "around" it, namely: the floor and ceiling functions of the mean. We return the minimum fuel found
    // after considering those two points.
    private static int part2(List<Integer> crabPositions) {
        // Find the mean
        long sum = 0;
        for (Integer pos : crabPositions) {
            sum += pos;
        }

        double mean = (double)sum / crabPositions.size();

        // Test out two potential locations for minimum fuel values
        int potentialLocation1 = (int)Math.floor(mean);
        int potentialLocation2 = (int)Math.ceil(mean);

        // Find the total fuel consumption needed for each crab to move to the potential locations.
        int minFuel1 = 0;
        int minFuel2 = 0;
        for (Integer crabPos : crabPositions) {
            int distance1 = Math.abs(crabPos - potentialLocation1);
            int distance2 = Math.abs(crabPos - potentialLocation2);
            minFuel1 += (distance1 * (distance1 + 1)) / 2;
            minFuel2 += (distance2 * (distance2 + 1)) / 2;
        }

        return Math.min(minFuel1, minFuel2);
    }
}
