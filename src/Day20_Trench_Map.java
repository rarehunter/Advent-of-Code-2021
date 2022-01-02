import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day20_Trench_Map {
    public static void main(String[] args) {
        File file = new File("./inputs/day20/day20.example.txt");

        try {
            Scanner sc = new Scanner(file);
            String imageEnhancementLookup = "";

            int height = 0;
            int width = 0;

            boolean firstLineRead = false;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (!firstLineRead) {
                    imageEnhancementLookup = line;
                    firstLineRead = true;
                    continue;
                }

                if (line.equals("")) continue;

                width = line.length();
                height++;
            }

            char[][] grid = new char[height][width];

            Scanner sc2 = new Scanner(file);

            int row = 0;
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine();

                if (line.equals("") || line.length() == 512) continue;

                for (int i = 0; i < line.length(); i++) {
                    grid[row][i] = line.charAt(i);
                }

                row++;
            }

            int part1 = part1(grid, imageEnhancementLookup);
            System.out.println("Part 1 is: " + part1);

            int part2 = part2(grid, imageEnhancementLookup);
            System.out.println("Part 2 is: " + part2);

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

    // Returns the decimal representation of a given 9-bit binary string (most-significant bit first)
    private static int binToDec(String binary) {
        int output = 0;
        for (int i = 8; i >= 0; i--) {
            char bit = binary.charAt(i);

            if (bit == '#') {
                output += (int)Math.pow(2, 8-i);
            }
        }

        return output;
    }

    // Given a grid and a character c, fills the grid completely with that character.
    private static void fillGrid(char[][] grid, char c) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = c;
            }
        }
    }

    // Given a grid and a string of the image enhancement lookup, returns a grid of characters
    // gotten after applying a 3x3 mask to each pixel of the original grid and indexing into the look up string
    // using the decimal representation of the 3x3 mask of characters.
    private static char[][] runImageEnhancement(char[][] grid, String imageEnhancementLookup) {
        char[][] newgrid = new char[grid.length][grid[0].length];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                // Check the bounds
                if (i == 0 || i == grid.length - 1 || j == 0 || j == grid[i].length -1) {
                    // If dark pixels at the edges of the grid, then we assume that the index is 0.
                    // Otherwise, if there are light pixels at the edges, then we assume that the index is 511.
                    if (grid[i][j] == '.') {
                        newgrid[i][j] = imageEnhancementLookup.charAt(0);
                    } else if (grid[i][j] == '#') {
                        newgrid[i][j] = imageEnhancementLookup.charAt(511);
                    }
                    continue;
                }

                StringBuilder sb = new StringBuilder();

                sb.append(grid[i-1][j-1]);
                sb.append(grid[i-1][j]);
                sb.append(grid[i-1][j+1]);
                sb.append(grid[i][j-1]);
                sb.append(grid[i][j]);
                sb.append(grid[i][j+1]);
                sb.append(grid[i+1][j-1]);
                sb.append(grid[i+1][j]);
                sb.append(grid[i+1][j+1]);

                int index = binToDec(sb.toString());

                newgrid[i][j] = imageEnhancementLookup.charAt(index);
            }
        }

        return newgrid;
    }

    // Given a grid of chars, counts the occurrences of the light pixels ('#')
    private static int countLightPixels(char[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    // Given a grid and an integer number of padding, returns a new grid that is padding by n rows/cols
    // in each direction.
    private static char[][] padGridByN(char[][] grid, int padding) {
        char[][] newgrid = new char[grid.length+padding*2][grid[0].length+padding*2];
        fillGrid(newgrid, '.');

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                newgrid[i + padding][j + padding] = grid[i][j];
            }
        }

        return newgrid;
    }

    // Part 1: Run the image enhancement algorithm 2 times with the original grid padded by 3 rows and 3 columns
    // in each direction.
    private static int part1(char[][] grid, String imageEnhancementLookup) {
        // Pad original grid by 3 rows and 3 columns in each direction
        char[][] newgrid = padGridByN(grid, 3);

        char[][] result = newgrid;
        for (int i = 1; i <= 2; i++) {
            result = runImageEnhancement(result, imageEnhancementLookup);
        }

        int num = countLightPixels(result);
        return num;
    }

    // Part 2: Runs the image enhancement algorithm 50 times with the original grid padded by 55 rows and 55 colums
    // in each direction.
    private static int part2(char[][] grid, String imageEnhancementLookup) {
        // Pad original grid by 3 rows and 3 columns in each direction
        char[][] newgrid = padGridByN(grid, 55);

        char[][] result = newgrid;
        for (int i = 1; i <= 50; i++) {
            result = runImageEnhancement(result, imageEnhancementLookup);
        }

        int num = countLightPixels(result);

        return num;
    }
}
