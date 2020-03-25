package javachess.Game;

/**
 *
 * @author Arttu Kangas
 */

public class Game {
    boolean whiteKingCastling, whiteQueenCastling, blackQueenCastling, blackKingCastling;
    int turns;
    boolean whiteToMove;
    
    public Game() {
        turns = 0;
        whiteToMove = true;
        whiteKingCastling = true;
        whiteQueenCastling = true;
        blackQueenCastling = true;
        blackKingCastling = true;
    }
    
    
    
}
