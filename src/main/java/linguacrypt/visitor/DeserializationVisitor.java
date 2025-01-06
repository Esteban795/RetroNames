package linguacrypt.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import linguacrypt.model.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class DeserializationVisitor implements Visitor {
    private ObjectMapper objectMapper;
    private Game game;
    private static final String LOG_FILE_PATH = "logs/deserialization_debug.log";
    private static final String DEFAULT_GAMES_PATH = "src/main/resources/saves/";
    private final String savePath;

    public DeserializationVisitor() {
        this(DEFAULT_GAMES_PATH);
    }

    public DeserializationVisitor(String savePath) {
        objectMapper = new ObjectMapper();
        this.savePath = savePath;
        log("DeserializationVisitor initialized.");
    }


    public Game loadLatestGame() {
        File directory = new File(savePath);
        File[] files = directory.listFiles((dir, name) -> name.startsWith("game") && name.endsWith(".json"));
        
        if (files == null || files.length == 0) {
            log("No saved games found");
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

    public Game loadGame(String filePath) {
        try {
            File file = new File(filePath);
            game = objectMapper.readValue(file, Game.class);
            log("Successfully loaded game from file: " + filePath);
            return game;
        } catch (Exception e) {
            log("Error during deserialization: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void log(String message) {
        String timestamp = LocalDateTime.now().toString();
        String logMessage = timestamp + " - " + message + "\n";
        
        try {
            File logFile = new File(LOG_FILE_PATH);
            logFile.getParentFile().mkdirs();
            
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(logMessage);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Minimal required visit methods - empty since Jackson handles deserialization
    @Override public void visit(Game game) {}
    @Override public void visit(Player player) {}
    @Override public void visit(Deck deck) {}
    @Override public void visit(GameConfiguration config) {}
}