# Javachess

Java chess is a chess game implemented in Java. It is a practice project for a Computer Science course at University of Helsinki, by Arttu Kangas. The game will have at least normal chess against human or AI, and the games will be saved and can be rewatched.


### Documentation

Current state of the game: Only the "Play with human" -button works. You can almost play normal chess, but there are a couple special cases not added yet: Castling is not yet disabled if king will be checked in new position. You also cannot promote a pawn. Checkmate so far is announced only by a system out print.

How to run it on linux with maven: <code>mvn compile exec:java -Dexec.mainClass=javachess.Main</code>

+ [Requirement specification](https://github.com/ShootingStar91/Javachess/blob/master/documentation/requirementspecification.md)
+ [Working hours tracking](https://github.com/ShootingStar91/Javachess/blob/master/documentation/workinghours.md)
