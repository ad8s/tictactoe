package tictactoe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.model.Player;
import tictactoe.model.FileManager;

import java.io.IOException;

public class MenuController {

    private Stage stage;
    private Player player;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @FXML
    private void onNewGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameView.fxml"));
            Parent root = loader.load();

            GameController ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.startNewGame(player);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StatisticsView.fxml"));
            Parent root = loader.load();

            StatisticsController ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setPlayer(player);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogout() {
        new FileManager().savePlayer(player);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
            Parent root = loader.load();
            MainViewController c = loader.getController();
            c.setStage(stage);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
