import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Day2_Dive {
    private static final String FORWARD = "forward";
    private static final String UP = "up";
    private static final String DOWN = "down";

    public static void main(String[] args) {
        File file = new File("./inputs/day2/day2.txt");

        try {
            // Creating an object of BufferedReader class
            BufferedReader br = new BufferedReader(new FileReader(file));
            //int multipliedSubmarinePosition = submarinePosition(br);
            int multipliedSubmarinePosition = submarinePositionWithAim(br);

            System.out.println("Submarine has multiplied position: " + multipliedSubmarinePosition);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Parse an instruction that looks like "forward 8" into the string "forward" and the string "8"
    private static String[] parseInput(String instruction) {
        return instruction.split(" ");
    }

    // Part 1
    private static int submarinePosition(BufferedReader br) throws IOException {
        String instruction;
        int depth = 0;
        int horizontalPosition = 0;

        while((instruction = br.readLine()) != null) {
            String[] tokens = parseInput(instruction);
            String direction = tokens[0];
            int value = Integer.parseInt(tokens[1]);

            switch(direction) {
                case FORWARD: horizontalPosition += value;
                    break;
                case UP: depth -= value;
                    break;
                case DOWN: depth += value;
                    break;
                default: break;
            }
        }

        return depth * horizontalPosition;
    }

    // Part 2
    private static int submarinePositionWithAim(BufferedReader br) throws IOException {
        String instruction;
        int depth = 0;
        int horizontalPosition = 0;
        int aim = 0;

        while((instruction = br.readLine()) != null) {
            String[] tokens = parseInput(instruction);
            String direction = tokens[0];
            int value = Integer.parseInt(tokens[1]);

            switch(direction) {
                case FORWARD:
                    horizontalPosition += value;
                    depth += aim * value;
                    break;
                case UP: aim -= value;
                    break;
                case DOWN: aim += value;
                    break;
                default: break;
            }
        }

        return depth * horizontalPosition;
    }

}
