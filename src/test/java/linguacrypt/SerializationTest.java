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
        setupTestData();
    }

    private void setupTestData() {
        // Setup test deck
        String[] testWords = {
                "CHAT", "CHIEN", "OISEAU", "POISSON", "LAPIN",
                "VOITURE", "VELO", "MOTO", "AVION", "TRAIN",
                "POMME", "POIRE", "ORANGE", "BANANE", "FRAISE",
                "MAISON", "JARDIN", "ROUTE", "ARBRE", "FLEUR",
                "SOLEIL", "LUNE", "ETOILE", "NUAGE", "PLUIE"
        };

        Deck testDeck = new Deck("Test Deck");
        for (String word : testWords) {
            testDeck.addCard(new Card(word));
        }
        game.getConfig().setCurrentDeck(testDeck);

        // Setup teams
        Team redTeam = new Team("Red Team", Color.RED);
        redTeam.addPlayer(new Player("Alice"));
        redTeam.addPlayer(new Player("Bob"));

        Team blueTeam = new Team("Blue Team", Color.BLUE);
        blueTeam.addPlayer(new Player("Charlie"));
        blueTeam.addPlayer(new Player("David"));

        game.getConfig().getTeamManager().addTeam(redTeam);
        game.getConfig().getTeamManager().addTeam(blueTeam);

        game.initGrid();
        game.loadGrid();
    }

    @Test
    void testSerializeEmptyGame() {
        Game emptyGame = new Game();
        emptyGame.accept(visitor);
        String json = visitor.getResult();
        assertNotNull(json);
        assertTrue(json.contains("\"grid\""));
        assertTrue(json.contains("\"config\""));
    }

    @Test
    void testSerializeWithTeams() {
        game.accept(visitor);
        String json = visitor.getResult();
        assertTrue(json.contains("\"Red Team\""));
        assertTrue(json.contains("\"Blue Team\""));
        assertTrue(json.contains("\"Alice\""));
        assertTrue(json.contains("\"Charlie\""));
    }

    @Test
    void testSerializeWithCards() {
        game.accept(visitor);
        String json = visitor.getResult();
        assertTrue(json.contains("\"CHAT\""));
        assertTrue(json.contains("\"CHIEN\""));
        assertTrue(json.contains("\"cardColor\""));
    }

    @Test
    void testSerializeWithGrid() {
        game.accept(visitor);
        String json = visitor.getResult();
        assertTrue(json.contains("\"grid\""));
        assertTrue(json.contains("\"cardName\""));
        assertTrue(json.contains("\"found\""));
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(new File(TEST_RESOURCES));
    }
}