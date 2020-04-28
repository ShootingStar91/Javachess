# Javachess

Java chess is a chess game implemented in Java. It is a practice project for a Computer Science course at University of Helsinki, by Arttu Kangas. The game will have at least normal chess against human or AI, and the games will be saved and can be rewatched.

__Current state__:
+ Fully playable chess against human
+ Also against computer
  + Not an intelligent opponent yet, chooses random moves, but it's still proper chess
+ Save games
+ Rewatch games
+ Erroneous game situation: In some cases the game will say it finished in a stalemate, even though it was actually a checkmate. The reason for this is still unrecognized, but it will be fixed for final version. However, often it does recognize checkmate correctly.

__Important:__ 
+ When saving a game, do not press enter in the dialog. Instead, click the OK-button with your mouse. Sometimes the application crashes if you press enter. This might be a bug of JavaFX interacting badly with the UI of OS, because [similar errors are reported on the web](https://stackoverflow.com/questions/18512654/jvm-crashes-on-pressing-press-enter-key-in-a-textfield)
+ If you want to test the game saving and rewatching features, you need to complete a game. Quick way to do this is [Fool's Mate](https://en.wikipedia.org/wiki/Fool%27s_mate)
  + Or just try to beat the AI. It shouldn't be too difficult!

# Links

+ [Code](https://github.com/ShootingStar91/Javachess/blob/master/src/)
+ [Instructions on how to use the application](https://github.com/ShootingStar91/Javachess/blob/master/documentation/instructions.md)
+ [Architecture](https://github.com/ShootingStar91/Javachess/blob/master/documentation/architecture.md)
+ [Requirement specification](https://github.com/ShootingStar91/Javachess/blob/master/documentation/requirementspecification.md)
+ [Working hours tracking](https://github.com/ShootingStar91/Javachess/blob/master/documentation/workinghours.md)
+ [Release 1 (week 5)](https://github.com/ShootingStar91/Javachess/releases/tag/viikko5)
+ **[Release 2 (week 6)](https://github.com/ShootingStar91/Javachess/releases/tag/viikko6)**

# Command line instructions

Compile: 
```
mvn compile exec:java -Dexec.mainClass=javachess.main.Main
```

Create .jar into /target/Javachess-1.0-SNAPSHOT.jar:
```
mvn package
```

Run tests:
```
mvn test
```

Create jacoco report of test coverage into /target/site/jacoco/:
```
mvn test jacoco:report
```

View this by opening the generated file index.html with a browser

Create checkstyle report into /target/site/:
```
mvn jxr:jxr checkstyle:checkstyle
```

Create JavaDoc into /target/site/apidocs/:
```
mvn javadoc:javadoc
```
