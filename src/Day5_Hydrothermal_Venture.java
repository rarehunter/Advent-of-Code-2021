import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Day5_Hydrothermal_Venture {
    public static void main(String[] args) {
        File file = new File("./inputs/day5/day5.txt");

        try {
            Scanner sc = new Scanner(file);

            int largestX = 0;
            int largestY = 0;

            List<LineSegment> lineSegments = new ArrayList<>();

            while(sc.hasNextLine()) {
                String line = sc.nextLine();

                // Extract out the relevant points from the input data
                String[] bothPoints = line.split(" -> ");
                String[] firstPoint = bothPoints[0].split(",");
                String[] secondPoint = bothPoints[1].split(",");
                int x1 = Integer.parseInt(firstPoint[0]);
                int y1 = Integer.parseInt(firstPoint[1]);
                int x2 = Integer.parseInt(secondPoint[0]);
                int y2 = Integer.parseInt(secondPoint[1]);
                Point p1 = new Point(x1, y1);
                Point p2 = new Point(x2, y2);

                lineSegments.add(new LineSegment(p1, p2));

                // Update the largest x and y
                if (x1 > largestX) largestX = x1;
                if (x2 > largestX) largestX = x2;
                if (y1 > largestY) largestY = y1;
                if (y2 > largestY) largestY = y2;
            }

            // Construct the 2d array of the coordinate system
            int[][] grid = new int[largestY + 1][largestX + 1];

            int part1 = part1(lineSegments, grid);
            System.out.println("Part 1: " + part1);

            clearGrid(grid);

            int part2 = part2(lineSegments, grid);
            System.out.println("Part 2: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Given a 2d integer array, sets all values to 0.
    private static void clearGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0;
            }
        }
    }

    // Given a grid and two points representing a vertical line, plot the vertical line
    // in the grid by incrementing all the cells corresponding to the points of the line by one.
    private static void plotVerticalLine(int[][] grid, Point p1, Point p2) {
        int start = Math.min(p1.y, p2.y);
        int end = Math.max(p1.y, p2.y);

        for (int i = start; i <= end; i++) {
            grid[i][p1.x] += 1; // For vertical lines, x is held constant
        }
    }

    // Given a grid and two points representing a horizontal line, plot the horizontal line
    // in the grid by incrementing all the cells corresponding to the points of the line by one.
    private static void plotHorizontalLine(int[][] grid, Point p1, Point p2) {
        int start = Math.min(p1.x, p2.x);
        int end = Math.max(p1.x, p2.x);

        for (int i = start; i <= end; i++) {
            grid[p1.y][i] += 1; // For horizontal lines, y is held constant
        }
    }

    // Given a grid and two points representing a diagonal line with a 45-degree angle, plot
    // the diagonal line in the grid by incrementing all cells corresponding to the points of the line by one.
    private static void plotDiagonalLine(int[][] grid, Point p1, Point p2) {
        // Line is sloping downwards (both x and y move in the same direction)
        // and first point is smaller than second point: e.g. (1,1) => (3,3)
        if (p1.x < p2.x && p1.y < p2.y) {
            for (int i = p1.x, j = p1.y; i <= p2.x && j <= p2.y; i++, j++) {
                grid[j][i] += 1;
            }
        }

        // Line is sloping downwards (both x and y move in the same direction)
        // and first point is larger than second point: (3,3) => (1,1)
        if (p1.x > p2.x && p1.y > p2.y) {
            for (int i = p1.x, j = p1.y; i >= p2.x && j >= p2.y; i--, j--) {
                grid[j][i] += 1;
            }
        }

        // Line is sloping upwards (x and y move in opposite directions)
        // and the first point has a smaller x than the x of the second point: (7,9) => (9,7)
        if (p1.x < p2.x && p1.y > p2.y) {
            for (int i = p1.x, j = p1.y; i <= p2.x && j >= p2.y; i++, j--) {
                grid[j][i] += 1;
            }
        }

        // Line is sloping upwards (x and y move in opposite directions)
        // and the first point has a larger x than the x of the second point: (9,7) => (7,9)
        if (p1.x > p2.x && p1.y < p2.y) {
            for (int i = p1.x, j = p1.y; i >= p2.x && j <= p2.y; i--, j++) {
                grid[j][i] += 1;
            }
        }
    }

    // Given a 2D integer array, counts the number of times a number greater than or equal to 2 appears.
    // In other words, counts the number of points at which at least two lines overlap.
    private static int countAtLeastTwoOverlaps(int[][] grid) {
        int atLeastTwoOverlaps = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] >= 2) {
                    atLeastTwoOverlaps++;
                }
            }
        }

        return atLeastTwoOverlaps;
    }

    // Returns true if the two given points form a horizontal line (x values differ and y values are the same)
    private static boolean isHorizontalLine(Point p1, Point p2) {
        return p1.x != p2.x && p1.y == p2.y;
    }

    // Returns true if the two given points form a vertical line (y values differ and x values are the same)
    private static boolean isVerticalLine(Point p1, Point p2) {
        return p1.y != p2.y && p1.x == p2.x;
    }

    // Returns true if the two given points form a diagonal line (y values differ and x values differ).
    // Note: The problem allows us the luxury of assuming that all diagonal lines will be at 45 degree angles.
    private static boolean isDiagonalLine(Point p1, Point p2) {
        return p1.y != p2.y && p1.x != p2.x;
    }

    // Part 1: Given a map of line segments, plots them on the given grid using integers
    // denoting how many segments overlap at any given point. Returns the number of at least two overlapping points
    // Note: Here, we only consider vertical or horizontal lines.
    private static int part1(List<LineSegment> lineSegments, int[][] grid) {
        for (LineSegment segment : lineSegments) {
            Point p1 = segment.p1;
            Point p2 = segment.p2;

            // For this part, we only consider vertical or horizontal lines
            if (isHorizontalLine(p1, p2)) {
                plotHorizontalLine(grid, p1, p2);
            } else if (isVerticalLine(p1, p2)) {
                plotVerticalLine(grid, p1, p2);
            } else if (p1.x == p2.x && p1.y == p2.y) { // It's possible that both points are the same.
                grid[p1.y][p1.x] += 1;
            }
        }

        int atLeastTwoOverlaps = countAtLeastTwoOverlaps(grid);
        return atLeastTwoOverlaps;
    }

    // Part 2: Given a map of line segments, plots them on the given grid using integers
    // denoting how many segments overlap at any given point. Returns the number of at least two overlapping points
    // Note: Here, we include vertical, horizontal, and diagonal lines at a 45 degree angle.
    private static int part2(List<LineSegment> lineSegments, int[][] grid) {
        for (LineSegment segment : lineSegments) {
            Point p1 = segment.p1;
            Point p2 = segment.p2;

            // For this part, we only consider vertical or horizontal lines
            if (isHorizontalLine(p1, p2)) {
                plotHorizontalLine(grid, p1, p2);
            } else if (isVerticalLine(p1, p2)) {
                plotVerticalLine(grid, p1, p2);
            } else if (isDiagonalLine(p1, p2)) {
                plotDiagonalLine(grid, p1, p2);
            } else if (p1.x == p2.x && p1.y == p2.y) { // It's possible that both points are the same.
                grid[p1.y][p1.x] += 1;
            }
        }

        int atLeastTwoOverlaps = countAtLeastTwoOverlaps(grid);
        return atLeastTwoOverlaps;
    }

    // Class representing a line segment formed by two points.
    static class LineSegment {
        Point p1;
        Point p2;

        public LineSegment(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }
}
