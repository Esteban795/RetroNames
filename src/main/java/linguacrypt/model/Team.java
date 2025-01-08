package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * Represents a team in the game.
 */
public class Team {
    @JsonProperty("playerList")
    private ArrayList<Player> playerList;
    
    @JsonProperty("teamName")
    private String teamName;
    
    @JsonProperty("teamColor")
    private Color teamColor;
    
    @JsonProperty("nbSpy")
    private int nbSpy;

    @JsonProperty("nbFoundCards")
    private int nbFoundCards;

    @JsonCreator
    public Team(
        @JsonProperty("teamName") String teamName,
        @JsonProperty("teamColor") Color teamColor) {
        this.teamName = teamName;
        this.teamColor = teamColor;
        this.playerList = new ArrayList<>();
        this.nbSpy = 0;
        this.nbFoundCards = 3;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    /*
     * Remove a player from the team
     * @param player the player to remove
     * Will probably need some changes to adapt to the player equality check
     */
    public void removePlayer(Player player) {
        playerList.remove(player);
    }

    public void setName(String teamName) {
        this.teamName = teamName;
    }

    public String getName() {
        return teamName;
    }

    public Color getColor() {
        return teamColor;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public int getNbSpy() {
        return nbSpy;
    }

    public void setNbSpy(int nbSpy) {
        this.nbSpy = nbSpy;
    }

    public int getNbFoundCards() {
        return nbFoundCards;
    }

    public void setNbFoundCards(int nbFoundCards) {
        this.nbFoundCards = nbFoundCards;
    }
}