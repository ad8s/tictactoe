package tictactoe.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tictactoe.model.Player;

public class StatisticsController {

    @FXML
    private Label lblPlayed;
    @FXML
    private Label lblWins;
    @FXML
    private Label lblLosses;
    @FXML
    private Label lblDraws;

    private Stage stage;
    private Player player;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPlayer(Player player) {
        this.player = player;
        loadStats();
    }

    private void loadStats() {
        var stats = player.getStatistics();
        lblPlayed.setText(String.valueOf(stats.getPlayed()));
        lblWins.setText(String.valueOf(stats.getWins()));
        lblLosses.setText(String.valueOf(stats.getLosses()));
        lblDraws.setText(String.valueOf(stats.getDraws()));
    }

    @FXML
    private void onBack() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/MenuView.fxml"));
            javafx.scene.Parent root = loader.load();
            MenuController ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setPlayer(player);
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
