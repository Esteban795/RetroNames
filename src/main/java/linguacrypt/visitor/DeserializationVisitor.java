package linguacrypt.visitor;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import linguacrypt.model.*;
import java.io.*;
import java.time.LocalDateTime;

/**
 * Visiteur pour la désérialisation des objets du jeu depuis JSON.
 * Utilise Jackson pour la conversion depuis JSON.
 */
public class DeserializationVisitor implements Visitor {
    private final ObjectMapper objectMapper;
    private Game game;
    private static final String LOG_FILE_PATH = "logs/deserialization.log";
    private static final String DEFAULT_GAMES_PATH = "src/main/resources/saves/";
    private final String savePath;

    public DeserializationVisitor() {
        this(DEFAULT_GAMES_PATH);
    }

    public DeserializationVisitor(String savePath) {
        this.objectMapper = new ObjectMapper();
        // Configurer l'objectMapper pour gérer la visibilité des champs
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.savePath = savePath;
    }

    /**
     * Charge la dernière partie sauvegardée.
     * @return La dernière partie sauvegardée ou null si aucune sauvegarde n'existe
     */
    public Game loadLatestGame() {
        File directory = new File(savePath);
        File[] files = directory.listFiles((dir, name) -> name.startsWith("game") && name.endsWith(".json"));
        
        if (files == null || files.length == 0) {
            //log("No saved games found");
            return null;
        }

        File latestGame = files[0];
        for (File file : files) {
            if (file.lastModified() > latestGame.lastModified()) {
                latestGame = file;
            }
        }

        return loadGame(latestGame.getPath());
    }

    /**
     * Charge une partie depuis un fichier spécifié.
     * @param filePath Chemin du fichier à charger
     * @return La partie chargée ou null en cas d'erreur
     */
    public Game loadGame(String filePath) {
        try {
            game = objectMapper.readValue(new File(filePath), Game.class);
            return game;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Fonction de débugging pour écrire dans un fichier les logs.
     */
    private void log(String message) {
        String timestamp = LocalDateTime.now().toString();
        String logMessage = timestamp + " - " + message + "\n";
        
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE_PATH, true)))) {
            new File(LOG_FILE_PATH).getParentFile().mkdirs();
            out.write(logMessage);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    // Méthodes visit non utilisées actuellement mais requises par l'interface et potentiellement utiles pour des extensions futures
    @Override public void visit(Game game) {}
    @Override public void visit(Player player) {}
    @Override public void visit(Deck deck) {}
    @Override public void visit(DeckManager deckManager) {}
    @Override public void visit(GameConfiguration config) {}
}