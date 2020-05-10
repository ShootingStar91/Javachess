package gametest;

import java.util.ArrayList;
import javachess.game.AIEngine;
import javachess.game.Board;
import javachess.game.Game;
import javachess.game.Move;
import javachess.game.Phase;
import javachess.game.Piece;
import javachess.game.PieceType;
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
    Board board;
    
    public GameTest() {
        
    }
    
    @Before
    public void setUp() {
        game = new Game(false);
        board = game.getBoard();
    }
    
    private void setBoardEmpty() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board.set(x, y, null);
            }
        }
    }

    @Test
    public void potentialMovesReturnsEmptyList() {
        ArrayList<Spot> moves = board.get(new Spot(0, 0)).getMoves();
        assertTrue(moves.isEmpty());
    }

    @Test
    public void potentialMovesReturnsCastling() {
        setBoardEmpty();
        board.set(4, 0, new Piece(PieceType.KING, false));
        board.set(4, 7, new Piece(PieceType.KING, true));
        board.set(7, 0, new Piece(PieceType.ROOK, false));
        game.move(new Move(new Spot(4, 7), new Spot(4, 6)));

        ArrayList<Spot> moves = board.get(new Spot(4, 0)).getMoves();
        boolean found = false;
        for (Spot move : moves) {
            if (move.getX() == 7 && move.getY() == 0) {
                found = true;
            }
        }
        assertTrue(found);
    }
    
    @Test
    public void checkMateReturn() {
        game.move(new Move(new Spot(4, 6), new Spot(4, 5)));
        game.move(new Move(new Spot(5, 1), new Spot(5, 2)));
        game.move(new Move(new Spot(5, 6), new Spot(5, 5)));
        game.move(new Move(new Spot(6, 1), new Spot(6, 2)));
        game.move(new Move(new Spot(5, 5), new Spot(5, 4)));
        game.move(new Move(new Spot(6, 2), new Spot(6, 3)));
        Phase checkmate = game.move(new Move(new Spot(3,7), new Spot(7,3)));
        assertTrue(checkmate == Phase.CHECKMATE);
    }
    
    @Test
    public void castlingDetectedAndExecuted() {
        setBoardEmpty();
        board.set(4, 0, new Piece(PieceType.KING, false));
        board.set(4, 7, new Piece(PieceType.KING, true));
        board.set(7, 0, new Piece(PieceType.ROOK, false));
        game.move(new Move(new Spot(4, 7), new Spot(4, 6)));
        game.move(new Move(new Spot(4, 0), new Spot(7, 0)));
        assertTrue(board.get(5, 0).getType() == PieceType.ROOK 
                && board.get(6, 0).getType() == PieceType.KING);
    }

    @Test
    public void AIEngineReturnsCorrectPhase() {
        game.move(new Move(new Spot(1, 1), new Spot(1, 3)));
        AIEngine ai = new AIEngine(game);
        Phase phase = ai.doTurn();
        assertTrue(phase == Phase.PLAY);
    }
    
    @Test
    public void pieceCapturingExecutedCorrectly() {
        setBoardEmpty();
        board.set(5, 4, new Piece(PieceType.PAWN, false));
        board.set(4, 0, new Piece(PieceType.KING, false));
        board.set(3, 0, new Piece(PieceType.KING, true));
        game.move(new Move(new Spot(3, 0), new Spot(4, 0)));
        assertTrue(board.get(3, 0) == null 
                && board.get(4, 0).getType() == PieceType.KING);
    }
    
}
