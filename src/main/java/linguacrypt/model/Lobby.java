package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class Lobby {
    @JsonProperty("blueTeam")
    private Team blueTeam;
    
    @JsonProperty("redTeam")
    private Team redTeam;
    
    @JsonProperty("teamsList")
    private ArrayList<Team> teamsList;

    public Lobby() {
        this.blueTeam = new Team("Blue Team", Color.BLUE);
        this.redTeam = new Team("Red Team", Color.RED);
        this.teamsList = new ArrayList<>();
        this.teamsList.add(blueTeam);
        this.teamsList.add(redTeam);
    }

    @JsonCreator
    public Lobby(
        @JsonProperty("blueTeam") Team blueTeam,
        @JsonProperty("redTeam") Team redTeam,
        @JsonProperty("teamsList") ArrayList<Team> teamsList) {
        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
        this.teamsList = teamsList != null ? teamsList : new ArrayList<>();
    }

    public void addTeam(Team team) {
        if (team.getTeamColor() == Color.BLUE) {
            this.blueTeam = team;
        } else if (team.getTeamColor() == Color.RED) {
            this.redTeam = team;
        }
        this.teamsList.add(team);
    }

    public void removeTeam(Team team) {
        this.teamsList.remove(team);
        if (team.equals(blueTeam)) {
            blueTeam = null;
        } else if (team.equals(redTeam)) {
            redTeam = null;
        }
    }

    // Getters
    public Team getBlueTeam() { return blueTeam; }
    public Team getRedTeam() { return redTeam; }
    public ArrayList<Team> getTeamsList() { return teamsList; }
}