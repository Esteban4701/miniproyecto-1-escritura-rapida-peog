package com.example.escriturarapida;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Entry point of the Escritura Rapida application.
 * Extends {@link Application} to initialize the JavaFX runtime,
 * load custom fonts, and display the start screen.
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 */
public class Main extends Application {

    /**
     * Main method that launches the JavaFX application.
     * Internally calls {@link Application#launch(String...)} which initializes
     * the JavaFX toolkit and invokes {@link #start(Stage)}.
     *
     * @param args command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * JavaFX entry point called after the runtime is initialized.
     * Loads custom fonts into the JavaFX font registry, loads the start screen FXML,
     * applies the global stylesheet, and displays the primary stage.
     * <p>
     * Fonts loaded:
     * <ul>
     *   <li>Rajdhani Regular — used for general UI text.</li>
     *   <li>Orbitron Bold — used for titles and headings.</li>
     *   <li>Share Tech Mono — used for the countdown timer.</li>
     * </ul>
     *
     * @param primaryStage the main window provided by the JavaFX runtime.
     * @throws IOException if the FXML file or stylesheet cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/com/example/escriturarapida/fonts/Rajdhani-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/com/example/escriturarapida/fonts/Orbitron-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/com/example/escriturarapida/fonts/ShareTechMono-Regular.ttf"), 14);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/start-view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/com/example/escriturarapida/styles/styles.css")).toExternalForm()
        );
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/escriturarapida/images/F.png"))));
        primaryStage.setTitle("Escritura Rapida");
        primaryStage.show();
    }
}