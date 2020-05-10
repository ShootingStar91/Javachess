
package javachess.game;

/**
 *
 * @author Arttu Kangas
 */
public class Move {
    private Spot from;
    private Spot to;
    
    public Move(Spot from, Spot to) {
        this.from = from;
        this.to = to;
    }

    public Spot from() {
        return from;
    }

    public void setFrom(Spot from) {
        this.from = from;
    }

    public Spot to() {
        return to;
    }

    public void setTo(Spot to) {
        this.to = to;
    }
    
}
