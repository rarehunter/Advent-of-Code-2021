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
            List<int[][]> boards = new ArrayList<>();
            List<Integer> bingoNumbers = new ArrayList<>();

            processInput(sc, boards, bingoNumbers);

            int part1 = part1(bingoNumbers, boards);
            System.out.println("Part 1: " + part1);

            int part2 = part2(bingoNumbers, boards);
            System.out.println("Part 2: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void processInput(Scanner sc, List<int[][]> boards, List<Integer> bingoNumbers) {
        // The first line of the input is all the numbers to be drawn
        String numbers = sc.nextLine();
        String[] splitNumbers = numbers.split(",");
        for (int i = 0; i < splitNumbers.length; i++) {
            bingoNumbers.add(Integer.parseInt(splitNumbers[i]));
        }

        sc.nextLine(); // read off the next line which is an empty line

        // Now, we have populate a list of 2D arrays to store all the bingo boards
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
    }

    // Checks whether a given bingo board is complete by
    // checking if any row is completely filled or if any column is completely filled.
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

    // Part 1: Determines the score of the first bingo board to be complete.
    // Iterates through the list of bingo numbers and mark that number on each board in our list of boards.
    // The first board to have either a row or column with all numbers marked wins. 
    // The board score is calculated by finding the sum of all unmarked numbers on that board.
    // Then, multiply that current bingo number.
    private static int part1(List<Integer> bingoNumbers, List<int[][]> boards) {
        for (int number : bingoNumbers) {
            markBoards(boards, number);
            int completedBoard = checkForCompletedBoard(boards);
            if (completedBoard > -1) {
                return calculateBoardScore(boards.get(completedBoard), number);
            }
        }

        return 0; // If no board has won, just return 0 here.
    }

    // Given the current state of all boards, if every board is marked true, then all boards are complete.
    private static boolean allBoardsComplete(boolean[] boardStates) {
        for (int i = 0; i < boardStates.length; i++) {
            if (!boardStates[i]) {
                return false;
            }
        }

        return true;
    }

    // Given a list of boards and the current state of all boards,
    // checks whether any new boards are complete.
    // Returns a list of the indices of newly completed boards.
    private static List<Integer> checkForNewlyCompletedBoards(List<int[][]> boards, boolean[] boardStates) {
        List<Integer> newlyCompletedBoards = new ArrayList<>();
        for (int i = 0; i < boards.size(); i++) {
            // If the current board state is false and the board is now complete, it is considered newly complete.
            if (!boardStates[i] && isBoardComplete(boards.get(i))) {
                newlyCompletedBoards.add(i);
            }
        }

        return newlyCompletedBoards;
    }

    // Part 2: Determines the score of the last bingo board to be complete.
    // Iterates through the list of bingo numbers and mark that number on each board in our list of boards.
    // Keep track of a board completion state and after every bingo number, determine if any boards
    // are newly complete. Repeat until all boards are complete. Find the last board to be completed
    // and calculate its board score.
    // Important things of note:
    // 1) We assume that there is only one unique last board.
    // 2) Multiple boards can be completed at the same time.
    // 3) Not all boards may be complete before the list of bingo numbers runs out.
    private static int part2(List<Integer> bingoNumbers, List<int[][]> boards) {
        boolean[] boardStates = new boolean[boards.size()]; // A true means the board at index i is complete.
        int lastCompleteBoardIndex = -1;
        int lastBingoNumber = -1;

        for (int number : bingoNumbers) {
            markBoards(boards, number);
            List<Integer> completedBoardIndices = checkForNewlyCompletedBoards(boards, boardStates);
            if (completedBoardIndices.size() > 0) {
                // Here, we just assume that the last completed board is the first one in our list.
                // We also assume that at the end of the bingo game, there will be only one unique last board.
                lastCompleteBoardIndex = completedBoardIndices.get(0);
                lastBingoNumber = number;

                // Mark completed boards
                for (int newlyCompletedBoardIndex : completedBoardIndices) {
                    boardStates[newlyCompletedBoardIndex] = true;
                }

                if (allBoardsComplete(boardStates)) {
                    break;
                }
            }
        }

        return calculateBoardScore(boards.get(lastCompleteBoardIndex), lastBingoNumber);
    }
}
