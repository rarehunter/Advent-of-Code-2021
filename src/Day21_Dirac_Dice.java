import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day21_Dirac_Dice {
    private static int deterministicDieValue = 0;


    public static void main(String[] args) {
        File file = new File("./inputs/day21/day21.example.txt");

        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
            }

            int player1Starting = 4;
            int player2Starting = 3;

            long part1 = part1(player1Starting, player2Starting);
            System.out.println("Part 1 is: " + part1);

            long part2 = part2(player1Starting, player2Starting);
            System.out.println("Part 2 is: " + part2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    // Get the next die value of the deterministic die.
    public static int getNextDeterministicDieValue() {
        return ++deterministicDieValue;
    }

    // Part 1: Play a game using a deterministic die.
    // Players alternate turns until one player scores at least 1000 points.
    // Die is rolled three times and player moves pawn forward by the dice sum.
    // Player scores points equal to the position the pawn ends up.
    private static long part1(int player1Starting, int player2Starting) {
        int player1Score = 0;
        int player2Score = 0;
        int numDiceRolls = 0;
        int player1Position = player1Starting;
        int player2Position = player2Starting;
        boolean player1Turn = true;

        while (player1Score < 1000 && player2Score < 1000) {
            int firstDiceRoll = getNextDeterministicDieValue();
            int secondDiceRoll = getNextDeterministicDieValue();
            int thirdDiceRoll = getNextDeterministicDieValue();
            int diceSum = firstDiceRoll + secondDiceRoll + thirdDiceRoll;
            numDiceRolls += 3;

            if (player1Turn) {
                player1Position = (((player1Position+diceSum) - 1) % 10) + 1;
                player1Score += player1Position;
                player1Turn = false;
            } else {
                player2Position = (((player2Position+diceSum) - 1) % 10) + 1;
                player2Score += player2Position;
                player1Turn = true;
            }
        }

        if (player1Score > player2Score) {
            return (long) player2Score * numDiceRolls;
        } else {
            return (long) player1Score * numDiceRolls;
        }
    }

    // Given a game state, plays the game from this point onwards.
    private static WinCounts playGame(Game game) {
        long player1Wins = 0L;
        long player2Wins = 0L;

        // If a player has won, return a tuple denoting which player won.
        if (game.player1Score >= 21) {
            return new WinCounts(1, 0);
        } else if (game.player2Score >= 21) {
            return new WinCounts(0, 1);
        }

        // Populate the diceSums list with all possible dice sums and their frequency.
        List<DiceSum> diceSums = new ArrayList<>();
        diceSums.add(new DiceSum(3,1));
        diceSums.add(new DiceSum(4,3));
        diceSums.add(new DiceSum(5,6));
        diceSums.add(new DiceSum(6,7));
        diceSums.add(new DiceSum(7,6));
        diceSums.add(new DiceSum(8,3));
        diceSums.add(new DiceSum(9,1));

        // Iterate all possible dice sums.
        for (int i = 0; i < diceSums.size(); i++) {
            int diceSum = diceSums.get(i).diceSum;

            // For each dice sum, calculate the current player's new position and score.
            // Generate a new game state and recurse, playing the game from that point onward.
            if (game.player1Turn) {
                int newPlayer1Position = (((game.player1Position+diceSum) - 1) % 10) + 1;
                int newPlayer1Score = game.player1Score + newPlayer1Position;

                Game nextGame = new Game(newPlayer1Position, game.player2Position, newPlayer1Score, game.player2Score, false);
                WinCounts wc = playGame(nextGame);

                // Record the number of times a player has won in this game state tree and multiply it
                // by the frequency of the dice sums (so as to avoid redoing the same amount of work).
                player1Wins += wc.player1Wins * diceSums.get(i).amount;
                player2Wins += wc.player2Wins * diceSums.get(i).amount;
            } else {
                int newPlayer2Position = (((game.player2Position+diceSum) - 1) % 10) + 1;
                int newPlayer2Score = game.player2Score + newPlayer2Position;

                Game nextGame = new Game(game.player1Position, newPlayer2Position, game.player1Score, newPlayer2Score, true);
                WinCounts wc = playGame(nextGame);

                // Record the number of times a player has won in this game state tree and multiply it
                // by the frequency of the dice sums (so as to avoid redoing the same amount of work).
                player1Wins += wc.player1Wins * diceSums.get(i).amount;
                player2Wins += wc.player2Wins * diceSums.get(i).amount;
            }
        }

        return new WinCounts(player1Wins, player2Wins);
    }

    // Part 2: Using quantum die.
    private static long part2(int player1Starting, int player2Starting) {
        Game g = new Game(player1Starting, player2Starting, 0, 0, true);
        WinCounts wc = playGame(g);

        return Math.max(wc.player1Wins, wc.player2Wins);
    }

    // Class to associate a dice sum (3,4,5,6,7,8,9) with its frequency.
    static class DiceSum {
        int diceSum;
        int amount;

        public DiceSum(int diceSum, int amount) {
            this.diceSum = diceSum;
            this.amount = amount;
        }
    }

    // Class to bundle the win counts of player 1 and player 2.
    static class WinCounts {
        long player1Wins;
        long player2Wins;

        public WinCounts(long player1Wins, long player2Wins) {
            this.player1Wins = player1Wins;
            this.player2Wins = player2Wins;
        }

        public String toString() { return this.player1Wins + " " + this.player2Wins; }
    }

    // Class to represent a Game state
    static class Game {
        int player1Position;
        int player2Position;
        int player1Score;
        int player2Score;
        boolean player1Turn;

        public Game(int player1Position, int player2Position, int player1Score, int player2Score, boolean player1Turn) {
            this.player1Position = player1Position;
            this.player2Position = player2Position;
            this.player1Score = player1Score;
            this.player2Score = player2Score;
            this.player1Turn = player1Turn;
        }

        public void setPlayer1Position(int position) {
            this.player1Position = position;
        }

        public void setPlayer2Position(int position) {
            this.player2Position = position;
        }

        public void updatePlayer1Score(int score) {
            this.player1Score += score;
        }

        public void updatePlayer2Score(int score) {
            this.player2Score += score;
        }

        public void togglePlayerTurn() {
            this.player1Turn = !this.player1Turn;
        }
    }
}
