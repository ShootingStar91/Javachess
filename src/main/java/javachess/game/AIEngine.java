package javachess.game;

import java.util.ArrayList;
import java.util.HashMap;
import javachess.userinterface.UserInterface;

/**
 * This class provides an artificial player that will find out which moves are
 * possible for it and chooses one of those.
 */
public final class AIEngine {

    Game game;
    HashMap<PieceType, double[][]> pieceScores;
    private final int maxSearchDepth = 3;

    /**
     * Initializes the AIEngine
     *
     * @param game The game object which will use this AI
     */
    public AIEngine(Game game) {
        this.game = game;
        pieceScores = new HashMap<>();
        initPieceScores();
    }

    private void initPieceScores() {
        initPawnScores();
        initKnightScores();
        initBishopScores();
        initRookScores();
        initQueenScores();
        initKingScores();
    }
    
    private void initQueenScores() {
        double[][] queenScores = initPieceTypeScores(PieceType.QUEEN);
        double[][] queenPositionScores
                = {{-2, -1, -1, -0.5, -0.5, -1, -1, -2},
                    {-1, 0, 0, 0, 0, 0, 0, -1},
                    {-1, 0, 0.5, 0.5, 0.5, 0.5, 0, -1},
                    {-0.5, 0, 0.5, 0.5, 0.5, 0.5, 0, -0.5},
                    {0, 0, 0.5, 0.5, 0.5, 0.5, 0, -0.5},
                    {-1, 0.5, 0.5, 0.5, 0.5, 0.5, 0, -1},
                    {-1, 0, 0.5, 0, 0, 0, 0, -1},
                    {-2, -1, -1, -0.5, -0.5, -1, -1, -2}};
        addArrays(queenScores, queenPositionScores);
        pieceScores.put(PieceType.QUEEN, queenScores);
    }
    
    private void initRookScores() {
        double[][] rookScores = initPieceTypeScores(PieceType.ROOK);
        double[][] rookPositionScores
                = {{0, 0, 0, 0, 0, 0, 0, 0},
                    {0.5, 1, 1, 1, 1, 1, 1, 0.5},
                    {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                    {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                    {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                    {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                    {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                    {0, 0, 0, 0.5, 0.5, 0, 0, 0}};
        addArrays(rookScores, rookPositionScores);
        pieceScores.put(PieceType.ROOK, rookScores);
    }
    
    private void initKnightScores() {
        double[][] knightScores = initPieceTypeScores(PieceType.KNIGHT);
        double[][] knightPositionScores
                = {{-5, -4, -3, -3, -3, -3, -4, -5},
                    {-4, -2, 0, 0, 0, 0, -2, -4},
                    {-3, 0, 1, 1.5, 1.5, 1, 0, -3},
                    {-3, 0.5, 1.5, 2, 2, 1.5, 0.5, -3},
                    {-3, 0, 1.5, 2, 2, 1.5, 0, -3},
                    {3, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3},
                    {-4, -2, 0, 0.5, 0.5, 0, -2, -4},
                    {-5, -4, -3, -3, -3, -3, -4, -5}};
        addArrays(knightScores, knightPositionScores);
        pieceScores.put(PieceType.KNIGHT, knightScores);
    }

    private void initPawnScores() {
        double[][] pawnScores = initPieceTypeScores(PieceType.PAWN);
        double[][] pawnPositionScores
                = {{0, 0, 0, 0, 0, 0, 0, 0},
                    {5, 5, 5, 5, 5, 5, 5, 5},
                    {1, 1, 2, 3, 3, 2, 1, 1},
                    {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
                    {0, 0, 0, 2, 2, 0, 0, 0},
                    {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
                    {0.5, 1.0, 1.0, -2, -2, 1, 1, 0.5},
                    {0, 0, 0, 0, 0, 0, 0, 0}};
        addArrays(pawnScores, pawnPositionScores);
        pieceScores.put(PieceType.PAWN, pawnScores);
    }
    
    private void initKingScores() {    
        double[][] kingScores = initPieceTypeScores(PieceType.KING);
        double[][] kingPositionScores
                = {{-3, -4, -4, -5, -5, -4, -4, -3},
                    {-3, -4, -4, -5, -5, -4, -4, -3},
                    {-3, -4, -4, -5, -5, -4, -4, -3},
                    {-3, -4, -4, -5, -5, -4, -4, -3},
                    {-2, -3, -3, -4, -4, -3, -3, -2},
                    {-1, -2, -2, -2, -2, -2, -2, -1},
                    {2, 2, 0, 0, 0, 0, 2, 2},
                    {2, 3, 1, 0, 0, 1, 3, 2}};
        addArrays(kingScores, kingPositionScores);
        pieceScores.put(PieceType.KING, kingScores);
    }
    
    private void initBishopScores() {
        double[][] bishopScores = initPieceTypeScores(PieceType.BISHOP);
        double[][] bishopPositionScores
                = {{-2, -1, -1, -1, -1, -1, -1, -1},
                    {-1, 0, 0, 0, 0, 0, 0, -1},
                    {-1, 0, 0.5, 1, 1, 0.5, 0, -1},
                    {-1, 0.5, 0.5, 1, 1, 0.5, 0.5, -1},
                    {-1, 0, 1, 1, 1, 1, 0, -1},
                    {-1, 1, 1, 1, 1, 1, 1, -1},
                    {-1, 0.5, 0, 0, 0, 0, 0.5, -1},
                    {-2, -1, -1, -1, -1, -1, -1, -2}};
        addArrays(bishopScores, bishopPositionScores);
        pieceScores.put(PieceType.BISHOP, bishopScores);
    }

    private void addArrays(double[][] destArray, double[][] sourceArray) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                destArray[y][x] += sourceArray[y][x];
            }
        }
    }

    private double[][] initPieceTypeScores(PieceType type) {
        double[][] pieceScoreArray = new double[8][8];
        int typeScore = getTypeScore(type);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                pieceScoreArray[x][y] = typeScore;
            }
        }
        return pieceScoreArray;
    }
    
    private int getTypeScore(PieceType type) {
        switch (type) {
            case PAWN:
                return 10;
            case KNIGHT:
                return 30;
            case BISHOP:
                return 30;
            case ROOK:
                return 50;
            case QUEEN:
                return 90;
            case KING:
                return 900;
        }
        return 0;
    }

    /**
     * Chooses a move and tells game-class to execute it. Calls UserInterface's
     * drawBoard() method afterwards to show the user the move instantly after
     * it is done.
     * @return Phase which the game-class returned to this method after
     * executing the move
     */
    public Phase doTurn() {
        ArrayList<Move> moves = new ArrayList<>();
        getMoves(moves, game);
        int bestMoveIndex = chooseMoveRoot(moves);
        if (bestMoveIndex < 0) {
            bestMoveIndex = 0;
        }
        Phase phase = game.move(moves.get(bestMoveIndex));
        if (phase == Phase.PROMOTION) {
            game.promote(moves.get(bestMoveIndex).to(), PieceType.QUEEN);
            return Phase.PLAY;
        }
        return phase;
    }


    private int chooseMoveRoot(ArrayList<Move> moves) {
        int bestMoveIndex = -1;
        double bestScore = -9999;
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            Game gameCopy = game.copyGame();
            Phase phase = gameCopy.move(move);
            if (phase == Phase.PROMOTION) {
                gameCopy.promote(move.to(), PieceType.QUEEN);
            }
            double score = chooseMove(1, gameCopy, -10000, 10000);
            if (score > bestScore) {
                bestScore = score;
                bestMoveIndex = i;
            }
        }
        return bestMoveIndex;
    }

    private double chooseMove(int depth, Game gameCopy, double alpha, double beta) {
        if (depth == maxSearchDepth) {
            double score = boardScore(gameCopy.getBoard().getBoard());
            return score;
        }
        double score = -9999;
        if (gameCopy.whiteToMove()) {
            score *= -1;
        }
        ArrayList<Move> moves = new ArrayList<>();
        getMoves(moves, gameCopy);
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            Game newGameCopy = gameCopy.copyGame();
            Phase phase = newGameCopy.move(move);
            if (phase == Phase.PROMOTION) {
                newGameCopy.promote(move.to(), PieceType.QUEEN);
            }
            if (!gameCopy.whiteToMove()) {
                score = Math.max(chooseMove(depth + 1, newGameCopy, alpha, beta), score);
                alpha = Math.max(alpha, score);
            } else {
                score = Math.min(chooseMove(depth + 1, newGameCopy, alpha, beta), score);
                beta = Math.min(beta, score);
            }
            if (beta <= alpha) {
                return score;
            }
        }
        return score;
    }

    private void getMoves(ArrayList<Move> moves, Game sourceGame) {
        moves.clear();
        for (Spot spot : sourceGame.getBoard().getPiecesOnBoard()) {
            Piece piece = sourceGame.getBoard().get(spot);
            if (piece.isWhite() != sourceGame.whiteToMove()) {
                continue;
            }
            for (Spot moveTo : sourceGame.getBoard().get(spot).getMoves()) {
                moves.add(new Move(spot, moveTo));
            }
        }
    }

    private double getPieceScore(Piece piece, Spot spot) {
        if (!piece.isWhite()) {
            spot.setY(7 - spot.getY());
        }
        return pieceScores.get(piece.getType())[spot.getY()][spot.getX()];
    }

    private double boardScore(Piece[][] board) {
        double totalScore = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board[x][y];
                if (piece == null) {
                    continue;
                }
                double newScore = getPieceScore(piece, new Spot(x, y));
                if (piece.isWhite()) {
                    totalScore -= newScore;
                } else {
                    totalScore += newScore;
                }
            }
        }
        return totalScore;
    }

}
