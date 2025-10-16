package tictactoe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the initial/main view that can navigate to login or exit.
 */
public class MainViewController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        /* nothing for now */ }

    @FXML
    private void onOpenLogin() {
        loadScene("/view/LoginView.fxml");
    }

    @FXML
    private void onExit() {
        if (stage != null)
            stage.close();
        else
            System.exit(0);
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof LoginController)
                ((LoginController) controller).setStage(stage);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
