package gametest;

import java.util.ArrayList;
import javachess.game.Game;
import javachess.game.Phase;
import javachess.game.Spot;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;


/**
 *
 * @author arkangas
 */
public class GameTest {
    
    Game game;
    
    public GameTest() {
        
    }
    
    @Before
    public void setUp() {
        game = new Game(false);
    }
    

    @Test
    public void potentialMovesReturnsEmptyList() {
        ArrayList<Spot> moves = game.getMoves(new Spot(0, 0));
        assertTrue(moves.isEmpty());
    }

    @Test
    public void checkMateReturn() {
        game.move(new Spot(4,6), new Spot(4,5));
        game.move(new Spot(5,1), new Spot(5,2));
        game.move(new Spot(5,6), new Spot(5,5));
        game.move(new Spot(6,1), new Spot(6,2));
        game.move(new Spot(5,5), new Spot(5,4));
        game.move(new Spot(6,2), new Spot(6,3));
        Phase checkmate = game.move(new Spot(3,7), new Spot(7,3));
        assertTrue(checkmate == Phase.CHECKMATE);
    }
    
    @Test
    public void moveReturnsPlay() {
        assertTrue(game.move(new Spot(1,6), new Spot(3, 6)) == Phase.PLAY);
    }
    
    @Test
    public void getPotentialMovesCorrect() {
        ArrayList<Spot> moves = game.getMoves(new Spot(0, 1));
        assertTrue(moves.size() == 1 && moves.get(0).getX() == 0 && 
                moves.get(0).getY() == 2);
    }

    @Test
    public void AIEngineReturnsCorrectPhase() {
        game = new Game(true);
        Phase phase = game.move(new Spot(4, 6), new Spot(4, 5));
        assertTrue(phase == Phase.PLAY);
    }
    
}
