# Javachess

Java chess is a chess game implemented in Java. It is a practice project for a Computer Science course at University of Helsinki, by Arttu Kangas. The game will have at least normal chess against human or AI, and the games will be saved and can be rewatched.


### Documentation

__Current state__ of the project is that there is a runnable version that has main menu, and if you click "Play against human" there is a chess board. You can already move and capture pieces. Playing chess against a human sitting next to you (or yourself) is thus possible. However, the game does not yet prevent moving in situation where a check is produced, nor does the game ever end, but moves are otherwise only possible in places specified by the chess rules.

How to run it on linux with maven: <code>mvn compile exec:java -Dexec.mainClass=javachess.Main</code>

+ [Requirement specification](https://github.com/ShootingStar91/Javachess/blob/master/documentation/requirementspecification.md)
+ [Working hours tracking](https://github.com/ShootingStar91/Javachess/blob/master/documentation/workinghours.md)
