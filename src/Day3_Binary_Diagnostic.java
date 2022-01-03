import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day3_Binary_Diagnostic {
    public static void main(String[] args) {
        File file = new File("./inputs/day3/day3.txt");

        try {
            Scanner sc = new Scanner(file);
            List<String> inputs = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                inputs.add(line);
            }

            int part1 = part1(inputs);
            System.out.println("Part 1: " + part1);

            int part2 = part2(inputs);
            System.out.println("Part 2: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Given an integer array of 1s and 0s representing a binary number,
    // converts that array to an integer.
    // [1, 0, 1, 0] => 10
    private static int convertIntArrayOfBinaryToInteger(int[] array) {
        int output = 0;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 0) continue;
            if (array[i] == 1) {
                output += (int)Math.pow(2, (array.length - 1 - i));
            }
        }

        return output;
    }

    // Given an integer array of 1s and 0s, returns a new array where every bit is flipped.
    private static int[] flipBits(int[] array) {
        int[] flipped = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) flipped[i] = 1;
            else if (array[i] == 1) flipped[i] = 0;
        }

        return flipped;
    }

    // Part 1: Finds the most and least common bit in the corresponding positions of all numbers in the input.
    // Gamma rate is the binary number formed by the most common bit in each position
    // Epsilon rate is the binary number formed by the least common bit in each position.
    // Power consumption of the submarine is the product of gamma rate and epsilon rate.
    private static int part1(List<String> inputs) {
        int binaryNumberLength = inputs.size() > 0 ? inputs.get(0).length() : 0;
        int[] mostCommonBits = new int[binaryNumberLength];

        // Approach: Iterate through each input bits string.
        // Keep track of a value for each position of the input bits.
        // For every '0', decrement the value. For every '1', increment the value.
        // At the end, if the value is positive, the most common bit at that position is 1.
        // If the value is negative, the most common bit at that position is 0.
        for (String inputBits : inputs) {
            for (int i = 0; i < inputBits.length(); i++) {
                char bit = inputBits.charAt(i);
                if (bit == '0') {
                    mostCommonBits[i] -= 1;
                } else if (bit == '1') {
                    mostCommonBits [i] += 1;
                }
            }
        }

        // Iterate through the mostCommonBits array and determine if the most common bit is 1 or 0.
        for (int i = 0; i < mostCommonBits.length; i++) {
            if (mostCommonBits[i] > 0) {
                mostCommonBits[i] = 1;
            } else if (mostCommonBits[i] < 0) {
                mostCommonBits[i] = 0;
            }
        }

        int gammaRate = convertIntArrayOfBinaryToInteger(mostCommonBits);
        int[] leastCommonBits = flipBits(mostCommonBits);
        int epsilonRate = convertIntArrayOfBinaryToInteger(leastCommonBits);
        return gammaRate * epsilonRate;
    }

    // Given a list of binary numbers as strings, keep elements in the list in which the bit at the
    // given position is equivalent to the bit that we want to filter on.
    private static List<String> filterInputList(List<String> inputList, int consideringBit, char bitToFilterOn) {
        return inputList
                .stream()
                .filter(b -> b.charAt(consideringBit) == bitToFilterOn)
                .collect(Collectors.toList());
    }

    // Given a list of input binary numbers, return the decimal number remaining by
    // determine the most common bit (0 or 1) in each bit position (from left to right),
    // and keeping numbers with that bit in that position. If 0 and 1 are equally common,
    // keep values with a 1 in the position being considered.
    private static int calculateOxygenGeneratorRating(List<String> inputs) {
        int binaryNumberLength = inputs.size() > 0 ? inputs.get(0).length() : 0;
        int mostCommonBit = 0;

        // Iterate through each bit position.
        for (int consideringBit = 0; consideringBit < binaryNumberLength; consideringBit++) {
            mostCommonBit = 0;

            // Determine the most common bit in that bit position.
            // A negative number means the most common bit is 0.
            // A positive number means the most common bit is 1.
            for (int i = 0; i < inputs.size(); i++) {
                String bits = inputs.get(i);
                if (bits.charAt(consideringBit) == '0') mostCommonBit--;
                if (bits.charAt(consideringBit) == '1') mostCommonBit++;
            }

            // If 1 is the most common bit, keep values with a '1' in the bit position.
            // If 0 is the most common bit, keep values with a '0' in the bit position.
            // If 0 and 1 are equally common, keep values with a '1' in the bit position.
            if (mostCommonBit == 0) {
                inputs = filterInputList(inputs, consideringBit, '1');
            } else if (mostCommonBit > 0){
                inputs = filterInputList(inputs, consideringBit, '1');
            } else if (mostCommonBit < 0) {
                inputs = filterInputList(inputs, consideringBit, '0');
            }

            // If we only have one number remaining, we've found our OGR.
            if (inputs.size() == 1)
                return Integer.parseInt(inputs.get(0), 2);
        }

        return Integer.parseInt(inputs.get(0), 2);
    }

    // Given a list of input binary numbers, return the decimal number remaining by
    // determine the least common bit (0 or 1) in each bit position (from left to right),
    // and keeping numbers with that bit in that position. If 0 and 1 are equally common,
    // keep values with a 0 in the position being considered.
    private static int calculateCO2ScrubberRating(List<String> inputs) {
        int binaryNumberLength = inputs.size() > 0 ? inputs.get(0).length() : 0;
        int mostCommonBit = 0;

        // Iterate through each bit position.
        for (int consideringBit = 0; consideringBit < binaryNumberLength; consideringBit++) {
            mostCommonBit = 0;

            // Determine the most common bit in that bit position.
            // A negative number means the most common bit is 0.
            // A positive number means the most common bit is 1.
            for (int i = 0; i < inputs.size(); i++) {
                String bits = inputs.get(i);
                if (bits.charAt(consideringBit) == '0') mostCommonBit--;
                if (bits.charAt(consideringBit) == '1') mostCommonBit++;
            }

            // If 1 is the most common bit, keep values with a '0' in the bit position.
            // If 0 is the most common bit, keep values with a '1' in the bit position.
            // If 0 and 1 are equally common, keep values with a '0' in the bit position.
            if (mostCommonBit == 0) {
                inputs = filterInputList(inputs, consideringBit, '0');
            } else if (mostCommonBit > 0){
                inputs = filterInputList(inputs, consideringBit, '0');
            } else if (mostCommonBit < 0) {
                inputs = filterInputList(inputs, consideringBit, '1');
            }

            if (inputs.size() == 1)
                return Integer.parseInt(inputs.get(0), 2);
        }

        return Integer.parseInt(inputs.get(0), 2);
    }

    // Part 2: Calculate both the OGR and CSR.
    // To calculate OGR, determine the most common bit (0 or 1) in the current bit position,
    // and keep only numbers with that bit in that position. If 0 and 1 are equally common,
    // keep values with a 1 in the position being considered.
    // To calculate CSR, determine the least common bit in the current bit position and keep only numbers with that
    // bit in that position. If 0 and 1 are equally common, keep values with a 0 in the position being considered.
    // In other words, for OGR, we are incrementally filtering out binary numbers whose bits (from left to right)
    // are the most common. For CSR, we are incrementally filtering out binary numbers whose bits from left to right
    // are the least common.
    // Return the life support rating of the submarine which is the product of the OGR and CSR.
    private static int part2(List<String> inputs) {
        int ogr =  calculateOxygenGeneratorRating(inputs);
        int csr = calculateCO2ScrubberRating(inputs);
        return ogr * csr;
    }

    // Further improvements:
    // 1) In part 1, once we have an integer that represents the binary number formed by
    // the most common bit in each position, it seems silly to flip all the bits of an int array and recalculate
    // an integer for the least common bit. There is a way to use some bit shifting to generate a bit mask and XOR
    // operations to get the decimal number that is the bitwise inversion of the first number.
    //
    // 2) In part 2, the code for calculating the OGR and CSR is very similar even though
    // they both filter out the input list in different ways. The current algorithm results in a lot of repeated
    // work: one pass is to repeatedly calculating the most common bits and filtering the list based off of that.
    // Another pass is performed to repeatedly calculating the least common bits and filtering the list based off of
    // that. There should be a way to elegantly combine this work into a single pass instead of two.
}
