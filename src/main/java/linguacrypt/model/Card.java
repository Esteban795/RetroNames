package linguacrypt.model;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @JsonProperty("cardUrl")
    private String cardUrl;

    public Card(String cardName) {
        this.cardName = cardName;
        this.cardColor = Color.WHITE;// Default color, neutral card
        this.found = false;
        this.cardUrl = null;
    }

    public Card(String cardName,String cardUrl) {
        this.cardName = cardName;
        this.cardColor = Color.WHITE;// Default color, neutral card
        this.found = false;
        this.cardUrl = cardUrl;
    }
    
    @JsonCreator
    public Card(
        @JsonProperty("cardName") String cardName,
        @JsonProperty("cardColor") Color cardColor,
        @JsonProperty("found") boolean found,
        @JsonProperty("cardUrl") String cardUrl){
        this.cardName = cardName;
        this.cardColor = cardColor;
        this.found = found;
        this.cardUrl = cardUrl;
    }

    public String getCardName() {
        return cardName;
    }

    public Color getCardColor() {
        return cardColor;
    }

    public boolean isFound() {
        return found;
    }

    public void setCardColor(Color cardColor) {
        this.cardColor = cardColor;
    }

    public void reveal() {
        this.found = true;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public Node getCardView() {
        if (cardUrl != null) {
            File file = new File(cardUrl);
            Image img = new Image(file.toURI().toString());
            return new ImageView(img);
        }
        return new Label(cardName);
    }
}