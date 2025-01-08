package linguacrypt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import linguacrypt.model.*;
import java.util.ArrayList;

public class GameDisplayTest {
    
    private Game game;
    private Deck testDeck;
    
    @BeforeEach
    void setUp() {
        game = new Game();
        testDeck = new Deck();
        
        // Create 25 test cards
        String[] testWords = {
            "CHAT", "CHIEN", "OISEAU", "POISSON", "LAPIN",
            "VOITURE", "VELO", "MOTO", "AVION", "TRAIN",
            "POMME", "POIRE", "ORANGE", "BANANE", "FRAISE",
            "MAISON", "JARDIN", "ROUTE", "ARBRE", "FLEUR",
            "SOLEIL", "LUNE", "ETOILE", "NUAGE", "PLUIE"
        };
        
        for (String word : testWords) {
            testDeck.addCard(new Card(word));
        }
        
        game.setDeck(testDeck);
        game.initGrid();
        game.loadGrid();
    }
    
    @Test
    void testGridInitialization() {
        ArrayList<ArrayList<Card>> grid = game.getGrid();
        assertEquals(5, grid.size());
        for (ArrayList<Card> row : grid) {
            assertEquals(5, row.size());
            assertNotNull(row);
        }
    }
    
    @Test
    void testRevealCard() {
        Card card = game.getGrid().get(0).get(0);
        assertFalse(card.isFound());
        game.revealCard(card);
        assertTrue(card.isFound());
    }
    
    @Test
    void testCardColors() {
        int redCount = 0;
        int blueCount = 0;
        
        for (ArrayList<Card> row : game.getGrid()) {
            for (Card card : row) {
                if (card.getColor() == Color.RED) redCount++;
                if (card.getColor() == Color.BLUE) blueCount++;
            }
        }
        
        assertTrue(redCount > 0);
        assertTrue(blueCount > 0);
    }
}