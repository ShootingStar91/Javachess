package javachess.userinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javachess.dao.Dao;
import javachess.game.Game;
import javachess.game.Phase;
import javachess.game.Piece;
import javachess.game.PieceType;
import javachess.game.Spot;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 *
 * @author Arttu Kangas
 */

public class UserInterface extends Application {
    
    Dao gameDao;
    Scene mainMenu, gameScene, gameSelectionScene;
    Game game;
    boolean pieceSelected;
    Spot selectedPiece;
    GridPane gamePane;
    GridPane selectionPane;
    ImageView [][] boardImages;
    HashMap<String, Image> images;
    ArrayList<Spot> highlightedSpots;
    Stage window;
    Spot promotionSpot;
    Label turnLabel;
    ArrayList<Piece[][]> boardHistory;
    boolean watchingGame;
    int turn;
    
    
    @Override
    public void start(Stage gameWindow) {
        window = gameWindow;
        images = new HashMap<>();
        pieceSelected = false;
        selectedPiece = new Spot();
        boardImages = new ImageView[8][9];
        gamePane = new GridPane();
        gamePane.setPadding(new Insets(20, 20, 20, 20));
        gamePane.setMinSize(100, 100);
        gameScene = new Scene(gamePane);
        turnLabel = new Label();
        watchingGame = false;
        turn = 0;
        selectionPane = new GridPane();
        gameDao = new Dao();
        gameSelectionScene = new Scene(selectionPane);
        selectionPane.setPadding(new Insets(100, 100, 100, 100));
        selectionPane.setVgap(20);
        selectionPane.setHgap(20);

        
        initImages();
        gameWindow.getIcons().add(images.get("qll"));

        for (int x = 2; x <= 5; x++) {
            gamePane.add(new ImageView(), x, 0);
        }
        gamePane.add(turnLabel, 0, 0);
        for (int x = 0; x < 8; x++) {
            gamePane.getColumnConstraints().add(new ColumnConstraints(80));
        }
        for (int y = 0; y < 9; y++) {
            gamePane.getRowConstraints().add(new RowConstraints(80));
        }
        
        Button returnToMenu = new Button("Exit");
        returnToMenu.setOnAction(event -> {
            exitButton();
        });
        gamePane.add(returnToMenu, 7, 0);
        Button cancelSelection = new Button("Return to main menu");
        cancelSelection.setOnAction(event -> {
            window.setScene(mainMenu);
        });
        selectionPane.add(cancelSelection, 7, 0);

        
        window.setTitle("Javachess 0.82");
        window.setMinHeight(400);
        window.setMinWidth(500);
        
        GridPane pane = new GridPane();
        mainMenu = new Scene(pane);

        pane.setVgap(10);
        pane.setHgap(10);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(50, 50, 50, 50));
        
        
        
                
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 9; y++) {
                ImageView imageView = new ImageView();
                
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (watchingGame) return;
                        int row = GridPane.getRowIndex(imageView);
                        int col = GridPane.getColumnIndex(imageView);
                        drawBoard();
                        
                        if (row == 0 && col >= 2 && col <=5) {
                            
                            PieceType type = PieceType.QUEEN;
                            switch (col) {
                                case 2:
                                    type = PieceType.QUEEN;
                                    break;
                                case 3:
                                    type = PieceType.ROOK;
                                    break;
                                case 4:
                                    type = PieceType.BISHOP;
                                    break;
                                case 5:
                                    type = PieceType.KNIGHT;
                                    break;
                            }
                            game.promote(promotionSpot, type);
                            showPromotionBar(false);
                        } else {
                            clickedOn(col, row - 1);
                        }
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

        });
        
        
        
        Button playAI = new Button();
        playAI.setText("Play against AI");
        
        playAI.setOnAction(event -> {
            startGame(true);
        });
        
        Button rewatch = new Button();
        rewatch.setText("Rewatch a previously played game");
        rewatch.setOnAction(event -> {
            if (loadGames()) {
                window.setScene(gameSelectionScene);
            } else {
               Alert alert = new Alert(AlertType.INFORMATION);
               alert.setTitle("No games found");
               alert.setHeaderText(null);
               alert.setContentText("No saved games were found!");
               alert.showAndWait();
            }
        });

        pane.add(label, 0, 0);
        pane.add(playHuman, 0, 2);
        pane.add(playAI, 0, 3);
        pane.add(rewatch, 0, 4);

        window.setScene(mainMenu);
        window.show();
    }
    
    public void exitButton() {
        if (!watchingGame) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit game");
            alert.setHeaderText("Game will be lost");
            alert.setContentText("Do you really want to stop playing?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                return;
            }
        }
        exitOk();
    }
    
    
    
    public void exitOk() {
        game = null;
        watchingGame = false;
        window.setScene(mainMenu);
    }
    
    public boolean loadGames() {
        boolean gamesAvailable = true;
        int buttonRow = 2;
        int buttonCol = 2;
       
        ArrayList<String> availableGames = gameDao.getAvailableGames();
        if (availableGames == null || availableGames.isEmpty()) {
            gamesAvailable = false;
        } else {
            for (String title : availableGames) {

                Button button = new Button(title);

                button.setOnAction(event -> {
                    boardHistory = gameDao.load(title);
                    turn = 0;
                    watchingGame = true;
                    window.setScene(gameScene);
                    showRewatchButtons(true);
                    drawBoard();
                });

                selectionPane.add(button, buttonCol, buttonRow);

                if (buttonCol > 6) {
                    buttonCol = 2;
                    buttonRow++;
                } else {
                    buttonCol++;
                }
            }
        }
        return gamesAvailable;
    }

    public void showRewatchButtons(boolean visible) {
        Image forward = images.get("forward");
        Image backward = images.get("backward");
        if (!visible) {
            forward = null;
            backward = null;
            } else {
            boardImages[3][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (turn > 0) {
                           turn--;
                           drawBoard();
                        }
                    }
                });
            boardImages[4][0].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (turn < boardHistory.size() - 1) {
                           turn++;
                           drawBoard();
                        }
                    }
                });
        }
        boardImages[3][0].setImage(backward);
        boardImages[4][0].setImage(forward);  
    }
    
    public void showPromotionBar(boolean visible) {
        
        if (visible) {
            String playerLetter = "d";
            if (!game.whiteToMove()) {
                playerLetter = "l";
            }
            boardImages[2][0].setImage(images.get("q" + playerLetter + "d"));
            boardImages[3][0].setImage(images.get("r" + playerLetter + "l"));
            boardImages[4][0].setImage(images.get("b" + playerLetter + "d"));
            boardImages[5][0].setImage(images.get("n" + playerLetter + "l"));
        } else {
            for (int x = 2; x <= 5; x++) {
                boardImages[x][0].setImage(null);
            }
        }
        drawBoard();
    }
    
    public void clickedOn(int col, int row) {
        if (highlightedSpots!=null) {
        
            if (!highlightedSpots.isEmpty()) {
                for (Spot spot : highlightedSpots) {
                    if (spot.getX()==col && spot.getY()==row) {
                        move(spot);
                        highlightedSpots=null;
                        drawBoard();
                        
                        return;
                    }
                }
                highlightedSpots = null;
                drawBoard();

            }
        }
        
        
        selectedPiece = new Spot(col, row);
        if (game.getBoard()[col][row] != null && game.getBoard()[col][row].isWhite() != game.whiteToMove()) return;
        
        highlight(col, row);
        
    }
    
    public void highlight(int col, int row) {
        highlightedSpots = game.getMoves(new Spot(col, row));
        if (highlightedSpots==null) return;
        for (Spot move : highlightedSpots) {
            InnerShadow innerShadow = new InnerShadow();
            innerShadow.setRadius(14);
            boardImages[move.getX()][move.getY()+1].setEffect(innerShadow);
        }
        
    }
    
    public void move(Spot to) {
        Phase phase = game.move(selectedPiece, to);
        if (phase == Phase.CHECKMATE || phase == Phase.STALEMATE) {
            highlightedSpots = null;
            drawBoard();
            gameFinished(phase);
            return;
        } else if (phase == Phase.PROMOTION) {
            showPromotionBar(true);
            promotionSpot = to;
        }
        drawBoard();
    }
    
    
    
    
    public void gameFinished(Phase phase) {
        TextInputDialog dialog = new TextInputDialog();
        String gameResult = "";
        if (phase == Phase.CHECKMATE) {
            gameResult = "Checkmate, ";
            if (game.whiteToMove()) {
                gameResult += "black won!";
            } else {
                gameResult += "white won!";
            }
        } else if (phase == Phase.STALEMATE) {
            gameResult = "The game was a stalemate! It means there is no winner. " +
                    "Either the game ran for 50 turns, or ended up in a situation" +
                    " where the player to move had no legal moves left, but was" +
                    " not in check.";
        }
        dialog.setTitle("Game finished");
        dialog.setHeaderText(gameResult);
        
        dialog.setContentText("Please write a title for the game to save it (or" 
                + " leave empty to not save it). \nNotice: Do not choose a name" 
                + " that you have used before. \nWARNING: Do not press enter!" 
                + " Click the OK button instead.");

        Optional<String> title = dialog.showAndWait();
        if (title.isPresent() && !(title.get().equals(""))) {
            String newTitle = title.get();
            gameDao.save(game, newTitle);
        }
        exitOk();

    }
    
    
    
    
    public void drawBoard() {
        if (game == null && !watchingGame) {
            return;
        }
        Piece[][] board;
        
        if (watchingGame) {
            board = boardHistory.get(turn);
        } else {
            board = game.getBoard();
        }
        
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                String img = "";
                String bg = "";
               
                if (board[x][y] != null) {
                    img += board[x][y].getLetter();
                }
                if (x % 2 == y % 2) {
                    bg += "l";
                } else {
                    bg += "d";
                }
                
                if (board[x][y]==null) {
                    img = bg;
                } else if (board[x][y].isWhite()) {
                    img += "l";
                    img += bg;
                } else {
                    img += "d";
                    img += bg;
                }
                
                
                boardImages[x][y + 1].setImage(images.get(img));
                boardImages[x][y + 1].setEffect(null);
            }
        }
        int turns = turn / 2;
        if (!watchingGame) turns = game.getTurn();
        turnLabel.setText("Turn: " + turns);
    }
    
    public void startGame(boolean againstAI) {
        
        watchingGame = false;
        
        game = new Game(againstAI);
        
        drawBoard();
        
        showRewatchButtons(false);
        window.setScene(gameScene);
        
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
        images.put("forward", new Image("/assets/forward.png"));
        images.put("backward", new Image("/assets/backward.png"));
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
