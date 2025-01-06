package linguacrypt;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import linguacrypt.model.Game;
import linguacrypt.visitor.DeserializationVisitor;

public class DeserializationTest {
    private DeserializationVisitor visitor;
    private static final String TEST_RESOURCES = "src/test/resources/saves/";

    @BeforeEach
    void setUp() {
        visitor = new DeserializationVisitor(TEST_RESOURCES);
    }

    @Test
    void testDeserializeAllSavedGames() {
        File directory = new File(TEST_RESOURCES);
        File[] files = directory.listFiles((dir, name) -> name.startsWith("game") && name.endsWith(".json"));
        
        assertNotNull(files, "Le répertoire de test devrait exister");
        assertTrue(files.length > 0, "Il devrait y avoir au moins un fichier de sauvegarde");

        for (File file : files) {
            Game loaded = visitor.loadGame(file.getPath());
            assertNotNull(loaded, "Le jeu chargé depuis " + file.getName() + " ne devrait pas être null");
            assertNotNull(loaded.getPlayers(), "La liste des joueurs ne devrait pas être null");
        }
    }

    @Test
    void testDeserializeGameWithPlayer() {
        Game loaded = visitor.loadGame(TEST_RESOURCES + "game.json");
        assertNotNull(loaded, "Le jeu chargé ne devrait pas être null");
        assertEquals(1, loaded.getPlayers().size(), "Le jeu devrait avoir un joueur");
        assertEquals("Alice", loaded.getPlayers().get(0).getName(), "Le nom du joueur devrait être Alice");
    }

    @Test
    void testDeserializeEmptyGame() {
        Game loaded = visitor.loadGame(TEST_RESOURCES + "game1.json");
        assertNotNull(loaded, "Le jeu vide chargé ne devrait pas être null");
        assertTrue(loaded.getPlayers().isEmpty(), "La liste des joueurs devrait être vide");
    }

    @Test
    void testLoadLatestGame() {
        Game latest = visitor.loadLatestGame();
        assertNotNull(latest, "Le dernier jeu sauvegardé ne devrait pas être null");
    }

    @Test
    void testDeserializeInvalidFile() {
        Game loaded = visitor.loadGame("invalid_file.json");
        assertNull(loaded, "Le chargement d'un fichier invalide devrait retourner null");
    }
}