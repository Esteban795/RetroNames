package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameStatistics {

    @JsonProperty("blueTeamStats")
    private int[] blueTeamStats;

    @JsonProperty("redTeamStats")
    private int[] redTeamStats;

    @JsonProperty("blueTeamAvgTimeToAnswer")
    private double blueTeamAvgTimeToAnswer;

    @JsonProperty("redTeamAvgTimeToAnswer")
    private double redTeamAvgTimeToAnswer;

    @JsonProperty("nbRounds")
    private int nbRounds;

    public GameStatistics() {
        this.blueTeamStats = new int[4];
        this.redTeamStats = new int[4];
        this.blueTeamAvgTimeToAnswer = 0;
        this.redTeamAvgTimeToAnswer = 0;
        this.nbRounds = 0;
    }

    public void updateStats(boolean redTeam, Color cardColor) {
        int[] teamStats = redTeam ? redTeamStats : blueTeamStats;
        switch (cardColor) {
            case BLUE:
                teamStats[0]++;
                break;
            case RED:
                teamStats[1]++;
                break;
            case BLACK:
                teamStats[2]++;
                break;
            case WHITE:
                teamStats[3]++;
                break;
        }
    }

    public void updateAvgTimeToAnswer(boolean redTeam, double time) {
        if (redTeam) {
            redTeamAvgTimeToAnswer = (redTeamAvgTimeToAnswer * nbRounds + time) / (nbRounds + 1);
        } else {
            blueTeamAvgTimeToAnswer = (blueTeamAvgTimeToAnswer * nbRounds + time) / (nbRounds + 1);
        }
    }

    public void incrementNbRounds() {
        nbRounds++;
    }

    public int[] getBlueTeamStats() {
        return blueTeamStats;
    }

    public int[] getRedTeamStats() {
        return redTeamStats;
    }

    public double getBlueTeamAvgTimeToAnswer() {
        return blueTeamAvgTimeToAnswer;
    }

    public double getRedTeamAvgTimeToAnswer() {
        return redTeamAvgTimeToAnswer;
    }

    public int getNbRounds() {
        return nbRounds;
    }
}
