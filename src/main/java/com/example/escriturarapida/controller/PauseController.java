package com.example.escriturarapida.controller;

import com.example.escriturarapida.model.GameModel;
import com.example.escriturarapida.model.GameModelAdapter;
import com.example.escriturarapida.model.IGameModel;
import com.example.escriturarapida.utilities.AudioManager;
import com.example.escriturarapida.utilities.AudioReceiver;
import com.example.escriturarapida.utilities.GameData;
import com.example.escriturarapida.view.GameView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the results/pause screen displayed after the game ends.
 * Reads the game outcome from {@link GameData} and displays the appropriate
 * win or lose message, the level reached, and the remaining time.
 * Provides options to restart the game or return to the start screen.
 * Implements {@link AudioReceiver} to receive the shared {@link AudioManager}
 * instance and lower the volume while the results are shown.
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 * @see AudioManager
 * @see AudioReceiver
 * @see GameView
 */
public class PauseController implements Initializable, AudioReceiver {

    /** Label displaying the number of levels completed by the player. */
    @FXML private Label lblLevelsResult;

    /** Label displaying the win or lose message based on the game outcome. */
    @FXML private Label labWinOrLost;

    /** Label displaying the remaining time when the game ended. */
    @FXML private Label lblTimeResult;

    /** Button that restarts the game from the current saved level. */
    @FXML private Button btnRestart;

    /**
     * Game model accessed through the interface.
     * Used only to reset game data on restart or exit.
     */
    private final IGameModel gameModel = new GameModelAdapter(new GameModel());

    /** View handler responsible for scene transitions. */
    private final GameView gameView = new GameView();

    /** Path to the game screen FXML. */
    private static final String GAME_VIEW  = "/com/example/escriturarapida/view/game-view.fxml";

    /** Path to the start screen FXML. */
    private static final String START_VIEW = "/com/example/escriturarapida/view/start-view.fxml";

    /**
     * Shared audio manager instance injected via {@link #setAudioManager(AudioManager)}.
     * Volume is lowered on this screen and restored before any scene transition.
     */
    private AudioManager audioManager;

    /**
     * Initialization method called automatically by JavaFX after the FXML is loaded.
     * Reads the game outcome from {@link GameData} and updates all result labels.
     * Outcome codes:
     * <ul>
     *   <li>{@code 1} — Player completed all 45 levels.</li>
     *   <li>{@code 2} — Player completed the last level just as time ran out.</li>
     *   <li>{@code 3} — Player lost because time ran out.</li>
     * </ul>
     *
     * @param url            URL of the loaded FXML (not used).
     * @param resourceBundle Internationalization resources (not used).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labWinOrLost.setStyle("-fx-text-fill: white");

        switch (GameData.reasonWinOrLose) {
            case 1 -> labWinOrLost.setText("        YOU WIN\nGAME COMPLETE");
            case 2 -> labWinOrLost.setText("  YOU WIN\nSO CLOSE!");
            case 3 -> labWinOrLost.setText("YOU LOSE\nTIME'S UP");
        }

        lblLevelsResult.setText(String.valueOf(GameData.level));
        lblTimeResult.setText(String.format("%02d.%02d", GameData.seconds, GameData.milliseconds));
    }

    /**
     * Restores the volume to 100%, resets all game data, and navigates to the game screen.
     * Volume is restored before the scene transition so the next controller
     * receives the {@link AudioManager} at full volume.
     */
    @FXML
    private void onRestart() {
        audioManager.setVolume(1.0);
        gameModel.resetData();
        gameView.changeScene(GAME_VIEW, btnRestart, audioManager);
    }

    /**
     * Restores the volume to 100%, resets all game data, and navigates back to the start screen.
     * Volume is restored before the scene transition so the next controller
     * receives the {@link AudioManager} at full volume.
     */
    @FXML
    private void onExit() {
        audioManager.setVolume(1.0);
        gameModel.resetData();
        gameView.changeScene(START_VIEW, btnRestart, audioManager);
    }

    /**
     * Receives the shared {@link AudioManager} instance from the previous scene.
     * Lowers the volume to 25% while the results screen is displayed,
     * resets the music state to {@link AudioManager#GAME_START}, and triggers playback.
     * Called automatically by {@link GameView} after the FXML is loaded,
     * via the {@link AudioReceiver} interface.
     *
     * @param audioManager the shared audio manager carrying the active {@code MediaPlayer}.
     */
    @Override
    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
        audioManager.setVolume(0.25);
        AudioManager.currentStatus = AudioManager.GAME_START;
        audioManager.update();
    }
}