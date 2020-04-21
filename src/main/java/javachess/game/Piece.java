
package javachess.game;

import java.util.ArrayList;

/**
 *
 * @author Arttu Kangas
 */

public class Piece {
    private PieceType type;
    private boolean white;
    private ArrayList<Spot> moves;
    
    public Piece(PieceType type, boolean isWhite) {
        this.type = type;
        this.white = isWhite;
        this.moves = new ArrayList<>();
    }
    
    public Piece(char letter) {
        if (Character.isUpperCase(letter)) {
            white = true;
        } else {
            white = false;
        }
        letter = Character.toLowerCase(letter);
        setType(letter);
    }
    
    public void setType(char letter) {
        if (letter == 'p') {
            type = PieceType.PAWN;
        } else if (letter == 'n') {
            type = PieceType.KNIGHT;
        } else if (letter == 'r') {
            type = PieceType.ROOK;
        } else if (letter == 'b') {
            type = PieceType.BISHOP;
        } else if (letter == 'q') {
            type = PieceType.QUEEN;
        } else if (letter == 'k') {
            type = PieceType.KING;
        }
    }
    
    public PieceType getType() {
        return type;
    }
    
    public void putMoves(ArrayList<Spot> moves) {
        this.moves = moves;
    }
    
    public ArrayList<Spot> getMoves() {
        return moves;
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
