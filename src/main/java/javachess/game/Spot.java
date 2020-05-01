
package javachess.game;

/**
 * Class which represents a single spot within a game.
 * It consists of x and y integers, simple getters and
 * setters for those, as well as a method to quickly check
 * if the spot is within the limits of the chess board.
 */
public class Spot {
    
    int x;
    int y;

    public Spot() {
        x = -1;
        y = -1;
    }
    public Spot(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { 
        return x; 
    }
    
    public int getY() { 
        return y; 
    }
    
    public void setX(int x) { 
        this.x = x; 
    }
    public void setY(int y) { 
        this.y = y; 
    }
    
    /**
     * Checks if the spot is on the chess board.
     * @return boolean value indicating if this spot is within the limits of 
     * the board
     */
    public boolean onBoard() {
        return this.x >= 0 && this.x < 8 && this.y >= 0 && this.y < 8;
    }
    
    public String toString() {
        return "" + x + "" + y;
    }
}
