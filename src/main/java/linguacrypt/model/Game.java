package linguacrypt.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

/**
 * Représente une partie de LinguaCrypt.
 * Cette classe est le point central du jeu, gérant :
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

    @JsonProperty("currentTeam")
    private boolean currentTeam;

    @JsonProperty("stats")
    private GameStatistics stats;

    @JsonProperty("hasStarted")
    private boolean hasStarted;

    /**
     * Constructeur par défaut.
     * Initialise une nouvelle partie avec :
     * - Une grille vide
     * - Une configuration par défaut
     */
    public Game() {
        this.grid = new ArrayList<>();
        this.config = new GameConfiguration();
        this.stats = new GameStatistics();
        this.nbTurn = 0;
        this.currentTeam = true;
        this.hasStarted = false;
    }

    /**
     * Constructeur pour la désérialisation JSON.
     *
     * @param grid   La grille de jeu
     * @param config La configuration
     * @param nbTurn Le nombre de tours joués
     */
    @JsonCreator
    public Game(
            @JsonProperty("grid") ArrayList<ArrayList<Card>> grid,
            @JsonProperty("config") GameConfiguration config,
            @JsonProperty("nbTurn") int nbTurn,
            @JsonProperty("currentTeam") boolean currentTeam,
            @JsonProperty("stats") GameStatistics stats,
            @JsonProperty("hasStarted") boolean hasStarted) {
        this.grid = grid;
        this.config = config;
        this.nbTurn = nbTurn;
        this.currentTeam = currentTeam;
        this.stats = stats;
        this.hasStarted = hasStarted;
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
        if (this.config.getCurrentDeck() != null) {
            loadGrid();
        }
    }

    /**
     * Charge les cartes du deck dans la grille. Les cartes sont placées
     * séquentiellement dans la grille, de gauche à droite et de haut en bas.
     * Cette méthode est appelée après initGrid().
     */
    public void loadGrid() {
        Deck deck = config.getCurrentDeck();
        if (grid == null || deck == null) {
            System.err.println("Grid or deck is null");
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
        hasStarted = true;
    }

    public void switchTeam() {
        currentTeam = !currentTeam;
    }

    // Getters and Setters
    public ArrayList<ArrayList<Card>> getGrid() {
        return grid;
    }

    public GameConfiguration getConfig() {
        return config;
    }

    public Team getCurrentTeam() {
        return (currentTeam ? config.getTeamManager().getRedTeam() : config.getTeamManager().getBlueTeam());
    }

    public int getBlueTeamFoundCards() {
        return config.getTeamManager().getBlueTeam().getNbFoundCards();
    }

    public int getRedTeamFoundCards() {
        return config.getTeamManager().getRedTeam().getNbFoundCards();
    }

    public int getNbTurn() {
        return nbTurn;
    }

    public GameStatistics getStats() {
        return stats;
    }

    public void setGrid(List<Card> key) {
        ArrayList<ArrayList<Card>> keyMatrix = new ArrayList<>();
        int size = config.getGridSize();
        int keyIndex = 0;

        for (int i = 0; i < size; i++) {
            ArrayList<Card> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(key.get(keyIndex++));
            }
            keyMatrix.add(row);
        }
        this.grid = keyMatrix;
    }

    public int revealCard(Card card) {
        card.reveal();
        Team redTeam = config.getTeamManager().getRedTeam();
        Team blueTeam = config.getTeamManager().getBlueTeam();
        if (card.getColor() == Color.RED) {
            redTeam.setNbFoundCards(redTeam.getNbFoundCards() + 1);
            // System.out.println("Red card");
            return 0;
        } else if (card.getColor() == Color.BLUE) {
            blueTeam.setNbFoundCards(blueTeam.getNbFoundCards() + 1);
            // System.out.println("Blue card");
            return 0;
        } else if (card.getColor() == Color.WHITE) {
            // System.out.println("White card");
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
