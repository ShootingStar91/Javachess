
package javachess.game;

import java.util.ArrayList;

/**
 * This class represents the chessboard. It does not generate any moves, but it
 * contains each piece and remembers if the kings or rooks have been moved at
 * a previous point in game, and provides methods to check that in order to 
 * enable or disable castling.
 * @author Arttu Kangas
 */
public class Board {
    private boolean whiteKingCastling;
    private boolean whiteQueenCastling;
    private boolean blackQueenCastling;
    private boolean blackKingCastling;
    private Piece[][] board;
    private Spot blackKingSpot;
    private Spot whiteKingSpot;
    private final int boardSize = 8;
    private ArrayList<Spot> knightMoves;
    private ArrayList<Spot> bishopDirections;
    private ArrayList<Spot> rookDirections;
    private ArrayList<Spot> kingDirections;
    private boolean[][] whiteAttacks;
    private boolean[][] blackAttacks;

    /**
     * Constructor of the Board-class. Initializes the chessboard to a starting
     * position.
     */
    public Board() {
        board = new Piece[boardSize][boardSize];
        whiteKingCastling = true;
        whiteQueenCastling = true;
        blackQueenCastling = true;
        blackKingCastling = true;
        whiteAttacks = new boolean[boardSize][boardSize];
        blackAttacks = new boolean[boardSize][boardSize];
        initPieces();
        initPawns();
        initMoves();
    }
    
    /**
     * Saves the entire state of the Board-object and returns a copy of it
     * @return A copy of the current state of this board including all variables
     */
    public Board copyBoard() {
        Board newBoard = new Board();
        newBoard.whiteKingCastling = whiteKingCastling;
        newBoard.whiteQueenCastling = whiteQueenCastling;
        newBoard.blackKingCastling = blackKingCastling;
        newBoard.blackQueenCastling = blackQueenCastling;
        newBoard.whiteAttacks = copyArr(whiteAttacks);
        newBoard.blackAttacks = copyArr(blackAttacks);
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (get(x, y) == null) {
                    newBoard.set(x, y, null);
                    continue;
                }
                newBoard.set(x, y, get(x, y).copy());
            }
        }
        return newBoard;
    }
    
    /**
     * Goes through all the moves of the pieces on board and updates
     * info about which spots they are attacking
     */
    public void updateAttackedSpots() {
        clearAttacked();
        for (Spot spot : getPiecesOnBoard()) {
            updateAttackedSpot(spot);
        }
    }
    
    private void updateAttackedSpot(Spot spot) {
        Piece piece = get(spot);
        int direction = 1;
        if (piece.isWhite()) {
            direction = -1;
        }
        if (piece.getType() == PieceType.PAWN) {
            addPawnAttacks(spot, piece.isWhite(), direction);
            return;
        }
        for (Spot move : piece.getMoves()) {
            setAttacked(piece.isWhite(), move, true);
        }
    }
    
    /**
     * Detects if the given move is a castling move or not
     * @param move The move to be inspected
     * @return true if the move is a castling, false otherwise
     */
    public boolean detectCastling(Move move) {
        return !(get(move.from()) == null
                || get(move.to()) == null
                || (get(move.from()).isWhite()
                != get(move.to()).isWhite()));
    }
    
    /**
     * Gives the spot where the king is supposed to be located after 
     * a castling
     * @param move The castling move
     * @param whiteToMove Which player is doing the castling
     * @return Spot where the king is to be moved
     */
    public Spot getCastlingTarget(Move move, boolean whiteToMove) {
        int x;
        int y;
        if (!whiteToMove) {
            y = 7;
        } else {
            y = 0;
        }
        if (move.from().getX() == 0 || move.to().getX() == 0) {
            x = 2;
        } else {
            x = 6;
        }
        return new Spot(x, y);
    }

    private void addPawnAttacks(Spot spot, boolean white, int direction) {
        Spot leftSpot = new Spot(spot.getX() - 1, spot.getY() + direction);
        Spot rightSpot = new Spot(spot.getX() + 1, spot.getY() + direction);
        if (leftSpot.onBoard()) {
            setAttacked(white, leftSpot, true);
        }
        if (rightSpot.onBoard()) {
            setAttacked(white, rightSpot, true);
        }
    }
    
    private void disableCastlingFromKing(Piece piece) {
        if (piece.isWhite()) {
            whiteKingCastling = false;
            whiteQueenCastling = false;
        } else {
            blackKingCastling = false;
            blackQueenCastling = false;
        }
    }
    
    private void disableCastlingFromRook(Piece piece, Spot from) {
        if (piece.isWhite() && from.getX() == 0) {
            whiteQueenCastling = false;
        } else if (piece.isWhite() && from.getX() == 7) {
            whiteKingCastling = false;
        } else if (!piece.isWhite() && from.getX() == 0) {
            blackQueenCastling = false;
        } else if (!piece.isWhite() && from.getX() == 7) {
            blackKingCastling = false;
        }
    }
    
    /**
     * Returns whether the given spot is attacked by the given player
     * @param byWhite true if you want to know if white is attacking the spot,
     * false for black
     * @param spot Spot you want to know the attacked status of
     * @return boolean representing if the spot is attacked
     */
    public boolean getAttacked(boolean byWhite, Spot spot) {
        if (byWhite) {
            return whiteAttacks[spot.getX()][spot.getY()];
        } else {
            return blackAttacks[spot.getX()][spot.getY()];
        }
    }
    
    public boolean kingCastlingBlocked(int x, int y, boolean white) {
        return  get(x, y) != null 
             || get(x + 1, y) != null
             || getAttacked(!white, new Spot(x - 1, y)) 
             || getAttacked(!white, new Spot(x, y))
             || getAttacked(!white, new Spot(x + 1, y));
    }
    
    public boolean queenCastlingBlocked(int x, int y, boolean white) {
        return get(x, y) != null 
                || get(x + 1, y) != null
                || get(x + 2, y) != null 
                || getAttacked(!white, new Spot(x + 1, y))
                || getAttacked(!white, new Spot(x + 2, y))
                || getAttacked(!white, new Spot(x + 3, y));
    }
    
    /**
     * Returns true if the castling is disabled by any rule
     * @param white True if player is white, false for black
     * @param kingSide True if the castling to be inspected is 
     * kingside, false for queenside
     * @return True if the castling is disabled, false if it is allowed
     */
    public boolean castlingDisabled(boolean white, boolean kingSide) {
        return (kingSide && white && !whiteKingCastling) || (!kingSide && white
                && !whiteQueenCastling) || (kingSide && !white 
                && !blackKingCastling)
                || (!kingSide && !white && !blackQueenCastling);
    }
    
    /**
     * Sets all attacked spots for both players to false.
     */
    public void clearAttacked() {
        whiteAttacks = new boolean[boardSize][boardSize];
        blackAttacks = new boolean[boardSize][boardSize];
    }

    /**
     * Sets the attacked spot
     * @param byWhite True if white is attacking, false for black
     * @param spot Which spot is attacked
     * @param attacks True if spot is attacked, false if not
     */
    public void setAttacked(boolean byWhite, Spot spot, boolean attacks) {
        if (byWhite) {
            whiteAttacks[spot.getX()][spot.getY()] = attacks;
        } else {
            blackAttacks[spot.getX()][spot.getY()] = attacks;
        }
    }
    
    private boolean[][] copyArr(boolean [][] array) {
        boolean[][] newArray = new boolean[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                newArray[x][y] = array[x][y];
            }
        }
        return newArray;
    }
    
    private void initMoves() {
        initKnightMoves();
        initBishopDirections();
        initRookDirections();
        kingDirections = new ArrayList<>();
        kingDirections.addAll(bishopDirections);
        kingDirections.addAll(rookDirections);
    }
    
    private void initPawns() {
        for (int x = 0; x < boardSize; x++) {
            set(x, 1, new Piece(PieceType.PAWN, false));
            set(x, 6, new Piece(PieceType.PAWN, true));
        }
    }
    
    private void initBishopDirections() {
        bishopDirections = new ArrayList<>();
        bishopDirections.add(new Spot(-1, -1));
        bishopDirections.add(new Spot(-1, 1));
        bishopDirections.add(new Spot(1, 1));
        bishopDirections.add(new Spot(1, -1));
    }
    
    /**
     * Returns true if the player has no moves left at all
     * @param white Which player is in question, true for white
     * @return true if player has 0 moves, false otherwise
     */
    public boolean noMovesLeft(boolean white) {
        for (Spot spot : getPiecesOnBoard()) {
            if (get(spot).isWhite() == white) {
                if (get(spot).getMoves() != null 
                        && get(spot).getMoves().size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Empty moves from every piece on the board
     */
    public void clearMoves() {
        for (Spot spot : getPiecesOnBoard()) {
            get(spot).putMoves(new ArrayList<>());
        }
    }

    public Spot initCastlingSpot(boolean kingSide, boolean white) {
        int x = 0;
        int y = 0;
        if (kingSide) {
            x = 5;
        } else {
            x = 1;
        }
        if (white) {
            y = 7;
        } else {
            y = 0;
        }
        return new Spot(x, y);
    }
    
    private void initRookDirections() {
        rookDirections = new ArrayList<>();
        rookDirections.add(new Spot(0, -1));
        rookDirections.add(new Spot(-1, 0));
        rookDirections.add(new Spot(0, 1));
        rookDirections.add(new Spot(1, 0));
    }
    
    private void initKnightMoves() {
        knightMoves = new ArrayList<>();
        knightMoves.add(new Spot(1, 2));
        knightMoves.add(new Spot(2, 1));
        knightMoves.add(new Spot(-1, 2));
        knightMoves.add(new Spot(1, -2));
        knightMoves.add(new Spot(-1, -2));
        knightMoves.add(new Spot(-2, 1));
        knightMoves.add(new Spot(2, -1));
        knightMoves.add(new Spot(-2, -1));
    }
    
    /**
     * Returns the corresponding player's king current location
     * @param white True if the wanted king is white, false for black
     * @return Spot representing where the king is
     */
    public Spot getKingSpot(boolean white) {
        if (white) {
            return whiteKingSpot;
        } else {
            return blackKingSpot;
        }
    }
    
    /**
     * Updates the class variables which tell where the king of each player
     * is located
     */
    public void updateKingSpots() {
        for (Spot spot : getPiecesOnBoard()) {
            Piece piece = get(spot);
            if (piece.getType() == PieceType.KING) {
                if (piece.isWhite()) {
                    whiteKingSpot = spot;
                } else {
                    blackKingSpot = spot;
                }
            }
        }
    }
    /**
     * Method to generate raw knight moves, this means all the moves
     * that the knight could move if unlimited by any pieces or board borders
     * @param spot Where the knight is currently located
     * @return ArrayList<Spot> representing all the moves
     */
    public ArrayList<Spot> generateKnightMoves(Spot spot) {
        ArrayList<Spot> moves = new ArrayList<>();
        for (Spot move : knightMoves) {
            moves.add(new Spot(spot.getX() + move.getX(),
                    spot.getY() + move.getY()));
        }
        return moves;
    }

    private void initPieces() {
        set(0, 0, new Piece(PieceType.ROOK, false));
        set(7, 0, new Piece(PieceType.ROOK, false));
        set(0, 7, new Piece(PieceType.ROOK, true));
        set(7, 7, new Piece(PieceType.ROOK, true));
        set(1, 0, new Piece(PieceType.KNIGHT, false));
        set(6, 0, new Piece(PieceType.KNIGHT, false));
        set(1, 7, new Piece(PieceType.KNIGHT, true));
        set(6, 7, new Piece(PieceType.KNIGHT, true));
        set(2, 0, new Piece(PieceType.BISHOP, false));
        set(5, 0, new Piece(PieceType.BISHOP, false));
        set(2, 7, new Piece(PieceType.BISHOP, true));
        set(5, 7, new Piece(PieceType.BISHOP, true));
        set(3, 0, new Piece(PieceType.QUEEN, false));
        set(3, 7, new Piece(PieceType.QUEEN, true));
        set(4, 0, new Piece(PieceType.KING, false));
        set(4, 7, new Piece(PieceType.KING, true));
        whiteKingSpot = new Spot(4, 7);
        blackKingSpot = new Spot(4, 0);

    }

    /**
     * Gathers a list of all pieces on the board (both players)
     * @return ArrayList of all the Spots that have a piece in them
     */
    public ArrayList<Spot> getPiecesOnBoard() {
        ArrayList<Spot> pieces = new ArrayList<>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (get(x, y) != null) {
                    pieces.add(new Spot(x, y));
                }
            }
        }
        return pieces;
    }

    /**
     * Takes a spot of a unit that is moved and disables the castling of a
     * player if that is necessary
     * @param spot Spot where the moved unit is
     */
    public void disableCastling(Spot spot) {
        Piece piece = get(spot);
        if (piece == null) {
            return;
        }
        if (piece.getType() == PieceType.KING) {
            disableCastlingFromKing(piece);
        } else if (piece.getType() == PieceType.ROOK) {
            disableCastlingFromRook(piece, spot);
        }
    }
    
    /**
     * Returns an ArrayList of Spot-objects that represent all the directions
     * a given PieceType can move to
     * @param type
     * @return ArrayList<Spot> representing directions the given type can
     * move to
     */
    public ArrayList<Spot> getDirections(PieceType type) {
        switch (type) {
            case BISHOP:
                return bishopDirections;
            case ROOK:
                return rookDirections;
            case KING:
                return kingDirections;
            case QUEEN:
                return kingDirections;
        }
        return new ArrayList<>();
    }
    
    /**
     * Copies only the chessboard with pieces, not including class variables
     * @return Piece[][] array with Piece objects or null to represent an
     * empty spot on the chessboard
     */
    public Piece[][] copyRawBoard() {
        Piece[][] tempBoard = new Piece[boardSize][boardSize];
        for (Spot spot : getPiecesOnBoard()) {
            tempBoard[spot.getX()][spot.getY()] = get(spot);
        }
        return tempBoard;
    }
    
    public Piece[][] getBoard() {
        return board;
    }
    
    public void setBoard(Piece[][] board) {
        this.board = board;
    }
    
    public Piece get(int x, int y) {
        return board[x][y];
    }
    
    public Piece get(Spot spot) {
        return board[spot.getX()][spot.getY()];
    }
    
    public void set(int x, int y, Piece piece) {
        board[x][y] = piece;
    }
    
    public void set(Spot spot, Piece piece) {
        board[spot.getX()][spot.getY()] = piece;
    }

}
