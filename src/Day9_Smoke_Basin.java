import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Day9_Smoke_Basin {
    private static final int TERMINAL_HEIGHT = 9;
    private static final int HEIGHT = 100;
    private static final int WIDTH = 100;

    public static void main(String[] args) {
        File file = new File("./inputs/day9/day9.txt");

        int[][] grid = new int[HEIGHT][WIDTH];

        try {
            Scanner sc = new Scanner(file);

            int row = 0;

            // Parse in the input as a 2d array of integers
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                for(int i = 0; i < line.length(); i++) {
                    grid[row][i] = Integer.parseInt(Character.toString(line.charAt(i)));
                }

                row++;
            }

            int part1 = part1(grid);
            System.out.println("Part 1: " + part1);

            int part2 = part2(grid);
            System.out.println("Part 2: " + part2);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Given a grid and a point, return a list of neighbors of the point.
    // We find neighbors by considering the four directions of any given points, bounds-checking as we go.
    private static List<Point> getNeighbors(int[][] grid, Point p) {
        List<Point> neighbors = new ArrayList<>();

        int row = p.x;
        int col = p.y;
        List<Point> directions = new ArrayList<>();
        directions.add(new Point(0, 1)); // represents going right
        directions.add(new Point(0, -1)); // represents going left
        directions.add(new Point(-1, 0)); // represents going up
        directions.add(new Point(1, 0)); // represents going down

        for (Point direction : directions) {
            int newRow = row + direction.x;
            int newCol = col + direction.y;

            // Make sure that the new point is within the grid boundary
            if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length) {
                neighbors.add(new Point(newRow, newCol));
            }
        }

        return neighbors;
    }

    // Returns a list of the low points in the grid
    // Low points are defined as points in which the height of a particular location
    // is lower than any of its adjacent locations.
    private static List<Point> getLowPoints(int[][] grid) {
        List<Point> lowestPoints = new ArrayList<>();

        // For each point in the grid, get its neighbors. Check to see if the height of the current point
        // is the lowest amongst its neighbors.
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                List<Point> neighbors = getNeighbors(grid, new Point(i, j));

                boolean lowestPoint = true;
                for (Point neighbor : neighbors) {
                    if (grid[i][j] >= grid[neighbor.x][neighbor.y]) {
                        lowestPoint = false;
                    }
                }

                if (lowestPoint) {
                    lowestPoints.add(new Point(i, j));
                }
            }
        }

        return lowestPoints;
    }

    // Part 1: Given a grid of numbers representing heights, finds the "low points" and calculates
    // the sum of all the risk levels. A "low point" is defined as locations that have lower height
    // than any of its adjacent neighbors (not including diagonal neighbors). The risk level of a low points
    // is its height + 1.
    private static int part1(int[][] grid) {
        List<Point> lowPoints = getLowPoints(grid);

        // The risk level of a low point is its height + 1.
        // Sum up all the risk levels.
        int sum = 0;
        for (Point p : lowPoints) {
            int height = grid[p.x][p.y];
            int riskLevel = height + 1;
            sum += riskLevel;
        }

        return sum;
    }

    // Part 2: Finds the three largest basin sizes. A basin is all locations that eventually
    // flow "downward" to a single low point. For every low point that we've found, run a BFS
    // and count the size of each area.
    private static int part2(int[][] grid) {
        List<Point> lowPoints = getLowPoints(grid);

        List<Integer> basinSizes = new ArrayList<>();
        LinkedList<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        int basinSize = 0;

        // Iterate through all the low points.
        for (Point p : lowPoints) {
            queue.add(p);
            visited.add(p); // mark that we've considered this point
            basinSize++; // increment our basin size count

            while (!queue.isEmpty()) {
                Point currP = queue.pop();

                List<Point> neighbors = getNeighbors(grid, currP);
                for (Point neighbor : neighbors) {
                    // Only consider a neighbor if the neighbor is greater than our current value,
                    // and it's not a 9, and we haven't already visited it.
                    int neighborValue = grid[neighbor.x][neighbor.y];
                    int currValue = grid[currP.x][currP.y];

                    if (neighborValue > currValue && neighborValue < TERMINAL_HEIGHT && !visited.contains(neighbor)) {
                        queue.add(neighbor);
                        visited.add(neighbor); // mark that we've considered this point
                        basinSize++; // increment our basin size count
                    }
                }
            }

            // We've found the size of the basin so store it and
            // clear out the variables to run the next low point.
            basinSizes.add(basinSize);
            basinSize = 0;
            queue.clear();
        }

        // Find the product of the three largest basins.
        Collections.sort(basinSizes);
        int lastIndex = basinSizes.size() - 1;
        return basinSizes.get(lastIndex) * basinSizes.get(lastIndex-1) * basinSizes.get(lastIndex-2);
    }
}
