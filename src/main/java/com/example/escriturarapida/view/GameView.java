package com.example.escriturarapida.view;

import com.example.escriturarapida.utilities.AudioManager;
import com.example.escriturarapida.utilities.AudioReceiver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * View class responsible for scene navigation in the game.
 * Handles loading FXML files, applying stylesheets, and
 * transitioning between screens.
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 */
public class GameView {

    /** Path to the global stylesheet applied to all scenes. */
    private static final String STYLES_PATH =
            "/com/example/escriturarapida/styles/styles.css";

    /**
     * Loads an FXML scene and sets it on the current stage.
     * The stage is resolved from any node currently on screen.
     *
     * @param fxmlPath the classpath-relative path to the FXML file.
     * @param node     any node belonging to the current scene, used to retrieve the {@link Stage}.
     */
    public void changeScene(String fxmlPath, Node node, AudioManager audioManager) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof AudioReceiver) {
                ((AudioReceiver) controller).setAudioManager(audioManager);
            }

            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(
                    Objects.requireNonNull(
                            getClass().getResource(STYLES_PATH)
                    ).toExternalForm()
            );

            stage.setScene(scene);

        } catch (IOException e) {
            System.out.println("Load Scene Error: " + e.getMessage());
        }
    }
}