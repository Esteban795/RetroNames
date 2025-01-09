package linguacrypt.model;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import linguacrypt.visitor.DeserializationVisitor;
import linguacrypt.visitor.SerializationVisitor;
import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DeckManager implements Visitable {
    @JsonProperty("deckList")
    private ArrayList<Deck> deckList;

    @JsonProperty("deletedDeckList")
    private ArrayList<Deck> deletedDeckList;

    @JsonProperty("newlyAddedDeckList")
    private ArrayList<Deck> newlyAddedDeckList;


    @JsonCreator
    public DeckManager() {
        this.deckList = new ArrayList<>();
        this.deletedDeckList = new ArrayList<>();
        this.newlyAddedDeckList = new ArrayList<>();
    }

    public Boolean addDeck(Deck deck) {
        if (getDeck(deck.getName()) != null) {
            return false;
        }
        int insertIndex = 0;
        while (insertIndex < deckList.size() && deckList.get(insertIndex).getName().compareTo(deck.getName()) < 0) {
            insertIndex++;
        }
        deckList.add(insertIndex, deck);
        newlyAddedDeckList.add(deck);
        return true;
    }

    public void addDeckAgain(Deck deck) {
        if (getDeck(deck.getName()) != null) {
            return;
        }
        int insertIndex = 0;
        while (insertIndex < deckList.size() && deckList.get(insertIndex).getName().compareTo(deck.getName()) < 0) {
            insertIndex++;
        }
        deckList.add(insertIndex, deck);
    }

    public void removeDeck(Deck deck) {
        if (deckList.remove(deck)) {
            deletedDeckList.add(deck);
        }
    }

    public void restoreAllDecks() {
        for (Deck deck : deletedDeckList) {
            addDeckAgain(deck);
            System.out.println("Restored deck: " + deck.getName());
        }
        for (Deck deck : newlyAddedDeckList) {
            deckList.remove(deck);
        }
        newlyAddedDeckList.clear();
    }

    public ArrayList<Deck> getDeckList() {
        return deckList;
    }

    public ArrayList<Deck> getDeletedDeckList() {
        return deletedDeckList;
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

    public void sortDecks() {
        if (deckList.size() <= 1)
            return;
        for (int i = 0; i < deckList.size() - 1; i++) {
            if (deckList.get(i).getName().compareTo(deckList.get(i + 1).getName()) > 0) {
                // List is not sorted, continue with sort
                Collections.sort(deckList, (d1, d2) -> d1.getName().compareTo(d2.getName()));
                return;
            }
            if (i == deckList.size() - 2) {
                // List is already sorted
                return;
            }
        }
    }
}
