# Package and class structure

![Architecture](https://github.com/ShootingStar91/Javachess/blob/master/documentation/packagediagram.png)

Main-class is here omitted, since it's only purpose is to launch UserInterface-class. It has its own package, "main".

Packages explained:
+ **ui** is responsible for showing user interface and telling game-class what input user has given. This all happens in UserInterface-class.
+ **game** has the game logic classes and enums, which are:
  + **Game** which handles the main logic of the chess game. It changes turns and saves each board on every turn to memory. It uses the Board-class to remember where the pieces are and interacts with it to generate all potential moves on each turn.
  + **Board** represents the chessboard with its pieces. It also saves variables that tell if the kings or rooks have been moved, which will prevent castling. It provides different methods for the Game-class to use in generating moves and checking if the game is in checkmate or not.
  + **AIEngine** is the engine of the AI-opponent. It chooses the best possible move for black player by the means of a minmax-algorithm. It has to know the Game-object which is used in playing in order to function.
  + **Spot** to represent a spot on the board (defined by integers x and y)
  + **Move** to represent a chess move, defined by two spots: "from" and "to" which have getters and setters
  + **Piece** to represent a single piece on board
  + **PieceType** is an enum which represents the different chesspieces such as PAWN and KING.
  + **Phase** is an enum which represents the games phase or state. PLAY is the default where the next player has moves left. CHECKMATE is when player has won. STALEMATE means a tie as defined by the chess rules. PROMOTION means that a pawn is promoted, and the Game-class wants input from either a user or AI Engine to know which piece type the pawn is promoted to.
+ **dao** has class Dao, which contains all methods that deal with saving and loading games, and it is used through ui-package

# User interface

The interface has three different scenes:
+ Main menu
+ Game scene
  + Accessed by two buttons of main menu: Either game against human or AI
  + When playing against AI Engine, the processing of AIEngine-class is threaded separately so that the player will see their own move first and the AI engine will move after it has processed. Meanwhile there is an icon showing that the AI is thinking above the chessboard.
+ Loading previously saved game
  + Has buttons of each saved game

The three scenes are added onto a single stage whenever they are used.

Text input is asked by a text dialog that popups separately from the window when a game ends. Also alert is shown if no games are found, or if user wants to leave a game that is not finished.

# Application logic

The sequence diagram below details the main logic that the UserInterface, Game and Board -classes interact by when the User plays against a human opponent.

![Sequence diagram 1](https://github.com/ShootingStar91/Javachess/blob/master/documentation/playhumansequence.png)

The following sequence diagram explains how the AIEngine is put to work by the UserInterface to provide AI opponent for the human player.

![Sequence diagram 2](https://github.com/ShootingStar91/Javachess/blob/master/documentation/aienginesequence.png)

# Saving games

The saving of the games is done through sqlite using jdbc. The application will check if there is a file named "javachessDatabase.db" in the root folder of the game, and if there is not, it will create it.

The database has one table Savedgames with three columns: id (primary key), name (title for the game) and boardhistory, which is a string that the load-method can parse and understand as a complete chessgame.

# Weaknesses of the architecture

The application logic was good, but there was some weaknesses. The game- and board- classes division is not completely clear: Some methods in them could belong in the other class. They were originally just one Game-class, but its length was close to 900 lines, and the checkstyle rules limited file length to 500. However, they do have a somewhat reasonable division: The game-class is more like a chess player who looks at the board and thinks where they can move, whereas the Board-class represents a "dead" board and does not itself generate any moves.

The AI Engine could be faster if the Game-class was optimized better. It has some repetition: clearing attacked spots and updating them again many times inside one turn. A different architecture might have made these repetitions easier to spot and fix.

One method in AIEngine class exceeds the maximum length of 20 lines by about 10 lines. I let this be because the method felt very difficult to divide, it is the recursive alpha-beta-pruned minmax algorithm.
