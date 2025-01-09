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
        game = new Game(); // Creates default config
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

        // Set deck in game config
        game.getConfig().setCurrentDeck(testDeck);
        System.out.println("Initializing grid...");
        game.initGrid();
        System.out.println("Grid initialized with size: " + game.getGrid().size());

        // Print grid content
        for (int i = 0; i < game.getGrid().size(); i++) {
            for (int j = 0; j < game.getGrid().get(i).size(); j++) {
                Card card = game.getGrid().get(i).get(j);
                System.out.printf("Card at (%d,%d): %s%n", i, j,
                        card != null ? card.getName() : "null");
            }
        }
    }

    @Test
    void testGridInitialization() {
        ArrayList<ArrayList<Card>> grid = game.getGrid();
        assertNotNull(grid, "Grid should not be null");
        assertEquals(5, grid.size(), "Grid should have 5 rows");

        for (int i = 0; i < grid.size(); i++) {
            ArrayList<Card> row = grid.get(i);
            assertNotNull(row, "Row " + i + " should not be null");
            assertEquals(5, row.size(), "Row " + i + " should have 5 cards");

            for (int j = 0; j < row.size(); j++) {
                Card card = row.get(j);
                assertNotNull(card, String.format("Card at position (%d,%d) should not be null", i, j));
            }
        }
    }

    @Test
    void testRevealCard() {
        Card card = game.getGrid().get(0).get(0);
        assertNotNull(card, "Test card should not be null");
        assertFalse(card.isFound(), "Card should not be revealed initially");
        game.revealCard(card);
        assertTrue(card.isFound(), "Card should be revealed after revealCard()");
    }

    @Test
    void testCardColors() {
        int redCount = 0;
        int blueCount = 0;

        for (ArrayList<Card> row : game.getGrid()) {
            for (Card card : row) {
                assertNotNull(card, "Card should not be null");
                if (card.getColor() == Color.RED)
                    redCount++;
                if (card.getColor() == Color.BLUE)
                    blueCount++;
            }
        }

        System.out.println("Red cards: " + redCount);
        System.out.println("Blue cards: " + blueCount);
        assertTrue(redCount > 0, "Should have at least one red card");
        assertTrue(blueCount > 0, "Should have at least one blue card");
    }
}