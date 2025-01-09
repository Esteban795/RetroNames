package linguacrypt.model;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * Represents a card in the game with a name and a color.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
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
        //TEMPORAIRE POUR TESTER L'AFFICHAGE
        Random random = new Random();
        Color[] colors = Color.values();
        this.cardColor = colors[random.nextInt(colors.length)];// Default color, neutral card
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

    @JsonProperty("cardName")
    public String getName() {
        return cardName;
    }

    @JsonIgnore

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