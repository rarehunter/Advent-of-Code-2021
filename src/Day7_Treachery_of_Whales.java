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

            //int minimumFuel = getMinimumFuelForCrabAlignmentMedian(crabPositions);
            int minimumFuel = getMinimumFuelForCrabAlignmentMean(crabPositions);
            System.out.println("The minimum fuel needed to align the crabs is: " + minimumFuel);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: Find the median of the list of crab positions.
    // The distance between the median and the rest of the values is less than the distance from any other point.
    private static int getMinimumFuelForCrabAlignmentMedian(List<Integer> crabPositions) {
        Collections.sort(crabPositions);
        int median = 0;
        int size = crabPositions.size();
        if (size % 2 == 1) {
            median = crabPositions.get((size - 1) / 2);
        } else {
            median = (crabPositions.get(size / 2) + crabPositions.get((size / 2) - 1))/2;
        }

        int minFuel = 0;
        for (Integer pos : crabPositions) {
            minFuel += Math.abs(pos - median);
        }

        return minFuel;
    }

    private static int calculateMinimumFuelFromPosition(List<Integer> crabPositions, int position) {
        int minFuel = 0;
        for (Integer crabPos : crabPositions) {
            int distance = Math.abs(crabPos - position);
            // sum of numbers from 1,...,n = (n * (n+1)) / 2
            minFuel += (distance * (distance + 1)) / 2;
        }

        return minFuel;
    }

    // Part 2: Find the mean of the list of crab positions.
    private static int getMinimumFuelForCrabAlignmentMean(List<Integer> crabPositions) {
        long sum = 0;
        for (Integer pos : crabPositions) {
            sum += pos;
        }
        int mean = (int)Math.rint(sum / (crabPositions.size()));

        int minFuel = calculateMinimumFuelFromPosition(crabPositions, mean);
        return minFuel;
    }
}
