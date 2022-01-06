import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Day11_Dumbo_Octopus {
    public static void main(String[] args) {
        File file = new File("./inputs/day11/day11.txt");

        int width = 10;
        int height = 10;
        int[][] grid = new int[width][height];

        try {
            Scanner sc = new Scanner(file);

            int row = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    int intValue = c - '0';
                    grid[row][i] = intValue;
                }

                row++;
            }

            //int flashes = getNumberOctopusFlashes(grid, 100);
            //System.out.println("The answer is: " + flashes);

            int syncStep = getOctopusSynchronizationStep(grid);
            System.out.println("The answer is: " + syncStep);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Increases all energy levels of octopus in the grid by 1 and
    // returns a queue of the points in which the octopus level is greater than 9
    private static List<Point> increaseEnergyLevelByOne(int[][] grid) {
        List<Point> octopusesReady = new LinkedList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] += 1;

                if (grid[i][j] > 9) {
                    octopusesReady.add(new Point(i, j));
                }
            }
        }

        return octopusesReady;
    }

    private static void setFlashedOctopusesEnergyToZero(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 9)
                    grid[i][j] = 0;
            }
        }
    }


    // Given a point on the grid, returns all adjacent neighbors (including diagonally adjacent)
    private static List<Point> getNeighbors(int[][] grid, Point p) {
        List<Point> neighbors = new ArrayList<>();
        int i = p.x;
        int j = p.y;

        if (i == 0) {
            if (j == 0) { // top left corner
                neighbors.add(new Point(i, j+1));
                neighbors.add(new Point(i+1, j));
                neighbors.add(new Point(i+1, j+1));
            } else if (j == grid[0].length - 1) { // top right corner
                neighbors.add(new Point(i, j-1));
                neighbors.add(new Point(i+1, j));
                neighbors.add(new Point(i+1, j-1));
            } else { // top edge
                neighbors.add(new Point(i, j+1));
                neighbors.add(new Point(i, j-1));
                neighbors.add(new Point(i+1, j));
                neighbors.add(new Point(i+1, j+1));
                neighbors.add(new Point(i+1, j-1));
            }
        } else if (i == grid.length - 1) {
            if (j == 0) { // bottom left corner
                neighbors.add(new Point(i, j+1));
                neighbors.add(new Point(i-1, j));
                neighbors.add(new Point(i-1, j+1));
            } else if (j == grid[0].length - 1) { // bottom right corner
                neighbors.add(new Point(i, j-1));
                neighbors.add(new Point(i-1, j));
                neighbors.add(new Point(i-1, j-1));
            } else { // bottom edge
                neighbors.add(new Point(i, j+1));
                neighbors.add(new Point(i, j-1));
                neighbors.add(new Point(i-1, j));
                neighbors.add(new Point(i-1, j-1));
                neighbors.add(new Point(i-1, j+1));
            }
        } else if (j == 0) { // left side
            neighbors.add(new Point(i-1, j));
            neighbors.add(new Point(i+1, j));
            neighbors.add(new Point(i, j+1));
            neighbors.add(new Point(i-1, j+1));
            neighbors.add(new Point(i+1, j+1));

        } else if (j == grid[0].length - 1) { // right side
            neighbors.add(new Point(i-1, j));
            neighbors.add(new Point(i+1, j));
            neighbors.add(new Point(i, j-1));
            neighbors.add(new Point(i-1, j-1));
            neighbors.add(new Point(i+1, j-1));

        } else { // anything in the middle
            neighbors.add(new Point(i-1, j));
            neighbors.add(new Point(i+1, j));
            neighbors.add(new Point(i, j-1));
            neighbors.add(new Point(i, j+1));
            neighbors.add(new Point(i-1, j-1));
            neighbors.add(new Point(i-1, j+1));
            neighbors.add(new Point(i+1, j-1));
            neighbors.add(new Point(i+1, j+1));
        }

        return neighbors;
    }

    // Part 1: For each step of the octopus flash sequence, runs a BFS on any points
    // where the octopus level exceeds 9. Returns the number of flashes that occur.
    private static int getNumberOctopusFlashes(int[][] grid, int step) {
        int flashes = 0;
        LinkedList<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        List<Point> octopusesThatFlashed = new ArrayList<>();

        for (int i = 1; i <= step; i++) {
            // Gives a list of octopuses whose levels are over 9 and are ready to flash
            List<Point> octopusesReady = increaseEnergyLevelByOne(grid);

            for (Point octopusReady : octopusesReady) {
                if (!visited.contains(octopusReady)) {
                    queue.push(octopusReady);
                    visited.add(octopusReady);
                }

                while (!queue.isEmpty()) {
                    Point currentOctopus = queue.pop();
                    flashes++;
                    octopusesThatFlashed.add(currentOctopus);

                    List<Point> neighbors = getNeighbors(grid, currentOctopus);

                    // For each neighbor...
                    for (Point neighbor : neighbors) {
                        // Increase its energy level by one
                        grid[neighbor.x][neighbor.y] += 1;

                        // and if its energy level is now over 9 and we haven't visited it yet,
                        // add it to the queue.
                        if (grid[neighbor.x][neighbor.y] > 9 && !visited.contains(neighbor)) {
                            queue.push(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
            }

            // For each octopus that flashed this step, set its level to 0
            for (Point octopus : octopusesThatFlashed) {
                grid[octopus.x][octopus.y] = 0;
            }

            queue.clear();
            visited.clear();
            octopusesThatFlashed.clear();
        }

        return flashes;
    }

    private static boolean checkSyncFound(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    // Part 2: Same algorithm as part 1. The only difference is that this runs until we hit
    // a grid state where all levels are set to 0. Returns the step number when this happens.
    private static int getOctopusSynchronizationStep(int[][] grid) {
        int step = 1;
        LinkedList<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        List<Point> octopusesThatFlashed = new ArrayList<>();

        while (true) {
            // Gives a list of octopuses whose levels are over 9 and are ready to flash
            List<Point> octopusesReady = increaseEnergyLevelByOne(grid);

            for (Point octopusReady : octopusesReady) {
                if (!visited.contains(octopusReady)) {
                    queue.push(octopusReady);
                    visited.add(octopusReady);
                }

                while (!queue.isEmpty()) {
                    Point currentOctopus = queue.pop();
                    octopusesThatFlashed.add(currentOctopus);

                    List<Point> neighbors = getNeighbors(grid, currentOctopus);

                    // For each neighbor...
                    for (Point neighbor : neighbors) {
                        // Increase its energy level by one
                        grid[neighbor.x][neighbor.y] += 1;

                        // and if its energy level is now over 9 and we haven't visited it yet,
                        // add it to the queue.
                        if (grid[neighbor.x][neighbor.y] > 9 && !visited.contains(neighbor)) {
                            queue.push(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
            }

            // For each octopus that flashed this step, set its level to 0
            for (Point octopus : octopusesThatFlashed) {
                grid[octopus.x][octopus.y] = 0;
            }

            queue.clear();
            visited.clear();
            octopusesThatFlashed.clear();

            boolean isSyncFound = checkSyncFound(grid);

            if (isSyncFound) {
                break;
            }

            step++;
        }

        return step;
    }
}
