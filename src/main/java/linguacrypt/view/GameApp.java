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
import linguacrypt.model.*;
import linguacrypt.visitor.SerializationVisitor;
import linguacrypt.visitor.DeserializationVisitor;

public class GameApp extends Application {
    private Game game;
    private TextArea textArea;
    private static final String RESOURCES_PATH = "src/main/resources/saves/";

    @Override
    public void start(Stage primaryStage) {
        initializeUI(primaryStage);
        loadOrCreateNewGame();
    }

    private void initializeUI(Stage primaryStage) {
        primaryStage.setTitle("LinguaCrypt Game");
        textArea = new TextArea();
        textArea.setEditable(false);
        
        Button saveButton = new Button("Sauvegarder");
        saveButton.setOnAction(e -> saveGame());
        
        Button loadButton = new Button("Charger");
        loadButton.setOnAction(e -> loadGame(primaryStage));
        
        Button initGameButton = new Button("Nouvelle Partie");
        initGameButton.setOnAction(e -> createNewGame());
        
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(textArea, saveButton, loadButton, initGameButton);
        
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadOrCreateNewGame() {
        DeserializationVisitor deserializationVisitor = new DeserializationVisitor();
        game = deserializationVisitor.loadLatestGame();

        if (game == null) {
            createNewGame();
            saveGame();
        } else {
            displayGameState();
        }
    }

    private void createNewGame() {
        game = new Game();
        
        Team blueTeam = new Team("Blue Team", Color.BLUE);
        Team redTeam = new Team("Red Team", Color.RED);
        
        game.getLobby().addTeam(blueTeam);
        game.getLobby().addTeam(redTeam);
        
        // Add some default cards
        game.addCard(new Card("Word1", Color.BLUE));
        game.addCard(new Card("Word2", Color.RED));
        game.addCard(new Card("Word3", Color.BLACK));
        
        game.initGrid();
        game.loadGrid();
        
        displayGameState();
    }

    private void displayGameState() {
        textArea.clear();
        textArea.appendText("=== État du Jeu ===\n");
        
        Lobby lobby = game.getLobby();
        Team blueTeam = lobby.getBlueTeam();
        Team redTeam = lobby.getRedTeam();
        
        textArea.appendText("\nÉquipes dans le lobby: \n");
        
        textArea.appendText("\nÉquipe Bleue:\n");
        if (blueTeam != null && blueTeam.getPlayerList() != null) {
            for (Player player : blueTeam.getPlayerList()) {
                textArea.appendText("- " + player.getName() + "\n");
            }
        }
        
        textArea.appendText("\nÉquipe Rouge:\n");
        if (redTeam != null && redTeam.getPlayerList() != null) {
            for (Player player : redTeam.getPlayerList()) {
                textArea.appendText("- " + player.getName() + "\n");
            }
        }
        
        textArea.appendText("\nGrille de jeu initialisée.\n");
    }

    private void saveGame() {
        SerializationVisitor serializationVisitor = new SerializationVisitor();
        game.accept(serializationVisitor);
        textArea.appendText("Partie sauvegardée.\n");
    }

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
                displayGameState();
                textArea.appendText("Partie chargée : " + selectedFile.getName() + "\n");
            } else {
                textArea.appendText("Erreur lors du chargement.\n");
            }
        }
    }

    @Override
    public void stop() {
        saveGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}