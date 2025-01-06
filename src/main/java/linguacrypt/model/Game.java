package linguacrypt.model;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Game implements Visitable {
    @JsonProperty("players")
    private List<Player> players;
    @JsonProperty("deck") 
    private Deck deck;
    @JsonProperty("configuration")
    private GameConfiguration configuration;

    public Game() {
        players = new ArrayList<>();
    }

    @JsonCreator
    public Game(
        @JsonProperty("players") List<Player> players,
        @JsonProperty("deck") Deck deck, 
        @JsonProperty("configuration") GameConfiguration configuration) {
        this.players = players != null ? players : new ArrayList<>();
        this.deck = deck;
        this.configuration = configuration;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setConfiguration(GameConfiguration configuration) {
        this.configuration = configuration;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
