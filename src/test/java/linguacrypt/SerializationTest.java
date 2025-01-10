//Plus à jour

// package linguacrypt;

// import linguacrypt.model.*;
// import linguacrypt.visitor.SerializationVisitor;
// import org.apache.commons.io.FileUtils;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;



// import java.io.File;
// import java.io.IOException;
// import java.util.ArrayList;
// import static org.junit.jupiter.api.Assertions.*;

// public class SerializationTest {
//     private Game game;
//     private SerializationVisitor visitor;
//     private static final String TEST_RESOURCES = "src/test/resources/temp_saves/";

//     @BeforeEach
//     void setUp() {
//         game = new Game();
//         visitor = new SerializationVisitor(TEST_RESOURCES);
//         new File(TEST_RESOURCES).mkdirs();

//         // Prepare any new structures in the game
//         if (game.getConfig().getCurrentDeck() == null || game.getConfig().getCurrentDeck().getCardList().isEmpty()) {
//             Deck testDeck = new Deck("TestDeck");
//             testDeck.setCardList(generateTestCards());
//             game.getConfig().setCurrentDeck(testDeck);
//         }

//         // Configure teams
//         Team redTeam = new Team("Red Team", Color.RED);
//         redTeam.addPlayer(new Player("Alice"));
//         Team blueTeam = new Team("Blue Team", Color.BLUE);
//         blueTeam.addPlayer(new Player("Charlie"));
//         game.getConfig().getTeamManager().addTeam(redTeam);
//         game.getConfig().getTeamManager().addTeam(blueTeam);

//         // Initialize and load the grid
//         game.initGrid();
//         //game.loadGrid();
//     }

//     private ArrayList<Card> generateTestCards() {
//         ArrayList<Card> cards = new ArrayList<>();
//         String[] words = {
//             "CHAT", "CHIEN", "OISEAU", "POISSON", "LAPIN",
//             "CAR", "BIKE", "PLANE", "TRAIN", "BOAT",
//             "APPLE", "PEAR", "ORANGE", "BANANA", "STRAWBERRY",
//             "HOUSE", "GARDEN", "ROAD", "TREE", "FLOWER",
//             "SUN", "MOON", "STAR", "CLOUD", "RAIN"
//         };
//         for (String word : words) {
//             cards.add(new Card(word));
//         }
//         return cards;
//     }

//     @Test
//     void testSerializeUpdatedGameStructure() {
//         game.accept(visitor);
//         String json = visitor.getResult();
//         assertTrue(json.contains("Red Team"));
//         assertTrue(json.contains("Blue Team"));
//         assertTrue(json.contains("\"grid\""));
//     }

//     @Test
//     void testSerializeEmptyGame() {
//         Game emptyGame = new Game();
//         emptyGame.accept(visitor);
//         String json = visitor.getResult();
//         assertNotNull(json);
//         assertTrue(json.contains("\"grid\""));
//         assertTrue(json.contains("\"config\""));
//     }

//     @Test
//     void testSerializeWithTeams() {
//         game.accept(visitor);
//         String json = visitor.getResult();
//         assertTrue(json.contains("Red Team"));
//         assertTrue(json.contains("Blue Team"));
//         assertTrue(json.contains("Alice"));
//         assertTrue(json.contains("Charlie"));
//     }

//     @Test
//     void testSerializeWithGrid() {
//         game.accept(visitor);
//         String json = visitor.getResult();
//         assertTrue(json.contains("CHAT"));
//         assertTrue(json.contains("grid"));
//         assertTrue(json.contains("found"));
//     }

//     @AfterEach
//     void tearDown() throws IOException {
//         FileUtils.deleteDirectory(new File(TEST_RESOURCES));
//     }
// }