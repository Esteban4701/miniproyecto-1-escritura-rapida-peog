package com.example.escriturarapida.controller;

import com.example.escriturarapida.model.GameModel;
import com.example.escriturarapida.utilities.GameData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the results/pause screen displayed after the game ends.
 * Reads the game outcome from {@link GameData} and displays the appropriate
 * win or lose message, the level reached, and the remaining time.
 * Provides options to restart the game or return to the start screen.
 *
 * @author TuNombre
 * @version 1.0
 * @since 2024
 */
public class PauseController implements Initializable {

    /** Label displaying the number of levels completed by the player. */
    @FXML private Label lblLevelsResult;

    /** Label displaying the win or lose message based on the game outcome. */
    @FXML private Label labWinOrLost;

    /** Label displaying the remaining time when the game ended. */
    @FXML private Label lblTimeResult;

    /** Button that restarts the game from the current saved level. */
    @FXML private Button btnRestart;

    /** Instance of the game model used to reset game data on restart or exit. */
    private final GameModel gameModel = new GameModel();

    /**
     * Initialization method called automatically by JavaFX after the FXML is loaded.
     * Reads the game outcome from {@link GameData} and updates all result labels accordingly.
     * <ul>
     *   <li>{@code reasonWinOrLose == 1}: Player completed all levels.</li>
     *   <li>{@code reasonWinOrLose == 2}: Player completed the last level just as time ran out.</li>
     *   <li>{@code reasonWinOrLose == 3}: Player lost because time ran out.</li>
     * </ul>
     *
     * @param url            URL of the loaded FXML (not used).
     * @param resourceBundle Internationalization resources (not used).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labWinOrLost.setStyle("-fx-text-fill: white");
        if (GameData.reasonWinOrLose == 1) {
            labWinOrLost.setText("        YOU WIN " +
                    "\nGAME COMPLETE");
        }
        if (GameData.reasonWinOrLose == 2) {
            labWinOrLost.setText("  YOU WIN " +
                    "\nSO CLOSE!");
        }
        if (GameData.reasonWinOrLose == 3) {
            labWinOrLost.setText("YOU LOSE \nTIME'S UP");
        }
        lblLevelsResult.setText(String.valueOf(GameData.level));
        lblTimeResult.setText(String.format("%02d.%02d", GameData.seconds, GameData.milliseconds));
    }

    /**
     * Handles the restart button action.
     * Resets all game data and navigates to the game screen,
     * continuing from the level saved in {@link GameData}.
     */
    @FXML
    private void onRestart() {
        gameModel.resetData();
        changeScene("/com/example/escriturarapida/view/game-view.fxml");
    }

    /**
     * Handles the exit button action.
     * Resets all game data and navigates back to the start screen.
     */
    @FXML
    private void onExit() {
        gameModel.resetData();
        changeScene("/com/example/escriturarapida/view/start-view.fxml");
    }

    /**
     * Loads and displays a new scene from the given FXML path.
     * Applies the global stylesheet to the new scene before displaying it.
     *
     * @param fxmlFile the full resource path to the FXML file to load,
     *                 e.g. {@code "/com/example/escriturarapida/view/game-view.fxml"}.
     */
    private void changeScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/com/example/escriturarapida/styles/styles.css")).toExternalForm()
            );

            Stage stage = (Stage) btnRestart.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Load Scene Error: " + e.getMessage());
        }
    }
}
