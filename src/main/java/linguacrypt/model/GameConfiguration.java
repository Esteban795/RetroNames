package linguacrypt.model;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameConfiguration implements Visitable {
    // Vos attributs avec @JsonProperty
    
    public GameConfiguration() {
    }

    // @JsonCreator
    // public GameConfiguration(/* vos */) {
    //     // Initialisation
    // }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}