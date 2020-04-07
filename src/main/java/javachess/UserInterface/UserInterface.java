package javachess.UserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javachess.Dao.Dao;
import javachess.Game.Game;
import javachess.Game.Piece;
import javachess.Game.Spot;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Arttu Kangas
 */

public class UserInterface extends Application {
    
    Scene mainMenu, gameScene;
    Game game;
    boolean pieceSelected;
    Spot selectedPiece;
    GridPane gamePane;
    ImageView [][] boardImages;
    HashMap<String, Image> images;
    ArrayList<Spot> highlightedSpots;
    
    @Override
    public void start(Stage window) {
        images = new HashMap<>();
        pieceSelected = false;
        selectedPiece = new Spot();
        boardImages = new ImageView[8][8];
        
        initImages();
        
        
        
        window.setTitle("Javachess 0.05");
        window.setMinHeight(600);
        window.setMinWidth(800);
        
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(50, 50, 50, 50));
        
        
        gamePane = new GridPane();
        gamePane.setPadding(new Insets(100, 100, 100, 100));
        
                
        for (int x = 0; x<8; x++) {
            for (int y = 0; y<8; y++) {
                ImageView imageView = new ImageView();
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = GridPane.getRowIndex(imageView);
                        int col = GridPane.getColumnIndex(imageView);
                        drawBoard();
                        clickedOn(col, row);
                    }
                });
                boardImages[x][y] = imageView;
                gamePane.add(imageView, x, y);
            }
        }
        
        Label label = new Label();
        label.setText("Welcome to play chess!");
        Button playHuman = new Button();
        playHuman.setText("Play against human");

        playHuman.setOnAction(event -> {
            startGame(false);
            window.setScene(gameScene);
        });
        
        
        
        Button playAI = new Button();
        playAI.setText("Play against AI");
        Button rewatch = new Button();
        rewatch.setText("Rewatch a previously played game");
        pane.add(label, 0, 0);
        pane.add(playHuman, 0, 2);
        pane.add(playAI, 0, 3);
        pane.add(rewatch, 0, 4);
        mainMenu = new Scene(pane);
        gameScene = new Scene(gamePane);

        window.setScene(mainMenu);
        window.show();
    }
    
    public void clickedOn(int col, int row) {
        
        if (highlightedSpots!=null) {
        // Piece selected
        
            if (!highlightedSpots.isEmpty()) {
                // Piece selected and can move
                
                for (Spot spot : highlightedSpots) {
                    if (spot.getX()==col && spot.getY()==row) {
                        move(spot);
                        highlightedSpots=null;
                        drawBoard();
                        
                        return;
                    }
                }
                highlightedSpots=null;
                drawBoard();

            }
        }
        
        
        selectedPiece = new Spot(col, row);
        if (game.getBoard()[col][row]!=null && game.getBoard()[col][row].isWhite() != game.whiteToMove()) return;
        
        highlight(col, row);
        
    }
    
    public void highlight(int col, int row) {
        highlightedSpots = game.getPotentialMoves(new Spot(col, row));
        if (highlightedSpots==null) return;
        for (Spot move : highlightedSpots) {
            InnerShadow innerShadow = new InnerShadow();
            innerShadow.setRadius(14);
            boardImages[move.getX()][move.getY()].setEffect(innerShadow);
        }
        
    }
    
    public void move(Spot to) {
        if(game.move(selectedPiece, to)) {
            checkMate();
            return;
        }
        drawBoard();
    }
    
    public void checkMate() {
        if (game.whiteToMove()) {
            System.out.println("Black won!");
        } else {
            System.out.println("White won!");
        }
        
        Dao gameSaver = new Dao();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write the title to save the game with");
        String title = scanner.nextLine();
        System.out.println("Game saved as " + title);
        gameSaver.save(game, title);
        
    }
    
    public void drawBoard() {
        
        Piece[][] board = game.getBoard();
        
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                String img = "";
                String bg = "";
               
                if (board[x][y]!=null) img += board[x][y].getLetter();
                if (x%2==y%2) {
                    bg+="l";
                } else {
                    bg+="d";
                }
                
                if (board[x][y]==null) {
                    img = bg;
                } else if (board[x][y].isWhite()) {
                    img+="l";
                    img+=bg;
                } else {
                    img+="d";
                    img+=bg;
                }
                
                
                boardImages[x][y].setImage(images.get(img));
                boardImages[x][y].setEffect(null);
            }
        }
    }
    
    public void startGame(boolean againstAI) {
        game = new Game(againstAI);
        
        drawBoard();
        
    }
    
    public void initImages() {
        String [] type = {"p", "n", "b", "r", "q", "k"};
        String [] color = {"l", "d"};
        for (int i=0; i<type.length; i++) {
            for (int j=0; j<2; j++) {
                for (int k=0; k<2; k++) {
                    String letters = "";
                    letters+=type[i]+color[j]+color[k];
                    String filename = "/assets/"+letters+".png";
                    images.put(letters, new Image(filename));
                }
            }
        }
        images.put("d", new Image("/assets/d.png"));
        images.put("l", new Image("/assets/l.png"));
    }
 
    public static void main(String[] args) {
        launch(args);
    }
    

    
}
