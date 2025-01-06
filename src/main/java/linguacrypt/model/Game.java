package linguacrypt.model;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une partie de LinguaCrypt.
 * Implémente Visitable pour le pattern Visitor utilisé dans la sérialisation.
 * 
 */

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

    /**
     * Constructeur pour la désérialisation JSON.
     * @param players Liste des joueurs
     * @param deck Deck de la partie
     * @param configuration Configuration de la partie
     * 
     */

    @JsonCreator
    public Game(
        @JsonProperty("players") List<Player> players,
        @JsonProperty("deck") Deck deck, 
        @JsonProperty("configuration") GameConfiguration configuration) {
        this.players = players != null ? players : new ArrayList<>();
        this.deck = deck;
        this.configuration = configuration;
    }

    // Getters et Setters avec annotations JSON
    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }
    public Deck getDeck() { return deck; }
    public void setDeck(Deck deck) { this.deck = deck; }
    public GameConfiguration getConfiguration() { return configuration; }
    public void setConfiguration(GameConfiguration configuration) { this.configuration = configuration; }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
