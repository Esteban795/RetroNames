package linguacrypt.model;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a deck of cards in the game.
 * Contains a list of cards and methods to manage them.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Deck implements Visitable {
    @JsonProperty("deckName")
    private String deckName;

    @JsonProperty("cardList")
    private ArrayList<Card> cardList;

    public Deck() {
        this.deckName = "Default Deck";
        this.cardList = new ArrayList<>();
    }

    public Deck(String deckName) {
        this.deckName = deckName;
        this.cardList = new ArrayList<>();
    }

    @JsonCreator
    public Deck(
            @JsonProperty("deckName") String deckName,
            @JsonProperty("cardList") ArrayList<Card> cardList) {
        this.deckName = deckName;
        this.cardList = cardList != null ? cardList : new ArrayList<>();
    }

    @JsonIgnore
    // Getters and Setters
    @JsonProperty("deckName")
    public String getName() {
        return deckName;
    }

    @JsonProperty("deckName")
    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    public void setCardList(ArrayList<Card> cardList) {
        this.cardList = cardList;
    }

    public Card getCard(String cardName) {
        for (Card card : cardList) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Add a card to the deck
     * 
     * @param card Card to add
     */
    public void addCard(Card card) { // Add a card to the deck (the deck is supposed to be sorted) at the correct
                                     // position
        int index = 0;
        for (Card c : cardList) {
            if (c.getName().compareTo(card.getName()) > 0) {
                break;
            }
            index++;
        }
        cardList.add(index, card);
    }

    /**
     * Remove a card from the deck
     * 
     * @param card Card to remove
     *             Will probably need some changes to handle duplicates or specific
     *             card removal logic
     */

    public void removeCard(Card card) {
        cardList.remove(card);
    }

    // Methods
    @JsonIgnore
    public void sortCards() { // Sort the cards in the deck
        // Used when the deck is loaded from a file (it is possible that the cards are
        // not sorted if the file was created or edited manually)
        boolean isSorted = true;
        for (int i = 0; i < cardList.size() - 1; i++) {
            if (cardList.get(i).getName().compareTo(cardList.get(i + 1).getName()) > 0) {
                isSorted = false;
                break;
            }
        }
        if (!isSorted) {
            Collections.sort(cardList, (c1, c2) -> c1.getName().compareTo(c2.getName()));
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public boolean containsCard(Card selectedCard) {
        return cardList.contains(selectedCard);
    }

    public ArrayList<Card> deepCopyCards() {
        ArrayList<Card> copy = new ArrayList<>();
        for (Card card : cardList) {
            copy.add(new Card(card.getName(), card.getCardUrl()));
        }
        return copy;
    }
}