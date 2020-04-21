# Javachess

Java chess is a chess game implemented in Java. It is a practice project for a Computer Science course at University of Helsinki, by Arttu Kangas. The game will have at least normal chess against human or AI, and the games will be saved and can be rewatched.


### Documentation

Current state of the game: Only the "Play with human" -button works. You can almost play normal chess. Upgrading pawn not yet possible and castling conditions need fine-tuning. Game is saved in a text file after checkmate, user is prompted the title in command line. Text file is at /src/main/resources/savedgames.txt

How to run it on linux with maven: <code>mvn compile exec:java -Dexec.mainClass=javachess.main.Main</code>

+ [Requirement specification](https://github.com/ShootingStar91/Javachess/blob/master/documentation/requirementspecification.md)
+ [Working hours tracking](https://github.com/ShootingStar91/Javachess/blob/master/documentation/workinghours.md)
