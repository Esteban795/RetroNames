package linguacrypt;

import linguacrypt.model.*;
import linguacrypt.visitor.DeserializationVisitor;
import linguacrypt.visitor.SerializationVisitor;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationCycleTest {
    private Game game;
    private SerializationVisitor serializationVisitor;
    private DeserializationVisitor deserializationVisitor;
    private static final String TEST_RESOURCES = "src/test/resources/cycle_saves/";

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

        TeamManager teamManager = new TeamManager();

        Team blueTeam = new Team("Blue Team", Color.BLUE);
        blueTeam.addPlayer(new Player("Alice"));
        teamManager.addTeam(blueTeam);

        Team redTeam = new Team("Red Team", Color.RED);
        redTeam.addPlayer(new Player("Bob"));
        teamManager.addTeam(redTeam);

        GameConfiguration.teamManager = teamManager;

        Deck deck = new Deck();

        Card card1 = new Card("Word1", Color.BLUE);
        deck.addCard(card1);
        GameConfiguration.currentDeck = deck;
        game.initGrid();
        game.loadGrid();

        // Serialize
        game.accept(serializationVisitor);

        // Deserialize
        Game loadedGame = deserializationVisitor.loadLatestGame();
        
        // Verify game state
        assertNotNull(loadedGame, "Loaded game should not be null");
        TeamManager loadedTeamManager = GameConfiguration.teamManager;
        assertEquals("Blue Team", loadedTeamManager.getBlueTeam().getTeamName(), "Blue team name should match");
        assertEquals("Red Team", loadedTeamManager.getRedTeam().getTeamName(), "Red team name should match");
        assertEquals(Color.BLUE, loadedTeamManager.getBlueTeam().getTeamColor(), "Blue team color should match");
        assertEquals(Color.RED, loadedTeamManager.getRedTeam().getTeamColor(), "Red team color should match");
        
        // Verify players
        assertEquals("Alice", loadedTeamManager.getBlueTeam().getPlayerList().get(0).getName(), "Blue team player should be Alice");
        assertEquals("Bob", loadedTeamManager.getRedTeam().getPlayerList().get(0).getName(), "Red team player should be Bob");
        
        Deck loadedDeck = GameConfiguration.currentDeck;

        // Verify cards
        assertTrue(loadedDeck.getCardList().size() > 0, "Card list should not be empty");
        assertEquals("Word1", loadedDeck.getCardList().get(0).getCardName(), "First card name should match");
        assertEquals(Color.BLUE, loadedDeck.getCardList().get(0).getCardColor(), "First card color should match");
    }

    @AfterEach
    void cleanup() {
        try {
            FileUtils.deleteDirectory(new File(TEST_RESOURCES));
        } catch (IOException e) {
            System.err.println("Failed to delete test resources directory: " + TEST_RESOURCES);
            e.printStackTrace();
        }
    }
    
}