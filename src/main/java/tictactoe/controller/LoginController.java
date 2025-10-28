package tictactoe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tictactoe.model.Player;
import tictactoe.service.StorageService;
import tictactoe.utils.Helpers;

import java.io.IOException;

/**
 * Controller for the login screen. Responsible for authenticating or creating a
 * player.
 */
public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    private Stage stage;
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

    @FXML
    private void onLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Helpers.showError("Login error", "Please enter username and password.");
            return;
        }

        Player p = storageService.loadPlayer(username);
        if (p == null) {
            p = new Player(username, password);
            boolean ok = storageService.savePlayer(p);
            if (!ok) {
                Helpers.showError("File error", "Could not create user file.");
            }
        } else {
            if (!p.getPassword().equals(password)) {
                Helpers.showError("Login error", "Incorrect password.");
                return;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuView.fxml"));
            Parent root = loader.load();

            MenuController ctrl = loader.getController();
            ctrl.setStage(stage);
            ctrl.setPlayer(p);
            ctrl.setStorageService(storageService);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
