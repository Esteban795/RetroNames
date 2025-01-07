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
    public static int gridSize;
    
    @JsonProperty("nbPlayers")
    public static int nbPlayers;
    
    @JsonProperty("maxNbSpy")
    public static int maxNbSpy;
    
    @JsonProperty("maxNbOperative")
    public static int maxNbOperative;
    
    @JsonProperty("limitedTime")
    public static int limitedTime; // -1 if time not limited

    @JsonProperty("currentDeck")
    public static Deck currentDeck;



    public GameConfiguration() {
        // Valeurs par défaut
        gridSize = 5; // 5x5 par défaut
        nbPlayers = 4;
        maxNbSpy = 1;
        maxNbOperative = 1;
        limitedTime = -1;
    }

    @JsonCreator
    public GameConfiguration(
        @JsonProperty("gridSize") int gridSize,
        @JsonProperty("nbPlayers") int nbPlayers,
        @JsonProperty("maxNbSpy") int maxNbSpy,
        @JsonProperty("maxNbOperative") int maxNbOperative,
        @JsonProperty("limitedTime") int limitedTime) {
        this.gridSize = gridSize;
        this.nbPlayers = nbPlayers;
        this.maxNbSpy = maxNbSpy;
        this.maxNbOperative = maxNbOperative;
        this.limitedTime = limitedTime;
    }

    // Getters and Setters
    public int getGridSize() { return gridSize; }
    public void setGridSize(int gridSize) { this.gridSize = gridSize; }
    
    public int getNbPlayers() { return nbPlayers; }
    public void setNbPlayers(int nbPlayers) { this.nbPlayers = nbPlayers; }
    
    public int getMaxNbSpy() { return maxNbSpy; }
    public void setMaxNbSpy(int maxNbSpy) { this.maxNbSpy = maxNbSpy; }
    
    public int getMaxNbOperative() { return maxNbOperative; }
    public void setMaxNbOperative(int maxNbOperative) { this.maxNbOperative = maxNbOperative; }
    
    public int getLimitedTime() { return limitedTime; }
    public void setLimitedTime(int limitedTime) { this.limitedTime = limitedTime; }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}