import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2_Dive {
    private static final String FORWARD = "forward";
    private static final String UP = "up";
    private static final String DOWN = "down";

    public static void main(String[] args) {
        File file = new File("./inputs/day2/day2.txt");

        try {
            Scanner sc = new Scanner(file);
            List<String> commands = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                commands.add(line);
            }

            int part1 = part1(commands);
            System.out.println("Part 1: " + part1);

            int part2 = part2(commands);
            System.out.println("Part 2: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: Iterates through the list of commands and calculates the final horizontal position
    // and final depth of the submarine.
    // The command: "forward X" increases the horizontal position by X units.
    // The command: "down X" increases the depth by X units.
    // The command: "up X" decreases the depth by X units.
    // Returns the product of the final horizontal position and the final depth.
    private static int part1(List<String> commands) {
        int depth = 0;
        int horizontalPosition = 0;

        for (String command : commands) {
            String[] tokens = command.split(" ");
            String direction = tokens[0];
            int value = Integer.parseInt(tokens[1]);

            switch(direction) {
                case FORWARD:
                    horizontalPosition += value;
                    break;
                case UP:
                    depth -= value;
                    break;
                case DOWN:
                    depth += value;
                    break;
                default:
                    break;
            }
        }

        return depth * horizontalPosition;
    }

    // Part 2: Iterates through the list of commands and calculates the final horizontal position
    // and final depth of the submarine.
    // The command: "down X" increases aim by X units.
    // The command: "up X" decreases aim by X units.
    // The command: "forward X" increases horizontal position by X units and increases depth by aim multiplied by X.
    // Returns the product of the final horizontal position and the final depth.
    private static int part2(List<String> commands) {
        int depth = 0;
        int horizontalPosition = 0;
        int aim = 0;

        for (String command : commands) {
            String[] tokens = command.split(" ");
            String direction = tokens[0];
            int value = Integer.parseInt(tokens[1]);

            switch(direction) {
                case FORWARD:
                    horizontalPosition += value;
                    depth += aim * value;
                    break;
                case UP:
                    aim -= value;
                    break;
                case DOWN:
                    aim += value;
                    break;
                default: break;
            }
        }

        return depth * horizontalPosition;
    }
}
