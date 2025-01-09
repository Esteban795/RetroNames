package linguacrypt;

import java.io.IOException;
import java.net.URL;

// import linguacrypt.model.Card;
// import linguacrypt.model.Deck;
import linguacrypt.scenes.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.model.Model;

public class Main extends Application {

    private Model model;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.model = new Model(true);
        // // Create test deck with 25 cards
        // Deck testDeck = new Deck();
        // String[] testWords = {
        //         "CHAT", "CHIEN", "OISEAU", "POISSON", "LAPIN",
        //         "VOITURE", "VELO", "MOTO", "AVION", "TRAIN",
        //         "POMME", "POIRE", "ORANGE", "BANANE", "FRAISE",
        //         "MAISON", "JARDIN", "ROUTE", "ARBRE", "FLEUR",
        //         "SOLEIL", "LUNE", "ETOILE", "NUAGE", "PLUIE"
        // };

        // for (String word : testWords) {
        //     Card card = new Card(word);
        //     testDeck.addCard(card);
        // }
        // model.getDeckManager().addDeck(testDeck);
        // model.getGame().getConfig().setCurrentDeck(testDeck);
        
        SceneManager sm = SceneManager.getInstance(primaryStage, model);

        // Fake scene that shouldn't be used. It is used to initialize the SceneManager
        // to correct values
        URL fxmlURL = getClass().getResource("/Init.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlURL);

        Parent root = loader.load();
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setScene(scene);

        // Actual Initial Scene
        ManagedScene MenuScene = new MenuScene(sm);
        sm.pushScene(MenuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
