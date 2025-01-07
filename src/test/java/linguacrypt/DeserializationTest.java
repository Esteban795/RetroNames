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
//         assertNotNull(loaded);
//         assertNotNull(loaded.getLobby());
//         assertNotNull(loaded.getDeck());
//         assertNotNull(loaded.getGrid());
//         assertNotNull(loaded.getConfig());
//     }

//     @Test
//     void testTeamDeserialization() {
//         Game loaded = visitor.loadGame(TEST_RESOURCES + "game.json");
//         assertNotNull(loaded);
//         Team blueTeam = loaded.getLobby().getBlueTeam();
//         assertEquals("Blue Team", blueTeam.getTeamName());
//         assertEquals(Color.BLUE, blueTeam.getTeamColor());
//     }

// }