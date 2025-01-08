package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Random;

/**
 * Represents a card in the game with a name and a color.
 */
public class Card {
    @JsonProperty("cardName")
    private String cardName;
    
    @JsonProperty("cardColor")
    private Color cardColor;

    @JsonProperty("found")
    private boolean found;

    public Card(String cardName) {
        this.cardName = cardName;
        //TEMPORAIRE POUR TESTER L'AFFICHAGE
        Random random = new Random();
        Color[] colors = Color.values();
        this.cardColor = colors[random.nextInt(colors.length)];// Default color, neutral card
        this.found = false;
    }

    @JsonCreator
    public Card(
        @JsonProperty("cardName") String cardName,
        @JsonProperty("cardColor") Color cardColor) {
        this.cardName = cardName;
        this.cardColor = cardColor;
    }

    public String getName() {
        return cardName;
    }

    public Color getColor() {
        return cardColor;
    }

    public boolean isFound() {
        return found;
    }

    public void setColor(Color cardColor) {
        this.cardColor = cardColor;
    }

    public void reveal() {
        this.found = true;
    }
}