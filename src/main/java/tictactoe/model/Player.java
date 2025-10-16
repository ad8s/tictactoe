package tictactoe.model;

import java.io.Serializable;

/**
 * Represents a player and stores credentials and statistics.
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private Statistics statistics;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.statistics = new Statistics();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return "Player{" + "username='" + username + '\'' + '}';
    }
}
