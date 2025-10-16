package tictactoe.model;

import java.io.Serializable;

/**
 * Snapshot of a game's state (board + whose turn)
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Board board;
    private boolean playerTurn;

    public GameState(Board board, boolean playerTurn) {
        this.board = board;
        this.playerTurn = playerTurn;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }
}
