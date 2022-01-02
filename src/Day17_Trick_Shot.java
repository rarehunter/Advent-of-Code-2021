import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Day17_Trick_Shot {
    public static void main(String[] args) {
        File file = new File("./inputs/day17/day17.txt");

        try {
            Scanner sc = new Scanner(file);
            String line = "";

            while (sc.hasNextLine()) {
                line = sc.nextLine();
            }

            String[] tokens = line.split(" ");
            String xArea = tokens[2];
            String yArea = tokens[3];

            String[] xBounds = xArea.substring(2, xArea.length() - 1).split("[.][.]");
            String[] yBounds = yArea.substring(2).split("[.][.]");

            int topLeftX = Integer.parseInt(xBounds[0]);
            int bottomRightX = Integer.parseInt(xBounds[1]);
            int topLeftY = Integer.parseInt(yBounds[1]);
            int bottomRightY = Integer.parseInt(yBounds[0]);

            // Construct the top-left and bottom-right coordinates of the target area.
            Point topLeft = new Point(topLeftX, topLeftY);
            Point bottomRight = new Point(bottomRightX, bottomRightY);

            int part1 = part1(topLeft, bottomRight);
            System.out.println("Part 1 is: " + part1);

            int part2 = part2(topLeft, bottomRight);
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Given a probe position (pp), its velocity, and the top left and bottom points of the target area,
    // returns whether the probe position is past the target area.
    private static boolean isProbePastTarget(Point pp, Point probeVelocity, Point targetTopLeft, Point targetBottomRight) {
        if (pp.y < targetBottomRight.y && probeVelocity.y < 0) {
            return true;
        } else if (pp.x > targetBottomRight.x && probeVelocity.y < 0) {
            return true;
        }

        return false;
    }

    // Given a probe position (pp) and the top left and bottom points of the target area,
    // returns whether the probe position is within the target area.
    private static boolean isProbeInTarget(Point pp, Point targetTopLeft, Point targetBottomRight) {
        return (pp.x >= targetTopLeft.x &&
            pp.x <= targetBottomRight.x &&
            pp.y <= targetTopLeft.y &&
            pp.y >= targetBottomRight.y);
    }

    // Executes a "step" of a probe:
    // x position increases by its x velocity, y position increases by its y velocity.
    // x velocity changes by 1 "towards" the value of 0.
    // y velocity decreases by 1
    private static void stepProbe(Point probePosition, Point probeVelocity) {
        probePosition.x += probeVelocity.x;
        probePosition.y += probeVelocity.y;

        if (probeVelocity.x > 0) {
            probeVelocity.x -= 1;
        } else if (probeVelocity.x < 0) {
            probeVelocity.x += 1;
        }

        probeVelocity.y -= 1;
    }

    // Part 1: Brute force solution to try a series of potential velocities, stepping through the probe's position
    // and keeping track of the probe's highest Y value if it were to hit the target area.
    private static int part1(Point targetTopLeft, Point targetBottomRight) {
        Point probe;
        int highestY = 0;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                probe = new Point(0, 0);
                Point potentialVelocity = new Point(x, y);

                int potentialHighestY = 0;
                while (!isProbePastTarget(probe, potentialVelocity, targetTopLeft, targetBottomRight)) {
                    stepProbe(probe, potentialVelocity);

                    if (probe.y > potentialHighestY) {
                        potentialHighestY = probe.y;
                    }

                    if (isProbeInTarget(probe, targetTopLeft, targetBottomRight)) {
                        if (potentialHighestY > highestY) {
                            highestY = potentialHighestY;
                        }
                        break;
                    }
                }
            }
        }

        return highestY;
    }

    // Part 2: Brute force solution to try a series of potential velocities, stepping through the probe's position
    // and keeping track of the number of target area hits.
    private static int part2(Point targetTopLeft, Point targetBottomRight) {
        Point probe;
        int numVelocities = 0;

        for (int x = 0; x < 500; x++) {
            for (int y = -500; y < 500; y++) {
                probe = new Point(0, 0);
                Point potentialVelocity = new Point(x, y);

                while (!isProbePastTarget(probe, potentialVelocity, targetTopLeft, targetBottomRight)) {
                    stepProbe(probe, potentialVelocity);

                    if (isProbeInTarget(probe, targetTopLeft, targetBottomRight)) {
                        numVelocities++;
                        break;
                    }
                }
            }
        }

        return numVelocities;
    }

    // Part 1: Initial solution using math and probably only applies if both y values of the target area
    // are less than 0. Stepping through a few steps of the example solution reveals that the y position is symmetrical
    // over time. A positive y velocity increases the y position in each step but as the y velocity decreases, the rate
    // of increase in the y position slows down until it hits a peak when y velocity hits 0.
    // Then, as y velocity continues to decrease in the negative numbers, the y position starts to decrease as well
    // in the opposite rate that it increased (hence the y-position symmetry). Once the y position has hit 0,
    // one more step puts the y-position right at the lower edge of our target area. Therefore, an observation is made
    // that the highest Y position is achieved when the last step of our iteration puts the y-position right at the
    // lower edge of our target area (where y-position is the smaller y value in the target area).
    // The optimal y velocity in this case is -(lowestYPosition - 1) which we'll call highestPossibleYVelocity.
    // Finally, in order to get the highest y position, we sum up all the y velocities over time until 0:
    // (highestPossibleYVelocity * (highestPossibleYVelocity + 1)) / 2
    private static int part1_math(Point targetTopLeft, Point targetBottomRight) {
        int lowestYCoord = targetBottomRight.y;
        int highestPossibleYVelocity = -(lowestYCoord + 1);

        int summation = (highestPossibleYVelocity * (highestPossibleYVelocity+1)) / 2;
        return summation;
    }

}
