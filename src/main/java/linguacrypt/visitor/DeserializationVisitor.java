package linguacrypt.visitor;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import linguacrypt.model.*;
import java.io.*;
import java.time.LocalDateTime;
import linguacrypt.exception.CorruptedSaveException;

/**
 * Visiteur pour la désérialisation des objets du jeu depuis JSON.
 * Utilise Jackson pour la conversion depuis JSON.
 */
public class DeserializationVisitor implements Visitor {
    private final ObjectMapper objectMapper;
    private Game game;
    private static final String LOG_FILE_PATH = "logs/deserialization.log";
    private static final String DEFAULT_GAMES_PATH = "src/main/resources/saves/";
    private final String loadPath;

    public DeserializationVisitor() {
        this(DEFAULT_GAMES_PATH);
    }

    public DeserializationVisitor(String loadPath) {
        this.objectMapper = new ObjectMapper();
        // Configurer l'objectMapper pour gérer la visibilité des champs
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.loadPath = loadPath;
    }

    /**
     * Charge la dernière partie sauvegardée.
     * @return La dernière partie sauvegardée ou null si aucune sauvegarde n'existe
     */
    public Game loadLatestGame() {
        File directory = new File(loadPath);
        File[] files = directory.listFiles((dir, name) -> name.startsWith("game") && name.endsWith(".json"));
        
        if (files == null || files.length == 0) {
            log("No saved games found");
            return null;
        }

        File latestGame = files[0];
        for (File file : files) {
            if (file.lastModified() > latestGame.lastModified()) {
                log("Found a newer saved game: " + file.getName());
                latestGame = file;
            }
        }
        if( latestGame == null) {
            log("No saved games found");
            return null;
        }
        System.out.println("Chemin du fichier trouvé : " + latestGame.getPath());
        return loadGame(latestGame.getPath());
    }

    /**
     * Charge une partie depuis un fichier spécifié.
     * @param filePath Chemin du fichier à charger
     * @return La partie chargée ou null en cas d'erreur
     */
    public Game loadGame(String filePath) {
        try {
            System.out.println("On charge : " + filePath);
            game = objectMapper.readValue(new File(filePath), Game.class);
            if(game == null || game.getGrid().isEmpty()) {
                if(game == null || game.getGrid() == null || game.getGrid().isEmpty()) {
                    String errorMsg = "Corrupted save file: " + filePath + " - Missing or invalid grid data";
                    log(errorMsg);
                    throw new CorruptedSaveException(errorMsg);
                }
            }
            return game;
            } catch (IOException e) {
                throw new CorruptedSaveException("Failed to read save file: " + e.getMessage());
            } catch (Exception e) {
                throw new CorruptedSaveException("Unexpected error loading save: " + e.getMessage());
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


    public DeckManager loadDeckManager(String filename) {
        try {
            File file = new File(loadPath + filename);
            return objectMapper.readValue(file, DeckManager.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthodes visit non utilisées actuellement mais requises par l'interface et potentiellement utiles pour des extensions futures
    @Override public void visit(Game game) {}
    @Override public void visit(Player player) {}
    @Override public void visit(Deck deck) {}
    @Override public void visit(DeckManager deckManager) {}
    @Override public void visit(GameConfiguration config) {}
}