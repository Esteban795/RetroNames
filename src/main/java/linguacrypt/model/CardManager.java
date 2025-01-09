package linguacrypt.model;

import java.util.ArrayList;
import java.util.HashMap;

public class CardManager {
    private ArrayList<Card> cards;
    private HashMap<Card, ArrayList<Deck>> deletedCards;
    private HashMap<Card, ArrayList<Deck>> cardDeckMap;

    public CardManager() {
        this.cards = new ArrayList<>();
        this.deletedCards = new HashMap<>();
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
        System.out.println("delete card " + card.getName() + " from deck " + deck.getName());
        this.cards.remove(card);
        
        // Add to deletedCards HashMap
        if (!deletedCards.containsKey(card)) {
            deletedCards.put(card, new ArrayList<>());
        }
        deletedCards.get(card).add(deck);
        
        this.cardDeckMap.get(card).remove(deck);
    }

    public void restoreCard(Card card, Deck deck) {
        System.out.println("restore card " + card.getName() + " to deck " + deck.getName());
        this.cards.add(card);
        deck.addCard(card);
        
        // Remove from deletedCards
        if (deletedCards.containsKey(card)) {
            deletedCards.get(card).remove(deck);
            if (deletedCards.get(card).isEmpty()) {
                deletedCards.remove(card);
            }
        }
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public HashMap<Card, ArrayList<Deck>> getDeletedCards() {
        return deletedCards;
    }
    
    public HashMap<Card, ArrayList<Deck>> getCardDeckMap() {
        return this.cardDeckMap;
    }

    public ArrayList<Deck> getDecks(Card card) {
        return this.cardDeckMap.get(card);
    }

    public ArrayList<Deck> getDecks(String cardName) {
        for (Card card : this.cards) {
            if (card.getName().equals(cardName)) {
                return this.cardDeckMap.get(card);
            }
        }
        return null;
    }

    public String toString(ArrayList<Deck> deck){
        System.out.println("toString");
        String deckNames = "";
        for (Deck d : deck) {
            System.out.println(d.getName());
            deckNames += d.getName() + ", ";
        }
        if (!deckNames.isEmpty()) {
            deckNames = deckNames.substring(0, deckNames.length() - 2);
        }
        return deckNames;
    }

    public Card getCard(String cardName) {
        for (Card card : this.cards) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        for (Card card : this.deletedCards.keySet()) {
            if (card.getName().equals(cardName)) {
                return card;
            }
        }
        return null;
    }

}
