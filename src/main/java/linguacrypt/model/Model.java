package linguacrypt.model;

import linguacrypt.model.DeckManager;
import linguacrypt.model.CardManager;
import linguacrypt.model.Game;

public class Model {

    private Game game;
    private DeckManager deckManager;
    private CardManager cardManager;

    public Model() {
        this.game = new Game();
        this.deckManager = new DeckManager();
    }

    public Model(Boolean loadDeckManager) {
        this.game = new Game();
        this.deckManager = DeckManager.loadDeckManager("deckManager.json");
        this.cardManager = new CardManager();
        if (this.deckManager != null && !this.deckManager.getDeckList().isEmpty()) {
            this.deckManager.getDeckList().forEach(deck -> deck.sortCards()); // Assure that cards are sorted
            this.deckManager.getDeckList().forEach(deck -> deck.getCardList().forEach(card -> cardManager.addCard(card, deck))); // Add cards to cardManager            
            this.cardManager.consolidateDuplicateCards();
            this.deckManager.sortDecks();
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
    
    public CardManager getCardManager() {
        return cardManager;
    }

    public void setDeckManager(DeckManager deckManager) {
        this.deckManager = new DeckManager(deckManager);
    }
}
