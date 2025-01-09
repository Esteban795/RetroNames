package linguacrypt.model;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration d'une partie de LinguaCrypt.
 * Gère les paramètres comme la taille de la grille, nombre de joueurs, etc.
 */

public class GameConfiguration implements Visitable {
    @JsonProperty("gridSize")
    private int gridSize;

    @JsonProperty("nbPlayers")
    private int nbPlayers;

    @JsonProperty("maxNbSpy")
    private int maxNbSpy;

    @JsonProperty("maxNbOperative")
    private int maxNbOperative;

    @JsonProperty("limitedTime")
    private int limitedTime; // -1 if time not limited

    @JsonProperty("nbCardsGoal")
    private int nbCardsGoal;

    @JsonProperty("firstTeam")
    private boolean firstTeam; // Utile pour savoir quelle équipe commence, et donc le nombre de carte à
                               // deviner

    @JsonProperty("currentDeck")
    private Deck currentDeck;

    @JsonProperty("teamManager")
    private TeamManager teamManager;

    public GameConfiguration() {
        // Valeurs par défaut
        gridSize = 5; // 5x5 par défaut
        nbPlayers = 4;
        maxNbSpy = 1;
        maxNbOperative = 1;
        limitedTime = -1;
        this.nbCardsGoal = 9;
        this.firstTeam = true;
        currentDeck = new Deck();
        teamManager = new TeamManager();
    }

    @JsonCreator
    public GameConfiguration(
            @JsonProperty("gridSize") int gridSize,
            @JsonProperty("nbPlayers") int nbPlayers,
            @JsonProperty("maxNbSpy") int maxNbSpy,
            @JsonProperty("maxNbOperative") int maxNbOperative,
            @JsonProperty("limitedTime") int limitedTime,
            @JsonProperty("currentDeck") Deck currentDeck,
            @JsonProperty("teamManager") TeamManager teamManager,
            @JsonProperty("nbCardsGoal") int nbCardsGoal,
            @JsonProperty("firstTeam") boolean firstTeam) {
        this.gridSize = gridSize;
        this.nbPlayers = nbPlayers;
        this.maxNbSpy = maxNbSpy;
        this.maxNbOperative = maxNbOperative;
        this.limitedTime = limitedTime;
        this.nbCardsGoal = nbCardsGoal;
        this.firstTeam = firstTeam;
        this.currentDeck = currentDeck;
        this.teamManager = teamManager;
    }

    // Getters and Setters
    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getNbPlayers() {
        return nbPlayers;
    }

    public void setNbPlayers(int nbPlayers) {
        this.nbPlayers = nbPlayers;
    }

    public int getMaxNbSpy() {
        return maxNbSpy;
    }

    public void setMaxNbSpy(int maxNbSpy) {
        this.maxNbSpy = maxNbSpy;
    }

    public int getMaxNbOperative() {
        return maxNbOperative;
    }

    public void setMaxNbOperative(int maxNbOperative) {
        this.maxNbOperative = maxNbOperative;
    }

    public int getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(int limitedTime) {
        this.limitedTime = limitedTime;
    }

    public int getNbCardsGoal() {
        return nbCardsGoal;
    }

    public void setNbCardsGoal(int nbCardsGoal) {
        this.nbCardsGoal = nbCardsGoal;
    }

    public boolean isFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(boolean firstTeam) {
        this.firstTeam = firstTeam;
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(Deck currentDeck) {
        this.currentDeck = currentDeck;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public void setTeamManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}