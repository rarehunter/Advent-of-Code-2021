import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day1_Sonar_Sweep {
    public static void main(String[] args) {
        try {
            Stream<String> lines = Files.lines(Path.of("./inputs/day1/day1.txt"));
            IntStream intStream = lines.mapToInt(num -> Integer.parseInt(num));
            Iterable<Integer> iterable = intStream::iterator;

            //int partOneCount = getDepthMeasurementIncreases(iterable);
            int partTwoCount = getDepthMeasurementIncreasesUsingSlidingWindow(intStream);

            //System.out.println("Number of times depth measurement increases: " + partOneCount);
            System.out.println("Number of times depth measurement increases using three-measurement sliding window: " + partTwoCount);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1
    private static int getDepthMeasurementIncreases(Iterable<Integer> iterable) {
        int depthMeasurementIncreases = 0;
        int previousDepth = Integer.MAX_VALUE;

        for (Integer depth : iterable) {
            if (depth > previousDepth) {
                depthMeasurementIncreases++;
            }

            previousDepth = depth;
        }

        return depthMeasurementIncreases;
    }

    // Part 2
    private static int getDepthMeasurementIncreasesUsingSlidingWindow(IntStream intStream) {
        List<Integer> depths = intStream.boxed().toList();
        int depthMeasurementIncreases = 0;
        int previousSlidingWindowSum = Integer.MAX_VALUE;

        for (int i = 2; i < depths.size(); i++) {
            int slidingWindowSum = depths.get(i) + depths.get(i - 1) + depths.get(i - 2);

            if (slidingWindowSum > previousSlidingWindowSum) {
                depthMeasurementIncreases++;
            }

            previousSlidingWindowSum = slidingWindowSum;
        }

        return depthMeasurementIncreases;
    }
}
