import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day3_Binary_Diagnostic {
    private static final Integer LENGTH_OF_INPUT_BITS = 12;

    public static void main(String[] args) {
        File file = new File("./inputs/day3/day3.txt");

        try {
            // Creating an object of BufferedReader class
            BufferedReader br = new BufferedReader(new FileReader(file));
            //int powerConsumption = getSubmarinePowerConsumption(br);
            //System.out.println("Submarine power consumption is: " + powerConsumption);

            int lifeSupportRating = getSubmarineLifeSupportRating(br);
            System.out.println("Submarine power consumption is: " + lifeSupportRating);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static List<String> readBufferedReaderIntoList(BufferedReader br) throws IOException {
        List<String> inputList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            inputList.add(line);
        }
        return inputList;
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

    private static int convertStringOfBinaryToInteger(String binary) {
        int output = 0;
        char[] chars = binary.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] == '0') continue;
            if (chars[i] == '1') {
                output += (int)Math.pow(2, (chars.length - 1 - i));
            }
        }

        return output;
    }

    // Given an integer array of 1s and 0s, flips the bits of that array
    private static int[] flipBits(int[] array) {
        int[] flipped = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) flipped[i] = 1;
            else if (array[i] == 1) flipped[i] = 0;
        }

        return flipped;
    }

    // Part 1: Power consumption of the submarine is gamma rate * epsilon rate
    // Gamma rate is the binary number formed by the most common bit in each position
    // Epsilon rate is the binary number formed by the least common bit in each position
    private static int getSubmarinePowerConsumption(BufferedReader br) throws IOException {
        int[] mostCommonBits = new int[LENGTH_OF_INPUT_BITS];
        String inputBits;

        // Iterate through each input bits string and for every '0', decrement the value stored
        // in mostCommonBits and for every '1', increment the value stored in mostCommonBits
        // At the end, if the value in each position is positive, the most common bit is 1.
        // If the value in each position is negative, the most common bit is 0.
        while((inputBits = br.readLine()) != null) {
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

    private static List<String> filterInputList(List<String> inputList, int consideringBit, char bitToFilterOn) {
        return inputList
                .stream()
                .filter(b -> b.charAt(consideringBit) == bitToFilterOn)
                .collect(Collectors.toList());
    }

    private static int calculateOxygenGeneratorRating(List<String> inputList) {
        int mostCommonBit = 0;
        for (int consideringBit = 0; consideringBit < LENGTH_OF_INPUT_BITS; consideringBit++) {
            mostCommonBit = 0;
            for (int i = 0; i < inputList.size(); i++) {
                String bits = inputList.get(i);
                if (bits.charAt(consideringBit) == '0') mostCommonBit--;
                if (bits.charAt(consideringBit) == '1') mostCommonBit++;
            }

            if (mostCommonBit == 0) {
                inputList = filterInputList(inputList, consideringBit, '1');
            } else if (mostCommonBit > 0){
                inputList = filterInputList(inputList, consideringBit, '1');
            } else if (mostCommonBit < 0) {
                inputList = filterInputList(inputList, consideringBit, '0');
            }

            if (inputList.size() == 1)
                return convertStringOfBinaryToInteger(inputList.get(0));
        }

        return convertStringOfBinaryToInteger(inputList.get(0));
    }

    private static int calculateCO2ScrubberRating(List<String> inputList) {
        int mostCommonBit = 0;
        for (int consideringBit = 0; consideringBit < LENGTH_OF_INPUT_BITS; consideringBit++) {
            mostCommonBit = 0;
            for (int i = 0; i < inputList.size(); i++) {
                String bits = inputList.get(i);
                if (bits.charAt(consideringBit) == '0') mostCommonBit--;
                if (bits.charAt(consideringBit) == '1') mostCommonBit++;
            }

            if (mostCommonBit == 0) {
                inputList = filterInputList(inputList, consideringBit, '0');
            } else if (mostCommonBit > 0){
                inputList = filterInputList(inputList, consideringBit, '0');
            } else if (mostCommonBit < 0) {
                inputList = filterInputList(inputList, consideringBit, '1');
            }

            if (inputList.size() == 1)
                return convertStringOfBinaryToInteger(inputList.get(0));
        }

        return convertStringOfBinaryToInteger(inputList.get(0));
    }

    // Part 2: Life support rating (LSR) is the oxygen generator rating (OGR) * CO2 scrubber rating (CSR)
    // OGR is the number gotten by incrementally filtering out numbers whose bits from left to right
    // are the most common. CSR is the number gotten by incrementally filtering out numbers whose
    // bits from left to right are the least common.
    private static int getSubmarineLifeSupportRating(BufferedReader br) throws IOException {
        List<String> inputList = readBufferedReaderIntoList(br);

        int ogr =  calculateOxygenGeneratorRating(inputList);
        int csr = calculateCO2ScrubberRating(inputList);
        return ogr * csr;
    }
}
