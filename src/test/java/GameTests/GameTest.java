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
    public void potentialMovesReturnsEmptyList() {
        game = new Game(true);
        ArrayList<Spot> moves = game.getPotentialMoves(new Spot(0, 0));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void checkMateReturnsTrue() {
        game = new Game(true);
        game.move(new Spot(4,6), new Spot(4,5));
        game.move(new Spot(5,1), new Spot(5,2));
        game.move(new Spot(5,6), new Spot(5,5));
        game.move(new Spot(6,1), new Spot(6,2));
        game.move(new Spot(5,5), new Spot(5,4));
        game.move(new Spot(6,2), new Spot(6,3));
        boolean checkmate = game.move(new Spot(3,7), new Spot(7,3));
        assertTrue(checkmate);
    }
    
    @Test
    public void checkMateReturnsFalse() {
        game = new Game(true);
        assertTrue(!game.move(new Spot(1,6), new Spot(3, 6)));
    }
    
    @Test
    public void getPotentialMovesCorrect() {
        game = new Game(true);
        ArrayList<Spot> moves = game.getPotentialMoves(new Spot(0, 1));
        assertTrue(moves.size()==1 && moves.get(0).getX()==0 && moves.get(0).getY()==2);
    }
    
}
