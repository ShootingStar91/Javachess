# Javachess

Javachess is a chess game implemented in Java. It is a practice project for a Computer Science course at University of Helsinki, by Arttu Kangas. The game will have at least normal chess against human or AI, and the games will be saved and can be rewatched.

It features playing normal chess against your friend sitting next to you, or an AI engine. The AI is quite good in the mid-game and will do no stupid mistakes. However, it lacks the ability to force checkmate in end-game, and often finishes in stalemate. This is a common problem with minmax-algorithms. One solution is a pre-saved database of many different end-game situations, but that is out of the scope for this learning project.

The game is finished for review and will no longer be updated.

__Important:__ 
+ When saving a finished game, please do not press enter key, but instead click OK-button with your mouse. This is likely only important when you are using Linux and a specific UI.
  + The application often crashes when pressing enter in the text field and having a special linux UI theme. This might be a bug of JavaFX interacting badly with the UI of OS, because [similar errors are reported on the web](https://stackoverflow.com/questions/18512654/jvm-crashes-on-pressing-press-enter-key-in-a-textfield)
+ If you want to test the game saving and rewatching features, you need to complete a game. Quick way to do this is [Fool's Mate](https://en.wikipedia.org/wiki/Fool%27s_mate)

# Links

+ [Code](https://github.com/ShootingStar91/Javachess/blob/master/src/)
+ [Instructions](https://github.com/ShootingStar91/Javachess/blob/master/documentation/instructions.md)
+ [Architecture](https://github.com/ShootingStar91/Javachess/blob/master/documentation/architecture.md)
+ [Requirement specification](https://github.com/ShootingStar91/Javachess/blob/master/documentation/requirementspecification.md)
+ [Testing document](https://github.com/ShootingStar91/Javachess/blob/master/documentation/testingdocument.md)
+ [Working hours tracking](https://github.com/ShootingStar91/Javachess/blob/master/documentation/workinghours.md)

# Release

+ **[Final release](https://github.com/ShootingStar91/Javachess/releases/tag/viikko7)**

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
