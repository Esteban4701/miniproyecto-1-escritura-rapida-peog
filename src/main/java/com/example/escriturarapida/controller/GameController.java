package com.example.escriturarapida.controller;

import com.example.escriturarapida.model.GameModel;
import com.example.escriturarapida.model.GameModelAdapter;
import com.example.escriturarapida.model.IGameModel;
import com.example.escriturarapida.utilities.AudioManager;
import com.example.escriturarapida.utilities.AudioReceiver;
import com.example.escriturarapida.utilities.GameData;
import com.example.escriturarapida.view.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main controller for the game screen.
 * Coordinates UI events, the countdown timer, word validation, and background music.
 * Delegates business logic to {@link IGameModel} and scene transitions to {@link GameView}.
 * Implements {@link AudioReceiver} to receive the shared {@link AudioManager} instance
 * from the previous scene without recreating the underlying {@code MediaPlayer}.
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 */
public class GameController implements Initializable, AudioReceiver {

    /** Label used to display popup feedback messages on correct or incorrect answers. */
    @FXML private Label lblPopUp;

    /** Label displaying the player's current level. */
    @FXML private Label lblLevel;

    /** Label displaying the "LEVEL" text descriptor. */
    @FXML private Label lblLevelText;

    /** Label displaying the timer text descriptor. */
    @FXML private Label lblTimerText;

    /** Label displaying the "SEC" text next to the timer. */
    @FXML private Label lblSecText;

    /** Label displaying the word the player must type. */
    @FXML private Label lblWord;

    /** Label displaying the remaining time in MM.SS format. */
    @FXML private Label lblTimer;

    /** Text field where the player types the word. */
    @FXML private TextField tfWriteField;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    /**
     * Game model accessed through the interface.
     * Decoupled from the concrete implementation via the Adapter pattern.
     */
    private final IGameModel gameModel = new GameModelAdapter(new GameModel());

    /**
     * View handler responsible for scene transitions.
     */
    private final GameView gameView = new GameView();

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    /** Path to the pause/results screen FXML. */
    private static final String PAUSE_VIEW =
            "/com/example/escriturarapida/view/pause-view.fxml";

    /**
     * Current player level.
     * Initialized from {@link GameData} if there is saved progress, otherwise starts at 1.
     */
    private int level = GameData.level > 0 ? GameData.level : 1;

    /** Main countdown timer for the current level. */
    private Timeline timer;

    /** Remaining seconds on the current timer. */
    private int seconds = 20;

    /** Remaining milliseconds on the current timer. */
    private int milliseconds = 0;

    /**
     * Shared audio manager instance injected via {@link #setAudioManager(AudioManager)}.
     * Controls background music state transitions throughout the game.
     */
    private AudioManager audioManager;

    /**
     * Initialization method called automatically by JavaFX after the FXML is loaded.
     * Configures visual glow effects, label styles, and starts the game.
     *
     * @param url            URL of the loaded FXML (not used).
     * @param resourceBundle Internationalization resources (not used).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        applyGlowEffect();
        applyLabelStyles();
        gameModel.loadWords();
        screen();
        startTimer();
    }

    /**
     * Applies a purple glow effect to the word label.
     */
    private void applyGlowEffect() {
        DropShadow innerGlow = new DropShadow();
        innerGlow.setColor(Color.web("#B427CF"));
        innerGlow.setRadius(5);
        innerGlow.setSpread(1.0);

        DropShadow outerGlow = new DropShadow();
        outerGlow.setColor(Color.web("#B427CF"));
        outerGlow.setRadius(15);
        outerGlow.setSpread(0.1);
        outerGlow.setInput(innerGlow);

        lblWord.setStyle("-fx-text-fill: white;");
        lblWord.setClip(null);
        lblWord.setPickOnBounds(false);
        lblWord.setEffect(outerGlow);
    }

    /**
     * Sets the initial orange color style on timer and level labels.
     */
    private void applyLabelStyles() {
        lblTimerText.setStyle("-fx-text-fill: #df5b00;");
        lblLevel.setStyle("-fx-text-fill: #df5b00;");
        lblLevelText.setStyle("-fx-text-fill: #df5b00;");
    }

    /**
     * Displays the first random word on screen and updates the level label.
     */
    private void screen() {
        lblWord.setText(gameModel.randomSelect());
        lblLevel.setText(String.valueOf(level));
    }

    /**
     * Handles the validation button action from the FXML.
     * Compares the typed word against the displayed word.
     * On a correct answer, advances to the next level and restarts the timer.
     * On an incorrect answer, displays a negative feedback message.
     * When level 45 is reached, saves game data and transitions to the results screen.
     * <p>
     * Music conditions are evaluated on every validation attempt via {@link #musicConditions()}.
     */
    @FXML
    private void onValidation() {
        musicConditions();
        String word = lblWord.getText();
        String written = tfWriteField.getText();

        if (gameModel.validateWord(written, word)) {
            level++;
            tfWriteField.clear();
            gameModel.removeWord(word);

            if (level == 45) {
                GameData.reasonWinOrLose = 1;
                lblLevel.setText(String.valueOf(level));
                timer.stop();
                gameModel.saveData(level, seconds, milliseconds);
                gameView.changeScene(PAUSE_VIEW, lblWord, audioManager);
            } else {
                lblWord.setText(gameModel.randomSelect());
                lblLevel.setText(String.valueOf(level));
                showMessage(true);
                startTimer();
            }
        } else {
            showMessage(false);
        }
    }

    /**
     * Starts or restarts the countdown timer for the current level.
     * Available time decreases by 2 seconds every 5 levels.
     * Timer color changes based on remaining time:
     * <ul>
     *   <li>Orange by default.</li>
     *   <li>Red when 10 or fewer seconds remain.</li>
     *   <li>Alternating dark orange and dark red when 5 or fewer seconds remain.</li>
     * </ul>
     * If time runs out, delegates to {@link #handleTimeOut()}.
     */
    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }

        seconds = 20 - ((level / 5) * 2);
        milliseconds = 0;

        timer = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            milliseconds--;
            updateTimerColor();

            if (milliseconds < 0) {
                milliseconds = 99;
                seconds--;
            }

            if (seconds < 0) {
                handleTimeOut();
                return;
            }

            lblTimer.setText(String.format("%02d.%02d", seconds, milliseconds));
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * Updates the timer label color based on remaining seconds.
     */
    private void updateTimerColor() {
        String color;
        if (seconds <= 5) {
            color = (milliseconds % 50 < 25) ? "#df5b00" : "#8b0000";
        } else if (seconds <= 10) {
            color = "#ff0000";
        } else {
            color = "#df5b00";
        }
        lblTimer.setStyle("-fx-text-fill: " + color + ";");
        lblSecText.setStyle("-fx-text-fill: " + color + ";");
    }

    /**
     * Handles the case when the countdown reaches zero.
     * Checks if the player typed the correct word just in time.
     * Transitions to the results screen with the appropriate reason code:
     * <ul>
     *   <li>{@code 2} — Win by the skin of one's teeth (correct word entered at timeout).</li>
     *   <li>{@code 3} — Loss by timeout (incorrect or empty input).</li>
     * </ul>
     * Music conditions are evaluated before the scene transition via {@link #musicConditions()}.
     */
    private void handleTimeOut() {
        musicConditions();
        seconds = 0;
        milliseconds = 0;
        timer.stop();

        String written = tfWriteField.getText();
        String word = lblWord.getText();

        if (gameModel.validateWord(written, word)) {
            level++;
            tfWriteField.clear();
            gameModel.removeWord(word);

            if (level == 45) {
                GameData.reasonWinOrLose = 2;
                lblLevel.setText(String.valueOf(level));
                gameModel.saveData(level, seconds, milliseconds);
                gameView.changeScene(PAUSE_VIEW, lblWord, audioManager);
            } else {
                lblWord.setText(gameModel.randomSelect());
                lblLevel.setText(String.valueOf(level));
                showMessage(true);
                startTimer();
            }
        } else {
            GameData.reasonWinOrLose = 3;
            gameModel.saveData(level, seconds, milliseconds);
            gameView.changeScene(PAUSE_VIEW, lblWord, audioManager);
        }
    }

    /**
     * Displays a popup feedback message for 1 second.
     * The message and color differ based on whether the answer was correct or not.
     *
     * @param correct {@code true} if the word was correct, {@code false} otherwise.
     */
    private void showMessage(boolean correct) {
        if (correct) {
            lblPopUp.setText(gameModel.getRandomPositiveMessage());
            lblPopUp.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 28px; -fx-font-weight: bold;");
        } else {
            lblPopUp.setText(gameModel.getRandomNegativeMessage());
            lblPopUp.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 28px; -fx-font-weight: bold;");
        }

        Timeline messageTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), (ActionEvent e) -> lblPopUp.setText(""))
        );
        messageTimer.play();
    }

    /**
     * Receives the shared {@link AudioManager} instance from the previous scene.
     * Sets the music state to {@link AudioManager#GAME_START} and triggers playback.
     * Called automatically by {@link com.example.escriturarapida.view.GameView}
     * after the FXML is loaded, via the {@link AudioReceiver} interface.
     *
     * @param audioManager the shared audio manager carrying the active {@code MediaPlayer}.
     */
    @Override
    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
        AudioManager.currentStatus = AudioManager.GAME_START;
        audioManager.update();
    }

    /**
     * Evaluates level-based conditions and updates the music state accordingly.
     * Called on every word validation and on timeout to ensure music transitions
     * are synchronized with game progression:
     * <ul>
     *   <li>Level 14 — transitions to {@link AudioManager#GAME_START_2}.</li>
     *   <li>Level 18 — transitions to {@link AudioManager#GAME_MIDDLE}.</li>
     *   <li>Level 33 — transitions to {@link AudioManager#GAME_FINAL}.</li>
     * </ul>
     */
    public void musicConditions() {
        if (level == 14) {
            AudioManager.currentStatus = AudioManager.GAME_START_2;
            audioManager.update();
        }
        if (level == 18) {
            AudioManager.currentStatus = AudioManager.GAME_MIDDLE;
            audioManager.update();
        }
        if (level == 33) {
            AudioManager.currentStatus = AudioManager.GAME_FINAL;
            audioManager.update();
        }
    }
}