package tictactoe.model;

import tictactoe.utils.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple file manager for players and history. Each player is stored in
 * data/<username>.dat
 * History is stored as a list of Match objects in data/history.dat
 */
public class FileManager {

    public Player loadPlayer(String username) {
        File f = new File(Constants.DATA_DIR + username + ".dat");
        if (!f.exists())
            return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Player) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean savePlayer(Player player) {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();
        File f = new File(Constants.DATA_DIR + player.getUsername() + ".dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(player);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Match> loadHistory() {
        File f = new File(Constants.DATA_DIR + "history.dat");
        if (!f.exists())
            return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<Match>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean saveHistory(List<Match> history) {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();
        File f = new File(Constants.DATA_DIR + "history.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(history);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean appendMatchToHistory(Match match) {
        List<Match> history = loadHistory();
        history.add(match);
        return saveHistory(history);
    }
}
