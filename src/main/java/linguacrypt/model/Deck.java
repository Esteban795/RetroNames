package linguacrypt.model;

import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

/**
 * Represents a deck of cards in the game.
 * Contains a list of cards and methods to manage them.
 */
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
    public String getName() {
        return deckName;
    }

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

    public ArrayList<Card> deepCopyCards() {
        ArrayList<Card> copy = new ArrayList<>();
        for (Card card : cardList) {
            copy.add(new Card(card.getName(),card.getCardUrl()));
        }
        return copy;
    }
}