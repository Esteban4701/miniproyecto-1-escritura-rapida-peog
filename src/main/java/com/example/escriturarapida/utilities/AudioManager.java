package com.example.escriturarapida.utilities;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
/**
 * Manages background music playback for the application by dividing a single
 * audio file into named sections and switching between them based on the
 * current game state.
 *
 * <p>The audio file is expected to contain all music tracks concatenated in one
 * resource. Each game state maps to a specific time window {@code [start, end]}
 * within that file, which is looped indefinitely until the state changes.</p>
 *
 * <p>State transitions are driven externally: callers update the public static
 * field {@link #currentStatus} and then invoke {@link #update()} to apply the
 * change. {@code update()} is idempotent — if the state has not changed since
 * the last call, playback is left untouched.</p>
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * String url = getClass().getResource("/audio/soundtrack.mp3").toExternalForm();
 * AudioManager audio = new AudioManager(url);
 *
 * // Later, when entering the game:
 * AudioManager.currentStatus = AudioManager.GAME_START;
 * audio.update();
 * }</pre>
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 * @see javafx.scene.media.MediaPlayer
 */
public class AudioManager {

    /**
     * The underlying JavaFX media player used for playback.
     * A single instance is reused across all state transitions.
     */
    private final MediaPlayer mediaPlayer;

    // -----------------------------------------------------------------------
    // State constants
    // -----------------------------------------------------------------------

    /** Audio state constant for the main menu screen. */
    public static final int MENU = 0;

    /** Audio state constant for the early stage of a game session. */
    public static final int GAME_START = 1;

    /** Audio state constant for the second phase of the game start. */
    public static final int GAME_START_2 = 2;

    /** Audio state constant for the middle stage of a game session. */
    public static final int GAME_MIDDLE = 7;

    /** Audio state constant for the final stage of a game session. */
    public static final int GAME_FINAL = 8;

    // -----------------------------------------------------------------------
    // Shared mutable state
    // -----------------------------------------------------------------------

    /**
     * The currently desired audio state. Must be set to one of the state
     * constants ({@link #MENU}, {@link #GAME_START}, etc.) before calling
     * {@link #update()}.
     *
     * <p><b>Note:</b> this field is {@code static} and therefore shared across
     * all instances. Applications should maintain only one {@code AudioManager}
     * at a time to avoid conflicts.</p>
     */
    public static int currentStatus = MENU;

    /**
     * Tracks the state that was active during the last {@link #update()} call.
     * Used to detect changes and avoid redundant seeks or restarts.
     * Initialized to {@code -1} so the first {@code update()} always plays.
     */
    private int previousState = -1;

    // -----------------------------------------------------------------------
    // Time-window definitions  [startSeconds, endSeconds]
    // -----------------------------------------------------------------------

    /** Time window for the menu music section, in seconds. */
    private static final double[] musicMenu       = {   3.0,  20.9 };

    /** Time window for the game-start music section, in seconds. */
    private static final double[] musicGameStart  = {  22.0,  45.9 };

    /** Time window for the second game-start music section, in seconds. */
    private static final double[] musicGameStart2 = {  46.0,  54.3 };

    /** Time window for the mid-game music section, in seconds. */
    private static final double[] musicGameMiddle = {  54.4,  96.0 };

    /** Time window for the final-game music section, in seconds. */
    private static final double[] musicGameFinal  = { 175.0, 198.9 };

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Creates a new {@code AudioManager} backed by the audio file at the given URL.
     *
     * @param audioRoute the URI string of the audio resource (e.g. obtained via
     *                   {@code getClass().getResource(...).toExternalForm()});
     *                   must be a valid JavaFX {@link javafx.scene.media.Media} URI
     */
    public AudioManager(String audioRoute) {
        Media media = new Media(audioRoute);
        mediaPlayer = new MediaPlayer(media);
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Stops the current playback, seeks to the start of the given time window,
     * and begins looping that section indefinitely.
     *
     * @param section a two-element array where {@code section[0]} is the start
     *                time and {@code section[1]} is the stop time, both in seconds
     */
    private void playSection(double[] section) {
        mediaPlayer.stop();
        mediaPlayer.setStartTime(Duration.seconds(section[0]));
        mediaPlayer.setStopTime(Duration.seconds(section[1]));
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.seek(Duration.seconds(section[0]));
        mediaPlayer.play();
    }

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Applies the current audio state if it has changed since the last call.
     *
     * <p>Compares {@link #currentStatus} against the internally tracked
     * {@link #previousState}. If they differ, the corresponding music section
     * is played via {@link #playSection}. If the state is unchanged, this
     * method returns immediately without affecting playback.</p>
     *
     * <p>Supported state transitions:</p>
     * <ul>
     *   <li>{@link #MENU} → plays the menu section</li>
     *   <li>{@link #GAME_START} → plays the game-start section</li>
     *   <li>{@link #GAME_START_2} → plays the second game-start section</li>
     *   <li>{@link #GAME_MIDDLE} → plays the mid-game section</li>
     *   <li>{@link #GAME_FINAL} → plays the final-game section</li>
     * </ul>
     */
    public void update() {
        if (currentStatus == previousState) return;
        previousState = currentStatus;

        switch (currentStatus) {
            case MENU:        playSection(musicMenu);       break;
            case GAME_START:  playSection(musicGameStart);  break;
            case GAME_START_2:playSection(musicGameStart2); break;
            case GAME_MIDDLE: playSection(musicGameMiddle); break;
            case GAME_FINAL:  playSection(musicGameFinal);  break;
        }
    }

    /**
     * Adjusts the playback volume of the underlying media player.
     *
     * @param volume the desired volume level, in the range {@code [0.0, 1.0]}
     *               where {@code 0.0} is silent and {@code 1.0} is full volume
     */
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }
}