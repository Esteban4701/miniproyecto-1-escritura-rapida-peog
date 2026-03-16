package com.example.escriturarapida.model;

/**
 * Interface that defines the contract for the game's business logic.
 * Any class implementing this interface must provide word management,
 * validation, feedback messages, and game state persistence.
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 */
public interface IGameModel {

    /**
     * Loads the full word list into memory and shuffles it randomly.
     * Must be called before any word selection is attempted.
     */
    void loadWords();

    /**
     * Returns a randomly selected word from the current word list.
     *
     * @return a random word from the list.
     */
    String randomSelect();

    /**
     * Validates whether the typed word matches the target word.
     * Comparison is case-insensitive and ignores leading/trailing whitespace.
     *
     * @param written the word typed by the player.
     * @param word    the target word displayed on screen.
     * @return {@code true} if the words match, {@code false} otherwise.
     */
    boolean validateWord(String written, String word);

    /**
     * Removes a word from the active word list after it has been correctly answered.
     *
     * @param word the word to remove from the list.
     */
    void removeWord(String word);

    /**
     * Saves the current game state for use across scenes.
     *
     * @param level        the current player level.
     * @param seconds      the remaining seconds when the game ended.
     * @param milliseconds the remaining milliseconds when the game ended.
     */
    void saveData(int level, int seconds, int milliseconds);

    /**
     * Resets all game state back to zero.
     * Called when the player restarts or exits to the start screen.
     */
    void resetData();

    /**
     * Returns a randomly selected positive feedback message.
     *
     * @return a random positive message string.
     */
    String getRandomPositiveMessage();

    /**
     * Returns a randomly selected negative feedback message.
     *
     * @return a random negative message string.
     */
    String getRandomNegativeMessage();
}
