package linguacrypt;

import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.visitor.SerializationVisitor;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class SerializationTest {
    private Game game;
    private SerializationVisitor visitor;
    private static final String TEST_RESOURCES = "src/test/resources/temp_saves/";

    @BeforeEach
    void setUp() {
        game = new Game();
        visitor = new SerializationVisitor(TEST_RESOURCES);
        new File(TEST_RESOURCES).mkdirs();
    }

    @Test
    void testSerializeEmptyGame() {
        game.accept(visitor);
        String json = visitor.getResult();
        assertNotNull(json);
        assertTrue(json.contains("\"players\":[]"));
    }

    @Test
    void testSerializeWithPlayers() {
        game.addPlayer(new Player("Alice"));
        game.accept(visitor);
        String json = visitor.getResult();
        assertTrue(json.contains("Alice"));
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