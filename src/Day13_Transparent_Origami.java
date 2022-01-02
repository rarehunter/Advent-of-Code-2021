import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13_Transparent_Origami {
    public static void main(String[] args) {
        File file = new File("./inputs/day13/day13.txt");
        int maxWidth = 0;
        int maxHeight = 0;
        List<String> folds = new ArrayList<>();
        char[][] grid;

        try {
            Scanner sc = new Scanner(file);

            // Two passes through the input file.
            // First time is to find the max width and height in
            // order to initialize the grid
            // Second time is to actually fill in the grid values.
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.equals("")) {
                    break;
                }
                String[] tokens = line.split(",");
                int currX = Integer.parseInt(tokens[0]);
                int currY = Integer.parseInt(tokens[1]);
                if (currX > maxWidth) {
                    maxWidth = currX;
                }
                if (currY > maxHeight) {
                    maxHeight = currY;
                }
            }

            grid = new char[maxHeight + 1][maxWidth + 1];
            initializeCharArray(grid);

            Scanner sc2 = new Scanner(file);
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine();
                if (line.equals("")) continue;

                // Add fold instructions
                if (line.contains("fold")) {
                    String[] tokens = line.split(" ");
                    folds.add(tokens[2]);
                } else { // fill in grid values
                    String[] tokens = line.split(",");
                    int currX = Integer.parseInt(tokens[0]);
                    int currY = Integer.parseInt(tokens[1]);
                    grid[currY][currX] = '#';
                }
            }

            int part1 = part1WithMatrix(grid, folds);
            System.out.println("Part 1 is: " + part1);
            part2WithMatrix(grid, folds);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void print2DArray(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // Populate an empty char away with dots (.)
    private static void initializeCharArray(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = '.';
            }
        }
    }

    // Given a grid and a y-index, folds the points below the y-index on top
    // of the points above the y-index.
    private static void foldUp(char[][] grid, int foldIndex) {
        for (int y = foldIndex + 1; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == '#') {
                    int newY = foldIndex - (y - foldIndex);

                    if (newY < 0) {
                        return;
                    }
                    grid[newY][x] = '#';
                }
            }
        }
    }

    // Given a grid and an x-index, folds the points right of the x-index on top
    // of the points left of the x-index.
    private static void foldLeft(char[][] grid, int foldIndex) {
        for (int x = foldIndex + 1; x < grid[0].length; x++) {
            for (int y = 0; y < grid.length; y++) {
                if (grid[y][x] == '#') {
                    int newX = foldIndex - (x - foldIndex);

                    if (newX < 0) {
                        return;
                    }

                    grid[y][newX] = '#';
                }
            }
        }
    }

    // Given a list of folds, finds the last y and x folds to determine the bounds
    // of the final folded area. Returns those x and y values as a Point object.
    private static Point determineFoldBounds(int gridWidth, int gridHeight, List<String> folds) {
        int lastXFoldIndex = gridWidth - 1;
        int lastYFoldIndex = gridHeight - 1;
        boolean lastXFound = false;
        boolean lastYFound = false;

        int i = folds.size() - 1;
        while ((!lastXFound || !lastYFound) && i >= 0) {
            String fold = folds.get(i);
            String[] tokens = fold.split("=");

            if (tokens[0].charAt(0) == 'x') {
                lastXFoldIndex = Integer.parseInt(tokens[1]);
                lastXFound = true;
            } else if (fold.charAt(0) == 'y') {
                lastYFoldIndex = Integer.parseInt(tokens[1]);
                lastYFound = true;
            }

            i--;
        }

        return new Point(lastXFoldIndex, lastYFoldIndex);
    }

    // Count only the dots in the area formed by considering the
    // last x and y folds
    private static int countDots(char[][] grid, List<String> folds) {
        int numDots = 0;

        Point p = determineFoldBounds(grid[0].length, grid.length, folds);
        int lastXFoldIndex = p.x;
        int lastYFoldIndex = p.y;

        for (int x = 0; x <= lastXFoldIndex; x++) {
            for (int y = 0; y <= lastYFoldIndex; y++) {
                if (grid[y][x] == '#') {
                    numDots++;
                }
            }
        }

        return numDots;
    }

    // Part 1: Given a grid and the first fold, performs the first fold
    // and counts the number of '#' in the resulting half grid afterwards
    private static int part1WithMatrix(char[][] grid, List<String> folds) {
        String fold = folds.get(0);
        String[] tokens = fold.split("=");

        char direction = tokens[0].charAt(0);
        int foldIndex = Integer.parseInt(tokens[1]);

        if (direction == 'y') {
            foldUp(grid, foldIndex);
        } else if (direction == 'x') {
            foldLeft(grid, foldIndex);
        }

        List<String> dummyList = new ArrayList<>();
        dummyList.add(fold);
        int numDots = countDots(grid, dummyList);

        return numDots;
    }

    // Part 2: Given a grid and the list of folds, performs all the folds and
    // prints out the resulting folded area.
    private static void part2WithMatrix(char[][] grid, List<String> folds) {
        for (String fold : folds) {
            String[] tokens = fold.split("=");

            char direction = tokens[0].charAt(0);
            int foldIndex = Integer.parseInt(tokens[1]);

            if (direction == 'y') {
                foldUp(grid, foldIndex);
            } else if (direction == 'x') {
                foldLeft(grid, foldIndex);
            }
        }

        Point p = determineFoldBounds(grid[0].length, grid.length, folds);
        int lastXFoldIndex = p.x;
        int lastYFoldIndex = p.y;

        for (int i = 0; i <= lastYFoldIndex; i++) {
            for (int j = 0; j <= lastXFoldIndex; j++) {
                System.out.print(grid[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
