package linguacrypt.view;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.visitor.SerializationVisitor;
import linguacrypt.visitor.DeserializationVisitor;

/**
 * Application principale JavaFX pour LinguaCrypt.
 * Gère l'interface utilisateur et les interactions avec le jeu.
 */
public class GameApp extends Application {
    private Game game;
    private TextArea textArea;
    private static final String RESOURCES_PATH = "src/main/resources/saves/";

    /**
     * Démarre l'application javaFX
     */
    @Override
    public void start(Stage primaryStage) {
        initializeUI(primaryStage);
        loadOrCreateNewGame();
    }

    /**
     * Initialise l'interface utilisateur
     */
    private void initializeUI(Stage primaryStage) {
        primaryStage.setTitle("LinguaCrypt Game");
        textArea = new TextArea();
        textArea.setEditable(false);
        
        Button saveButton = new Button("Sauvegarder");
        saveButton.setOnAction(e -> saveGame());
        
        Button loadButton = new Button("Charger");
        loadButton.setOnAction(e -> loadGame(primaryStage));
        
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(textArea, saveButton, loadButton);
        
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Charge la dernière partie ou en crée une nouvelle
     */
    private void loadOrCreateNewGame() {
        DeserializationVisitor deserializationVisitor = new DeserializationVisitor();
        game = deserializationVisitor.loadLatestGame();

        if (game == null) {
            createNewGame();
            saveGame();
        } else {
            textArea.appendText("Partie précédente chargée.\n");
        }
    }

    /**
     * Crée une nouvelle partie avec des joueurs par défaut
     */
    private void createNewGame() {
        game = new Game();
        game.addPlayer(new Player("Alice"));
        game.addPlayer(new Player("Bob"));
        textArea.appendText("Nouvelle partie créée.\n");
    }

    /**
     * Sauvegarde la partie actuelle
     */
    private void saveGame() {
        SerializationVisitor serializationVisitor = new SerializationVisitor();
        game.accept(serializationVisitor);
        textArea.appendText("Partie sauvegardée.\n");
    }

    /**
     * Charge une partie depuis un fichier choisi par l'utilisateur
     */
    private void loadGame(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(RESOURCES_PATH));
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers JSON", "*.json")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            DeserializationVisitor deserializationVisitor = new DeserializationVisitor();
            Game loadedGame = deserializationVisitor.loadGame(selectedFile.getPath());
            
            if (loadedGame != null) {
                game = loadedGame;
                textArea.appendText("Partie chargée : " + selectedFile.getName() + "\n");
            } else {
                textArea.appendText("Erreur lors du chargement.\n");
            }
        }
    }

    @Override
    public void stop() {
        saveGame(); // Sauvegarde automatique à la fermeture
    }

    public static void main(String[] args) {
        launch(args);
    }
}