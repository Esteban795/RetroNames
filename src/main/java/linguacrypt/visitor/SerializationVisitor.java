package linguacrypt.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;

import linguacrypt.model.*;
import java.io.*;


public class SerializationVisitor implements Visitor {
    private final ObjectMapper objectMapper;
    private String jsonResult;
    private final String savePath;
    private static final String LOG_FILE_PATH = "logs/serialization.log";
    private static final String DEFAULT_GAMES_PATH = "src/main/resources/saves/";

    public SerializationVisitor() {
        this(DEFAULT_GAMES_PATH);
    }

    public SerializationVisitor(String savePath) {
        this.objectMapper = new ObjectMapper();
        // Configurer l'objectMapper pour gérer la visibilité des champs
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.savePath = savePath;
    }

    @Override
    public void visit(Game game) {
        try {
            jsonResult = objectMapper.writeValueAsString(game);
            String uniqueFilename = getUniqueFilename(savePath, "game", ".json");
            String filePath = savePath + uniqueFilename;
            objectMapper.writeValue(new File(filePath), game);
        } catch (Exception e) {
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
    @Override public void visit(DeckManager deckManager) {}
    @Override public void visit(GameConfiguration config) {}

    public String getResult() {
        return jsonResult;
    }
}