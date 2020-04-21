
package javachess.dao;

        
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javachess.game.Game;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import javachess.game.Piece;


public class Dao {
    
    String fileName;
    
    public Dao() {
        fileName = "savedgames.txt";
    }
    
    public void save(Game game, String title) {
        
        ArrayList<Piece[][]> boardHistory = game.getBoardHistory();
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                
            }
        }
        try (FileWriter fileWriter = new FileWriter(fileName, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter writer = new PrintWriter(bufferedWriter)) {
            writer.println(" " + title);
            for (Piece[][] board : boardHistory) {
                writeBoard(writer, board);
                writer.println();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        
    }
    
    public void writeBoard(PrintWriter writer, Piece[][] board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
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
                writer.write(character);
            }
        }
    }
    
    public ArrayList<String> getAvailableGames() {
        try {
            ArrayList<String> games = new ArrayList<>();
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                if (line.charAt(0) == ' ') {
                    games.add(line.substring(1, line.length()));
                }
                line = reader.readLine();
            }
            reader.close();
            return games;
        } catch (Exception exception) {
            return null;
        }
    }
    
    public ArrayList<Piece[][]> load(String gameName) {
        try {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            boolean reading = false;
            ArrayList<Piece[][]> boardHistory = new ArrayList<>();
            getBoardHistoryFromFile(line, reading, boardHistory, gameName, 
                    reader);
            reader.close();
            return boardHistory;
        } catch (Exception exception) {
            System.out.println("Could not read the file to load a game. Exception: " + exception);
            return null;
        }
    }
    
    private void getBoardHistoryFromFile(String line, boolean reading,
            ArrayList<Piece[][]> boardHistory, String gameName,
            BufferedReader reader) throws IOException {
        boolean gameFound = false;
        while (line != null) {
            reading = processLine(line, reading, boardHistory, gameName);
            if (line.equals(" " + gameName)) {
                if (gameFound) {
                    break;
                }
                gameFound = true;
            }
            line = reader.readLine();
        }
    }
    
    private boolean processLine(String line, boolean reading, 
            ArrayList<Piece[][]> boardHistory, String gameName) {
        if (line.charAt(0) == ' ') {
            reading = false;
        }
        if (reading) {
            Piece[][] board = new Piece[8][8];
            readBoard(line, board);
            boardHistory.add(board);
        }
        if (line.equals(" " + gameName)) {
            reading = true;
        }
        return reading;
    }
    
    private void readBoard(String line, Piece[][] board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                char character = line.charAt(x * 8 + y);
                if (character == '_') {
                    board[x][y] = null;
                } else {
                    board[x][y] = new Piece(character);
                }
            }
        }
    }
    
    
}
