package javachess.UserInterface;

import java.util.ArrayList;
import javachess.Game.Game;
import javachess.Game.Spot;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    
    @Override
    public void start(Stage window) {
        pieceSelected = false;
        selectedPiece = new Spot();
        window.setTitle("Javachess 0.02");
        window.setMinHeight(600);
        window.setMinWidth(800);
        
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(50, 50, 50, 50));
        
        
        GridPane gamePane = new GridPane();
        gamePane.setPadding(new Insets(100, 100, 100, 100));
        
        
        Image piece = new Image("/assets/pawn.png");
        
        for (int x = 0; x<8; x++) {
            for (int y = 0; y<8; y++) {
                ImageView imageView = new ImageView(piece);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int row = GridPane.getRowIndex(imageView);
                        int col = GridPane.getColumnIndex(imageView);
                        System.out.println("Selected " + col + ", " + row);
                        if (selectedPiece.getX()==col && selectedPiece.getY()==row) {
                            pieceSelected = false;
                            return;
                        }
                        clickedOn(col, row);
                    }
                });
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
        pieceSelected = true;
        selectedPiece = new Spot(col, row);
        ArrayList<Spot> potentialMoves = game.getPotentialMoves(new Spot(col, row));
        
        
        
        
    }
    
    public void startGame(boolean againstAI) {
        game = new Game(againstAI);
        
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
