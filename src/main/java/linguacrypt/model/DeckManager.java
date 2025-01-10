package linguacrypt.model;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import linguacrypt.visitor.DeserializationVisitor;
import linguacrypt.visitor.SerializationVisitor;
import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

public class DeckManager implements Visitable {
    @JsonProperty("deckList")
    private ArrayList<Deck> deckList;

    @JsonCreator
    public DeckManager() {
        this.deckList = new ArrayList<>();
    }

    public Boolean addDeck(Deck deck) {
        if (getDeck(deck.getName()) == null) {
            deckList.add(deck);
            return true;
        } else {
            return false;
        }
    }

    public void removeDeck(Deck deck) {
        deckList.remove(deck);
    }

    public ArrayList<Deck> getDeckList() {
        return deckList;
    }

    public Deck getDeck(String deckName) {
        for (Deck deck : deckList) {
            if (deck.getName().equals(deckName)) {
                return deck;
            }
        }
        return null;
    }

    @JsonIgnore
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

    public static DeckManager loadDeckManager(String filename) {
        String path = "src/main/resources/deckSave/";
        System.out.println("Loading deck manager from file: " + filename);
        DeserializationVisitor visitor = new DeserializationVisitor(path);

        return visitor.loadDeckManager(filename);
    }

    public void saveDeckManager() {
        String path = "src/main/resources/deckSave/";
        // Save deck manager to file
        SerializationVisitor visitor = new SerializationVisitor(path);
        this.accept(visitor); // This will save the deckManager to a JSON file
    }


    public ArrayList<Card> shuffleDeck(Deck deck) {
        ArrayList<Card> cards = new ArrayList<>(deck.getCardList());
        Collections.shuffle(cards);
        return cards;
    }
}
