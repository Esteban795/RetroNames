package linguacrypt.model;

import linguacrypt.model.DeckManager;
import linguacrypt.model.Game;

public class Model {
    private Game game;
    private DeckManager deckManager;

    public Model() {
        this.game = new Game();
        this.deckManager = new DeckManager();
    }

    public Model(Boolean loadDeckManager){
        this.game = new Game();
        this.deckManager = DeckManager.loadDeckManager("deckManager.json");
        
    }

    public Game getGame() {
        return game;
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }
    
}
