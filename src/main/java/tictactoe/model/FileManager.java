package tictactoe.model;

import tictactoe.utils.Constants;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * File manager updated to use BufferedReader/Writer for player files and
 * DataOutputStream/DataInputStream for history (binary). Keeps the same
 * public API so controllers don't need to change. Also provides a place to
 * add RandomAccessFile based methods in Phase2.
 */
public class FileManager {

    private static final String PLAYER_EXT = ".dat";
    private static final String HISTORY_BIN = "history.bin";

    /**
     * Loads a player from a simple text record: username,password,wins,losses,draws
     */
    public Player loadPlayer(String username) {
        File f = new File(Constants.DATA_DIR + username + PLAYER_EXT);
        if (!f.exists())
            return null;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line = br.readLine();
            if (line == null || line.trim().isEmpty())
                return null;
            String[] parts = line.split(",");
            if (parts.length < 2) {
                return tryLoadPlayerBySerialization(f);
            }
            String user = parts.length > 0 ? parts[0] : username;
            String pass = parts.length > 1 ? parts[1] : "";
            Player p = new Player(user, pass);
            if (parts.length >= 5) {
                try {
                    int wins = Integer.parseInt(parts[2]);
                    int losses = Integer.parseInt(parts[3]);
                    int draws = Integer.parseInt(parts[4]);
                    p.getStatistics().setWins(wins);
                    p.getStatistics().setLosses(losses);
                    p.getStatistics().setDraws(draws);
                } catch (NumberFormatException nfe) {
                }
            }
            return p;
        } catch (Exception e) {
            try {
                return tryLoadPlayerBySerialization(f);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    private Player tryLoadPlayerBySerialization(File f) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            if (o instanceof Player)
                return (Player) o;
        }
        return null;
    }

    /**
     * Saves a player as a single-line CSV using BufferedWriter.
     */
    public boolean savePlayer(Player player) {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();
        File f = new File(Constants.DATA_DIR + player.getUsername() + PLAYER_EXT);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))) {
            String line = String.format("%s,%s,%d,%d,%d",
                    player.getUsername(),
                    player.getPassword(),
                    player.getStatistics().getWins(),
                    player.getStatistics().getLosses(),
                    player.getStatistics().getDraws());
            bw.write(line);
            bw.newLine();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads history from a binary file written with DataOutputStream in append
     * mode.
     * Format per record: UTF(username), int(resultOrdinal), long(epochMillis)
     */
    public List<Match> loadHistory() {
        File f = new File(Constants.DATA_DIR + HISTORY_BIN);
        if (!f.exists())
            return new ArrayList<>();
        List<Match> result = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(f)))) {
            while (dis.available() > 0) {
                String user = dis.readUTF();
                int res = dis.readInt();
                long epoch = dis.readLong();
                LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
                Match m = new Match(user, Match.Result.values()[res], dt);
                result.add(m);
            }
            return result;
        } catch (EOFException eof) {
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Saves entire history as binary (overwrites).
     */
    public boolean saveHistory(List<Match> history) {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();
        File f = new File(Constants.DATA_DIR + HISTORY_BIN);
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f, false)))) {
            for (Match m : history) {
                dos.writeUTF(m.getPlayerUsername());
                dos.writeInt(m.getResult().ordinal());
                long epoch = m.getDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                dos.writeLong(epoch);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Appends a single match to history using DataOutputStream in append mode.
     */
    public boolean appendMatchToHistory(Match match) {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();
        File f = new File(Constants.DATA_DIR + HISTORY_BIN);
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f, true)))) {
            dos.writeUTF(match.getPlayerUsername());
            dos.writeInt(match.getResult().ordinal());
            long epoch = match.getDateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            dos.writeLong(epoch);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Placeholder for a RandomAccessFile-based storage implementation (phase 2).
     */
    public boolean savePlayerRandomAccess(Player player) {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists())
            dir.mkdirs();
        File f = new File(Constants.DATA_DIR + "players.db");
        final int USERNAME_CHARS = 32;
        final int PASSWORD_CHARS = 32;
        final int RECORD_SIZE = 1 + (USERNAME_CHARS + PASSWORD_CHARS) * 2 + 4 + 4 + 4;
        try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
            long pos = findRecordByUsername(raf, player.getUsername(), USERNAME_CHARS, RECORD_SIZE);
            if (pos >= 0) {
                writeRecord(raf, pos, player, USERNAME_CHARS, PASSWORD_CHARS);
            } else {
                raf.seek(raf.length());
                writeRecord(raf, raf.getFilePointer(), player, USERNAME_CHARS, PASSWORD_CHARS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load a player from the random-access players database.
     */
    public Player loadPlayerRandomAccess(String username) {
        File f = new File(Constants.DATA_DIR + "players.db");
        if (!f.exists())
            return null;
        final int USERNAME_CHARS = 32;
        final int PASSWORD_CHARS = 32;
        final int RECORD_SIZE = 1 + (USERNAME_CHARS + PASSWORD_CHARS) * 2 + 4 + 4 + 4;
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            long pos = findRecordByUsername(raf, username, USERNAME_CHARS, RECORD_SIZE);
            if (pos >= 0) {
                return readRecordAt(raf, pos, USERNAME_CHARS, PASSWORD_CHARS);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Migrate existing per-player files into the random-access database.
     */
    public boolean migratePlayersToRandomAccess() {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists() || !dir.isDirectory())
            return false;
        File[] files = dir.listFiles((d, name) -> name.endsWith(PLAYER_EXT));
        if (files == null)
            return false;
        boolean ok = true;
        for (File f : files) {
            String name = f.getName();
            String username = name.substring(0, name.length() - PLAYER_EXT.length());
            Player p = loadPlayer(username);
            if (p != null) {
                boolean r = savePlayerRandomAccess(p);
                ok = ok && r;
            }
        }
        return ok;
    }

    private long findRecordByUsername(RandomAccessFile raf, String username, int usernameChars, int recordSize)
            throws IOException {
        long len = raf.length();
        long pos = 0;
        while (pos + recordSize <= len) {
            raf.seek(pos);
            byte active = raf.readByte();
            String uname = readFixedString(raf, usernameChars);
            if (active == 1 && username.equals(uname))
                return pos;
            pos += recordSize;
        }
        return -1;
    }

    private Player readRecordAt(RandomAccessFile raf, long pos, int usernameChars, int passwordChars)
            throws IOException {
        raf.seek(pos);
        byte active = raf.readByte();
        if (active != 1)
            return null;
        String uname = readFixedString(raf, usernameChars);
        String pass = readFixedString(raf, passwordChars);
        int wins = raf.readInt();
        int losses = raf.readInt();
        int draws = raf.readInt();
        Player p = new Player(uname, pass);
        p.getStatistics().setWins(wins);
        p.getStatistics().setLosses(losses);
        p.getStatistics().setDraws(draws);
        return p;
    }

    private void writeRecord(RandomAccessFile raf, long pos, Player player, int usernameChars, int passwordChars)
            throws IOException {
        raf.seek(pos);
        raf.writeByte(1);
        writeFixedString(raf, player.getUsername(), usernameChars);
        writeFixedString(raf, player.getPassword(), passwordChars);
        raf.writeInt(player.getStatistics().getWins());
        raf.writeInt(player.getStatistics().getLosses());
        raf.writeInt(player.getStatistics().getDraws());
    }

    private String readFixedString(RandomAccessFile raf, int length) throws IOException {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = raf.readChar();
            if (c != '\0')
                sb.append(c);
        }
        return sb.toString().trim();
    }

    private void writeFixedString(RandomAccessFile raf, String s, int length) throws IOException {
        StringBuilder sb = new StringBuilder(s == null ? "" : s);
        if (sb.length() > length)
            sb.setLength(length);
        while (sb.length() < length)
            sb.append('\0');
        raf.writeChars(sb.toString());
    }

}
