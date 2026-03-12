package com.example.escriturarapida.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the start screen of the application.
 * Handles the initial navigation from the start screen to the game screen.
 *
 * @author TuNombre
 * @version 1.0
 * @since 2024
 */
public class StartController implements Initializable {

    /** Button that starts the game and navigates to the game screen. */
    @FXML private Button btnStart;

    /**
     * Initialization method called automatically by JavaFX after the FXML is loaded.
     * Currently unused but required by the {@link Initializable} interface.
     *
     * @param url            URL of the loaded FXML (not used).
     * @param resourceBundle Internationalization resources (not used).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Handles the start button action.
     * Loads the game screen FXML, applies the global stylesheet,
     * and replaces the current scene with the game scene.
     */
    @FXML
    private void onStart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/escriturarapida/view/game-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/com/example/escriturarapida/styles/styles.css")).toExternalForm()
            );

            Stage stage = (Stage) btnStart.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Load Scene Error: " + e.getMessage());
        }
    }
}