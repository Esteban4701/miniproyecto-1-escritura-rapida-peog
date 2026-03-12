package com.example.escriturarapida.model;

import com.example.escriturarapida.utilities.GameData;

import java.util.*;

/**
 * Model class containing the core business logic of the game.
 * Manages the word list, word validation, feedback messages,
 * and saving or resetting game state via {@link GameData}.
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 */
public class GameModel {

    /** List of words available for the current game session. */
    private ArrayList<String> words = new ArrayList<>();

    /** Random number generator used for word and message selection. */
    private final Random random = new Random();

    /**
     * Array of positive feedback messages displayed when the player answers correctly.
     */
    public static final String[] positiveMessages = {
            "AMAZING!", "INCREDIBLE!", "PERFECT!", "UNSTOPPABLE!", "GENIUS!",
            "FLAWLESS!", "OUTSTANDING!", "LEGENDARY!", "PHENOMENAL!", "BRILLIANT!",
            "MAJESTIC!", "ELITE!", "UNBEATABLE!", "VICTORIOUS!", "SUPREME!",
            "MASTERMIND!", "SENSATIONAL!", "UNMATCHED!"
    };

    /**
     * Array of negative feedback messages displayed when the player answers incorrectly.
     */
    public static final String[] negativeMessages = {
            "WRONG!", "MISS!", "TRY AGAIN!", "NOPE!", "FAILED!", "INCORRECT!",
            "NOT QUITE!", "MISTAKE!", "ERROR!", "FALSE!", "DENIED!", "BOGUS!",
            "INVALID!", "FATAL!", "GIVE UP?", "REJECTED!", "BLUNDER!", "STRIKE!"
    };

    /**
     * Loads the full word list into memory and shuffles it randomly.
     * Must be called before any word selection is attempted.
     */
    public void loadWords() {
        words = new ArrayList<>(List.of(
                "agua", "bosque", "cielo", "dedo", "espejo", "fuego", "gato", "hielo",
                "isla", "jarras", "kilo", "lentes", "mesa", "nube", "ojo", "perro",
                "queso", "rutas", "sol", "tigre", "uvas", "viento", "web", "xenon",
                "yate", "zapato", "arroz", "barco", "carta", "disco", "elefante", "fresa",
                "globo", "huevo", "iman", "juego", "koala", "libro", "mano", "noche",
                "oreja", "pared", "quince", "reloj", "silla", "taza", "universo", "vaso",
                "wifi", "zorro", "aire", "baile", "calle", "duda", "enero", "flor",
                "golpe", "hora", "ideas", "jefe", "karma", "lago", "miedo", "nieve",
                "obras", "paz", "quinto", "rosa", "salto", "tierra", "uno", "vuelo",
                "whisky", "rayo", "zona", "azul", "broma", "clima", "datos", "error",
                "fase", "gente", "hoja", "islas", "joven", "kiwi", "leche", "meta",
                "norte", "olas", "piso", "quesos", "rama", "selva", "trote", "uñas",
                "vaca", "yoga", "zumo", "pelo"
        ));
        Collections.shuffle(words);
    }

    /**
     * Returns a randomly selected word from the current word list.
     *
     * @return a random word from the list.
     */
    public String randomSelect() {
        return words.get(random.nextInt(words.size()));
    }

    /**
     * Validates whether the typed word matches the target word.
     * Comparison is case-insensitive and ignores leading/trailing whitespace.
     *
     * @param written the word typed by the player.
     * @param word    the target word displayed on screen.
     * @return {@code true} if the words match, {@code false} otherwise.
     */
    public boolean validateWord(String written, String word) {
        return written.toLowerCase().trim().equals(word.toLowerCase());
    }

    /**
     * Removes a word from the active word list after it has been correctly answered.
     *
     * @param word the word to remove from the list.
     */
    public void removeWord(String word) {
        words.remove(word);
    }

    /**
     * Saves the current game state to {@link GameData} for use across scenes.
     *
     * @param level        the current player level.
     * @param seconds      the remaining seconds when the game ended.
     * @param milliseconds the remaining milliseconds when the game ended.
     */
    public void saveData(int level, int seconds, int milliseconds) {
        GameData.level = level;
        GameData.seconds = seconds;
        GameData.milliseconds = milliseconds;
    }

    /**
     * Resets all game state in {@link GameData} back to zero.
     * Called when the player restarts or exits to the start screen.
     */
    public void resetData() {
        GameData.level = 0;
        GameData.seconds = 0;
        GameData.milliseconds = 0;
    }

    /**
     * Returns a randomly selected positive feedback message.
     *
     * @return a random message from {@link #positiveMessages}.
     */
    public String getRandomPositiveMessage() {
        return positiveMessages[random.nextInt(positiveMessages.length)];
    }

    /**
     * Returns a randomly selected negative feedback message.
     *
     * @return a random message from {@link #negativeMessages}.
     */
    public String getRandomNegativeMessage() {
        return negativeMessages[random.nextInt(negativeMessages.length)];
    }
}

