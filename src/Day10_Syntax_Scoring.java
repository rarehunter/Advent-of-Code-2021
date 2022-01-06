import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day10_Syntax_Scoring {
    private static Set<Character> openChars;
    private static Set<Character> closeChars;

    public static void main(String[] args) {
        // Store the opening and closing characters.
        openChars = new HashSet<>(4);
        closeChars = new HashSet<>(4);

        openChars.add('(');
        openChars.add('[');
        openChars.add('{');
        openChars.add('<');
        closeChars.add(')');
        closeChars.add(']');
        closeChars.add('}');
        closeChars.add('>');

        File file = new File("./inputs/day10/day10.txt");

        try {
            Scanner sc = new Scanner(file);

            // The input will include both illegal and incomplete lines.
            // Illegal lines are lines which that contain incorrect characters.
            // In other words, lines in which the pairing of open and close characters do not form a legal pair
            // Legal pairs are: (), [], {}, and <>
            // Incomplete lines don't have any incorrect characters. Instead, they are missing some
            // closing characters at the end of the line.
            List<String> lines = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lines.add(line.trim());
            }

            // Stores the indices of lines in the input that are illegal (contains incorrect characters)
            List<Integer> illegalLines = new ArrayList<>();

            int part1 = part1(lines, illegalLines);
            System.out.println("Part 1: " + part1);

            long part2 = part2(lines, illegalLines);
            System.out.println("Part 2: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Returns true if the two characters are matching opening and closing pairs.
    private static boolean areOpenClosePairs(Character open, Character close) {
        if (open == '(' && close == ')') {
            return true;
        } else if (open == '[' && close == ']') {
            return true;
        } else if (open == '{' && close == '}') {
            return true;
        } else if (open == '<' && close == '>') {
            return true;
        }

        return false;
    }

    // Given the first illegal character found, calculates its syntax error score.
    // Only used in part 1.
    private static int calculateSyntaxErrorScore(List<Character> illegalCharacters) {
        int score = 0;

        for (Character c : illegalCharacters) {
            if (c == ')') {
                score += 3;
            } else if (c == ']') {
                score += 57;
            } else if (c == '}') {
                score += 1197;
            } else if (c == '>') {
                score += 25137;
            }
        }

        return score;
    }

    // Part 1: Using a stack, iterates through the characters of each input line, pushes any open
    // characters onto the stack and if a close character is encountered, checks the top of the stack to see
    // if the open/close characters are a matching pair. If so, the top of the stack is popped. If not,
    // an illegal character is found. Also adds the index of the input line that was illegal to the illegalLines
    // list to be consumed by part 2.
    private static int part1(List<String> lines, List<Integer> illegalLines) {
        List<Character> illegalCharacters = new ArrayList<>();
        Stack<Character> stack = new Stack<>();

        // Iterate through each line of the input.
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);

            // Iterate through each character of the line.
            for (int i = 0; i < line.length(); i++) {
                Character c = line.charAt(i);

                if (openChars.contains(c)) {
                    stack.push(c); // add open characters onto stack
                } else if (closeChars.contains(c)) {
                    // If a close character is found, check if the top of stack is its
                    // matching open character. If so, pop it and move on.
                    // Otherwise, we've found an illegal character and break.
                    Character top = stack.peek();
                    if (areOpenClosePairs(top, c)) {
                        stack.pop();
                    } else {
                        illegalCharacters.add(c);
                        illegalLines.add(lineIndex);
                        break;
                    }
                }
            }
        }

        // Calculate the final syntax error score
        return calculateSyntaxErrorScore(illegalCharacters);
    }

    // Given a stack of open characters, calculate the line completion score. Only used in part 2.
    private static long calculateLineCompletionScore(Stack<Character> stack) {
        long score = 0;
        while (!stack.isEmpty()) {
            Character top = stack.pop();
            score *= 5;

            if (top == '(') {
                score += 1;
            } else if (top == '[') {
                score += 2;
            } else if (top == '{') {
                score += 3;
            } else if (top == '<') {
                score += 4;
            }
        }

        return score;
    }

    // Part 2: Considering only incomplete lines (lines that don't have incorrect character, just missing
    // closing characters at the end of the line), find the sequence of closing characters that would close
    // all the pairs appropriately and calculate its line completion score. Returns the median score.
    private static long part2(List<String> lines, List<Integer> illegalLines) {
        String line = "";
        Stack<Character> stack = new Stack<>();
        List<Long> completionScores = new ArrayList<>();

        // Iterate through all lines of the input.
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            // Only check incomplete lines
            if (illegalLines.contains(lineIndex)) {
                continue;
            }

            line = lines.get(lineIndex);

            // Using a stack, iterate through each character of the line, pushing open characters onto the stack
            // and popping them off when encountering its matching closing pair.
            for (int i = 0; i < line.length(); i++) {
                Character c = line.charAt(i);

                if (openChars.contains(c)) {
                    stack.push(c);
                } else if (closeChars.contains(c)) {
                    Character top = stack.peek();
                    if (areOpenClosePairs(top, c)) {
                        stack.pop();
                    }
                }
            }

            // At this point the stack should include all the open characters that
            // are missing their counterparts.
            long completionScore = calculateLineCompletionScore(stack);
            completionScores.add(completionScore);
            stack.clear();
        }

        // Finds the median of all the completion scores.
        Collections.sort(completionScores);
        return completionScores.get(completionScores.size() / 2);
    }
}
