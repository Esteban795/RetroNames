//Plus à jour

// package linguacrypt;

// import linguacrypt.model.*;
// import linguacrypt.visitor.DeserializationVisitor;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import java.io.File;
// import static org.junit.jupiter.api.Assertions.*;

// public class DeserializationTest {
//     private DeserializationVisitor visitor;
//     private static final String TEST_RESOURCES = "src/test/resources/saves/";

//     @BeforeEach
//     void setUp() {
//         visitor = new DeserializationVisitor(TEST_RESOURCES);
//         new File(TEST_RESOURCES).mkdirs();
//     }

//     @Test
//     void testDeserializeFullGame() {
//         Game loaded = visitor.loadGame(TEST_RESOURCES + "game.json");
//         GameConfiguration config = loaded.getConfig();
//         assertNotNull(loaded);
//         assertNotNull(config);
//         assertNotNull(config.getCurrentDeck());
//         assertNotNull(loaded.getGrid());
//     }

//     @Test
//     void testTeamDeserialization() {
//         Game loaded = visitor.loadGame(TEST_RESOURCES + "game.json");
//         GameConfiguration config = loaded.getConfig();
//         assertNotNull(loaded);
//         Team blueTeam = config.getTeamManager().getBlueTeam();
//         assertEquals("Blue Team", blueTeam.getName());
//         assertEquals(Color.BLUE, blueTeam.getColor());
//     }

// }