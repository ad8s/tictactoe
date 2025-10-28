package tictactoe.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a finished match for history purposes.
 */
public class Match implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerUsername;
    private Result result;
    private LocalDateTime dateTime;

    public enum Result {
        WIN, LOSS, DRAW
    }

    public Match(String playerUsername, Result result) {
        this.playerUsername = playerUsername;
        this.result = result;
        this.dateTime = LocalDateTime.now();
    }

    /**
     * Create a match with an explicit date/time (used when loading history).
     */
    public Match(String playerUsername, Result result, LocalDateTime dateTime) {
        this.playerUsername = playerUsername;
        this.result = result;
        this.dateTime = dateTime;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public Result getResult() {
        return result;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
