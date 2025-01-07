package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;
import java.util.ArrayList;

public class Game implements Visitable {
    @JsonProperty("lobby")
    private Lobby lobby;
    
    @JsonProperty("cardList")
    private Deck cardList;
    
    @JsonProperty("grid")
    private ArrayList<ArrayList<Card>> grid;
    
    @JsonProperty("config")
    private GameConfiguration config;

    public Game() {
        this.lobby = new Lobby();
        this.cardList = new Deck();
        this.grid = new ArrayList<>();
        this.config = new GameConfiguration();
    }

    @JsonCreator
    public Game(
        @JsonProperty("lobby") Lobby lobby,
        @JsonProperty("cardList") Deck cardList,
        @JsonProperty("grid") ArrayList<ArrayList<Card>> grid,
        @JsonProperty("config") GameConfiguration config) {
        this.lobby = lobby != null ? lobby : new Lobby();
        this.cardList = cardList;
        this.grid = grid;
        this.config = config;
    }

    public void addCard(Card card) {
        if (cardList != null) {
            cardList.addCard(card);
        }
    }

    public void removeCard(Card card) {
        if (cardList != null) {
            cardList.removeCard(card);
        }
    }

    public void initGrid() {
        int size = (int) Math.sqrt(config.getGridSize());
        grid = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Card> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(null);
            }
            grid.add(row);
        }
    }

    public void loadGrid() {
        if (grid == null || cardList == null) return;
        
        ArrayList<Card> cards = cardList.getCardList();
        int size = (int) Math.sqrt(config.getGridSize());
        int cardIndex = 0;
        
        for (int i = 0; i < size && cardIndex < cards.size(); i++) {
            for (int j = 0; j < size && cardIndex < cards.size(); j++) {
                grid.get(i).set(j, cards.get(cardIndex++));
            }
        }
    }

    // Getters and Setters
    public Lobby getLobby() { return lobby; }
    public Deck getCardList() { return cardList; }
    public ArrayList<ArrayList<Card>> getGrid() { return grid; }
    public GameConfiguration getConfig() { return config; }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}