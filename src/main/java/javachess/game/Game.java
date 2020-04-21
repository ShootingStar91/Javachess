package javachess.game;

import java.util.ArrayList;
import java.util.Iterator;

public final class Game {

    private boolean whiteKingCastling;
    private boolean whiteQueenCastling;
    private boolean blackQueenCastling;
    private boolean blackKingCastling;
    private int turns;
    private boolean whiteToMove;
    private ArrayList<Spot> bishopDirections;
    private ArrayList<Spot> rookDirections;
    private Piece[][] board;
    private ArrayList<Spot> knightMoves;
    private boolean[][] whiteAttacks;
    private boolean[][] blackAttacks;
    private Spot whiteKingSpot;
    private Spot blackKingSpot;
    private int enpassant;
    private final int boardSize = 8;
    private final int kingX = 4;
    private final int blackSide = 0;
    private final int whiteSide = 7;
    private final int knightJump = 2;
    private final int enpassantDisabled = -2;
    ArrayList<Piece[][]> boardHistory;

    public Game(final boolean againstAI) {

        initMoves();

        initClassVariables();

        initPawns();

        initPieces();

        boardHistory.add(copyBoard());

        
        generatePotentialMoves(whiteToMove);

    }
    

    private void initMoves() {
        initKnightMoves();
        initBishopDirections();
        initRookDirections();
    }
    
    private void initBishopDirections() {
        bishopDirections = new ArrayList<>();
        bishopDirections.add(new Spot(-1, -1));
        bishopDirections.add(new Spot(-1, 1));
        bishopDirections.add(new Spot(1, 1));
        bishopDirections.add(new Spot(1, -1));
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
        knightMoves.add(new Spot(1, knightJump));
        knightMoves.add(new Spot(knightJump, 1));
        knightMoves.add(new Spot(-1, knightJump));
        knightMoves.add(new Spot(1, -knightJump));
        knightMoves.add(new Spot(-1, -knightJump));
        knightMoves.add(new Spot(-knightJump, 1));
        knightMoves.add(new Spot(knightJump, -1));
        knightMoves.add(new Spot(-knightJump, -1));
    }

    private void initClassVariables() {
        whiteAttacks = new boolean[boardSize][boardSize];
        blackAttacks = new boolean[boardSize][boardSize];
        whiteToMove = true;
        turns = 0;
        whiteKingCastling = true;
        whiteQueenCastling = true;
        blackQueenCastling = true;
        blackKingCastling = true;
        enpassant = enpassantDisabled;
        board = new Piece[boardSize][boardSize];
        boardHistory = new ArrayList<>();
        
    }

    private void initPawns() {
        for (int x = 0; x < boardSize; x++) {
            board[x][blackSide + 1] = new Piece(PieceType.PAWN, false);
            board[x][whiteSide - 1] = new Piece(PieceType.PAWN, true);
        }
    }
    
    public ArrayList<Piece[][]> getBoardHistory() {
        return boardHistory;
    }

    private void initPieces() {
        board[0][0] = new Piece(PieceType.ROOK, false);
        board[whiteSide][0] = new Piece(PieceType.ROOK, false);
        board[0][whiteSide] = new Piece(PieceType.ROOK, true);
        board[whiteSide][whiteSide] = new Piece(PieceType.ROOK, true);
        board[1][0] = new Piece(PieceType.KNIGHT, false);
        board[whiteSide - 1][0] = new Piece(PieceType.KNIGHT, false);
        board[1][whiteSide] = new Piece(PieceType.KNIGHT, true);
        board[whiteSide - 1][whiteSide] = new Piece(PieceType.KNIGHT, true);
        board[knightJump][0] = new Piece(PieceType.BISHOP, false);
        board[kingX + 1][0] = new Piece(PieceType.BISHOP, false);
        board[knightJump][whiteSide] = new Piece(PieceType.BISHOP, true);
        board[kingX + 1][whiteSide] = new Piece(PieceType.BISHOP, true);
        board[kingX - 1][0] = new Piece(PieceType.QUEEN, false);
        board[kingX - 1][whiteSide] = new Piece(PieceType.QUEEN, true);
        board[kingX][0] = new Piece(PieceType.KING, false);
        board[kingX][whiteSide] = new Piece(PieceType.KING, true);
        whiteKingSpot = new Spot(kingX, whiteSide);
        blackKingSpot = new Spot(kingX, blackSide);
    }

    private void clearMoves() {
        for (Spot spot : getPiecesOnBoard()) {
            board[spot.getX()][spot.getY()].putMoves(new ArrayList<>());
        }
    }

    public Phase move(final Spot from, final Spot to) {
        clearMoves();

        if (whiteToMove) {
            whiteToMove = false;
        } else {
            turns++;
            whiteToMove = true;
        }

        Spot promotion = executeMove(from, to, false);

        boardHistory.add(copyBoard());
        
        if (promotion != null) {
            return Phase.PROMOTION;
        }     
        return startTurn();
    }
    
    public Phase startTurn() {
        
        generatePotentialMoves(whiteToMove);

        if (checkMate()) {
            return Phase.CHECKMATE;
        }
        
        return Phase.PLAY;
        
    }

    public boolean checkMate() {
        for (Spot spot : getPiecesOnBoard()) {
            if (board[spot.getX()][spot.getY()].isWhite() == whiteToMove) {
                if (getMoves(spot) != null && getMoves(spot).size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void generatePotentialMoves(final boolean white) {
        if (white == whiteToMove) {
            clearAttacked();
        }
        for (Spot spot : getPiecesOnBoard()) {
            if (board[spot.getX()][spot.getY()].isWhite() != white) {
                continue;
            }
            ArrayList<Spot> moves = getPotentialMoves(spot);
            if (white == whiteToMove) {
                moves = detectChecks(spot, moves);
            }
            board[spot.getX()][spot.getY()].putMoves(moves);
        }
        if (white != whiteToMove) {
            return;
        }
        addCastling();
    }
    
    private void addCastling() {
        if (whiteToMove) {
            if (castling(true, true)) {
                board[kingX][whiteSide].getMoves()
                        .add(new Spot(whiteSide, whiteSide));
            }
            if (castling(true, false)) {
                board[kingX][whiteSide].getMoves()
                        .add(new Spot(0, whiteSide));
            }
        } else {
            if (castling(false, true)) {
                board[kingX][0].getMoves().add(new Spot(whiteSide, 0));
            }
            if (castling(false, false)) {
                board[kingX][0].getMoves().add(new Spot(0, 0));
            }
        }
    }

    private Spot executeMove(final Spot from, final Spot to,
            final boolean superficial) {
        Piece piece = board[from.getX()][from.getY()];
        doSpecialMove(from, to);
        if (superficial) {
            return null;
        }
        if (piece.getType() == PieceType.KING) {
            disableCastlingFromKing(piece);
        } else if (piece.getType() == PieceType.ROOK) {
            disableCastlingFromRook(piece, from);
        }
        disableCastling(from);
        updateEnPassant(piece, from, to);
        if (piece.getType() == PieceType.PAWN && (to.getY() == 0
                || to.getY() == whiteSide)) {
            return to;
        }
        return null;
    }
    
    private void doSpecialMove(Spot from, Spot to) {
        detectAndExecuteEnPassant(from, to);
        if (!detectAndExecuteCastling(from, to)) {
            board[to.getX()][to.getY()] = board[from.getX()][from.getY()];
            board[from.getX()][from.getY()] = null;
        }
    }

    private void disableCastlingFromKing(final Piece piece) {
        if (piece.isWhite()) {
            whiteKingCastling = false;
            whiteQueenCastling = false;
        } else {
            blackKingCastling = false;
            blackQueenCastling = false;
        }
    }

    private void updateEnPassant(final Piece piece, final Spot from,
            final Spot to) {
        if (piece.getType() == PieceType.PAWN
                && Math.abs(from.getY() - to.getY()) == knightJump) {
            enpassant = from.getX();
        } else {
            enpassant = enpassantDisabled;
        }
    }

    private void disableCastlingFromRook(final Piece piece, final Spot from) {
        if (piece.isWhite() && from.getX() == 0) {
            whiteQueenCastling = false;
        } else if (piece.isWhite() && from.getX() == whiteSide) {
            whiteKingCastling = false;
        } else if (!piece.isWhite() && from.getX() == 0) {
            blackQueenCastling = false;
        } else if (!piece.isWhite() && from.getX() == whiteSide) {
            blackKingCastling = false;
        }
    }

    private void detectAndExecuteEnPassant(final Spot from, final Spot to) {
        Piece piece = board[from.getX()][from.getY()];
        if (piece.getType() == PieceType.PAWN) {
            if (from.getX() != to.getX()
                    && board[to.getX()][to.getY()] == null) {
                if (to.getY() == knightJump) {
                    board[to.getX()][kingX - 1] = null;
                } else if (to.getY() == kingX + 1) {
                    board[to.getX()][kingX] = null;
                }
            }
        }
    }

    private void disableCastling(final Spot spot) {
        Piece piece = board[spot.getX()][spot.getY()];
        if (piece == null) {
            return;
        }
        if (piece.getType() == PieceType.KING) {
            disableCastlingFromKing(piece);
        } else if (piece.getType() == PieceType.ROOK) {
            if (piece.isWhite()) {
                disableCastlingFromRook(piece, spot);
            }
        }
    }

    public ArrayList<Spot> getMoves(final Spot spot) {
        if (board[spot.getX()][spot.getY()] == null) {
            return null;
        }
        return board[spot.getX()][spot.getY()].getMoves();
    }

    private void updateAttackedSpots() {
        clearAttacked();
        for (Spot spot : getPiecesOnBoard()) {
            updateAttackedSpot(spot);
        }
    }
    
    private void updateAttackedSpot(Spot spot) {
        Piece piece = board[spot.getX()][spot.getY()];
        if (piece.isWhite()) {
            for (Spot move : piece.getMoves()) {
                whiteAttacks[move.getX()][move.getY()] = true;
            }
            if (piece.getType() == PieceType.PAWN) {
                addPawnAttacks(spot, true);
            }
        } else {
            for (Spot move : piece.getMoves()) {
                blackAttacks[move.getX()][move.getY()] = true;
            }
            if (piece.getType() == PieceType.PAWN) {
                addPawnAttacks(spot, false);
            }
        }
    }

    private void addPawnAttacks(final Spot spot, final boolean white) {
        if (white) {
            addWhitePawnAttacks(spot);
        } else {
            addBlackPawnAttacks(spot);
        }
    }
    
    private void addWhitePawnAttacks(Spot spot) {
        Spot leftSpot = new Spot(spot.getX() - 1, spot.getY() - 1);
        Spot rightSpot = new Spot(spot.getX() + 1, spot.getY() - 1);
        if (leftSpot.onBoard()) {
            whiteAttacks[leftSpot.getX()][leftSpot.getY()] = true;
        }
        if (rightSpot.onBoard()) {
            whiteAttacks[rightSpot.getX()][rightSpot.getY()] = true;
        }
    }

    private void addBlackPawnAttacks(Spot spot) {
        Spot leftSpot = new Spot(spot.getX() - 1, spot.getY() + 1);
        Spot rightSpot = new Spot(spot.getX() + 1, spot.getY() + 1);
        if (leftSpot.onBoard()) {
            blackAttacks[leftSpot.getX()][leftSpot.getY()] = true;
        }
        if (rightSpot.onBoard()) {
            blackAttacks[rightSpot.getX()][rightSpot.getY()] = true;
        }
    }
    
    public Phase promote(Spot spot, PieceType type) {
        board[spot.getX()][spot.getY()] = new Piece(type,
                board[spot.getX()][spot.getY()].isWhite());
        
        return startTurn();
        
    }

    private ArrayList<Spot> getPotentialMoves(Spot spot) {

        if (board[spot.getX()][spot.getY()] == null) {
            return null;
        }


        boolean isWhite = board[spot.getX()][spot.getY()].isWhite();

        ArrayList<Spot> moves = new ArrayList<>();

        addPotentialMoves(spot, moves, isWhite);
        
        return moves;
    }
    
    private void addPotentialMoves(Spot spot, ArrayList<Spot> moves, 
            boolean isWhite) {
        int x = spot.getX();
        int y = spot.getY();

        if (board[x][y].getType() == PieceType.PAWN) {
            addPawnPotentialMoves(spot, x, y, moves, isWhite);
        } else if (board[x][y].getType() == PieceType.KNIGHT) {
            addKnightPotentialMoves(spot, moves, isWhite);
        } else if (board[x][y].getType() == PieceType.KING) {
            addKingPotentialMoves(spot, moves, isWhite);
        } else {
            moves.addAll(generateMovesFor(spot,
                    board[spot.getX()][spot.getY()].getType()));
        }
    }
    
    private void addPawnPotentialMoves(Spot spot, int x, int y, 
            ArrayList<Spot> moves, boolean isWhite) {
        int dir;
        if (board[x][y].isWhite()) {
            dir = -1;
        } else {
            dir = 1;
        }
        for (int dx = -1; dx <= 1; dx++) {
            Spot newSpot = new Spot(x + dx, y + dir);
            if (newSpot.onBoard()) {
                processSinglePotentialPawnMove(dx, x, y, moves, newSpot,
                        dir, spot, isWhite);
            }
        }
    }

    private void processSinglePotentialPawnMove(int dx, int x, int y,
            ArrayList<Spot> moves, Spot newSpot, int dir, Spot spot, 
            boolean isWhite) {
        if (dx == 0) {
            if (board[x][y + dir] == null) {
                moves.add(newSpot);
            }
            if ((spot.getY() == 1 && !whiteToMove)
                    || (spot.getY() == whiteSide - 1 
                    && whiteToMove)) {
                if ((new Spot(x, y + dir * 2).onBoard())
                        && board[x][y + dir * 2] == null
                        && board[x][y + dir] == null) {
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
        if (board[x + dx][y + dir] != null
                && board[x + dx][y + dir].isWhite() 
                != isWhite) {
            moves.add(newSpot);
        }
    }
    
    private void processPotentialEnPassant(int x, int y, Spot spot, int dir,
            ArrayList<Spot> moves) {
        if (enpassant != enpassantDisabled
            && (enpassant == x - 1
            || enpassant == x + 1)
            && (board[enpassant][spot.getY()] != null
            && board[enpassant][spot.getY()].isWhite()
            != board[spot.getX()][spot.getY()].isWhite()
            && board[spot.getX()][spot.getY()].getType()
            == PieceType.PAWN)) {
            if (board[enpassant][y + dir] == null) {
                moves.add(new Spot(enpassant, y + dir));
            }
        }
    }
    
    private void addKnightPotentialMoves(Spot spot,
        ArrayList<Spot> moves, boolean isWhite) {
        
        ArrayList<Spot> knightMovesFinal = generateKnightMoves(spot);
        
        for (Spot move : knightMovesFinal) {
            if (!move.onBoard()) {
                continue;
            }
            Piece piece = board[move.getX()][move.getY()];
            if (move.onBoard() && (piece == null
                || piece.isWhite() != isWhite)) {
                moves.add(move);
            }
        }
    }
    
    private void addKingPotentialMoves(Spot spot, ArrayList<Spot> moves, 
        boolean isWhite) {
        ArrayList<Spot> dirs = new ArrayList<>();
        dirs.addAll(bishopDirections);
        dirs.addAll(rookDirections);
        for (Spot dir : dirs) {
            Spot newspot = new Spot(spot.getX() + dir.getX(), spot.getY()
                    + dir.getY());
            if (!newspot.onBoard()) {
                continue;
            }
            Piece piece = board[newspot.getX()][newspot.getY()];
            if (newspot.onBoard()) {
                if (piece != null && piece.isWhite() == isWhite) {
                    continue;
                }
                moves.add(newspot);
            }
        }
    }

    private void clearAttacked() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                whiteAttacks[x][y] = false;
                blackAttacks[x][y] = false;
            }
        }
    }

    private ArrayList<Spot> detectChecks(final Spot spot, 
            final ArrayList<Spot> moves) {

        for (Iterator<Spot> iterator = moves.iterator(); iterator.hasNext();) {
            Piece[][] savedBoard = copyBoard();
            Spot move = iterator.next();
            executeMove(spot, move, true);
            updateKingSpots();
            generatePotentialMoves(!whiteToMove);
            updateAttackedSpots();
            if (checked()) {
                iterator.remove();
            }
            board = savedBoard;
            clearAttacked();
        }

        return moves;
    }
    
    private void updateKingSpots() {
        for (Spot pieceSpot : getPiecesOnBoard()) {
            Piece curPiece = board[pieceSpot.getX()][pieceSpot.getY()];
            if (curPiece.getType() == PieceType.KING) {
                if (curPiece.isWhite()) {
                    whiteKingSpot = pieceSpot;
                } else {
                    blackKingSpot = pieceSpot;
                }
            }
        }
    }
    
    private boolean checked() {
        Spot kingSpot = getKingSpot();
        return (whiteToMove && blackAttacks[kingSpot.getX()][kingSpot.getY()])
                    || (!whiteToMove
                    && whiteAttacks[kingSpot.getX()][kingSpot.getY()]);
    }
    
    private Spot getKingSpot() {
        if (whiteToMove) {
            return whiteKingSpot;
        } else {
            return blackKingSpot;
        }
    }

    private boolean detectAndExecuteCastling(final Spot from, final Spot to) {
        if (!detectCastling(from, to)) {
            return false;
        }
        Spot target = getCastlingTarget(from, to);
        board[from.getX()][from.getY()] = null;
        board[to.getX()][to.getY()] = null;
        board[target.getX()][target.getY()] = 
                new Piece(PieceType.KING, !whiteToMove);
        int rookX;
        if (target.getX() == 2) {
            rookX = kingX - 1;
        } else {
            rookX = kingX + 1;
        }
        board[rookX][target.getY()] = new Piece(PieceType.ROOK, !whiteToMove);
        return true;
    }
    
    private Spot getCastlingTarget(final Spot from, final Spot to) {
        int x;
        int y;
        if (!whiteToMove) {
            y = whiteSide;
        } else {
            y = 0;
        }
        if (from.getX() == 0 || to.getX() == 0) {
            x = knightJump;
        } else {
            x = whiteSide - 1;
        }
        return new Spot(x, y);
    }
    
    private boolean detectCastling(final Spot from, final Spot to) {
        return !(board[from.getX()][from.getY()] == null
                || board[to.getX()][to.getY()] == null
                || board[from.getX()][from.getY()].isWhite()
                != board[to.getX()][to.getY()].isWhite());
    }

    private boolean castling(final boolean white, final boolean kingSide) {
        int x = initCastlingX(kingSide);
        int y = initCastlingY(white);
        if (castlingDisabled(white, kingSide)) {
            return false;
        }
        updateAttackedSpots();
        boolean[][] attackedSpots = whiteAttacks;
        if (white) {
            attackedSpots = blackAttacks;
        }
        if (kingSide) {
            if (kingCastlingBlocked(x, y, attackedSpots)) {
                return false;
            }
        } else if (queenCastlingBlocked(x, y, attackedSpots)) {
            return false;
        }
        return true;
    }
    
    private boolean kingCastlingBlocked(int x, int y, boolean[][] attackedSpots) {
        return (board[x][y] != null || board[x + 1][y] != null
                    || attackedSpots[x - 1][y] || attackedSpots[x][y]
                    || attackedSpots[x + 1][y]);
    }
    
    private boolean queenCastlingBlocked(int x, int y, boolean[][] attackedSpots) {
        return (board[x][y] != null || board[x + 1][y] != null
                || board[x + 2][y]
                != null || attackedSpots[x + 1][y]
                || attackedSpots[x + 2][y]
                || attackedSpots[x + kingX - 1][y]);
    }
    
    private boolean castlingDisabled(boolean white, boolean kingSide) {
        return (kingSide && white && !whiteKingCastling) || (!kingSide && white
                && !whiteQueenCastling) || (kingSide && !white 
                && !blackKingCastling)
                || (!kingSide && !white && !blackQueenCastling);
    }
    
    private int initCastlingX(boolean kingSide) {
        if (kingSide) {
            return kingX + 1;
        } else {
            return 1;
        }
    }
    
    private int initCastlingY(boolean white) {
        if (white) {
            return whiteSide;
        } else {
            return 0;
        }
    }

    private Piece[][] copyBoard() {
        Piece[][] tempBoard = new Piece[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                tempBoard[x][y] = board[x][y];
            }
        }
        return tempBoard;
    }

    private ArrayList<Spot> generateMovesFor(final Spot spot, 
            final PieceType type) {
        ArrayList<Spot> dirs = new ArrayList<>();

        if (type == PieceType.BISHOP) {
            dirs.addAll(bishopDirections);
        } else if (type == PieceType.ROOK) {
            dirs.addAll(rookDirections);
        } else {
            dirs.addAll(bishopDirections);
            dirs.addAll(rookDirections);
        }

        ArrayList<Spot> moves = generateMovesFromDirs(spot, dirs);

        return moves;
    }

    private ArrayList<Spot> generateMovesFromDirs(final Spot spot, 
            final ArrayList<Spot> dirs) {
        boolean isWhite = board[spot.getX()][spot.getY()].isWhite();
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
            if (board[x][y] == null) {
                moves.add(new Spot(x, y));
                curSpot.setX(curSpot.getX() + dir.getX());
                curSpot.setY(curSpot.getY() + dir.getY());
                continue;
            }
            if (board[x][y].isWhite() != isWhite) {
                moves.add(new Spot(x, y));
            }
            break;
        }
    }

    private ArrayList<Spot> generateKnightMoves(final Spot spot) {
        ArrayList<Spot> moves = new ArrayList<>();
        for (Spot move : knightMoves) {
            moves.add(new Spot(spot.getX() + move.getX(),
                    spot.getY() + move.getY()));
        }
        return moves;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public boolean whiteToMove() {
        return whiteToMove;
    }

    public ArrayList<Spot> getPiecesOnBoard() {
        ArrayList<Spot> pieces = new ArrayList<>();
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (board[x][y] != null) {
                    pieces.add(new Spot(x, y));
                }
            }
        }
        return pieces;
    }
    
    
    public int getTurn() {
        return turns;
    }

}
