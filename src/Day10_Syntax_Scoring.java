import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day10_Syntax_Scoring {
    private static Character[] openChars;
    private static Character[] closeChars;

    public static void main(String[] args) {
        openChars = new Character[4];
        closeChars = new Character[4];

        openChars[0] = '(';
        openChars[1] = '[';
        openChars[2] = '{';
        openChars[3] = '<';
        closeChars[0] = ')';
        closeChars[1] = ']';
        closeChars[2] = '}';
        closeChars[3] = '>';

        File file = new File("./inputs/day10/day10.txt");

        try {
            Scanner sc = new Scanner(file);
            List<String> lines = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lines.add(line.trim());
            }

            List<Integer> illegalLines = new ArrayList<>();
            int syntaxErrorScore = findFirstSyntaxErrorScores(lines, illegalLines);
            System.out.println("The answer is: " + syntaxErrorScore);

            long middleScore = findLineCompletionMiddleScore(lines, illegalLines);
            System.out.println("The middle score is: " + middleScore);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Returns true if the given character is an open character (one of '(', '[', '{', '<')
    // Returns false otherwise.
    private static boolean isOpenChar(Character c) {
        for (Character open : openChars) {
            if (open == c) {
                return true;
            }
        }

        return false;
    }

    // Returns true if the given character is a close character (one of ')', ']', '}', '>')
    // Returns false otherwise.
    private static boolean isCloseChar(Character c) {
        for (Character close : closeChars) {
            if (close == c) {
                return true;
            }
        }

        return false;
    }

    // Returns true if the two characters are open and close pairs of each other
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
    private static int findFirstSyntaxErrorScores(List<String> lines, List<Integer> illegalLines) {
        List<Character> illegalCharacters = new ArrayList<>();
        Stack<Character> stack = new Stack<>();

        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);
            for (int i = 0; i < line.length(); i++) {
                Character c = line.charAt(i);

                if (isOpenChar(c)) {
                    stack.push(c);
                } else if (isCloseChar(c)) {
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

        return calculateSyntaxErrorScore(illegalCharacters);
    }

    // Given a stack of open characters, calculate the line completion score.
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

    // Part 2
    private static long findLineCompletionMiddleScore(List<String> lines, List<Integer> illegalLines) {
        String line = "";
        Stack<Character> stack = new Stack<>();
        List<Long> completionScores = new ArrayList<>();

        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            // Only check incomplete lines
            if (illegalLines.contains(lineIndex)) {
                continue;
            }

            line = lines.get(lineIndex);

            for (int i = 0; i < line.length(); i++) {
                Character c = line.charAt(i);

                if (isOpenChar(c)) {
                    stack.push(c);
                } else if (isCloseChar(c)) {
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

        Collections.sort(completionScores);
        return completionScores.get(completionScores.size() / 2);
    }
}
