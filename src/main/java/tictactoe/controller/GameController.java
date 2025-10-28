package tictactoe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tictactoe.model.Game;
import tictactoe.model.Match;
import tictactoe.model.Player;
import tictactoe.service.StorageService;

/**
 * Controller for the game board.
 */
public class GameController {

    @FXML
    private Label lblStatus;

    @FXML
    private Button btn00;
    @FXML
    private Button btn01;
    @FXML
    private Button btn02;
    @FXML
    private Button btn10;
    @FXML
    private Button btn11;
    @FXML
    private Button btn12;
    @FXML
    private Button btn20;
    @FXML
    private Button btn21;
    @FXML
    private Button btn22;

    private Stage stage;
    private Player player;
    private Game game;
    private StorageService storageService;

    /**
     * Sets the application stage.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Injects the storage service.
     */
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Starts a new game for the specified player.
     */
    public void startNewGame(Player player) {
        this.player = player;
        this.game = new Game(player);
        clearButtons();
        updateStatus("Your turn (X)");
    }

    private void clearButtons() {
        btn00.setText("");
        btn01.setText("");
        btn02.setText("");
        btn10.setText("");
        btn11.setText("");
        btn12.setText("");
        btn20.setText("");
        btn21.setText("");
        btn22.setText("");
    }

    private void updateStatus(String s) {
        lblStatus.setText(s);
    }

    @FXML
    private void onCellClick(javafx.event.ActionEvent e) {
        if (game.isFinished())
            return;
        Button b = (Button) e.getSource();
        String id = b.getId();
        int row = Character.getNumericValue(id.charAt(3));
        int col = Character.getNumericValue(id.charAt(4));

        boolean moved = game.playerMove(row, col);
        if (!moved)
            return;

        b.setText("X");

        if (game.isFinished()) {
            updateStatus("You won!");
            finishGame(Match.Result.WIN);
            return;
        }

        updateStatus("AI is thinking...");
        int[] ai = game.aiMove();
        if (ai != null) {
            Button ab = getButton(ai[0], ai[1]);
            if (ab != null)
                ab.setText("O");
        }

        if (game.isFinished()) {
            if (player.getStatistics().getLosses() > 0) {
                updateStatus("You lost...");
                finishGame(Match.Result.LOSS);
            } else if (player.getStatistics().getDraws() > 0) {
                updateStatus("Draw");
                finishGame(Match.Result.DRAW);
            } else {
                updateStatus("Game finished");
                finishGame(Match.Result.DRAW);
            }
            return;
        }

        updateStatus("Your turn (X)");
    }

    private Button getButton(int r, int c) {
        if (r == 0 && c == 0)
            return btn00;
        if (r == 0 && c == 1)
            return btn01;
        if (r == 0 && c == 2)
            return btn02;
        if (r == 1 && c == 0)
            return btn10;
        if (r == 1 && c == 1)
            return btn11;
        if (r == 1 && c == 2)
            return btn12;
        if (r == 2 && c == 0)
            return btn20;
        if (r == 2 && c == 1)
            return btn21;
        if (r == 2 && c == 2)
            return btn22;
        return null;
    }

    private void finishGame(Match.Result result) {
        storageService.savePlayer(player);
        Match match = new Match(player.getUsername(), result);
        storageService.appendMatchToHistory(match);
    }

    @FXML
    private void onBackToMenu() {
        storageService.savePlayer(player);
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/view/MenuView.fxml"));
            javafx.scene.Parent root = loader.load();
            MenuController ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setPlayer(player);
            ctrl.setStorageService(storageService);
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
