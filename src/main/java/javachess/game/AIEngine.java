
package javachess.game;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class provides an artificial player that will find out which
 * moves are possible for it and chooses one of those.
 */
public class AIEngine {
    
    Game game;
    ArrayList<Spot> movesFrom;
    ArrayList<Spot> movesTo;
    
    /**
     * Initializes the AIEngine
     * @param game The game object which will use this AI
     */
    public AIEngine(Game game) {
        this.game = game;
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
        Random random = new Random();
        
        int rand = random.nextInt(movesFrom.size());
        phase = game.move(movesFrom.get(rand), movesTo.get(rand));
        
        if (phase == Phase.PROMOTION) {
            game.promote(movesTo.get(rand), PieceType.QUEEN);
            return Phase.PLAY;
        }
        
        return phase;
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
    
}
