
package javachess.Dao;

        
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javachess.Game.Game;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import javachess.Game.Spot;


/**
 *
 * @author Arttu Kangas
 */
public class Dao {

    public Dao() {
        
    }
    
    public void save(Game game, String title) {
        
        String fileName = "src/main/resources/savedgames.txt";
        ArrayList<Spot> movesFrom = game.getMovesFrom();
        ArrayList<Spot> movesTo = game.getMovesTo();

        try (FileWriter fileWriter = new FileWriter(fileName, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter writer = new PrintWriter(bufferedWriter)) {
            writer.println(title);
            for (int i=0; i<movesFrom.size(); i++) {
                writer.println(movesFrom.get(i).toString()+movesTo.get(i).toString());
            }
            
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
    }
    
    public void read() {
        
    }
    
    
}
