package com.example.escriturarapida.utilities;

/**
 * Marks a controller or component as capable of receiving a shared
 * {@link AudioManager} instance through dependency injection.
 *
 * <p>Any class that needs to interact with the application's audio system
 * should implement this interface. The {@link AudioManager} is typically
 * created once at application startup and then propagated to each controller
 * as scenes are loaded, ensuring a single playback instance is shared
 * across the entire lifecycle of the app.</p>
 *
 * <p>Conventional usage during a scene transition:</p>
 * <pre>{@code
 * FXMLLoader loader = new FXMLLoader(getClass().getResource("next-view.fxml"));
 * Parent root = loader.load();
 * Object controller = loader.getController();
 * if (controller instanceof AudioReceiver receiver) {
 *     receiver.setAudioManager(audioManager);
 * }
 * }</pre>
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 * @see AudioManager
 */
public interface AudioReceiver {

    /**
     * Injects the shared {@link AudioManager} into this component.
     *
     * <p>Implementations should store the reference and use it to audioUpdate
     * {@link AudioManager#currentStatus} and call {@link AudioManager#audioUpdate()}
     * whenever the audio state needs to change for the current screen.</p>
     *
     * @param audioManager the application-wide audio manager; must not be {@code null}
     */
    void setAudioManager(AudioManager audioManager);
}