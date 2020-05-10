
package daotest;

import java.io.File;
import java.util.ArrayList;
import org.junit.Before;
import javachess.dao.Dao;
import javachess.game.Game;
import javachess.game.Piece;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Arttu Kangas
 */
public class DaoTest {
    
    Dao dao;
    String fileName;
    
    public DaoTest() {
        fileName = "jdbc:sqlite:testDatabase.db";
    }
    
    @Before
    public void init() {
        try {
            dao = new Dao(fileName);
        } catch (Exception e) {
        }
    }
    
    @After
    public void delete() {
        new File(fileName).delete();
    }
    
    @Test
    public void databaseFound() {
        assertTrue(new File(fileName).exists());
    }
    
    @Test
    public void savingAndReadingWorks() {
        Game game = new Game(false);
        dao.save(game, "testGame");
        ArrayList<String> titles = dao.loadAllTitles();
        ArrayList<Piece[][]> boardHistory = dao.load(1);
        assertTrue(boardHistory.size() == 1 && titles.size() == 1);
        
    }
    
}
