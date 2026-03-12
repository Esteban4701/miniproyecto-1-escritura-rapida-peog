package com.example.escriturarapida.utilities;

/**
 * Utility class that holds shared game state across scenes.
 * All fields are static, meaning there is only one copy in memory
 * accessible from any class without needing to create an instance.
 * <p>
 * This class acts as a simple data transfer object between controllers,
 * persisting values when navigating from the game screen to the results screen.
 * <p>
 * Values for {@link #reasonWinOrLose}:
 * <ul>
 *   <li>{@code 0} — Default, game has not ended yet.</li>
 *   <li>{@code 1} — Player won by completing all 45 levels.</li>
 *   <li>{@code 2} — Player won on the last level just as time ran out.</li>
 *   <li>{@code 3} — Player lost because time ran out.</li>
 * </ul>
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 */
public class GameData {

    /**
     * The level reached by the player when the game ended.
     * Reset to {@code 0} when the player restarts or exits to the start screen.
     */
    public static int level = 0;

    /**
     * The remaining seconds on the timer when the game ended.
     * Reset to {@code 0} when the player restarts or exits to the start screen.
     */
    public static int seconds = 0;

    /**
     * The remaining milliseconds on the timer when the game ended.
     * Reset to {@code 0} when the player restarts or exits to the start screen.
     */
    public static int milliseconds = 0;

    /**
     * The reason the game ended, used by {@link com.example.escriturarapida.controller.PauseController}
     * to display the appropriate win or lose message.
     * Reset to {@code 0} when the player restarts or exits to the start screen.
     */
    public static int reasonWinOrLose = 0;
}
