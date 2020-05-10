
package gametest;

import javachess.game.Piece;
import javachess.game.PieceType;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Arttu Kangas
 */
public class PieceTest {

    
    
    public PieceTest () {
        
    }

    @Test
    public void getLetterWorks() {
        Piece pawn = new Piece('P');
        Piece king = new Piece('K');
        Piece queen = new Piece('q');

        assertTrue(pawn.getLetter().equals("p") && pawn.getType() == PieceType.PAWN
        && pawn.isWhite() == true);
        assertTrue(king.getLetter().equals("k") && king.getType() == PieceType.KING
        && king.isWhite() == true);
        assertTrue(queen.getLetter().equals("q") && queen.getType() == PieceType.QUEEN
        && queen.isWhite() == false);

    }
    
}
