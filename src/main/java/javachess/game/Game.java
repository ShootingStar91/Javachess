package javachess.game;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The class responsible for handling the game logic. It contains
 * a chessboard which has all the pieces on it. It has methods for
 * generating all possible moves of player's pieces, and methods
 * for executing those moves. It also uses the AIEngine class to provide
 * an AI opponent.
 * 
 * @author Arttu Kangas
 */
public final class Game {
    private int turns;
    private boolean whiteToMove;
    private Board board;
    private int enpassant;
    private final int kingX = 4;
    private final int enpassantDisabled = -2;
    private final int turnLimit = 50;
    private boolean copiedGame;
    ArrayList<Piece[][]> boardHistory;
    /**
     * The constructor of a game. It can create a game between humans, or
     * with an AI opponent.
     * @param copiedGame set this to true if the game is a copy created
     * inside AIEngine, otherwise set it to false. Prevents unnecessary
     * saving of board history to optimize.
     */
    public Game(boolean copiedGame) {
        this.copiedGame = copiedGame;
        initClassVariables();
        if (!copiedGame) {
            boardHistory.add(board.copyRawBoard());
        }
        if (!copiedGame) {
            generatePotentialMoves(whiteToMove);
        }
    }

    /**
     * Produces a copy of this Game-object for the use of AIEngine.
     * @return A copy of this object
     */
    public Game copyGame() {
        Game game = new Game(true);
        game.enpassant = enpassant;
        game.whiteToMove = whiteToMove;
        game.board = board.copyBoard();
        generatePotentialMoves(whiteToMove);
        return game;
    }
    
    private void initClassVariables() {
        whiteToMove = true;
        turns = 0;
        enpassant = enpassantDisabled;
        board = new Board();
        boardHistory = new ArrayList<>();
    }
    
    /**
     * Method which executes a move and completes the turn or tells the caller
     * to ask player for promotion.
     * @param move Move to be processed and executed
     * @return Phase which the game is in after the move is executed
     */
    public Phase move(Move move) {
        board.clearMoves();
        if (whiteToMove) {
            whiteToMove = false;
        } else {
            turns++;
            whiteToMove = true;
        }
        Spot promotion = processMove(move, false);
        if (!copiedGame) {
            boardHistory.add(board.copyRawBoard());
        }
        if (promotion != null) {
            return Phase.PROMOTION;
        }
        return startTurn();
    }
    
    /**
     * Promotes a given pawn into the given type of piece and then finishes
     * the turn.
     * @param spot Where the promoted pawn is
     * @param type What type it will be promoted to
     * @return the phase the game will be in after promotion
     */
    public Phase promote(Spot spot, PieceType type) {
        board.set(spot, new Piece(type,
                board.get(spot).isWhite()));
        return startTurn();
    }

    private Phase startTurn() {
        generatePotentialMoves(whiteToMove);
        board.updateKingSpots();
        if (board.noMovesLeft(whiteToMove)) {
            generatePotentialMoves(!whiteToMove);
            board.updateAttackedSpots();
            if (checked()) {
                return Phase.CHECKMATE;
            } else {
                return Phase.STALEMATE;
            }
        }
        if (turns == turnLimit) {
            return Phase.STALEMATE;
        }
        
        return Phase.PLAY;
    }
    
    
    private void generatePotentialMoves(boolean white) {
        if (white == whiteToMove) {
            board.clearAttacked();
        }
        for (Spot spot : board.getPiecesOnBoard()) {
            if (board.get(spot).isWhite() != white) {
                continue;
            }
            ArrayList<Spot> moves = getPotentialMoves(spot);
            if (white == whiteToMove) {
                moves = detectChecks(spot, moves);
            }
            board.get(spot).putMoves(moves);
        }
        if (white == whiteToMove) {
            addCastling();
        }
    }
    
    private void addCastling() {
        if (whiteToMove) {
            if (castling(true, true)) {
                board.get(kingX, 7).addMove(new Spot(7, 7));
            }
            if (castling(true, false)) {
                board.get(kingX, 7).addMove(new Spot(0, 7));
            }
        } else {
            if (castling(false, true)) {
                board.get(kingX, 0).addMove(new Spot(7, 0));
            }
            if (castling(false, false)) {
                board.get(kingX, 0).addMove(new Spot(0, 0));
            }
        }
    }

    private Spot processMove(Move move, boolean superficial) {
        Piece piece = board.get(move.from());
        if (!superficial) {
            board.disableCastling(move.from());
        }
        executeMove(move);
        if (superficial) {
            return null;
        }
        updateEnPassant(piece, move);
        if (piece.getType() == PieceType.PAWN && (move.to().getY() == 0
                || move.to().getY() == 7)) {
            return move.to();
        }
        return null;
    }
    
    private void executeMove(Move move) {
        detectAndExecuteEnPassant(move);
        if (!detectAndExecuteCastling(move)) {
            board.set(move.to(), board.get(move.from()));
            board.set(move.from(), null);
        }
    }

    private void updateEnPassant(Piece piece, Move move) {
        if (piece.getType() == PieceType.PAWN
                && Math.abs(move.from().getY() - move.to().getY()) == 2) {
            enpassant = move.from().getX();
        } else {
            enpassant = enpassantDisabled;
        }
    }

    private void detectAndExecuteEnPassant(Move move) {
        Piece piece = board.get(move.from());
        if (piece.getType() == PieceType.PAWN) {
            if (move.from().getX() != move.to().getX()
                    && board.get(move.to()) == null) {
                if (move.to().getY() == 2) {
                    board.set(move.to().getX(), 3, null);
                } else if (move.to().getY() == 5) {
                    board.set(move.to().getX(), 4, null);
                }
            }
        }
    }

    private ArrayList<Spot> getPotentialMoves(Spot spot) {
        if (board.get(spot) == null) {
            return null;
        }
        boolean isWhite = board.get(spot).isWhite();
        ArrayList<Spot> moves = new ArrayList<>();
        addPotentialMoves(spot, moves, isWhite);
        return moves;
    }
    
    private void addPotentialMoves(Spot spot, ArrayList<Spot> moves, 
            boolean isWhite) {
        int x = spot.getX();
        int y = spot.getY();

        if (board.get(x, y).getType() == PieceType.PAWN) {
            addPawnPotentialMoves(spot, moves, isWhite);
        } else if (board.get(x, y).getType() == PieceType.KNIGHT) {
            addKnightPotentialMoves(spot, moves, isWhite);
        } else if (board.get(x, y).getType() == PieceType.KING) {
            addKingPotentialMoves(spot, moves, isWhite);
        } else {
            moves.addAll(generateMovesFor(spot,
                    board.get(spot).getType()));
        }
    }
    
    private void addPawnPotentialMoves(Spot spot, ArrayList<Spot> moves, 
            boolean isWhite) {
        int dir;
        if (board.get(spot).isWhite()) {
            dir = -1;
        } else {
            dir = 1;
        }
        for (int dx = -1; dx <= 1; dx++) {
            Spot newSpot = new Spot(spot.getX() + dx, spot.getY() + dir);
            if (newSpot.onBoard()) {
                processSinglePotentialPawnMove(dx, moves, newSpot,
                        dir, spot, isWhite);
            }
        }
    }

    private void processSinglePotentialPawnMove(int dx,
            ArrayList<Spot> moves, Spot newSpot, int dir, Spot spot, 
            boolean isWhite) {
        int x = spot.getX();
        int y = spot.getY();
        if (dx == 0) {
            if (board.get(x, y + dir) == null) {
                moves.add(newSpot);
            }
            if ((spot.getY() == 1 && !whiteToMove)
                    || (spot.getY() == 6 && whiteToMove)) {
                if ((new Spot(x, y + dir * 2).onBoard())
                        && board.get(x, y + dir * 2) == null
                        && board.get(x, y + dir) == null) {
                    moves.add(new Spot(x, y + dir * 2));
                }
            }
            processPotentialEnPassant(x, y, spot, dir, moves);
        } else {
            addPawnAttack(dx, x, y, dir, moves, isWhite, newSpot);
        }
    }
    
    private void addPawnAttack(int dx, int x, int y, int dir, 
        ArrayList<Spot> moves, boolean isWhite, Spot newSpot) {
        if (board.get(x + dx, y + dir) != null
                && board.get(x + dx, y + dir).isWhite() 
                != isWhite) {
            moves.add(newSpot);
        }
    }
    
    private void processPotentialEnPassant(int x, int y, Spot spot, int dir,
            ArrayList<Spot> moves) {
        if (enpassant != enpassantDisabled
            && (enpassant == x - 1
            || enpassant == x + 1)
            && (board.get(enpassant, spot.getY())) != null
            && board.get(enpassant, spot.getY()).isWhite()
            != board.get(spot).isWhite()
            && board.get(spot).getType()
            == PieceType.PAWN) {
            if (board.get(enpassant, y + dir) == null) {
                moves.add(new Spot(enpassant, y + dir));
            }
        }
    }
    
    private void addKnightPotentialMoves(Spot spot,
        ArrayList<Spot> moves, boolean isWhite) {
        ArrayList<Spot> knightMovesFinal = board.generateKnightMoves(spot);
        for (Spot move : knightMovesFinal) {
            if (!move.onBoard()) {
                continue;
            }
            Piece piece = board.get(move);
            if (move.onBoard() && (piece == null
                || piece.isWhite() != isWhite)) {
                moves.add(move);
            }
        }
    }
    
    private void addKingPotentialMoves(Spot spot, ArrayList<Spot> moves, 
        boolean isWhite) {
        ArrayList<Spot> dirs = new ArrayList<>();
        dirs.addAll(board.getDirections(PieceType.KING));
        for (Spot dir : dirs) {
            Spot newSpot = new Spot(spot.getX() + dir.getX(), spot.getY()
                    + dir.getY());
            if (!newSpot.onBoard()) {
                continue;
            }
            Piece piece = board.get(newSpot);
            if (newSpot.onBoard()) {
                if (piece != null && piece.isWhite() == isWhite) {
                    continue;
                }
                moves.add(newSpot);
            }
        }
    }

    private ArrayList<Spot> detectChecks(Spot startSpot, 
            ArrayList<Spot> moves) {
        for (Iterator<Spot> iterator = moves.iterator(); iterator.hasNext();) {
            Piece[][] savedBoard = board.copyRawBoard();
            Spot moveTo = iterator.next();
            Move move = new Move(startSpot, moveTo);
            processMove(move, true);
            board.updateKingSpots();
            generatePotentialMoves(!whiteToMove);
            board.updateAttackedSpots();
            if (checked()) {
                iterator.remove();
            }
            board.setBoard(savedBoard);
            board.clearAttacked();
        }
        return moves;
    }
    
    private boolean checked() {
        Spot kingSpot = board.getKingSpot(whiteToMove);
        return (whiteToMove && board.getAttacked(false, kingSpot))
                    || (!whiteToMove && board.getAttacked(true, kingSpot));
    }
    
    private boolean detectAndExecuteCastling(Move move) {
        if (!board.detectCastling(move)) {
            return false;
        }
        Spot target = board.getCastlingTarget(move, whiteToMove);
        board.set(move.from(), null);
        board.set(move.to(), null);
        board.set(target, new Piece(PieceType.KING, !whiteToMove));
        int rookX;
        if (target.getX() == 2) {
            rookX = kingX - 1;
        } else {
            rookX = kingX + 1;
        }
        board.set(rookX, target.getY(), 
                new Piece(PieceType.ROOK, !whiteToMove));
        return true;
    }
    
    private boolean castling(boolean white, boolean kingSide) {
        Spot spot = board.initCastlingSpot(kingSide, white);
        if (board.castlingDisabled(white, kingSide)) {
            return false;
        }
        board.updateAttackedSpots();
        if (kingSide) {
            if (board.kingCastlingBlocked(spot.getX(), spot.getY(), white)) {
                return false;
            }
        } else if (board.queenCastlingBlocked(spot.getX(), spot.getY(), 
                white)) {
            return false;
        }
        return true;
    }

    private ArrayList<Spot> generateMovesFor(Spot spot, PieceType type) {
        ArrayList<Spot> dirs = new ArrayList<>();
        if (type == PieceType.QUEEN) {
            dirs = board.getDirections(type);
        } else {
            dirs.addAll(board.getDirections(type));
        }
        ArrayList<Spot> moves = generateMovesFromDirs(spot, dirs);
        return moves;
    }

    private ArrayList<Spot> generateMovesFromDirs(Spot spot, 
            ArrayList<Spot> dirs) {
        boolean isWhite = board.get(spot).isWhite();
        ArrayList<Spot> moves = new ArrayList<>();
        for (Spot dir : dirs) {
            Spot curSpot = new Spot(spot.getX() + dir.getX(),
                    spot.getY() + dir.getY());
            walkLineFrom(curSpot, dir, moves, isWhite);
        }
        return moves;
    }
    
    private void walkLineFrom(Spot curSpot, Spot dir, ArrayList<Spot> moves,
            boolean isWhite) {
        while (curSpot.onBoard()) {
            int x = curSpot.getX();
            int y = curSpot.getY();
            if (board.get(x, y) == null) {
                moves.add(new Spot(x, y));
                curSpot.setX(curSpot.getX() + dir.getX());
                curSpot.setY(curSpot.getY() + dir.getY());
                continue;
            }
            if (board.get(x, y).isWhite() != isWhite) {
                moves.add(new Spot(x, y));
            }
            break;
        }
    }

    public Board getBoard() {
        return board;
    }

    public boolean whiteToMove() {
        return whiteToMove;
    }

    public int getTurn() {
        return turns;
    }
    
    public ArrayList<Piece[][]> getBoardHistory() {
        return boardHistory;
    }
}