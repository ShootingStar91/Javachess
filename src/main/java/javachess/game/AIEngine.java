
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
    ArrayList<Spot> movesFrom;
    ArrayList<Spot> movesTo;
    HashMap<PieceType, double[][]> pieceScores;
    
    /**
     * Initializes the AIEngine
     * @param game The game object which will use this AI
     */
    public AIEngine(Game game) {
        this.game = game;
        pieceScores = new HashMap<>();
        //initPieceScores();
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
                typeScore = 40;
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
        movesFrom = new ArrayList<>();
        movesTo = new ArrayList<>();
        Phase phase;
        getMoves();
        
        int moveIndex = chooseMove();
        phase = game.move(movesFrom.get(moveIndex), movesTo.get(moveIndex));
        if (phase == Phase.PROMOTION) {
            game.promote(movesTo.get(moveIndex), PieceType.QUEEN);
            return Phase.PLAY;
        }
        
        return phase;
    }
    
    private int chooseMove() {
        int moveIndex = -1;
        /** Random move:*/
        Random random = new Random();
        
        moveIndex = random.nextInt(movesFrom.size());
        
        /*
        for (int i = 0; i < movesFrom.size(); i++) {
            
        }
        */
        return moveIndex;
    }
    
    private void getMoves() {
        for (Spot spot : game.getPiecesOnBoard()) {
            Piece piece = game.getBoard()[spot.getX()][spot.getY()];
            if (piece.isWhite()) {
                continue;
            }
            for (Spot moveTo : game.getMoves(spot)) {
                movesFrom.add(spot);
                movesTo.add(moveTo);
            }
        }
    }
    
    private double getPieceScore(Piece piece, Spot spot) {
        return pieceScores.get(piece.getType())[spot.getX()][spot.getY()];
    }
    
    private double boardScore(Piece[][] board) {
        double totalScore = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] == null) continue;
                // noticed spot has swapped y x for ai positionscores:
                totalScore += getPieceScore(board[x][y], new Spot(y, x));
            }
        }
        return totalScore;
    }
    
}
