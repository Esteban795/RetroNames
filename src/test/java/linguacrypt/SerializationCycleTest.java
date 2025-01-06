package linguacrypt;

import linguacrypt.model.*;
import linguacrypt.visitor.DeserializationVisitor;
import linguacrypt.visitor.SerializationVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class SerializationCycleTest {
    private Game game;
    private SerializationVisitor serializationVisitor;
    private DeserializationVisitor deserializationVisitor;
    private static final String TEST_RESOURCES = "src/test/resources/saves/";

    @BeforeEach
    void setUp() {
        game = new Game();
        serializationVisitor = new SerializationVisitor(TEST_RESOURCES);
        deserializationVisitor = new DeserializationVisitor(TEST_RESOURCES);
        new File(TEST_RESOURCES).mkdirs();
    }

    @Test
    void testFullCycle() {
        // Setup game
        Team blueTeam = new Team("Blue Team", Color.BLUE);
        blueTeam.addPlayer(new Player("Alice"));
        game.addTeam(blueTeam);

        Team redTeam = new Team("Red Team", Color.RED);
        redTeam.addPlayer(new Player("Bob"));
        game.addTeam(redTeam);

        Card card1 = new Card("Word1", Color.BLUE);
        game.addCard(card1);
        game.initGrid();
        game.loadGrid();

        // Serialize
        game.accept(serializationVisitor);

        // Deserialize
        Game loadedGame = deserializationVisitor.loadLatestGame();
        
        // Verify game state
        assertNotNull(loadedGame, "Loaded game should not be null");
        assertEquals("Blue Team", loadedGame.getBlueTeam().getTeamName(), "Blue team name should match");
        assertEquals("Red Team", loadedGame.getRedTeam().getTeamName(), "Red team name should match");
        assertEquals(Color.BLUE, loadedGame.getBlueTeam().getTeamColor(), "Blue team color should match");
        assertEquals(Color.RED, loadedGame.getRedTeam().getTeamColor(), "Red team color should match");
        
        // Verify players
        assertEquals("Alice", loadedGame.getBlueTeam().getPlayerList().get(0).getName(), "Blue team player should be Alice");
        assertEquals("Bob", loadedGame.getRedTeam().getPlayerList().get(0).getName(), "Red team player should be Bob");
        
        // Verify cards
        assertTrue(loadedGame.getCardList().getCardList().size() > 0, "Card list should not be empty");
        assertEquals("Word1", loadedGame.getCardList().getCardList().get(0).getCardName(), "First card name should match");
        assertEquals(Color.BLUE, loadedGame.getCardList().getCardList().get(0).getCardColor(), "First card color should match");
    }
}