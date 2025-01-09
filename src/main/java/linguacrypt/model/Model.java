package linguacrypt.model;

public class Model {

    private Game game;
    private DeckManager deckManager;

    public Model() {
        this.game = new Game();
        this.deckManager = new DeckManager();
    }

    public Model(Boolean loadDeckManager) {
        this.game = new Game();
        this.deckManager = DeckManager.loadDeckManager("deckManager.json");
        if (this.deckManager != null && !this.deckManager.getDeckList().isEmpty()) {
            this.deckManager.getDeckList().forEach(deck -> deck.sortCards()); // Assure that cards are sorted
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }

}
