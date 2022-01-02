import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Day23_Amphipod {
    public static void main(String[] args) {
        File file = new File("./inputs/day23/day23.example.txt");

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
            }

            int part1 = part1();
            System.out.println("Part 1 is: " + part1);

            int part2 = part2();
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: For this part, there was no need to code up a solution. Rather, solving it manually by hand
    // proved to be faster.
    // Letters A,B,C,D denote rooms belonging to amphipods A,B,C, and D.
    // Numbers indicate indices of the 11 spot hallway.
    private static int part1() {
        // B->0
        // B->1
        // C->7
        // C->B
        // 7->C
        // D->C
        // D->B
        // A->D
        // A->D
        // 1->A
        // 0->A
        return 19046;
    }

    // Part 2: For this part, there was no need to code up a solution. Rather, solving it manually by hand
    // proved to be faster.
    // Letters A,B,C,D denote rooms belonging to amphipods A,B,C, and D.
    // Numbers indicate indices of the 11 spot hallway.
    private static int part2() {
        // B->10
        // B->9
        // B->5
        // B->0
        // 5->B
        // C->7
        // C->B
        // C->1
        // C->B
        // 7->C
        // 9->C
        // D->C
        // D->9
        // D->C
        // D->B
        // A->D
        // A->D
        // A->D
        // A->D
        // 9->A
        // 10->A
        // 1->A
        // 0->A
        return 47484;
    }
}
