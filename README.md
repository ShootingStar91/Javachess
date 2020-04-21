# Javachess

Java chess is a chess game implemented in Java. It is a practice project for a Computer Science course at University of Helsinki, by Arttu Kangas. The game will have at least normal chess against human or AI, and the games will be saved and can be rewatched.


### Documentation

Current state: Fully playable chess, with the exception that 50-turn rule is not implemented (yet).

__Important:__ 
+ When saving a game, do not press enter in the dialog. Instead, click the OK-button with your mouse. Sometimes the application crashes if you press enter. This might be a bug of JavaFX interacting badly with the UI of OS, because [similar errors are reported on the web](https://stackoverflow.com/questions/18512654/jvm-crashes-on-pressing-press-enter-key-in-a-textfield)
+ If you want to test the game saving and rewatching features, you need to complete a game. Quick way to do this is [Fool's Mate](https://en.wikipedia.org/wiki/Fool%27s_mate)

How to run it on linux with maven: <code>mvn compile exec:java -Dexec.mainClass=javachess.main.Main</code>

+ [Architecture](https://github.com/ShootingStar91/Javachess/blob/master/documentation/architecture.md)
+ [Requirement specification](https://github.com/ShootingStar91/Javachess/blob/master/documentation/requirementspecification.md)
+ [Working hours tracking](https://github.com/ShootingStar91/Javachess/blob/master/documentation/workinghours.md)
