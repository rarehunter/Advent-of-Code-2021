import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day25_Sea_Cucumber {
    public static void main(String[] args) {
        File file = new File("./inputs/day25/day25.txt");

        try {
            Scanner sc = new Scanner(file);

            Set<Point> eastCucumbers = new HashSet<>();
            Set<Point> southCucumbers = new HashSet<>();

            int width = 0;
            int height = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                width = line.length();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '>') {
                        eastCucumbers.add(new Point(height, i));
                    } else if (c == 'v') {
                        southCucumbers.add(new Point(height, i));
                    }
                }

                height++;
            }

            long part1 = part1(eastCucumbers, southCucumbers, height, width);
            System.out.println("Part 1 is: " + part1);

            int part2 = part2();
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Moves all cucumbers facing east one step (unless blocked by another cucumber).
    // Returns true if any cucumbers moved and false otherwise.
    private static boolean stepEast(Set<Point> eastCucumbers, Set<Point> southCucumbers, int width) {
        Set<Point> pointsToUpdate = new HashSet<>();
        for (Point cucumber : eastCucumbers) {
            // If the point immediately to the right of the cucumber is empty, note that we need to update it.
            Point rightNeighbor = new Point(cucumber.x, (cucumber.y+1) % width);
            if (!eastCucumbers.contains(rightNeighbor) && !southCucumbers.contains(rightNeighbor)) {
                pointsToUpdate.add(cucumber);
            }
        }

        // Update all the cucumbers
        for (Point p : pointsToUpdate) {
            Point newPoint = new Point(p.x, (p.y+1) % width);
            eastCucumbers.remove(p);
            eastCucumbers.add(newPoint);
        }

        // Indicate whether any cucumbers moved east.
        return !pointsToUpdate.isEmpty();
    }

    // Moves all cucumbers facing south one step (unless blocked by another cucumber).
    // Returns true if any cucumbers moved and false otherwise.
    private static boolean stepSouth(Set<Point> eastCucumbers, Set<Point> southCucumbers, int height) {
        Set<Point> pointsToUpdate = new HashSet<>();
        for (Point cucumber : southCucumbers) {
            // If the point immediately below the cucumber is empty, note that we need to update it.
            Point bottomNeighbor = new Point((cucumber.x+1) % height, cucumber.y);
            if (!eastCucumbers.contains(bottomNeighbor) && !southCucumbers.contains(bottomNeighbor)) {
                pointsToUpdate.add(cucumber);
            }
        }

        // Update all the cucumbers
        for (Point p : pointsToUpdate) {
            Point newPoint = new Point((p.x+1) % height, p.y);
            southCucumbers.remove(p);
            southCucumbers.add(newPoint);
        }

        // Indicate whether any cucumbers moved south.
        return !pointsToUpdate.isEmpty();
    }

    private static void printGrid(Set<Point> eastCucumbers, Set<Point> southCucumbers, int height, int width) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (eastCucumbers.contains(new Point(i,j))) {
                    System.out.print(">");
                    continue;
                }

                if (southCucumbers.contains(new Point(i,j))) {
                    System.out.print("v");
                    continue;
                }

                System.out.print(".");
            }
            System.out.println();
        }
    }

    // Part 1:
    private static long part1(Set<Point> eastCucumbers, Set<Point> southCucumbers, int height, int width) {
        boolean cucumbersMoved = true;
        long step = 0;
        while (cucumbersMoved) {
            step++;
            boolean cucumbersMovedEast = stepEast(eastCucumbers, southCucumbers, width);
            boolean cucumbersMovedSouth = stepSouth(eastCucumbers, southCucumbers, height);

            cucumbersMoved = cucumbersMovedEast || cucumbersMovedSouth;
        }

        return step;
    }

    // Part 2:
    private static int part2() {
        return 0;
    }
}
