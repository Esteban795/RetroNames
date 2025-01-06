package linguacrypt.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import linguacrypt.model.*;
import java.io.*;

/**
 * Visiteur pour la sérialisation des objets du jeu en JSON.
 * Utilise Jackson pour la conversion en JSON.
 */
public class SerializationVisitor implements Visitor {
    private final ObjectMapper objectMapper;
    private String jsonResult;
    private final String savePath;
    private static final String LOG_FILE_PATH = "logs/serialization_debug.log";
    private static final String DEFAULT_GAMES_PATH = "src/main/resources/saves/";

    public SerializationVisitor() {
        this(DEFAULT_GAMES_PATH);
    }

    public SerializationVisitor(String savePath) {
        this.objectMapper = new ObjectMapper();
        this.savePath = savePath;
        //log("SerializationVisitor initialized with path: " + savePath);
    }

    @Override
    public void visit(Game game) {
        try {
            jsonResult = objectMapper.writeValueAsString(game);
            //log("Serialized Game object to JSON: " + jsonResult);
            
            String uniqueFilename = getUniqueFilename(savePath, "game", ".json");
            String filePath = savePath + uniqueFilename;
            
            objectMapper.writeValue(new File(filePath), game);
            //log("Game saved successfully to " + filePath);
        } catch (Exception e) {
            //log("Error during serialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Génère un nom de fichier unique pour la sauvegarde sous la forme gameX avec X un nombre.
     */
    private String getUniqueFilename(String basePath, String baseName, String extension) {
        String filename = baseName + extension;
        File file = new File(basePath + filename);
        int count = 1;

        while (file.exists()) {
            filename = baseName + count + extension;
            file = new File(basePath + filename);
            count++;
        }

        //log("Unique filename generated: " + filename);
        return filename;
    }

    /**
     * Fonction de débugging pour écrire dans un fichier les logs.
     */
    private void log(String message) {
        File logDir = new File("logs");
        logDir.mkdirs();

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE_PATH, true)))) {
            out.println(message);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    // Méthodes visit non utilisées actuellement mais requises par l'interface et potentiellement utiles pour des extensions futures
    @Override public void visit(Player player) {}
    @Override public void visit(Deck deck) {}
    @Override public void visit(GameConfiguration config) {}

    public String getResult() {
        return jsonResult;
    }
}