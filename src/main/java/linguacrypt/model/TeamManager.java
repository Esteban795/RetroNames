package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class TeamManager {
    @JsonProperty("blueTeam")
    private Team blueTeam;
    
    @JsonProperty("redTeam")
    private Team redTeam;

    public TeamManager() {
        this.blueTeam = new Team("Blue Team", Color.BLUE);
        this.redTeam = new Team("Red Team", Color.RED);
    }

    @JsonCreator
    public TeamManager(
        @JsonProperty("blueTeam") Team blueTeam,
        @JsonProperty("redTeam") Team redTeam,
        @JsonProperty("teamsList") ArrayList<Team> teamsList) {
        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
    }


    public void addTeam(Team team) {
        if (team == null) return;
        
        if (team.getTeamColor() == Color.BLUE) {
            this.blueTeam = team;
        } else if (team.getTeamColor() == Color.RED) {
            this.redTeam = team;
        }
    }

    public void removeTeam(Team team) {
        if (team == null) return;
        
        if (team.equals(blueTeam)) {
            blueTeam = null;
        } else if (team.equals(redTeam)) {
            redTeam = null;
        }
    }

    public Team getBlueTeam() { return blueTeam; }
    public Team getRedTeam() { return redTeam; }
}