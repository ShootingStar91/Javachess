package GameTests;

import java.util.ArrayList;
import javachess.Game.Game;
import javachess.Game.Spot;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


/**
 *
 * @author arkangas
 */
public class GameTest {
    
    Game game;
    
    public GameTest() {
        
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        game = new Game(true);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void potentialMovesReturnsNull() {
        game = new Game(true);
        ArrayList<Spot> moves = game.getPotentialMoves(new Spot(0, 0));
        assertTrue(moves.isEmpty());
    }

}
