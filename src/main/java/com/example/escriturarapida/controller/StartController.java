package com.example.escriturarapida.controller;

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
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 */
public class StartController implements Initializable {

    /** Button that starts the game and navigates to the game screen. */
    @FXML private Button btnStart;

    /** View handler responsible for scene transitions. */
    private final GameView gameView = new GameView();

    private static final String GAME_VIEW = "/com/example/escriturarapida/view/game-view.fxml";

    /** Not used but required by {@link Initializable}. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}


    /**
     * Navigates to the game screen when the start button is pressed.
     */
    @FXML
    private void onStart() {
        gameView.changeScene(GAME_VIEW, btnStart);
    }
}