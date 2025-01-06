package linguacrypt.model;

import linguacrypt.visitor.Visitable;
import linguacrypt.visitor.Visitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Deck implements Visitable {
    // Vos attributs avec @JsonProperty

    public Deck() {
    }

    // @JsonCreator
    // public Deck(/* vos paramètres avec @JsonProperty */) {
    //     // Initialisation
    // }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}