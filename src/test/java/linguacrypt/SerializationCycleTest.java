package linguacrypt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.visitor.DeserializationVisitor;
import linguacrypt.visitor.SerializationVisitor;

public class SerializationCycleTest {
    private Game originalGame;
    private SerializationVisitor serializationVisitor;
    private DeserializationVisitor deserializationVisitor;
    private static final String TEST_RESOURCES = "src/test/resources/cycle_test/";

    @BeforeEach
    void setUp() {
        originalGame = new Game();
        serializationVisitor = new SerializationVisitor(TEST_RESOURCES);
        deserializationVisitor = new DeserializationVisitor(TEST_RESOURCES);
        new File(TEST_RESOURCES).mkdirs();
    }

    @Test
    void testFullCycleWithOnePlayer() {
        originalGame.addPlayer(new Player("Alice"));
        originalGame.accept(serializationVisitor);
        
        Game loaded = deserializationVisitor.loadLatestGame();
        assertNotNull(loaded, "Loaded game should not be null");
        assertEquals(1, loaded.getPlayers().size(), "Should have one player");
        assertEquals("Alice", loaded.getPlayers().get(0).getName(), "Player name should be preserved");
    }

    @Test
    void testFullCycleWithMultiplePlayers() {
        originalGame.addPlayer(new Player("Alice"));
        originalGame.addPlayer(new Player("Bob"));
        originalGame.accept(serializationVisitor);
        
        Game loaded = deserializationVisitor.loadLatestGame();
        assertNotNull(loaded, "Loaded game should not be null");
        assertEquals(2, loaded.getPlayers().size(), "Should have two players");
        assertEquals("Alice", loaded.getPlayers().get(0).getName(), "First player name should be preserved");
        assertEquals("Bob", loaded.getPlayers().get(1).getName(), "Second player name should be preserved");
    }

    @Test
    void testFullCycleWithEmptyGame() {
        originalGame.accept(serializationVisitor);
        
        Game loaded = deserializationVisitor.loadLatestGame();
        assertNotNull(loaded, "Loaded game should not be null");
        assertTrue(loaded.getPlayers().isEmpty(), "Should have no players");
    }

    @AfterEach
    void cleanup() {
        try {
            FileUtils.deleteDirectory(new File(TEST_RESOURCES));
        } catch (IOException e) {
            System.err.println("Failed to delete test resources directory.");
            e.printStackTrace();
        }
    }
}