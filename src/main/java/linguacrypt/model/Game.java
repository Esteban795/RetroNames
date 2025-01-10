package linguacrypt.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;
/**
 * Représente une partie de LinguaCrypt. Cette classe est le point central du
 * jeu, gérant : - La grille de jeu (matrice de cartes) - La configuration de la
 * partie (GameConfiguration)
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
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

    @JsonProperty("gamemode")
    private boolean gamemode; // true = duo, false = multi

    @JsonProperty("currentHint")
    private String currentHint;

    @JsonProperty("remainingGuesses")
    private int remainingGuesses;

    @JsonProperty("bonusGuess")
    private int bonusGuess;

    /**
     * Constructeur par défaut. Initialise une nouvelle partie avec : - Une
     * grille vide - Une configuration par défaut
     * currentTeam = true --> red team is playing 
     */
    public Game() {
        this.grid = new ArrayList<>();
        this.config = new GameConfiguration();
        this.stats = new GameStatistics();
        this.nbTurn = 0;
        this.currentTeam = true;
        this.hasStarted = false;
        this.gamemode = false;
        this.currentHint = "";
        this.remainingGuesses = 0;
        this.bonusGuess = 1;
    }

    /**
     * Constructeur pour la désérialisation JSON.
     *
     * @param grid La grille de jeu
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
            @JsonProperty("hasStarted") boolean hasStarted,
            @JsonProperty("gamemode") boolean gamemode,
            @JsonProperty("currentHint") String currentHint,
            @JsonProperty("remainingGuesses") int remainingGuesses,
            @JsonProperty("bonusGuess") int bonusGuess){
        this.grid = grid;
        this.config = config;
        this.nbTurn = nbTurn;
        this.currentTeam = currentTeam;
        this.stats = stats;
        this.hasStarted = hasStarted;
        this.gamemode = gamemode;
        this.currentHint = currentHint;
        this.remainingGuesses = remainingGuesses;
        this.bonusGuess = bonusGuess;
    }

    private void fillRange(List<Card> cards, int start, int end, Color color) {
        for (int i = start; i < end; i++) {
            cards.get(i).setColor(color);
        }
    }

/**
     * 
     * Initialise une nouvelle grille vide avec la taille définie dans la
     * configuration. La grille est une matrice carrée (ex: 5x5).
     * Charge les cartes du deck dans la grille. Les cartes sont placées
     * séquentiellement dans la grille, de gauche à droite et de haut en bas.
     * Les cartes sont mélangées avant d'être placées dans la grille.
     */
    public void initGrid() {
        Deck deck = config.getCurrentDeck();
        if (grid == null || deck == null) {
            System.err.println("Grid or deck is null");
            return;
        }
        int gridSize = config.getGridSize();

        ArrayList<Card> cards = deck.deepCopyCards();

        // Shuffle the cards to change order
        Collections.shuffle(cards);

        // Select the correct number of cards
        ArrayList<Card> selectedCards = new ArrayList<>(cards.subList(0, gridSize * gridSize));
        int step = gridSize * gridSize / 3;

        fillRange(selectedCards, 0, step + 1, Color.RED);
        fillRange(selectedCards, step + 1, 2 * step + 1, Color.BLUE);
        fillRange(selectedCards, 2 * (step + 1) + 1, 3 * step - 1, Color.WHITE);

        selectedCards.get(gridSize * gridSize - 1).setColor(Color.BLACK);

        Collections.shuffle(selectedCards);

        setGrid(selectedCards);
        hasStarted = true;
    }


    public void switchTeam() {
        currentTeam = !currentTeam;
    }
 
    public boolean getBooleanCurrentTeam() {
        return currentTeam;
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
            System.out.println("Red team found " + redTeam.getNbFoundCards() + " cards");
            return 0;
        } else if (card.getColor() == Color.BLUE) {
            blueTeam.setNbFoundCards(blueTeam.getNbFoundCards() + 1);
            return 0;
        } else if (card.getColor() == Color.WHITE) {
            return 0;
        } else {
            return 1;
        }
        
    }

    public boolean hasBlueTeamWon() {
        return config.getTeamManager().getBlueTeam().getNbFoundCards() == config.getNbCardsGoal();
    }

    public boolean hasRedTeamWon() {
        return config.getTeamManager().getRedTeam().getNbFoundCards() == config.getNbCardsGoal() + 1;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    // Getters and Setters
    public ArrayList<ArrayList<Card>> getGrid() {
        return grid;
    }

    public GameConfiguration getConfig() {
        return config;
    }

    @JsonIgnore
    public Team getCurrentTeam() {
        return (currentTeam ? config.getTeamManager().getRedTeam() : config.getTeamManager().getBlueTeam());
    }

    public Team getOppositeTeam() {
        return (currentTeam ? config.getTeamManager().getBlueTeam() : config.getTeamManager().getRedTeam());
    }

    @JsonIgnore
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

    public boolean hasStarted() {
        return hasStarted;
    }

    @JsonIgnore
    public boolean isDuo() {
        return gamemode;
    }

    public String getCurrentHint() {
        return currentHint;
    }

    public int getRemainingGuesses() {
        return remainingGuesses;
    }

    public int getBonusGuess() {
        return bonusGuess;
    }

    public int setRemainingGuesses(int remainingGuesses) {
        this.remainingGuesses = remainingGuesses;
        return this.remainingGuesses;
    }

    public int setBonusGuess(int bonusGuess) {
        this.bonusGuess = bonusGuess;
        return this.bonusGuess;
    }

    public void setCurrentHint(String currentHint) {
        this.currentHint = currentHint;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }
}
