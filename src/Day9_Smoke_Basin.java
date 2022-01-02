import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Day9_Smoke_Basin {
    private static final int TERMINAL_HEIGHT = 9;

    public static void main(String[] args) {
        File file = new File("./inputs/day9/day9.txt");
        int width = 100;
        int height = 100;

        int[][] grid = new int[height][width];

        try {
            Scanner sc = new Scanner(file);

            int row = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                for(int i = 0; i < line.length(); i++) {
                    grid[row][i] = Integer.parseInt(Character.toString(line.charAt(i)));
                }

                row++;
            }

            //int sum = getLowPointRiskLevelSum(grid);
            int product = getBasinSizes(grid);
            System.out.println("The answer is: " + product);
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

    private static List<Point> getNeighbors(int[][] grid, Point p) {
        List<Point> neighbors = new ArrayList<>();
        int i = p.x;
        int j = p.y;

        if (i == 0) {
            if (j == 0) { // top left corner
                neighbors.add(new Point(i, j+1));
                neighbors.add(new Point(i+1, j));
            } else if (j == grid[0].length - 1) { // top right corner
                neighbors.add(new Point(i, j-1));
                neighbors.add(new Point(i+1, j));
            } else {
                neighbors.add(new Point(i, j+1));
                neighbors.add(new Point(i, j-1));
                neighbors.add(new Point(i+1, j));
            }
        } else if (i == grid.length - 1) {
            if (j == 0) { // bottom left corner
                neighbors.add(new Point(i, j+1));
                neighbors.add(new Point(i-1, j));
            } else if (j == grid[0].length - 1) { // bottom right corner
                neighbors.add(new Point(i, j-1));
                neighbors.add(new Point(i-1, j));
            } else {
                neighbors.add(new Point(i, j+1));
                neighbors.add(new Point(i, j-1));
                neighbors.add(new Point(i-1, j));
            }
        } else if (j == 0) { // left side
            neighbors.add(new Point(i-1, j));
            neighbors.add(new Point(i+1, j));
            neighbors.add(new Point(i, j+1));
        } else if (j == grid[0].length - 1) { // right side
            neighbors.add(new Point(i-1, j));
            neighbors.add(new Point(i+1, j));
            neighbors.add(new Point(i, j-1));
        } else { // anything in the middle
            neighbors.add(new Point(i-1, j));
            neighbors.add(new Point(i+1, j));
            neighbors.add(new Point(i, j-1));
            neighbors.add(new Point(i, j+1));
        }

        return neighbors;
    }

    // Returns a list of the low points in the grid
    // Low points are defined as points in which the height of a particular location
    // is lower than any of its adjacent locations.
    private static List<Point> getLowPoints(int[][] grid) {
        List<Point> lowestPoints = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                List<Point> neighbors = getNeighbors(grid, new Point(i, j));

                boolean lowestPoint = true;
                for(Point neighbor : neighbors) {
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

    // Part 1: The risk level of a low point is 1 plus its height.
    private static int getLowPointRiskLevelSum(int[][] grid) {
        List<Point> lowPoints = getLowPoints(grid);

        int sum = 0;
        for (Point p : lowPoints) {
            int height = grid[p.x][p.y];
            int riskLevel = height + 1;
            sum += riskLevel;
        }

        return sum;
    }

    // Part 2: Run a BFS on the given grid, starting at each determined low point from Part 1
    // and counting the size of each area.
    private static int getBasinSizes(int[][] grid) {
        List<Integer> basinSizes = new ArrayList<>();
        List<Point> lowPoints = getLowPoints(grid);
        LinkedList<Point> queue = new LinkedList<>();
        Set<Point> seen = new HashSet<>();
        int basinSize = 0;

        for (Point p : lowPoints) {
            queue.add(p);
            seen.add(p); // mark that we've considered this point
            basinSize++; // increment our basin size count

            while (!queue.isEmpty()) {
                Point currP = queue.pop();

                List<Point> neighbors = getNeighbors(grid, currP);
                for (Point neighbor : neighbors) {
                    // Only consider a neighbor if the neighbor is greater than our current value
                    // and it's not a 9 and we haven't already seen it.
                    int neighborValue = grid[neighbor.x][neighbor.y];
                    int currValue = grid[currP.x][currP.y];
                    if (neighborValue > currValue && neighborValue < TERMINAL_HEIGHT && !seen.contains(neighbor)) {
                        queue.add(neighbor);
                        seen.add(neighbor); // mark that we've considered this point
                        basinSize++; // increment our basin size count
                    }
                }
            }

            basinSizes.add(basinSize);
            basinSize = 0;

            queue.clear();
        }

        Collections.sort(basinSizes);
        int lastIndex = basinSizes.size() - 1;
        int product = basinSizes.get(lastIndex) * basinSizes.get(lastIndex - 1) * basinSizes.get(lastIndex - 2);

        return product;
    }
}
