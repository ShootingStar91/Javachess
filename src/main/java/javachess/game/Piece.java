
package javachess.game;

import java.util.ArrayList;

/**
 * Represents a single piece of the game. Has the type of the piece,
 * the player (white or black) and all the currently possible moves of
 * the piece.
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
    
    /**
     * The constructor of a piece. Takes as a parameter a letter which
     * specifies both a type and a colour (player) of the piece.
     * @param letter Letter representing the type:
     * Pawn, kNight, Rook, Bishop, Queen or King, where the capitalized letter
     * is the parameter representing that type. If the letter is given as
     * capitalized, it will belong to the white player, otherwise to black.
     */
    public Piece(char letter) {
        if (Character.isUpperCase(letter)) {
            white = true;
        } else {
            white = false;
        }
        letter = Character.toLowerCase(letter);
        setType(letter);
    }
    
    /**
     * Set the type of this piece.
     * @param letter Letter representing the type:
     * Pawn, kNight, Rook, Bishop, Queen or King, where the capitalized letter
     * is the parameter representing that type. 
     */
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
    
    /**
     * Returns the letter corresponding to the type of this Piece
     * @return 
     */
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
