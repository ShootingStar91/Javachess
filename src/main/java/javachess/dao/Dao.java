
package javachess.dao;

import java.io.File;
import java.sql.*;
import javachess.game.Game;
import java.util.ArrayList;
import javachess.game.Piece;

/**
 * A class for a data access object. It provides methods
 * to save a board history of a game into a sqlite database.
 * @author arkangas
 */
public class Dao {
    
    private String databaseName;
    private final String fileName;
    
    /**
     * Initialize the dao.
     * @param fileName String of filename where the .db file should be located,
     * or will be created if it does not exist. Example: "javachessDatabase.db"
     * @throws SQLException 
     */
    public Dao(String fileName) throws SQLException {
        this.fileName = fileName;
        this.databaseName = "jdbc:sqlite:" + fileName;
        initDatabase();
    }
    
    private void initDatabase() {
        if (new File(fileName).exists()) {
            return;
        }
        try (Connection connection = DriverManager.getConnection(databaseName)) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE Savedgames (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, boardhistory TEXT)");
            connection.close();
        } catch (Exception exception) {
        }
    }
    
    /**
     * Method to save a game into the database.
     * @param game The game-object that has the board history to be saved.
     * @param title A title to save the game by.
     */
    public void save(Game game, String title) {
        ArrayList<Piece[][]> boardHistory = game.getBoardHistory();
        String gameString = createGameString(boardHistory);
        try {
            Connection connection = DriverManager.getConnection(databaseName);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Savedgames (name, boardhistory) VALUES (?, ?)");
            statement.setString(1, title);
            statement.setString(2, gameString);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException exception) {
        }
    }
    
    private String createGameString(ArrayList<Piece[][]> boardHistory) {
        String gameString = "";
        for (Piece[][] board : boardHistory) {
            gameString += createBoardString(board);
        }
        return gameString;
    }
    
    private String createBoardString(Piece[][] board) {
        String boardString = "";
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = board[x][y];
                String character;
                if (piece == null) {
                    character = "_";
                } else  {
                    character = piece.getLetter();
                    if (piece.isWhite()) {
                        character = character.toUpperCase();
                    }
                }
                boardString += character;
            }
        }
        return boardString;
    }

    /**
     * Loads all the game titles from the database into an ArrayList of strings.
     * @return ArrayList<String> all titles of games in database.
     */
    public ArrayList<String> loadAllTitles() {
        ArrayList<String> titles = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(databaseName);
            PreparedStatement query = connection.prepareStatement("SELECT name FROM Savedgames");
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                titles.add(resultSet.getString("name"));
            }
            resultSet.close();
            query.close();
            connection.close();
        } catch (Exception exception) {
        }
        return titles;
    }
    
    /**
     * Loads a single games board history into an ArrayList by the given id.
     * @param id to identify which game from database is wanted.
     * @return ArrayList<Piece[][]> Board history.
     */
    public ArrayList<Piece[][]> load(int id) {
        ArrayList<Piece[][]> boardHistory = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(databaseName);
            PreparedStatement query = connection.prepareStatement("SELECT boardhistory FROM Savedgames WHERE id = ?");
            query.setInt(1, id);
            ResultSet resultSet = query.executeQuery();
            boardHistory = processBoardHistory(resultSet.getString("boardhistory"));
            query.close();
            connection.close();
        } catch (Exception exception) {
        }
        return boardHistory;
    }
    
    private ArrayList<Piece[][]> processBoardHistory(String gameString) {
        ArrayList<Piece[][]> boardHistory = new ArrayList<>();
        for (int i = 0; i < gameString.length() - 63; i += 64) {
            Piece[][] board = new Piece[8][8];
            readBoard(gameString.substring(i, i + 64), board);
            boardHistory.add(board);
        }
        return boardHistory;
    }
    
    private void readBoard(String line, Piece[][] board) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                char character = line.charAt(y * 8 + x);
                if (character == '_') {
                    board[x][y] = null;
                } else {
                    board[x][y] = new Piece(character);
                }
            }
        }
    }
    
    
}
