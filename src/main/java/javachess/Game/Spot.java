
package javachess.Game;

/**
 *
 * @author Arttu Kangas
 */
public class Spot {
    int x, y;
    public Spot() {
        x=-1;
        y=-1;
    }
    public Spot(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    
    public boolean onBoard() {
        return this.x>=0 && this.x < 8 && this.y>=0 && this.y < 8;
    }
    
    public String toString() {
        return ""+x+""+y;
    }
}
