package com.example.escriturarapida.model;

/**
 * Adapter class that wraps {@link GameModel} and implements {@link IGameModel}.
 * Follows the Adapter design pattern to decouple the game logic from its
 * consumers, allowing substitution or extension without modifying existing code.
 *
 * <p>Usage example:</p>
 * <pre>
 *     IGameModel model = new GameModelAdapter(new GameModel());
 *     model.loadWords();
 *     String word = model.randomSelect();
 * </pre>
 *
 * @author Paulo Esteban Ordoñez Gutiérrez
 * @version 1.0
 * @since 2026
 */
public class GameModelAdapter implements IGameModel {

    /** The adaptee instance containing the real game logic. */
    private final GameModel gameModel;

    /**
     * Constructs a new adapter wrapping the given {@link GameModel}.
     *
     * @param gameModel the concrete game model to adapt; must not be {@code null}.
     */
    public GameModelAdapter(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    /** {@inheritDoc} */
    @Override
    public void loadWords() {
        gameModel.loadWords();
    }

    /** {@inheritDoc} */
    @Override
    public String randomSelect() {
        return gameModel.randomSelect();
    }

    /** {@inheritDoc} */
    @Override
    public boolean validateWord(String written, String word) {
        return gameModel.validateWord(written, word);
    }

    /** {@inheritDoc} */
    @Override
    public void removeWord(String word) {
        gameModel.removeWord(word);
    }

    /** {@inheritDoc} */
    @Override
    public void saveData(int level, int seconds, int milliseconds) {
        gameModel.saveData(level, seconds, milliseconds);
    }

    /** {@inheritDoc} */
    @Override
    public void resetData() {
        gameModel.resetData();
    }

    /** {@inheritDoc} */
    @Override
    public String getRandomPositiveMessage() {
        return gameModel.getRandomPositiveMessage();
    }

    /** {@inheritDoc} */
    @Override
    public String getRandomNegativeMessage() {
        return gameModel.getRandomNegativeMessage();
    }
}
