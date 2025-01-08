package linguacrypt.model;

import java.util.ArrayList;

public class CardManager {
    private ArrayList<Card> cards;
    private ArrayList<Card> deletedCards;

    public CardManager() {
        this.cards = new ArrayList<>();
        this.deletedCards = new ArrayList<>();
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void deleteCard(Card card) {
        this.cards.remove(card);
        this.deletedCards.add(card);
    }

    public void restoreCard(Card card) {
        this.cards.add(card);
        this.deletedCards.remove(card);
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public ArrayList<Card> getDeletedCards() {
        return this.deletedCards;
    }
    
}
