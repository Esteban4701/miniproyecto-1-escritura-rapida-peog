package com.example.escriturarapida.controller;

import com.example.escriturarapida.model.GameModel;
import com.example.escriturarapida.model.GameModelAdapter;
import com.example.escriturarapida.model.IGameModel;
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
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
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

    /**
     * Game model accessed through the interface.
     * Used only to reset game data on restart or exit.
     */
    private final IGameModel gameModel = new GameModelAdapter(new GameModel());

    /** View handler responsible for scene transitions. */
    private final GameView gameView = new GameView();


    private static final String GAME_VIEW  = "/com/example/escriturarapida/view/game-view.fxml";
    private static final String START_VIEW = "/com/example/escriturarapida/view/start-view.fxml";

    /**
     * Reads the game outcome from {@link GameData} and updates all result labels.
     * <ul>
     *   <li>{@code reasonWinOrLose == 1}: Player completed all levels.</li>
     *   <li>{@code reasonWinOrLose == 2}: Player completed the last level just as time ran out.</li>
     *   <li>{@code reasonWinOrLose == 3}: Player lost because time ran out.</li>
     * </ul>
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
     * Resets all game data and navigates to the game screen.
     */
    @FXML
    private void onRestart() {
        gameModel.resetData();
        gameView.changeScene(GAME_VIEW, btnRestart);
    }

    /**
     * Resets all game data and navigates back to the start screen.
     */
    @FXML
    private void onExit() {
        gameModel.resetData();
        gameView.changeScene(START_VIEW, btnRestart);
    }
}