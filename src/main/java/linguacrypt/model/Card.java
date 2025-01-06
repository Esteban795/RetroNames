package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a card in the game with a name and a color.
 */
public class Card {
    @JsonProperty("cardName")
    private String cardName;
    
    @JsonProperty("cardColor")
    private Color cardColor;

    public Card(String cardName) {
        this.cardName = cardName;
        this.cardColor = Color.WHITE; // Default color, neutral card
    }

    @JsonCreator
    public Card(
        @JsonProperty("cardName") String cardName,
        @JsonProperty("cardColor") Color cardColor) {
        this.cardName = cardName;
        this.cardColor = cardColor;
    }

    public String getCardName() {
        return cardName;
    }

    public Color getCardColor() {
        return cardColor;
    }

    public void setCardColor(Color cardColor) {
        this.cardColor = cardColor;
    }
}