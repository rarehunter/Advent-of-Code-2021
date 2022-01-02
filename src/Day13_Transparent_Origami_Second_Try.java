import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

// This attempt question 13 again but using a set to keep track of the points in the grid instead
// of constructing the entire grid in memory (which is not efficient due to likely sparse grid)
public class Day13_Transparent_Origami_Second_Try {
    private static int maxWidth = 0;
    private static int maxHeight = 0;

    public static void main(String[] args) {
        File file = new File("./inputs/day13/day13.txt");

        List<String> folds = new ArrayList<>();
        Set<Point> points = new HashSet<>();

        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.equals(""))
                    continue;

                // Add fold instructions
                if (line.contains("fold")) {
                    String[] tokens = line.split(" ");
                    folds.add(tokens[2]);
                } else { // add points to set while keeping track of max width and height
                    String[] tokens = line.split(",");
                    int currX = Integer.parseInt(tokens[0]);
                    int currY = Integer.parseInt(tokens[1]);
                    if (currX > maxWidth) {
                        maxWidth = currX;
                    }
                    if (currY > maxHeight) {
                        maxHeight = currY;
                    }
                    points.add(new Point(currX, currY));
                }
            }

            int answer = part1(points, folds);
            System.out.println("Part 1 is: " + answer);

            part2(points, folds);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void foldUp(Set<Point> points, int foldIndex) {
        Set<Point> pointsToRemove = new HashSet<>();
        Set<Point> pointsToAdd = new HashSet<>();

        for (Point p : points) {
            if (p.y < foldIndex) continue;

            // If we have a point that is below the fold,
            // add a new point to the set that has the same x value but a reflected y value
            Point reflectedPoint = new Point(p.x, foldIndex - (p.y - foldIndex));
            pointsToAdd.add(reflectedPoint);

            // Remove the old point
            pointsToRemove.add(p);
        }

        for (Point p : pointsToRemove) {
            points.remove(p);
        }

        for (Point p : pointsToAdd) {
            points.add(p);
        }
    }

    private static void foldLeft(Set<Point> points, int foldIndex) {
        Set<Point> pointsToRemove = new HashSet<>();
        Set<Point> pointsToAdd = new HashSet<>();

        for (Point p : points) {
            if (p.x < foldIndex) continue;

            // If we have a point that is right of the fold,
            // add a new point to the set that has the same y value but a reflected x value
            Point reflectedPoint = new Point(foldIndex - (p.x - foldIndex),  p.y);
            pointsToAdd.add(reflectedPoint);

            // Remove the old point
            pointsToRemove.add(p);
        }

        for (Point p : pointsToRemove) {
            points.remove(p);
        }

        for (Point p : pointsToAdd) {
            points.add(p);
        }
    }

    // Part 1 - We only want to do the first fold.
    private static int part1(Set<Point> points, List<String> folds) {
        String fold = folds.get(0);

        String[] tokens = fold.split("=");

        char direction = tokens[0].charAt(0);
        int foldIndex = Integer.parseInt(tokens[1]);

        if (direction == 'y') {
            foldUp(points, foldIndex);
        } else if (direction == 'x') {
            foldLeft(points, foldIndex);
        }

        return points.size();
    }

    private static void printSet1(Set<Point> points, int maxHeight, int maxWidth) {
        for (int i = 0; i <= maxHeight; i++) { // y
            for (int j = 0; j <= maxWidth; j++) { // x
                if (points.contains(new Point(j, i))) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private static void printSet2(Set<Point> points, int maxHeight, int maxWidth) {
        for (int i = 0; i <= maxHeight; i++) { // y
            for (int j = 0; j <= maxWidth; j++) { // x
                if (points.contains(new Point(j, i))) {
                    System.out.print("█");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    // Part 2 - Now, we do all the folds and then print out a visualization
    private static void part2(Set<Point> points, List<String> folds) {
        for (String fold : folds) {
            String[] tokens = fold.split("=");

            char direction = tokens[0].charAt(0);
            int foldIndex = Integer.parseInt(tokens[1]);

            if (direction == 'y') {
                foldUp(points, foldIndex);
            } else if (direction == 'x') {
                foldLeft(points, foldIndex);
            }
        }

        int maxWidth = 0;
        int maxHeight = 0;

        for (Point p : points) {
            if (p.x > maxWidth)
                maxWidth = p.x;
            if (p.y > maxHeight)
                maxHeight = p.y;
        }

        printSet1(points, maxHeight, maxWidth);
        printSet2(points, maxHeight, maxWidth);


    }
}
