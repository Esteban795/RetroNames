// src/main/java/linguacrypt/visitor/SerializationVisitor.java
package linguacrypt.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import linguacrypt.model.Deck;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SerializationVisitor implements Visitor {
    private ObjectMapper objectMapper;
    private String jsonResult;
    private static final String LOG_FILE_PATH = "logs/serialization_debug.log";
    private static final String DEFAULT_GAMES_PATH = "src/main/resources/saves/";
    private final String savePath;

    public SerializationVisitor() {
        this(DEFAULT_GAMES_PATH);
    }

    public SerializationVisitor(String savePath) {
        this.objectMapper = new ObjectMapper();
        this.savePath = savePath;
        log("SerializationVisitor initialized with path: " + savePath);
    }

    @Override
    public void visit(Game game) {
        try {
            jsonResult = objectMapper.writeValueAsString(game);
            log("Serialized Game object to JSON: " + jsonResult);

            String uniqueFilename = getUniqueFilename(savePath, "game", ".json");
            String filePath = savePath + uniqueFilename;
            
            objectMapper.writeValue(new File(filePath), game);
            log("Game saved successfully to " + filePath);
        } catch (Exception e) {
            log("Error during serialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getUniqueFilename(String basePath, String baseName, String extension) {
        String filename = baseName + extension;
        File file = new File(basePath + filename);
        int count = 1;

        // Check if the file already exists
        while (file.exists()) {
            filename = baseName + count + extension;
            file = new File(basePath + filename);
            count++;
        }

        log("Unique filename generated: " + filename);
        return filename;
    }

        /**
     * Méthode pour écrire des messages de log dans le fichier de log.
     *
     * @param message Le message à logger.
     */
    private void log(String message) {
        // Assurez-vous que le répertoire 'logs' existe
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        try (FileWriter fw = new FileWriter(LOG_FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(message);
        } catch (IOException e) {
            // En cas d'échec d'écriture dans le fichier de log, afficher l'erreur dans la console
            System.err.println("Failed to write to log file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    //Si on a besoin un jour (dieu nous garde) de sérialiser les autres objets
    @Override public void visit(Player player) {}
    @Override public void visit(Deck deck) {}
    @Override public void visit(GameConfiguration config) {}

    public String getResult() {
        return jsonResult;
    }
}