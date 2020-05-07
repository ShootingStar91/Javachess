
package javachess.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class provides an artificial player that will find out which
 * moves are possible for it and chooses one of those.
 */
public final class AIEngine {
    
    Game game;
    //ArrayList<Spot> movesFrom;
    //ArrayList<Spot> movesTo;
    HashMap<PieceType, double[][]> pieceScores;
    private int searchDepth;
    private int bestMoveIndex;
    private final int maxSearchDepth = 3;
    private int calc = 0;
    
    /**
     * Initializes the AIEngine
     * @param game The game object which will use this AI
     */
    public AIEngine(Game game) {
        searchDepth = 0;
        this.game = game;
        pieceScores = new HashMap<>();
        initPieceScores();
    }
    
    private void initPieceScores() {
        double[][] pawnScores = initPieceTypeScores(PieceType.PAWN);
        double[][] pawnPositionScores = 
                {{0, 0, 0, 0, 0, 0, 0, 0},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {1, 1, 2, 3, 3, 2, 1, 1},
                {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
                {0, 0, 0, 2, 2, 0, 0, 0},
                {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
                {0.5, 1.0, 1.0, -2, -2, 1, 1, 0.5},
                {0, 0, 0, 0, 0, 0, 0, 0}};
        addArrays(pawnScores, pawnPositionScores);
        double[][] knightScores = initPieceTypeScores(PieceType.KNIGHT);
        double[][] knightPositionScores =
                {{-5, -4, -3, -3, -3, -3, -4, -5},
                {-4, -2, 0, 0, 0, 0, -2, -4},
                {-3, 0, 1, 1.5, 1.5, 1, 0, -3},
                {-3, 0.5, 1.5, 2, 2, 1.5, 0.5, -3},
                {-3, 0, 1.5, 2, 2, 1.5, 0, -3},
                {3, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3},
                {-4, -2, 0, 0.5, 0.5, 0, -2, -4},
                {-5, -4, -3, -3, -3, -3, -4, -5}};        
        addArrays(knightScores, knightPositionScores);
        double[][] bishopScores = initPieceTypeScores(PieceType.BISHOP);
        double[][] bishopPositionScores =
                {{-2, -1, -1, -1, -1, -1, -1, -1},
                {-1, 0, 0, 0, 0, 0, 0, -1},
                {-1, 0, 0.5, 1, 1, 0.5, 0, -1},
                {-1, 0.5, 0.5, 1, 1, 0.5, 0.5, -1},
                {-1, 0, 1, 1, 1, 1, 0, -1},
                {-1, 1, 1, 1, 1, 1, 1, -1},
                {-1, 0.5, 0, 0, 0, 0, 0.5, -1},
                {-2, -1, -1, -1, -1, -1, -1, -2}};
        addArrays(bishopScores, bishopPositionScores);
        double[][] rookScores = initPieceTypeScores(PieceType.ROOK);
        double[][] rookPositionScores =
                {{0, 0, 0, 0, 0, 0, 0, 0},
                {0.5, 1, 1, 1, 1, 1, 1, 0.5},
                {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                {-0.5, 0, 0, 0, 0, 0, 0, -0.5},
                {0, 0, 0, 0.5, 0.5, 0, 0, 0}};
        addArrays(rookScores, rookPositionScores);
        double[][] queenScores = initPieceTypeScores(PieceType.QUEEN);
        double[][] queenPositionScores =
                {{-2, -1, -1, -0.5, -0.5, -1, -1, -2},
                {-1, 0, 0, 0, 0, 0, 0, -1},
                {-1, 0, 0.5, 0.5, 0.5, 0.5, 0, -1},
                {-0.5, 0, 0.5, 0.5, 0.5, 0.5, 0, -0.5},
                {0, 0, 0.5, 0.5, 0.5, 0.5, 0, -0.5},
                {-1, 0.5, 0.5, 0.5, 0.5, 0.5, 0, -1},
                {-1, 0, 0.5, 0, 0, 0, 0, -1},
                {-2, -1, -1, -0.5, -0.5, -1, -1, -2}};
        addArrays(queenScores, queenPositionScores);
        double[][] kingScores = initPieceTypeScores(PieceType.KING);
        double[][] kingPositionScores =
                {{-3, -4, -4, -5, -5, -4, -4, -3},
                {-3, -4, -4, -5, -5, -4, -4, -3},
                {-3, -4, -4, -5, -5, -4, -4, -3},
                {-3, -4, -4, -5, -5, -4, -4, -3},
                {-2, -3, -3, -4, -4, -3, -3, -2},
                {-1, -2, -2, -2, -2, -2, -2, -1},
                {2, 2, 0, 0, 0, 0, 2, 2},
                {2, 3, 1, 0, 0, 1, 3, 2}};
        addArrays(kingScores, kingPositionScores);
        pieceScores.put(PieceType.PAWN, pawnScores);
        pieceScores.put(PieceType.KNIGHT, knightScores);
        pieceScores.put(PieceType.BISHOP, bishopScores);
        pieceScores.put(PieceType.ROOK, rookScores);
        pieceScores.put(PieceType.QUEEN, queenScores);
        pieceScores.put(PieceType.KING, kingScores);
    }

    private void addArrays(double[][] destArray, double[][] sourceArray) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                destArray[y][x] += sourceArray[y][x];
            }
        }
    }
    
    private double[][] initPieceTypeScores(PieceType type) {
        double [][] pieceScoreArray = new double[8][8];
        int typeScore = 0;
        switch (type) {
            case PAWN:
                typeScore = 10;
                break;
            case KNIGHT:
                typeScore = 30;
                break;
            case BISHOP:
                typeScore = 30;
                break;
            case ROOK:
                typeScore = 50;
                break;
            case QUEEN:
                typeScore = 90;
                break;
            case KING:
                typeScore = 900;
                break;
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                pieceScoreArray[x][y] = typeScore;
            }
        }
        return pieceScoreArray;
    }
    
    /**
     * Chooses a move and tells game-class to execute it.
     * 
     * @return Phase which the game-class returned to this method after
     * executing the move
     */
    public Phase doTurn() {
        System.out.println("board score " + boardScore(game.getBoard()));
        ArrayList<Spot> movesFrom = new ArrayList<>();
        ArrayList<Spot> movesTo = new ArrayList<>();
        getMoves(movesFrom, movesTo, game);
        calc = 0;
        //System.out.println("SIZE: " + movesFrom.size() + " " + movesTo.size() + " whitetomove="+game.whiteToMove());
        double bestScore = -9999;
        for (int i = 0; i < movesFrom.size(); i++) {
            Spot from = movesFrom.get(i);
            Spot to = movesTo.get(i);
            Game gameCopy = game.copyGame();
            Phase phase = gameCopy.move(from, to);
            if (phase == Phase.PROMOTION) {
                gameCopy.promote(to, PieceType.QUEEN);
            }

            double score = chooseMove(1, gameCopy, -10000, 10000);
            if (score > bestScore) {
                bestScore = score;
                bestMoveIndex = i;
            }
        }
        //System.out.println("SIZE: " + movesFrom.size() + " " + movesTo.size() + " whitetomove="+game.whiteToMove());
        //System.out.println(bestMoveIndex);

        //getMoves(movesFrom, movesTo, game);
        //System.out.println("SIZE: " + movesFrom.size() + " " + movesTo.size() + " whitetomove="+game.whiteToMove());
        System.out.println("Moves searched: " + calc);
        if (bestMoveIndex > movesFrom.size()) {
            System.out.println("choosemove failed with index " + bestMoveIndex);
            bestMoveIndex = movesFrom.size() - 1;
            
        }
        Phase phase = game.move(movesFrom.get(bestMoveIndex), movesTo.get(bestMoveIndex));
        if (phase == Phase.PROMOTION) {
            game.promote(movesTo.get(bestMoveIndex), PieceType.QUEEN);
            return Phase.PLAY;
        }
        
        return phase;
    }
                /** 
             * Random move:
                Random random = new Random();
                moveIndex = random.nextInt(movesFrom.size());
            */
    
    /*
        palauttaa vähiten huonon siirron
    */
    private double chooseMove(int depth, Game gameCopy, double alpha, double beta) {
        calc++;
        if (depth == maxSearchDepth) {
            double score = boardScore(gameCopy.getBoard());
            return score;
        }
        
        double score = -9999;
        if (gameCopy.whiteToMove()) score *= -1;
        ArrayList<Spot> movesFrom = new ArrayList<>();
        ArrayList<Spot> movesTo = new ArrayList<>();
        getMoves(movesFrom, movesTo, gameCopy);
        
        for (int i = 0; i < movesFrom.size(); i++) {
            calc++;
            Spot from = movesFrom.get(i);
            Spot to = movesTo.get(i);
            Game newGameCopy = gameCopy.copyGame();
            Phase phase = newGameCopy.move(from, to);
            if (phase == Phase.PROMOTION) {
                newGameCopy.promote(to, PieceType.QUEEN);
            }

            if (!gameCopy.whiteToMove()) {
                score = Math.max(chooseMove(depth + 1, newGameCopy, alpha, beta), score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    return score;
                }
            } else {
                score = Math.min(chooseMove(depth + 1, newGameCopy, alpha, beta), score);
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    return score;
                }
            }
        }

        return score;
    }
    
    private void getMoves(ArrayList<Spot> movesFrom, ArrayList<Spot> movesTo, Game sourceGame) {
        movesFrom.clear();
        movesTo.clear();
        for (Spot spot : sourceGame.getPiecesOnBoard()) {
            Piece piece = sourceGame.getBoard()[spot.getX()][spot.getY()];
            if (piece.isWhite() != sourceGame.whiteToMove()) {
                continue;
            }
            for (Spot moveTo : sourceGame.getMoves(spot)) {
                movesFrom.add(spot);
                movesTo.add(moveTo);
            }
        }
    }
    
    private double getPieceScore(Piece piece, Spot spot) {
        if (!piece.isWhite()) { 
            spot.setY(7 - spot.getY());
            spot.setX(7- spot.getX());
        }
        return pieceScores.get(piece.getType())[spot.getX()][spot.getY()];
    }
    
    private double boardScore(Piece[][] board) {
        double totalScore = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = board[x][y];
                if (piece == null) continue;
                // notice spot has swapped y x for ai positionscores:
                double newScore = getPieceScore(piece, new Spot(y, x));
                if (piece.isWhite()) {
                    totalScore-=newScore;
                    
                } else {
                    totalScore+=newScore;
                }
            }
        }
        return totalScore;
    }
    
}
