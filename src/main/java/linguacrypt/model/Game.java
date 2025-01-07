package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;
import java.util.ArrayList;


/**
 * Représente une partie de LinguaCrypt.
 * Cette classe est le point central du jeu, gérant :
 * - Le deck de cartes (Deck)
 * - La grille de jeu (matrice de cartes)
 * - La configuration de la partie (GameConfiguration)
 */
public class Game implements Visitable {
    
    @JsonProperty("grid")
    private ArrayList<ArrayList<Card>> grid;
    
    @JsonProperty("config")
    private GameConfiguration config;

    @JsonProperty("nbTurn")
    private int nbTurn;

    /**
     * Constructeur par défaut.
     * Initialise une nouvelle partie avec :
     * - Un deck vide
     * - Une grille vide
     * - Une configuration par défaut
     */
    public Game() {
        this.grid = new ArrayList<>();
        this.config = new GameConfiguration();
        this.nbTurn = 0;
    }

    /**
     * Constructeur pour la désérialisation JSON.
     * @param grid La grille de jeu
     * @param config La configuration
     * @param nbTurn Le nombre de tours joués
     */

    @JsonCreator
    public Game(
        @JsonProperty("grid") ArrayList<ArrayList<Card>> grid,
        @JsonProperty("config") GameConfiguration config,
        @JsonProperty("nbTurn") int nbTurn) {
        this.grid = grid;
        this.config = config;
        this.nbTurn = nbTurn;
    }
    /**
     * Initialise une nouvelle grille vide avec la taille définie dans la configuration.
     * La grille est une matrice carrée (ex: 5x5).
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
     * Charge les cartes du deck dans la grille.
     * Les cartes sont placées séquentiellement dans la grille, de gauche à droite
     * et de haut en bas. Cette méthode est appelée après initGrid().
     */
    public void loadGrid() {
        Deck deck = config.getCurrentDeck();
        if (grid == null || deck == null) return;
        
        ArrayList<Card> cards = deck.getCardList();
        int size = config.getGridSize();
        int cardIndex = 0;
        
        for (int i = 0; i < size && cardIndex < cards.size(); i++) {
            for (int j = 0; j < size && cardIndex < cards.size(); j++) {
                grid.get(i).set(j, cards.get(cardIndex++));
            }
        }
    }

    // Getters and Setters
    public ArrayList<ArrayList<Card>> getGrid() { return grid; }
    public GameConfiguration getConfig() { return config; }
    public int getNbTurn() { return nbTurn; }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}