# Package and class structure

![Architecture](https://github.com/ShootingStar91/Javachess/blob/master/documentation/packagediagram.png)

Main-class is here omitted, since it's only purpose is to launch UserInterface-class. It has its own package, "main".

Packages explained:
+ **ui** is responsible for showing user interface and telling game-class what input user has given. This all happens in UserInterface-class.
+ **game** has the game logic class Game, as well as classes which support it: AIEngine for AI player, Spot to represent a spot on the board (defined by integers x and y) and Piece to represent a single piece on board
+ **dao** has class Dao, which contains all methods that deal with saving and loading games, and it is used through ui-package

# User interface

The interface has three different scenes:
+ Main menu
+ Game scene
  + Accessed by two buttons of main menu: Either game against human or AI
+ Loading previously saved game
  + Has buttons of each saved game

The three scenes are added onto a single stage whenever they are used.

Text input is asked by a text dialog that popups separately from the window when a game ends. Also alert is shown if no games are found, or if user wants to leave a game that is not finished.

# Application logic

The sequence diagram below details the starting and playing first turn of a game against AI

![Sequence diagram 1](https://github.com/ShootingStar91/Javachess/blob/master/documentation/sequencediagram.png)

# Saving games

Games are saved in a text file savedgames.txt at root folder of the application (in case of .jar, the same folder where it is).

The files are not meant to be readable without the application. The games have their title, then newline, then on each line a character representation of the board for every turn from beginning to end.

