package linguacrypt.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

public class DeckManager implements Visitable {
    @JsonProperty("deckList")
    private ArrayList<Deck> deckList;

    @JsonCreator
    public DeckManager() {
        this.deckList = new ArrayList<>();
    }

    public void addDeck(Deck deck) {
        deckList.add(deck);
    }

    public void removeDeck(Deck deck) {
        deckList.remove(deck);
    }

    public ArrayList<Deck> getDeckList() {
        return deckList;
    }

    public Deck getDeck(String deckName) {
        for (Deck deck : deckList) {
            if (deck.getDeckName().equals(deckName)) {
                return deck;
            }
        }
        return null;
    }
    
    public Deck getRandomDeck() {
        if (deckList.size() > 0) {
            return deckList.get((int) (Math.random() * deckList.size()));
        }
        return null;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
