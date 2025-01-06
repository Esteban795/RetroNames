package linguacrypt;

import linguacrypt.model.*;
import linguacrypt.visitor.DeserializationVisitor;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class DeserializationTest {
    private DeserializationVisitor visitor;
    private static final String TEST_RESOURCES = "src/test/resources/saves/";

    @BeforeEach
    void setUp() {
        visitor = new DeserializationVisitor(TEST_RESOURCES);
        new File(TEST_RESOURCES).mkdirs();
    }

    @Test
    void testDeserializeFullGame() {
        Game loaded = visitor.loadGame(TEST_RESOURCES + "game.json");
        assertNotNull(loaded);
        assertNotNull(loaded.getBlueTeam());
        assertNotNull(loaded.getRedTeam());
        assertNotNull(loaded.getCardList());
        assertNotNull(loaded.getGrid());
        assertNotNull(loaded.getConfig());
    }

    @Test
    void testTeamDeserialization() {
        Game loaded = visitor.loadGame(TEST_RESOURCES + "game.json");
        assertNotNull(loaded);
        Team blueTeam = loaded.getBlueTeam();
        assertEquals("Blue Team", blueTeam.getTeamName());
        assertEquals(Color.BLUE, blueTeam.getTeamColor());
    }

}