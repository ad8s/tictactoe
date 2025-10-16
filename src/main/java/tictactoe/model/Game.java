package tictactoe.model;

import java.util.Random;

/**
 * Game logic (player vs simple AI). The AI uses a random-empty move strategy.
 */
public class Game {

    private Board board;
    private Player player;
    private boolean playerTurn;
    private boolean finished;

    public Game(Player player) {
        this.player = player;
        this.board = new Board();
        this.playerTurn = true;
        this.finished = false;
    }

    /**
     * Player attempts a move. Returns true if move was placed.
     */
    public boolean playerMove(int row, int col) {
        if (finished)
            return false;
        if (board.getCell(row, col) != ' ')
            return false;

        board.setCell(row, col, 'X');

        if (board.hasWinner('X')) {
            player.getStatistics().addWin();
            finished = true;
        } else if (board.isFull()) {
            player.getStatistics().addDraw();
            finished = true;
        } else {
            playerTurn = false;
        }

        return true;
    }

    /**
     * AI makes its move and returns the chosen cell or null.
     */
    public int[] aiMove() {
        if (finished)
            return null;

        Random r = new Random();
        int row, col;
        int attempts = 0;
        do {
            row = r.nextInt(3);
            col = r.nextInt(3);
            attempts++;
            if (attempts > 50)
                break;
        } while (board.getCell(row, col) != ' ');

        if (board.getCell(row, col) == ' ') {
            board.setCell(row, col, 'O');

            if (board.hasWinner('O')) {
                player.getStatistics().addLoss();
                finished = true;
            } else if (board.isFull()) {
                player.getStatistics().addDraw();
                finished = true;
            } else {
                playerTurn = true;
            }
            return new int[] { row, col };
        }

        return null;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public boolean isFinished() {
        return finished;
    }
}
