package tictactoe.service;

import tictactoe.model.FileManager;
import tictactoe.model.Match;
import tictactoe.model.Player;

import java.util.List;

/**
 * Service that exposes storage operations for controllers.
 */
public class StorageService {

    private final FileManager fileManager = new FileManager();

    /**
     * Load a player by username.
     */
    public Player loadPlayer(String username) {
        return fileManager.loadPlayer(username);
    }

    /**
     * Save a player.
     */
    public boolean savePlayer(Player player) {
        return fileManager.savePlayer(player);
    }

    /**
     * Load match history.
     */
    public List<Match> loadHistory() {
        return fileManager.loadHistory();
    }

    /**
     * Save all match history.
     */
    public boolean saveHistory(List<Match> history) {
        return fileManager.saveHistory(history);
    }

    /**
     * Append a match to history.
     */
    public boolean appendMatchToHistory(Match match) {
        return fileManager.appendMatchToHistory(match);
    }

    /**
     * Save a player into the random-access players database.
     */
    public boolean savePlayerRandomAccess(Player player) {
        return fileManager.savePlayerRandomAccess(player);
    }

    /**
     * Load a player from the random-access players database.
     */
    public Player loadPlayerRandomAccess(String username) {
        return fileManager.loadPlayerRandomAccess(username);
    }

    /**
     * Migrate per-player files to the random-access database.
     */
    public boolean migratePlayersToRandomAccess() {
        return fileManager.migratePlayersToRandomAccess();
    }
}
