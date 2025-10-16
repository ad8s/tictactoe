package tictactoe.model;

import java.io.Serializable;

/**
 * Simple statistics counter.
 */
public class Statistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private int played;
    private int wins;
    private int losses;
    private int draws;

    public void addWin() {
        played++;
        wins++;
    }

    public void addLoss() {
        played++;
        losses++;
    }

    public void addDraw() {
        played++;
        draws++;
    }

    public int getPlayed() {
        return played;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }
}
