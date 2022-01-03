import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day4_Giant_Squid {
    private static final int BINGO_BOARD_SIZE = 5;
    private static final int FILLED = -1;

    public static void main(String[] args) {
        File file = new File("./inputs/day4/day4.txt");

        try {
            Scanner sc = new Scanner(file);

            // The first line of the input is all the numbers to be drawn
            String numbers = sc.nextLine();
            String[] splitNumbers = numbers.split(",");
            int[] bingoNumbers = new int[splitNumbers.length];
            for (int i = 0; i < splitNumbers.length; i++) {
                bingoNumbers[i] = Integer.parseInt(splitNumbers[i]);
            }

            sc.nextLine(); // read off the next line which is an empty line

            // Now, we have to create a list of 2D arrays in order to store all the bingo boards
            List<int[][]> boards = new ArrayList<>();
            int[][] board = new int[BINGO_BOARD_SIZE][BINGO_BOARD_SIZE];
            int boardIndex = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("")) {
                    boards.add(board);
                    board = new int[BINGO_BOARD_SIZE][BINGO_BOARD_SIZE];
                    boardIndex = 0;
                    continue;
                } else {
                    board[boardIndex] = new int[BINGO_BOARD_SIZE];
                    String[] tokens = line.trim().split("\\s+");

                    board[boardIndex][0] = Integer.parseInt(tokens[0]);
                    board[boardIndex][1] = Integer.parseInt(tokens[1]);
                    board[boardIndex][2] = Integer.parseInt(tokens[2]);
                    board[boardIndex][3] = Integer.parseInt(tokens[3]);
                    board[boardIndex][4] = Integer.parseInt(tokens[4]);
                    boardIndex++;
                }
            }

            // Add in the final board
            boards.add(board);

            //int winningScore = bingoWinningBoardScore(bingoNumbers, boards);
            //System.out.println("The score of the first complete board is: " + winningScore);

            int losingScore = bingoLosingBoardScore(bingoNumbers, boards);
            System.out.println("The score of the last complete board is: " + losingScore);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Checks whether a given bingo board is complete by
    // checking if any row is completely FILLED or if any column is completely filled.
    // Returns true if so or false otherwise.
    private static boolean isBoardComplete(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            // Check if any of the rows are complete
            if (board[i][0] == FILLED &&
                    board[i][1] == FILLED &&
                    board[i][2] == FILLED &&
                    board[i][3] == FILLED &&
                    board[i][4] == FILLED) {
                return true;
            }

            // Check if any of the cols are complete
            if (board[0][i] == FILLED &&
                    board[1][i] == FILLED &&
                    board[2][i] == FILLED &&
                    board[3][i] == FILLED &&
                    board[4][i] == FILLED) {
                return true;
            }
        }

        return false;
    }

    // Checks whether any of the given list of boards is complete.
    // If not, returns -1.
    // If so, returns the index of the completed board.
    private static int checkForCompletedBoard(List<int[][]> boards) {
        for (int i = 0; i < boards.size(); i++) {
            if (isBoardComplete(boards.get(i))) {
                return i;
            }
        }

        return -1;
    }

    // Given a bingo board and a number, sets the occurrence of the given number
    // in the board with a FILLED constant.
    private static void markBoard(int[][] board, int number) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == number) {
                    board[i][j] = FILLED;
                    return;
                }
            }
        }
    }

    // Given a list of bingo boards and a number, sets the occurrence of the given number
    // in each of the boards with a FILLED constant.
    private static void markBoards(List<int[][]> boards, int number) {
        for (int[][] board : boards) {
            markBoard(board, number);
        }

        return;
    }

    // Given a completed board and the number that was just called, calculates the board score.
    // Sum up all the un-marked numbers and multiply that by the number that was just called.
    private static int calculateBoardScore(int[][] board, int number) {
        int unMarkedSum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != FILLED) {
                    unMarkedSum += board[i][j];
                }
            }
        }

        return unMarkedSum * number;
    }

    // Part 1: Determines the score of the first bingo board to be complete
    private static int bingoWinningBoardScore(int[] bingoNumbers, List<int[][]> boards) {
        for (int number : bingoNumbers) {
            markBoards(boards, number);
            int completedBoard = checkForCompletedBoard(boards);
            if (completedBoard > -1) {
                return calculateBoardScore(boards.get(completedBoard), number);
            }
        }

        return -1;
    }

    private static boolean allBoardsComplete(int[] completedBoards) {
        for (int i = 0; i < completedBoards.length; i++) {
            if (completedBoards[i] == 0) {
                return false;
            }
        }

        return true;
    }

    private static boolean isBoardAlreadyCompleted(int[] completedBoards, int boardIndex) {
        return completedBoards[boardIndex] == 1 ? true : false;
    }

    // Given a list of boards and a list of already completedBoards,
    // checks whether any new boards are complete.
    // If not, returns -1.
    // If so, returns a list of the indices of newly completed boards.
    private static List<Integer> checkForNewlyCompletedBoards(List<int[][]> boards, int[] completedBoards) {
        List<Integer> newlyCompletedBoards = new ArrayList<>();
        for (int i = 0; i < boards.size(); i++) {
            if (!isBoardAlreadyCompleted(completedBoards, i) && isBoardComplete(boards.get(i))) {
                newlyCompletedBoards.add(i);
            }
        }

        return newlyCompletedBoards;
    }

    // Part 2: Determines the score of the last bingo board to be complete
    private static int bingoLosingBoardScore(int[] bingoNumbers, List<int[][]> boards) {
        int[] completedBoards = new int[boards.size()];
        int lastCompleteBoardIndex = -1;
        int lastCompletionNumber = -1;

        for (int number : bingoNumbers) {
            markBoards(boards, number);
            List<Integer> completedBoardIndices = checkForNewlyCompletedBoards(boards, completedBoards);
            if (completedBoardIndices.size() > 0) {
                // for now, we just assume that the last completed board is the first one in our list
                lastCompleteBoardIndex = completedBoardIndices.get(0);
                lastCompletionNumber = number;

                // Mark completed boards
                for (int newlyCompletedBoardIndex : completedBoardIndices) {
                    completedBoards[newlyCompletedBoardIndex] = 1;
                }

                if (allBoardsComplete(completedBoards)) {
                    break;
                }
            }
        }

        return calculateBoardScore(boards.get(lastCompleteBoardIndex), lastCompletionNumber);
    }
}
