import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day18_Snailfish_Second_Try {
    public static void main(String[] args) {
        File file = new File("./inputs/day18/day18.txt");

        try {
            Scanner sc = new Scanner(file);
            List<String> numbers = new ArrayList<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                numbers.add(line);
            }

            long part1 = part1(numbers);
            System.out.println("Part 1 is: " + part1);

            long part2 = part2(numbers);
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Given a string representation of a snail fish number, returns the index of the open/left bracket
    // of a pair that is nested within four pairs. This is done by counting the number of open brackets
    // seen without a closing bracket.
    private static int findIndexOfNumberNestedWithinFourPairs(String s) {
        int numLeftBrackets = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') {
                numLeftBrackets++;
            } else if (c == ']') {
                numLeftBrackets--;
            }

            if (numLeftBrackets >= 5) {
                return i;
            }
        }

        return -1;
    }

    // Given a string representation of a snail fish number and the index of the left bracket
    // containing a pair to be exploded, explodes the pair.
    private static String explode(String s, int index) {
        // Find the close bracket of the pair that explodes.
        int indexOfClosingBracket = -1;
        for (int i = index; i < s.length(); i++) {
            if (s.charAt(i) == ']') {
                indexOfClosingBracket = i;
                break;
            }
        }

        // Extract the left and right values of the pair to be exploded.
        String[] tokens = s.substring(index + 1, indexOfClosingBracket).split(",");
        int leftValue = Integer.parseInt(tokens[0].trim());
        int rightValue = Integer.parseInt(tokens[1].trim());

        // Find the last character of the number immediately to the left of
        // the exploding pair by iterating left from the index.
        int indexOfFirstLeftNumber = -1;
        for(int i = index; i >= 0; i--) {
            char c = s.charAt(i);
            if (c != '[' && c != ']' && c != ',') {
                indexOfFirstLeftNumber = i;
                break;
            }
        }

        // Find the first character of the number immediately to the right of
        // the exploding pair by iterating right from the index.
        int indexOfFirstRightNumber = -1;
        for(int i = indexOfClosingBracket; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '[' && c != ']' && c != ',') {
                indexOfFirstRightNumber = i;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();

        if (indexOfFirstLeftNumber >= 0) {
            // Find out how many digits the left number is.
            int ndigitnumber = 0;
            while (true) {
                if (isParsable(String.valueOf(s.charAt(indexOfFirstLeftNumber-ndigitnumber)))) {
                    ndigitnumber++;
                    continue;
                }
                break;
            }

            sb.append(s, 0, indexOfFirstLeftNumber-ndigitnumber + 1);

            int newLeftValue = Integer.parseInt(s.substring(indexOfFirstLeftNumber-ndigitnumber+1, indexOfFirstLeftNumber + 1)) + leftValue;

            sb.append(newLeftValue);
            sb.append(s, indexOfFirstLeftNumber + 1, index);
            sb.append(0);
        } else {
            sb.append(s, 0, index);
            sb.append(0);
        }

        if (indexOfFirstRightNumber >= 0) {
            sb.append(s, indexOfClosingBracket + 1, indexOfFirstRightNumber);

            // Find out how many digits the right number is.
            int ndigitnumber = 0;
            while (true) {
                if (isParsable(String.valueOf(s.charAt(indexOfFirstRightNumber+ndigitnumber)))) {
                    ndigitnumber++;
                    continue;
                }

                break;
            }

            int newRightValue = Integer.parseInt(s.substring(indexOfFirstRightNumber, indexOfFirstRightNumber+ndigitnumber)) + rightValue;
            sb.append(newRightValue);
            sb.append(s, indexOfFirstRightNumber+ndigitnumber, s.length());
        } else {
            sb.append(s, indexOfClosingBracket + 1, s.length());
        }

        return sb.toString();
    }

    // Returns true if the given string input is parsable to an integer.
    // Returns false otherwise.
    private static boolean isParsable(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    // Given a string representation of a snail fish number, returns the index of the first character
    // of a number that is greater than 9.
    private static int findIndexOfNumberGreaterThanNine(String s) {
        int index = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c != '[' && c != ']' && c != ',') {
                char nextC = s.charAt(i + 1);
                // We've found two numbers in a row so it's greater than 9
                if (nextC != '[' && nextC != ']' && nextC != ',') {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    // Given a string representation of a snail fish number and the index of the first character
    // of a number that is greater than 9, splits the number.
    private static String split(String s, int index) {
        // First, find how many digits are in this number.
        int ndigitnumber = 0;
        while (true) {
            if (isParsable(String.valueOf(s.charAt(index+ndigitnumber)))) {
                ndigitnumber++;
                continue;
            }

            break;
        }

        // Extract the number and calculate the new left and right values of the split number.
        int num = Integer.parseInt(s.substring(index, index+ndigitnumber));
        int newleftvalue = (int)Math.floor((double)num / 2);
        int newrightvalue = (int)Math.ceil((double)num / 2);

        StringBuilder sb = new StringBuilder();

        sb.append(s, 0, index);
        sb.append("[" + newleftvalue + "," + newrightvalue + "]");
        sb.append(s, index+ndigitnumber, s.length());

        return sb.toString();
    }

    // Given a string representing a snail fish number, reduces it, and returns the result.
    // To reduce a snail fish number, these criteria are checked in order and repeated until no criteria applies:
    // 1. If any pair is nested inside of four pairs, the leftmost such pair explodes.
    // 2. If any regular number is 10 or greater, the leftmost such regular number splits.
    // If no action applies, the snail fish number is reduced.
    private static String reduceSnailFishNumber(String s) {
        String result = s;
        while (true) {
            int index = findIndexOfNumberNestedWithinFourPairs(result);
            if (index >= 0) {
                String afterExploded = explode(result, index);
                result = afterExploded;
                continue;
            }

            int index2 = findIndexOfNumberGreaterThanNine(result);

            if (index2 >= 0) {
                String afterSplit = split(result, index2);
                result = afterSplit;
                continue;
            }

            break;
        }

        return result;
    }

    // Adds two snail fish numbers together. Start by wrapping each snail fish number in brackets
    // and then reducing.
    private static String addSnailFishNumbers(String s1, String s2) {
        String added = "[" + s1 + "," + s2 + "]";
        return reduceSnailFishNumber(added);
    }

    // Given the string representation of a Snailfish Pair (which we'll call a Node),
    // constructs the Node object by using a stack to keep track of each character seen so far.
    private static Node constructNodes(String line) {
        Stack<Object> stack = new Stack<>();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            // If we encounter any other character but a closing bracket, we add it to the stack.
            // If we encounter a closing bracket, we pop the top 4 elements of the stack
            // which should be: a right node, a comma, a left node, and an open bracket.
            if (c != ']') {
                stack.push(c);
            }
            else {
                Object rightObject = stack.pop();
                stack.pop(); // pop the comma
                Object leftObject = stack.pop();
                stack.pop(); // pop the left bracket

                Node newNode = new Node(",",null,  null);

                // If it's just a single number, we want to create a node of it
                if (!(rightObject instanceof Node)) {
                    Character charRep = (Character)rightObject;
                    Node rightNode = new Node(Character.toString(charRep), null, null);
                    newNode.addRight(rightNode);
                } else {
                    newNode.addRight((Node)rightObject);
                }

                // If it's just a single number, we want to create a node of it
                if (!(leftObject instanceof Node)) {
                    Character charRep = (Character)leftObject;
                    Node leftNode = new Node(Character.toString(charRep), null, null);
                    newNode.addLeft(leftNode);
                } else {
                    newNode.addLeft((Node)leftObject);
                }

                stack.push(newNode);
            }
        }

        // At this point, stack should contain a single node representing the whole tree.
        // Pop it and return it.
        return (Node)stack.pop();
    }

    // Given a node, find its magnitude. A node's magnitude is the 3*mag of left child + 2*mag of right child.
    // The magnitude of a node with no children is just its value.
    private static Long getMagnitude(Node n) {
        if (n.leftChild == null && n.rightChild == null) {
            n.magnitude = Long.parseLong(n.value);
            return n.magnitude;
        }

        n.magnitude = 3 * getMagnitude(n.leftChild) + 2 * getMagnitude(n.rightChild);
        return n.magnitude;
    }

    // Part 1: Incrementally add two snail fish numbers together (including reducing).
    // The result is a string representation of a reduced snail fish number.
    // From that string representation, construct a tree and recurse to find the magnitude of each node.
    // Return the magnitude of the head node.
    private static long part1(List<String> numbers) {
        String result = numbers.get(0);

        for (int i = 1; i < numbers.size(); i++) {
            String added = addSnailFishNumbers(result, numbers.get(i));
            result = added;
        }

        Node head = constructNodes(result);
        long magnitude = getMagnitude(head);
        return magnitude;
    }

    // Part 2: Calculate the magnitude of the sum of every pair of snail fish numbers
    // (both x + y and y + x since snail fish numbers are not commutative)
    // and return the largest magnitude.
    private static long part2(List<String> numbers) {
        long maxMagnitude = 0;

        for (int i = 0; i < numbers.size(); i++) {
            for (int j = 0; j < numbers.size(); j++) {
                if (i == j) continue;

                String result = addSnailFishNumbers(numbers.get(i), numbers.get(j));
                Node head = constructNodes(result);
                long magnitude = getMagnitude(head);

                if (magnitude > maxMagnitude) {
                    maxMagnitude = magnitude;
                }
            }
        }

        return maxMagnitude;
    }

    // Node class to represent a snail fish number.
    static class Node {
        String value;
        Node leftChild;
        Node rightChild;
        Long magnitude;

        public Node(String value, Node leftChild, Node rightChild) {
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public void addLeft(Node leftChild) { this.leftChild = leftChild; }
        public void addRight(Node rightChild) { this.rightChild = rightChild; }
        public String toString() { return "[" + this.leftChild + "," + this.rightChild + "]"; }
    }
}
