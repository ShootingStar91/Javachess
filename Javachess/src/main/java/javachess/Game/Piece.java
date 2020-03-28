
package javachess.Game;

import java.util.ArrayList;

/**
 *
 * @author Arttu Kangas
 */

public class Piece {
    PieceType type;
    boolean white;
    
    public Piece(PieceType type, boolean isWhite) {
        this.type = type;
        this.white = isWhite;
    }
    
    
    public PieceType getType() {
        return type;
    }
    
    public boolean isWhite() {
        return white;
    }
    
    public String getLetter() {
        switch (this.getType()) {
            case PAWN:
                return "p";
            case KNIGHT:
                return "n";
            case ROOK:
                return "r";
            case BISHOP:
                return "b";
            case QUEEN:
                return "q";
            case KING:
                return "k";
        }
        return "";
    }
    
    
}
