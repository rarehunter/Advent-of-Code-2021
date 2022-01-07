import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day8_Seven_Segment_Search {
    // The number of segments it takes to form a digit
    private static final int DIGIT_ONE_SEGMENTS = 2;
    private static final int DIGIT_FOUR_SEGMENTS = 4;
    private static final int DIGIT_SEVEN_SEGMENTS = 3;
    private static final int DIGIT_EIGHT_SEGMENTS = 7;

    private static final int UNIQUE_SIGNAL_PATTERNS = 10;
    private static final int OUTPUT_VALUE_DIGITS = 4;

    public static void main(String[] args) {
        File file = new File("./inputs/day8/day8.txt");

        try {
            Scanner sc = new Scanner(file);
            List<Entry> entries = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.split(" ");
                List<String> signalPatterns = new ArrayList<>();
                List<String> outputValues = new ArrayList<>();

                // The first tokens are the ten signal patterns.
                for (int i = 0; i < UNIQUE_SIGNAL_PATTERNS; i++) {
                    signalPatterns.add(tokens[i]);
                }

                // Skip the pipe (|) delimeter. The next four tokens represents the 4-digit output value.
                for (int i = UNIQUE_SIGNAL_PATTERNS+1; i <= UNIQUE_SIGNAL_PATTERNS + OUTPUT_VALUE_DIGITS; i++) {
                    outputValues.add(tokens[i]);
                }

                entries.add(new Entry(signalPatterns, outputValues));
            }

            int part1 = part1(entries);
            System.out.println("Part 1: " + part1);

            int part2 = part2(entries);
            System.out.println("Part 2: " + part2);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: Given a list of entries of the input file (each with ten unique signal patterns and a 4-digit
    // output value), returns the number of times digits that have a unique number of segments occur in the entries.
    // A "1" is made up of 2 segments. A "4" is made up of 4 segments. A "7" is made up of 3 segments"
    // And a "8" is made up of 7 segments. There are the only digits which use a unique number of segments.
    private static int part1(List<Entry> entries) {
        int count = 0;

        for (Entry entry : entries) {
            for (String value : entry.outputValues) {
                if (value.length() == DIGIT_ONE_SEGMENTS ||
                    value.length() == DIGIT_FOUR_SEGMENTS ||
                    value.length() == DIGIT_SEVEN_SEGMENTS ||
                    value.length() == DIGIT_EIGHT_SEGMENTS) {
                    count++;
                }
            }
        }

        return count;
    }

    // Returns true if s1 and s2 are anagrams of each other
    private static boolean isAnagram(String s1, String s2) {
        Map<Character, Integer> freq = new HashMap<>();

        // Put all the characters of the first string into a map with their character count.
        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);

            if (freq.containsKey(c)) {
                freq.put(c, freq.get(c)+1);
            } else {
                freq.put(c, 1);
            }
        }

        // Reduce the character count while iterating through the second string.
        // If a character is encountered that isn't in s1, return false.
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (freq.containsKey(c)) {
                freq.put(c, freq.get(c)-1);
            } else {
                return false;
            }
        }

        // Finally, check if our map contains all 0s. If two strings are anagrams, its characters
        // must appear the same number of times.
        for (Integer i : freq.values()) {
            if (i != 0) {
                return false;
            }
        }

        return true;
    }

    // Given a list of strings representing a four-digit number, use the digitPatterns mapping to
    // return the int representation of the 4-digit number.
    private static int determineOutputInts(List<String> outputValues, Map<String,Integer> digitPatterns) {
        StringBuilder sb = new StringBuilder();

        // For each string of the output values, convert it to a digit.
        // If it has a length that is a unique digit, that is fairly easy.
        // Otherwise, we compare the string to every pattern in our map to determine if two are anagrams
        // of each other. Signal patterns may not have segments in the same order which is why anagram-checking
        // is needed. If a match is found, we not down the digit associated with that pattern.
        for (String digit : outputValues) {
            if (digit.length() == DIGIT_ONE_SEGMENTS) {
                sb.append(1);
            } else if (digit.length() == DIGIT_FOUR_SEGMENTS) {
                sb.append(4);
            } else if (digit.length() == DIGIT_SEVEN_SEGMENTS) {
                sb.append(7);
            } else if (digit.length() == DIGIT_EIGHT_SEGMENTS) {
                sb.append(8);
            } else {
                for (String pattern : digitPatterns.keySet()) {
                    if (isAnagram(digit, pattern)) {
                        sb.append(digitPatterns.get(pattern));
                    }
                }
            }
        }

        return Integer.parseInt(sb.toString());
    }

    // Returns a string of characters that differ between the given strings
    private static String findDifference(String shorter, String longer) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < longer.length(); i++) {
            char c = longer.charAt(i);
            if (shorter.indexOf(c) < 0) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    // The digit 3 has five segments and is the only five-segment digit that shares the same segments as digit 1.
    private static String determineDigitThree(List<String> fiveSegmentPatterns, Map<String,Integer> digitPatterns) {
        String digitOnePattern = "";
        for (Map.Entry<String, Integer> pair : digitPatterns.entrySet()) {
            if (pair.getValue() == 1)
                digitOnePattern = pair.getKey();
        }

        // Find the pattern that contains the same segments as digit 1
        for (String signal : fiveSegmentPatterns) {
            if (signal.indexOf(digitOnePattern.charAt(0)) >= 0 && signal.indexOf(digitOnePattern.charAt(1)) >= 0) {
                digitPatterns.put(signal, 3);
                return signal;
            }
        }

        return "";
    }

    // The digit 5 has five segments and is the only five-segment digit that shares the left and middle
    // segments with digit 4.
    private static String determineDigitFive(List<String> fiveSegmentPatterns, Map<String,Integer> digitPatterns) {
        // First, find the pattern for 1 (the one that has exactly 2 segments).
        // Then, find the pattern for 4 (the one that has exactly 4 segments).
        // By determining the unique segments between the two signals, that is the left and middle segment
        // which is only present in segments of length 5 which has to be a value of 5.

        String digitOnePattern = "";
        String digitFourPattern = "";
        for (Map.Entry<String, Integer> pair : digitPatterns.entrySet()) {
            if (pair.getValue() == 1)
                digitOnePattern = pair.getKey();
            else if (pair.getValue() == 4)
                digitFourPattern = pair.getKey();
        }

        String leftAndMiddleSegments = findDifference(digitOnePattern, digitFourPattern);

        // Find the pattern that contains the same left and middle segments.
        for (String signal : fiveSegmentPatterns) {
            if (signal.indexOf(leftAndMiddleSegments.charAt(0)) >= 0 &&
                signal.indexOf(leftAndMiddleSegments.charAt(1)) >= 0) {
                digitPatterns.put(signal, 5);
                return signal;
            }
        }

        return "";
    }

    // The digit 6 has six segments and is the only six-segment digit
    // that does NOT have the same segments as digit 1.
    private static String determineDigitSix(List<String> sixSegmentPatterns, Map<String,Integer> digitPatterns) {
        String digitOnePattern = "";
        for (Map.Entry<String, Integer> pair : digitPatterns.entrySet()) {
            if (pair.getValue() == 1)
                digitOnePattern = pair.getKey();
        }

        // Find the pattern that does NOT share the same segments as digit 1.
        for (String signal : sixSegmentPatterns) {
            if (signal.indexOf(digitOnePattern.charAt(0)) < 0 || signal.indexOf(digitOnePattern.charAt(1)) < 0) {
                digitPatterns.put(signal, 6);
                return signal;
            }
        }

        return "";
    }

    // The digit 9 has six segments and is the only six-segment digit
    // that fully contains the same segments as digit 4
    private static String determineDigitNine(List<String> sixSegmentPatterns, Map<String,Integer> digitPatterns) {
        String digitFourPattern = "";
        for (Map.Entry<String, Integer> pair : digitPatterns.entrySet()) {
            if (pair.getValue() == 4)
                digitFourPattern = pair.getKey();
        }

        for (String signal : sixSegmentPatterns) {
            if (signal.indexOf(digitFourPattern.charAt(0)) >= 0 &&
                signal.indexOf(digitFourPattern.charAt(1)) >= 0 &&
                signal.indexOf(digitFourPattern.charAt(2)) >= 0 &&
                signal.indexOf(digitFourPattern.charAt(3)) >= 0) {
                digitPatterns.put(signal, 9);
                return signal;
            }
        }

        return "";
    }

    // Insert pattern-digit mappings for digits with five-segments (i.e. digits 2,3,5)
    private static void populateFiveSegmentDigits(List<String> fiveSegmentPatterns, Map<String,Integer> digitPatterns) {
        String digitThreePattern = determineDigitThree(fiveSegmentPatterns, digitPatterns);
        String digitFivePattern = determineDigitFive(fiveSegmentPatterns, digitPatterns);

        // Once digits 3 and 5 are inserted into our patterns mapping,
        // the remaining five-segment pattern string would be digit 2.
        for (String pattern : fiveSegmentPatterns) {
            if (!pattern.equals(digitThreePattern) && !pattern.equals(digitFivePattern)) {
                digitPatterns.put(pattern, 2);
            }
        }
    }

    // Insert pattern-digit mappings for digits with six-segments (i.e. digits 0,6,9)
    private static void populateSixSegmentDigits(List<String> sixSegmentPatterns, Map<String,Integer> digitPatterns) {
        String digitSixPattern = determineDigitSix(sixSegmentPatterns, digitPatterns);
        String digitNinePattern = determineDigitNine(sixSegmentPatterns, digitPatterns);

        // Once digits 6 and 9 are inserted into our patterns mapping,
        // the remaining six-segment pattern string would be digit 0.
        for (String pattern : sixSegmentPatterns) {
            if (!pattern.equals(digitSixPattern) && !pattern.equals(digitNinePattern)) {
                digitPatterns.put(pattern, 0);
            }
        }
    }

    // Given a list of signal pattern strings and a dictionary mapping a signal pattern string
    // to the digit it represents, insert four entries for the four digits with a unique number of segments.
    private static void populateUniqueDigits(List<String> signalPatterns, Map<String,Integer> digitPatterns) {
        for (String signal : signalPatterns) {
            if (signal.length() == DIGIT_ONE_SEGMENTS) {
                digitPatterns.put(signal, 1);
            } else if (signal.length() == DIGIT_FOUR_SEGMENTS) {
                digitPatterns.put(signal, 4);
            } else if (signal.length() == DIGIT_SEVEN_SEGMENTS) {
                digitPatterns.put(signal, 7);
            } else if (signal.length() == DIGIT_EIGHT_SEGMENTS) {
                digitPatterns.put(signal, 8);
            }
        }
    }

    // Given a list of signal pattern strings and a dictionary mapping a signal pattern string
    // to the digit it represents, insert the remaining six entries.
    private static void populateRemainingDigits(List<String> signalPatterns, Map<String,Integer> digitPatterns) {
        // First, let's only consider the signal patterns with five segments.
        List<String> fiveSegmentPatterns = signalPatterns
                .stream()
                .filter(s -> s.length() == 5)
                .collect(Collectors.toList());

        populateFiveSegmentDigits(fiveSegmentPatterns, digitPatterns);

        // Finally, let's consider the signal patterns with six segments.
        List<String> sixSegmentPatterns = signalPatterns
                .stream()
                .filter(s -> s.length() == 6)
                .collect(Collectors.toList());

        populateSixSegmentDigits(sixSegmentPatterns, digitPatterns);
    }

    // Part 2: Returns the sum of all the 4-digit output values given by the entries in the input list.
    // We construct a mapping between a signal pattern and its associated digit. Digits with a unique number
    // of segments (i.e. 1,4,7,8) are easy to associate. For digits with five and six segments, we have to
    // compare and contrast their segments with that of other digits in order to determine association.
    // Once the map is built, we use that to figure out each digit of the 4-digit output values and sum them all up.
    private static int part2(List<Entry> entries) {
        int sum = 0;

        // Iterate through each entry.
        for (Entry entry : entries) {
            List<String> signalPatterns = entry.signalPatterns;

            // Maps a unique signal string to the digit that it represents.
            Map<String, Integer> digitPatterns = new HashMap<>();

            // We know that signal strings of length 2,4,3,7 represent digits 1,4,7,8 respectively,
            // so we add those to our map first.
            populateUniqueDigits(signalPatterns, digitPatterns);

            // Add the non-unique digits to the map
            populateRemainingDigits(signalPatterns, digitPatterns);

            sum += determineOutputInts(entry.outputValues, digitPatterns);
        }

        return sum;
    }

    // Class representing an entry in the input file.
    // Associates the ten unique signal patterns
    static class Entry {
        List<String> signalPatterns;
        List<String> outputValues;

        public Entry(List<String> signalPatterns, List<String> outputValues) {
            this.signalPatterns = signalPatterns;
            this.outputValues = outputValues;
        }
    }
}