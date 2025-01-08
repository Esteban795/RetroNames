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

    @BeforeEach
    void setUp() {
        game = new Game();
        serializationVisitor = new SerializationVisitor();
        deserializationVisitor = new DeserializationVisitor();
    }

    @Test
    void testBasicSerializationCycle() {
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

        // Setup teams and players
        Team redTeam = new Team("Red Team", Color.RED);
        redTeam.addPlayer(new Player("Alice"));
        redTeam.addPlayer(new Player("Bob"));
        
        Team blueTeam = new Team("Blue Team", Color.BLUE);
        blueTeam.addPlayer(new Player("Charlie"));
        blueTeam.addPlayer(new Player("David"));

        game.getConfig().getTeamManager().addTeam(redTeam);
        game.getConfig().getTeamManager().addTeam(blueTeam);

        // Initialize game grid
        game.initGrid();
        game.loadGrid();

        // Serialize
        game.accept(serializationVisitor);

        // Deserialize
        Game loadedGame = deserializationVisitor.loadLatestGame();

        // Verify game state
        assertNotNull(loadedGame, "Loaded game should not be null");
        assertEquals(5, loadedGame.getGrid().size(), "Grid should be 5x5");
        
        // Verify teams
        assertEquals("Red Team", loadedGame.getConfig().getTeamManager().getRedTeam().getName());
        assertEquals("Blue Team", loadedGame.getConfig().getTeamManager().getBlueTeam().getName());
        
        // Verify players
        assertEquals(2, loadedGame.getConfig().getTeamManager().getRedTeam().getPlayerList().size());
        assertEquals(2, loadedGame.getConfig().getTeamManager().getBlueTeam().getPlayerList().size());
        assertEquals("Alice", loadedGame.getConfig().getTeamManager().getRedTeam().getPlayerList().get(0).getName());
        assertEquals("Charlie", loadedGame.getConfig().getTeamManager().getBlueTeam().getPlayerList().get(0).getName());

        // Verify deck
        assertEquals(25, loadedGame.getConfig().getCurrentDeck().getCardList().size());
        assertEquals("CHAT", loadedGame.getConfig().getCurrentDeck().getCardList().get(0).getName());
    }
}