package linguacrypt.model;

import java.io.File;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonIgnore
    private ImageView cardView;

    public Card(String cardName) {
        this.cardName = cardName;
        //TEMPORAIRE POUR TESTER L'AFFICHAGE
        Random random = new Random();
        Color[] colors = Color.values();
        this.cardColor = colors[random.nextInt(colors.length)];// Default color, neutral card
        this.found = false;
        this.cardUrl = null;
        this.cardView = null;
    }

    public Card(String cardName,String cardUrl) {
        this.cardName = cardName;
        this.cardColor = Color.WHITE;// Default color, neutral card
        this.found = false;
        this.cardUrl = cardUrl;
        createCardView();
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
        createCardView();
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

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
        createCardView();
    }
    
    @JsonIgnore
    public ImageView getCardView() {
        if (cardView != null) {
            return cardView;
        }
        return null;
    }

    private void createCardView() {
        if (cardUrl != null) {
            File file = new File(cardUrl);
            Image img = new Image(file.toURI().toString());
            this.cardView = new ImageView(img);
        } 
    }

    @JsonIgnore
    public boolean isImage() {
        return cardView != null;
    }

    @JsonIgnore
    public boolean isGif() {
        if (cardUrl == null) return false;
        return cardUrl.endsWith(".gif");
    }
}