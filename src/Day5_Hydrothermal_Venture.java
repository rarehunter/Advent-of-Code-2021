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

            // A dictionary of coordinates: (x,y) -> (a,b)
            Map<Point, List<Point>> coordinates = new HashMap<>();

            while(sc.hasNextLine()) {
                String line = sc.nextLine();

                // Extract out the relevant coordinates from the input data
                String[] bothPoints = line.split(" -> ");
                String[] firstPoint = bothPoints[0].split(",");
                String[] secondPoint = bothPoints[1].split(",");
                int x1 = Integer.parseInt(firstPoint[0]);
                int y1 = Integer.parseInt(firstPoint[1]);
                int x2 = Integer.parseInt(secondPoint[0]);
                int y2 = Integer.parseInt(secondPoint[1]);
                Point p1 = new Point(x1, y1);
                Point p2 = new Point(x2, y2);

                if (coordinates.containsKey(p1)) {
                    coordinates.get(p1).add(p2);
                } else {
                    List<Point> newPoints = new ArrayList<>();
                    newPoints.add(p2);
                    coordinates.put(p1, newPoints);
                }

                // Update the largest x and y
                if (x1 > largestX) largestX = x1;
                if (x2 > largestX) largestX = x2;
                if (y1 > largestY) largestY = y1;
                if (y2 > largestY) largestY = y2;
            }

            // Construct the 2d array of the coordinate system
            int[][] grid = new int[largestY + 1][largestX + 1];

            //int numberOfAtLeastTwoOverlapPoints = numberOfAtLeastTwoOverlapPoints(coordinates, grid);
            //System.out.println("Number of points with at least two overlapping lines: " + numberOfAtLeastTwoOverlapPoints);

            int numberOfAtLeastTwoOverlapPointsDiagonal = numberOfAtLeastTwoOverlapPointsWithDiagonals(coordinates, grid);
            System.out.println("Number of points with at least two overlapping lines including diagonals: " + numberOfAtLeastTwoOverlapPointsDiagonal);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void print2DArray(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    private static void plotVerticalLine(int[][] grid, Point p1, Point p2) {
        int start, end, constantX = p1.x;
        if (p1.y < p2.y) {
            start = p1.y;
            end = p2.y;
        } else {
            start = p2.y;
            end = p1.y;
        }

        for (int i = start; i <= end; i++) {
            grid[i][constantX] += 1;
        }
    }

    private static void plotHorizontalLine(int[][] grid, Point p1, Point p2) {
        int start, end, constantY = p1.y;

        if (p1.x < p2.x) {
            start = p1.x;
            end = p2.x;
        } else {
            start = p2.x;
            end = p1.x;
        }

        for (int i = start; i <= end; i++) {
            grid[constantY][i] += 1;
        }
    }

    private static void plotDiagonalLine(int[][] grid, Point p1, Point p2) {
        // Determine if the diagonal line is going upwards or going downwards

        // Line is sloping downwards (both x and y move in the same direction)
        // and first point is smaller than second point: (1,1) => (3,3)
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

    // Part 1: Given a map of line segments, plots them on the given grid using integers
    // denoting how many segments overlap at any given point. Returns the number of at least two overlapping points
    // Note: Here, we only consider vertical or horizontal lines.
    private static int numberOfAtLeastTwoOverlapPoints(Map<Point, List<Point>> coordinates, int[][] grid) {
        for (Point p1 : coordinates.keySet()) {
            List<Point> ends = coordinates.get(p1);

            for (Point p2: ends) {
                // First, let's check that we're only considering horizontal or vertical lines:
                // where x1 = x2 or y1 = y2
                if (p1.x == p2.x || p1.y == p2.y) {
                    // Vertical line
                    if (p1.x == p2.x && p1.y != p2.y) {
                        plotVerticalLine(grid, p1, p2);
                    }

                    // Horizontal line
                    if (p1.y == p2.y && p1.x != p2.x) {
                        plotHorizontalLine(grid, p1, p2);
                    }

                    // Single point
                    if (p1.y == p2.y && p1.x == p2.x) {
                        grid[p1.y][p1.x] += 1;
                    }
                }
            }
        }

        int atLeastTwoOverlaps = countAtLeastTwoOverlaps(grid);
        return atLeastTwoOverlaps;
    }

    // Part 2: Given a map of line segments, plots them on the given grid using integers
    // denoting how many segments overlap at any given point. Returns the number of at least two overlapping points
    // Note: Here, we include vertical, horizontal, and diagonal lines at a 45 degree angle.
    private static int numberOfAtLeastTwoOverlapPointsWithDiagonals(Map<Point, List<Point>> coordinates, int[][] grid) {
        for (Point p1 : coordinates.keySet()) {
            List<Point> ends = coordinates.get(p1);

            for (Point p2: ends) {
                // Vertical line
                if (p1.x == p2.x && p1.y != p2.y) {
                    plotVerticalLine(grid, p1, p2);
                }

                // Horizontal line
                if (p1.y == p2.y && p1.x != p2.x) {
                    plotHorizontalLine(grid, p1, p2);
                }

                // Diagonal line
                if (p1.x != p2.x && p1.y != p2.y) {
                    plotDiagonalLine(grid, p1, p2);
                }

                // Single point
                if (p1.y == p2.y && p1.x == p2.x) {
                    grid[p1.y][p1.x] += 1;
                }
            }
        }

        int atLeastTwoOverlaps = countAtLeastTwoOverlaps(grid);
        return atLeastTwoOverlaps;
    }
}
