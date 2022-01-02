import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day8_Seven_Segment_Search {
    private static final int NUM_SEGMENTS_ONE = 2;
    private static final int NUM_SEGMENTS_FOUR = 4;
    private static final int NUM_SEGMENTS_SEVEN = 3;
    private static final int NUM_SEGMENTS_EIGHT = 7;
    private static final int NUM_SEGMENTS_OTHER_FIVE = 5;
    private static final int NUM_SEGMENTS_OTHER_SIX = 6;

    public static void main(String[] args) {
        File file = new File("./inputs/day8/day8.txt");
        Map<Integer, List<String[]>> map = new HashMap<>();
        List<String> signalPatterns = new ArrayList<>();
        List<String> outputValues = new ArrayList<>();

        try {
            Scanner sc = new Scanner(file);

            // For part 1
            /*
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.split("\\|");
                String[] leftSide = tokens[0].trim().split(" ");
                String[] rightSide = tokens[1].trim().split(" ");

                for (String leftString : leftSide) {
                    signalPatterns.add(leftString);
                }

                for (String rightString : rightSide) {
                    outputValues.add(rightString);
                }
            }
            */

            // For part 2
            int i = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.split("\\|");
                String[] leftSide = tokens[0].trim().split(" ");
                String[] rightSide = tokens[1].trim().split(" ");

                List<String[]> entries = new ArrayList<>();
                entries.add(leftSide);
                entries.add(rightSide);
                map.put(i, entries);

                i++;
            }

            //int numUniqueSegmentDigits = countDigitsWithUniqueSegments(outputValues);
            //System.out.println("The number of unique segment digits is: " + numUniqueSegmentDigits);


            int sumOfOutputDigits = determineOutputDigits(map);
            System.out.println("The sum of output digits is: " + sumOfOutputDigits);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Part 1: Returns the number of times digits that have a unique number of segments (i.e. 1, 4, 7, 8)
    // occur in the input.
    private static int countDigitsWithUniqueSegments(List<String> outputValues) {
        int sum = 0;

        for (String value : outputValues) {
            if (value.length() == NUM_SEGMENTS_ONE ||
                    value.length() == NUM_SEGMENTS_FOUR ||
                    value.length() == NUM_SEGMENTS_SEVEN ||
                    value.length() == NUM_SEGMENTS_EIGHT) {
                sum++;
            }
        }

        return sum;
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

    // A value of 3 has five segments and is the only five-segment digit that has the same
    // segments as 1.
    private static String determineValueThree(String[] signals) {
        String one = "";
        for (String signal : signals) {
            if (signal.length() == NUM_SEGMENTS_ONE) {
                one = signal;
            }
        }

        for (String signal : signals) {
            if (signal.length() == NUM_SEGMENTS_OTHER_FIVE &&
                    signal.indexOf(one.charAt(0)) >= 0 &&
                    signal.indexOf(one.charAt(1)) >= 0) {
                return signal;
            }
        }

        return "";
    }

    // A value of 5 has five segments and is the only five-segment digit that shares the left and middle
    // segments as a 4.
    private static String determineValueFive(String[] signals) {
        // First, find the signal for 1 (the one that has only 2 segments).
        // Then, find the signal for 4 (the one that has only 4 segments).
        // By determining the unique segments between the two signals, that is the left and middle segment
        // which is only present in segments of length 5 which has to be a value of 5.

        String one = "";
        String four = "";
        for (String signal : signals) {
            if (signal.length() == NUM_SEGMENTS_ONE) {
                one = signal;
            } else if (signal.length() == NUM_SEGMENTS_FOUR) {
                four = signal;
            }
        }

        String leftAndMiddle = findDifference(one, four);

        for (String signal : signals) {
            if (signal.length() == NUM_SEGMENTS_OTHER_FIVE &&
                    signal.indexOf(leftAndMiddle.charAt(0)) >= 0 &&
                    signal.indexOf(leftAndMiddle.charAt(1)) >= 0) {
                return signal;
            }
        }

        return "";
    }

    // A value of 6 has six segments and is the only six-segment digit that does NOT have
    // the same segments as 1.
    private static String determineValueSix(String[] signals) {
        String one = "";
        for (String signal : signals) {
            if (signal.length() == NUM_SEGMENTS_ONE) {
                one = signal;
            }
        }

        for (String signal : signals) {
            if (signal.length() == NUM_SEGMENTS_OTHER_SIX &&
                    (signal.indexOf(one.charAt(0)) < 0 ||
                            signal.indexOf(one.charAt(1)) < 0)) {
                return signal;
            }
        }

        return "";
    }

    // A value of 9 has six segments and is the only six-segment that fully contains the same segments as 4
    private static String determineValueNine(String[] signals) {
        String four = "";
        for (String signal : signals) {
            if (signal.length() == NUM_SEGMENTS_FOUR) {
                four = signal;
            }
        }

        for (String signal : signals) {
            if (signal.length() == NUM_SEGMENTS_OTHER_SIX &&
                    signal.indexOf(four.charAt(0)) >= 0 &&
                    signal.indexOf(four.charAt(1)) >= 0 &&
                    signal.indexOf(four.charAt(2)) >= 0 &&
                    signal.indexOf(four.charAt(3)) >= 0) {
                return signal;
            }
        }

        return "";
    }

    // Given a string of characters and the characters that represent 0,2,3,5,6,9,
    // returns the int representation of s.
    private static int determineOtherSegmentValue(String s,
                                                  String twoString, String threeString, String fiveString,
                                                  String zeroString, String sixString, String nineString) {
        char[] sArray = s.toCharArray();
        Arrays.sort(sArray);

        if (sArray.length == NUM_SEGMENTS_OTHER_FIVE) {
            char[] twoArray = twoString.toCharArray();
            char[] threeArray = threeString.toCharArray();
            char[] fiveArray = fiveString.toCharArray();

            Arrays.sort(twoArray);
            Arrays.sort(threeArray);
            Arrays.sort(fiveArray);

            if (Arrays.equals(sArray, twoArray)) {
                return 2;
            } else if (Arrays.equals(sArray, threeArray)) {
                return 3;
            } else if (Arrays.equals(sArray, fiveArray)) {
                return 5;
            }
        } else if (sArray.length == NUM_SEGMENTS_OTHER_SIX) {
            char[] zeroArray = zeroString.toCharArray();
            char[] sixArray = sixString.toCharArray();
            char[] nineArray = nineString.toCharArray();

            Arrays.sort(zeroArray);
            Arrays.sort(sixArray);
            Arrays.sort(nineArray);

            if (Arrays.equals(sArray, zeroArray)) {
                return 0;
            } else if (Arrays.equals(sArray, sixArray)) {
                return 6;
            } else if (Arrays.equals(sArray, nineArray)) {
                return 9;
            }
        }

        return -1;
    }

    // Given an array of length 4 that represents a 4-digit number, returns the int representation of that
    // 4-digit number
    private static int determineOutputInts(
            String[] outputs,
            String twoString, String threeString, String fiveString,
            String zeroString, String sixString, String nineString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < outputs.length; i++) {
            String s = outputs[i];
            if (s.length() == NUM_SEGMENTS_ONE) {
                sb.append(1);
            } else if (s.length() == NUM_SEGMENTS_FOUR) {
                sb.append(4);
            } else if (s.length() == NUM_SEGMENTS_SEVEN) {
                sb.append(7);
            } else if (s.length() == NUM_SEGMENTS_EIGHT) {
                sb.append(8);
            } else {
                int value = determineOtherSegmentValue(s, twoString, threeString, fiveString, zeroString, sixString, nineString);
                sb.append(value);
            }
        }

        return Integer.parseInt(sb.toString());
    }

    // Part 2: Returns the sum of all the 4-digit numbers represented by a sequence of characters
    private static int determineOutputDigits(Map<Integer, List<String[]>> map) {
        // For each entry...
        int sum = 0;
        for (Integer i : map.keySet()) {
            String[] signals = map.get(i).get(0);
            String[] outputs = map.get(i).get(1);

            String[] fiveSegments = Arrays.stream(signals)
                    .filter(x -> x.length() == NUM_SEGMENTS_OTHER_FIVE)
                    .toArray(String[]::new);

            // Strings of values with five segments (2, 3, 5)
            String fiveString = determineValueFive(signals);
            String threeString = determineValueThree(signals);
            String twoString = "";
            for (String fiveSegment : fiveSegments) {
                if (!fiveSegment.equals(fiveString) && !fiveSegment.equals(threeString)) {
                    twoString = fiveSegment;
                }
            }

            String[] sixSegments = Arrays.stream(signals)
                    .filter(x -> x.length() == NUM_SEGMENTS_OTHER_SIX)
                    .toArray(String[]::new);

            // Strings of values with six segments (0, 6, 9)
            String sixString = determineValueSix(signals);
            String nineString = determineValueNine(signals);
            String zeroString = "";

            for (String sixSegment : sixSegments) {
                if (!sixSegment.equals(sixString) && !sixSegment.equals(nineString)) {
                    zeroString = sixSegment;
                }
            }

            int outputInts = determineOutputInts(
                    outputs,
                    twoString,
                    threeString,
                    fiveString,
                    zeroString,
                    sixString,
                    nineString);

            sum += outputInts;
        }

        return sum;
    }
}
