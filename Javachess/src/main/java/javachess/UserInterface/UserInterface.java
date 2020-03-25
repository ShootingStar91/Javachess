package javachess.UserInterface;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Arttu Kangas
 */

public class UserInterface extends Application {
    
    Scene mainMenu, gameScene;
    
    @Override
    public void start(Stage window) {
        window.setTitle("Javachess 0.02");
        window.setMinHeight(600);
        window.setMinWidth(800);
        
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(50, 50, 50, 50));
        
        
        GridPane gamePane = new GridPane();
        
        Image piece = new Image("/assets/pawn.png");
        Label gamelabel = new Label();

        for (int x = 0; x<8; x++) {
            for (int y = 0; y<8; y++) {
                gamePane.add(new ImageView(piece), x, y);
            }
        }
        
        Label label = new Label();
        label.setText("Welcome to play chess!");
        Button playHuman = new Button();
        playHuman.setText("Play against human");

        playHuman.setOnAction(event ->
            window.setScene(gameScene)
        );
        
        
        
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
 
    public static void main(String[] args) {
        launch(args);
    }
}
