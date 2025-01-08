package linguacrypt.model;

import java.util.ArrayList;
import java.util.HashMap;

public class CardManager {
    private ArrayList<Card> cards;
    private ArrayList<Card> deletedCards;
    private HashMap<Card, ArrayList<Deck>> cardDeckMap;

    public CardManager() {
        this.cards = new ArrayList<>();
        this.deletedCards = new ArrayList<>();
        this.cardDeckMap = new HashMap<>();
    }

    public void addCard(Card card, Deck deck) {
        this.cards.add(card);
        if (this.cardDeckMap.containsKey(card)) {
            this.cardDeckMap.get(card).add(deck);
            System.out.println("Card already exists");
        } else {
            ArrayList<Deck> decks = new ArrayList<>();
            decks.add(deck);
            this.cardDeckMap.put(card, decks);
        }
    }

    public void deleteCard(Card card, Deck deck) {
        this.cards.remove(card);
        this.deletedCards.add(card);
        this.cardDeckMap.get(card).remove(deck);        
    }

    public void restoreCard(Card card, Deck deck) {
        this.cards.add(card);
        this.deletedCards.remove(card);
        this.cardDeckMap.get(card).add(deck);
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public ArrayList<Card> getDeletedCards() {
        return this.deletedCards;
    }
    
    public HashMap<Card, ArrayList<Deck>> getCardDeckMap() {
        return this.cardDeckMap;
    }

    public ArrayList<Deck> getDecks(Card card) {
        return this.cardDeckMap.get(card);
    }

    public ArrayList<Deck> getDecks(String cardName) {
        for (Card card : this.cards) {
            if (card.getCardName().equals(cardName)) {
                return this.cardDeckMap.get(card);
            }
        }
        return null;
    }

    public String toString(ArrayList<Deck> deck){
        System.out.println("toString");
        String deckNames = "";
        for (Deck d : deck) {
            System.out.println(d.getDeckName());
            deckNames += d.getDeckName() + ", ";
        }
        return deckNames;
    }

	public Card getCard(String cardName) {
        for (Card card : this.cards) {
            if (card.getCardName().equals(cardName)) {
                return card;
            }
        }
        for (Card card : this.deletedCards) {
            if (card.getCardName().equals(cardName)) {
                return card;
            }
        }
        return null;
	}

}
