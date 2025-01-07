package linguacrypt.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

/**
 * Représente une partie de LinguaCrypt. Cette classe est le point central du
 * jeu, gérant : - Le lobby et les équipes (Lobby) - Le deck de cartes (Deck) -
 * La grille de jeu (matrice de cartes) - La configuration de la partie
 * (GameConfiguration)
 */
public class Game implements Visitable {

    @JsonProperty("lobby")
    private Lobby lobby;

    @JsonProperty("deck")
    private Deck deck;

    @JsonProperty("grid")
    private ArrayList<ArrayList<Card>> grid;

    @JsonProperty("config")
    private GameConfiguration config;

    @JsonProperty
    private ArrayList<ArrayList<Card>> key;

    /**
     * Constructeur par défaut. Initialise une nouvelle partie avec : - Un
     * nouveau lobby - Un deck vide - Une grille vide - Une configuration par
     * défaut
     */
    public Game() {
        this.lobby = new Lobby();
        this.deck = new Deck();
        this.grid = new ArrayList<>();
        this.key = new ArrayList<>();
        this.config = new GameConfiguration();
    }

    /**
     * Constructeur pour la désérialisation JSON.
     *
     * @param lobby Le lobby contenant les équipes
     * @param deck Le deck de cartes
     * @param grid La grille de jeu
     * @param config La configuration
     */
    @JsonCreator
    public Game(
            @JsonProperty("lobby") Lobby lobby,
            @JsonProperty("deck") Deck deck,
            @JsonProperty("grid") ArrayList<ArrayList<Card>> grid,
            @JsonProperty("config") GameConfiguration config,
            @JsonProperty("key") ArrayList<ArrayList<Card>> key) {
        this.lobby = lobby != null ? lobby : new Lobby();
        this.deck = deck;
        this.grid = grid;
        this.config = config;
    }

    public void addCard(Card card) {
        if (deck != null) {
            deck.addCard(card);
        }
    }

    public void removeCard(Card card) {
        if (deck != null) {
            deck.removeCard(card);
        }
    }

    /**
     * Initialise une nouvelle grille vide avec la taille définie dans la
     * configuration. La grille est une matrice carrée (ex: 5x5).
     */
    public void initGrid() {
        int size = config.getGridSize();
        grid = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Card> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(null);
            }
            grid.add(row);
        }
    }

    /**
     * Charge les cartes du deck dans la grille. Les cartes sont placées
     * séquentiellement dans la grille, de gauche à droite et de haut en bas.
     * Cette méthode est appelée après initGrid().
     */
    public void loadGrid() {
        if (grid == null || deck == null) {
            return;
        }

        ArrayList<Card> cards = deck.getCardList();
        int size = config.getGridSize();
        int cardIndex = 0;

        for (int i = 0; i < size && cardIndex < cards.size(); i++) {
            for (int j = 0; j < size && cardIndex < cards.size(); j++) {
                grid.get(i).set(j, cards.get(cardIndex++));
            }
        }
    }

    public ArrayList<ArrayList<Card>> getKey() {
        return key;
    }

    // Getters and Setters
    public Lobby getLobby() {
        return lobby;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<ArrayList<Card>> getGrid() {
        return grid;
    }

    public GameConfiguration getConfig() {
        return config;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
