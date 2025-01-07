package linguacrypt;

import linguacrypt.model.*;
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
        assertTrue(json.contains("\"blueTeam\""));
        assertTrue(json.contains("\"redTeam\""));
    }

    @Test
    void testSerializeWithTeams() {
        TeamManager lobby = game.getConfig().getTeamManager();

        Team blueTeam = new Team("Blue Team", Color.BLUE);
        blueTeam.addPlayer(new Player("Alice"));
        lobby.addTeam(blueTeam);

        Team redTeam = new Team("Red Team", Color.RED);
        redTeam.addPlayer(new Player("Bob"));
        lobby.addTeam(redTeam);

        game.accept(visitor);
        String json = visitor.getResult();
        assertTrue(json.contains("Alice"));
        assertTrue(json.contains("Bob"));
        assertTrue(json.contains("BLUE"));
        assertTrue(json.contains("RED"));
    }

    @Test
    void testSerializeWithCards() {
        Card card1 = new Card("Word1", Color.BLUE);
        Card card2 = new Card("Word2", Color.RED);
        game.getConfig().getCurrentDeck().addCard(card1);
        game.getConfig().getCurrentDeck().addCard(card2);
        game.initGrid();
        game.loadGrid();

        game.accept(visitor);
        String json = visitor.getResult();
        assertTrue(json.contains("Word1"));
        assertTrue(json.contains("Word2"));
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