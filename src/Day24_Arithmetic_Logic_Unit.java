import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Day24_Arithmetic_Logic_Unit {
    public static void main(String[] args) {
        long part1 = part1();
        System.out.println("Part 1 is: " + part1);

        long part2 = part2();
        System.out.println("Part 2 is: " + part2);
    }

    // For the first digit, no immediate dependency.
    private static int runCalculation1(int input1) {
        return input1 + 8;
    }

    // For the second digit, no immediate dependency.
    private static int runCalculation2(int input2, int z) {
        return (26 * z) + (input2 + 16);
    }

    // For the third digit, no immediate dependency.
    private static int runCalculation3(int input3, int z) {
        return (26 * z) + (input3 + 4);
    }

    // If the 3rd digit is 8,9
    // the 4th digit should be 1,2 respectively.
    private static int runCalculation4(int input4, int z) {
        return z / 26;
    }

    // For the fifth digit, no immediate dependency.
    private static int runCalculation5(int input5, int z) {
        return (26 * z) + (input5 + 13);
    }

    // For the sixth digit, no immediate dependency.
    private static int runCalculation6(int input6, int z) {
        return (26 * z) + (input6 + 5);
    }

    // For the seventh digit, no immediate dependency.
    private static int runCalculation7(int input7, int z) {
        return (26 * z) + input7;
    }

    // If the seventh digit is 6,7,8,9,
    // the eight digit should be 1,2,3,4 respectively.
    private static int runCalculation8(int input8, int z) {
        return z / 26;
    }

    // For the ninth digit, no immediate dependency.
    private static int runCalculation9(int input9, int z) {
        return (26 * z) + (input9 + 7);
    }

    // If the ninth digit is 1,2,
    // the tenth digit should be 8,9 respectively.
    private static int runCalculation10(int input10, int z) {
        return z / 26;
    }

    // If the sixth digit is 7,8,9,
    // the eleventh digit should be 1,2,3 respectively.
    private static int runCalculation11(int input11, int z) {
        return z / 26;
    }

    // If the fifth digit is 1,2,3,4,5,6,7,8,9,10,
    // the twelfth digit should be the same.
    private static int runCalculation12(int input12, int z) {
        return z / 26;
    }

    // If the second digit is 1,2,3,4,5,6,
    // the thirteenth digit should be 4,5,6,7,8,9 respectively.
    private static int runCalculation13(int input13, int z) {
        return z / 26;
    }

    // If the first digit is 4,5,6,7,8,9,
    // the fourteenth digit should be 1,2,3,4,5,6 respectively.
    private static int runCalculation14(int input14, int z) {
        return z / 26;
    }

    // Part 1: Manually (by hand) worked through the instructions given in the input file,
    // noting that there are three instructions that differ between each of the 14 input instruction sets:
    // the divisor by z, x += something and y += something.
    // For each digit of the hypothetical 14-digit number, worked through the instructions and determined how
    // the digits would relate to each other such that the final output of all the instructions is 0.
    // Worked out the maximum numbers for each digit.
    private static long part1() {
        return 96929994293996L;
    }

    // Part 2: This the same thing as part 1 but just the minimum numbers instead of the maximum numbers.
    private static long part2() {
        return 41811761181141L;
    }
}
