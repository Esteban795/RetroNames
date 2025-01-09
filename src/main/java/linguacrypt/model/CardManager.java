package linguacrypt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardManager {
    private ArrayList<Card> cards;
    private HashMap<Card, ArrayList<Deck>> cardDeckMap;

    public CardManager() {
        this.cards = new ArrayList<>();
        this.cardDeckMap = new HashMap<>();
    }

    public CardManager(CardManager other) {
        this.cards = new ArrayList<>(other.cards);
        this.cardDeckMap = new HashMap<>();
        
        // Deep copy of cardDeckMap
        for (Map.Entry<Card, ArrayList<Deck>> entry : other.cardDeckMap.entrySet()) {
            this.cardDeckMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
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


        this.cardDeckMap.get(card).remove(deck);
    }

    public void restoreCard(Card card, Deck deck) {
        System.out.println("restore card " + card.getName() + " to deck " + deck.getName());
        this.cards.add(card);
        deck.addCard(card);

    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public HashMap<Card, ArrayList<Deck>> getCardDeckMap() {
        return this.cardDeckMap;
    }

    public ArrayList<Deck> getDecks(Card card) {
        if (!cardDeckMap.containsKey(card)) {
            return new ArrayList<>(); // Return empty list instead of null
        }
        return cardDeckMap.get(card);
    }

    public ArrayList<Deck> getDecks(String cardName) {
        for (Card card : this.cards) {
            if (card.getName().equals(cardName)) {
                return this.cardDeckMap.get(card);
            }
        }
        return null;
    }

    public String toString(ArrayList<Deck> deck) {
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
        return null;
    }

    public void consolidateDuplicateCards() {
        HashMap<String, Card> uniqueCards = new HashMap<>();

        // First pass: identify unique cards
        for (Card card : cards) {
            String cardName = card.getName();
            if (!uniqueCards.containsKey(cardName)) {
                uniqueCards.put(cardName, card);
            }
        }

        // Second pass: update references in decks
        for (Map.Entry<Card, ArrayList<Deck>> entry : cardDeckMap.entrySet()) {
            Card currentCard = entry.getKey();
            String cardName = currentCard.getName();

            if (uniqueCards.containsKey(cardName)) {
                Card uniqueCard = uniqueCards.get(cardName);
                if (currentCard != uniqueCard) {
                    // Update deck references to use unique card
                    ArrayList<Deck> decks = entry.getValue();
                    for (Deck deck : decks) {
                        deck.replaceCard(currentCard, uniqueCard);
                    }

                    // Update cardDeckMap
                    if (!cardDeckMap.containsKey(uniqueCard)) {
                        cardDeckMap.put(uniqueCard, new ArrayList<>());
                    }
                    cardDeckMap.get(uniqueCard).addAll(decks);
                    cards.remove(currentCard);
                }
            }
        }
    }
}
