package linguacrypt.model;

import linguacrypt.model.DeckManager;
import linguacrypt.model.Game;

public class Model {
    private Game game;
    private DeckManager deckManager;

    public Model() {
        game = new Game();
        deckManager = new DeckManager();
    }

    public Game getGame() {
        return game;
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }
    
}
