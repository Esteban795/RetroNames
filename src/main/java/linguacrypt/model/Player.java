package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import linguacrypt.visitor.Visitor;

/**
 * Représente un joueur dans le jeu.
 * Stocke les informations du joueur comme son nom ou son rôle.
 * role = 1 si c'est un maître-espion, 0 sinon
 */

public class Player {
    @JsonProperty("name")
    private String name;
    private boolean role;

    public Player(String name, boolean role) { 
        this.name = name;
        this.role = role;
    }
     

    @JsonCreator
    public Player(@JsonProperty("name") String name) {
        this.name = name;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}