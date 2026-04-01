package com.example.escriturarapida.controller;

import com.example.escriturarapida.utilities.AudioManager;
import com.example.escriturarapida.utilities.AudioReceiver;
import com.example.escriturarapida.view.GameView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the start screen of the application.
 * Handles the initial navigation from the start screen to the game screen.
 *
 * <p>This controller manages the main entry point of the UI, binding the start
 * button to the scene transition logic and initializing the audio system
 * in menu state upon loading.</p>
 *
 * <p>Implements {@link Initializable} for JavaFX FXML lifecycle integration and
 * {@link AudioReceiver} to receive and manage the shared {@link AudioManager} instance.</p>
 *
 * <p>Usage example (injected by JavaFX FXMLLoader):</p>
 * <pre>{@code
 * FXMLLoader loader = new FXMLLoader(getClass().getResource("start-view.fxml"));
 * Parent root = loader.load();
 * StartController controller = loader.getController();
 * controller.setAudioManager(audioManager);
 * }</pre>
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 * @see AudioManager
 * @see AudioReceiver
 * @see GameView
 */
public class StartController implements Initializable, AudioReceiver {

    /**
     * Button injected by FXML that triggers the transition to the game screen.
     * Also used as the source node for the scene change in {@link GameView#changeScene}.
     */
    @FXML
    private Button btnStart;

    /**
     * View handler responsible for managing scene transitions between FXML views.
     * Instantiated eagerly since it carries no mutable state.
     */
    private final GameView gameView = new GameView();

    /**
     * Classpath location of the game screen FXML resource.
     * Passed to {@link GameView#changeScene} when the start button is pressed.
     */
    private static final String GAME_VIEW =
            "/com/example/escriturarapida/view/game-view.fxml";

    /**
     * Shared audio manager injected via {@link #setAudioManager(AudioManager)}.
     * Controls background music and sound effects across scenes.
     */
    private AudioManager audioManager;

    /**
     * Required by {@link Initializable}; no initialization logic is needed
     * for this controller since all setup is deferred to {@link #setAudioManager}.
     *
     * @param url            the location of the FXML resource (unused)
     * @param resourceBundle the resource bundle for the root object (unused)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Handles the action event fired by {@code btnStart}.
     * Delegates the scene transition to {@link GameView#changeScene},
     * passing the game view path, the source button node, and the audio manager.
     *
     * <p>Annotated with {@link FXML} so JavaFX can bind it to the
     * {@code onAction} attribute of the button in the FXML file.</p>
     */
    @FXML
    private void onStart() {
        gameView.changeScene(GAME_VIEW, btnStart, audioManager);
    }

    /**
     * Injects the shared {@link AudioManager} instance into this controller
     * and sets the audio state to {@link AudioManager#MENU}, triggering an update
     * so the correct background music plays while the start screen is active.
     *
     * <p>Must be called after the FXML is loaded and before the user interacts
     * with the start screen.</p>
     *
     * @param audioManager the application-wide audio manager; must not be {@code null}
     */
    @Override
    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
        AudioManager.currentStatus = AudioManager.MENU;
        audioManager.update();
    }
}