# Testing document

The software is tested through both integration and unit tests with JUnit and by playing the chess both against AI and human.

### Jacoco test coverage report

##### Overview

![Test coverage overview](https://github.com/ShootingStar91/Javachess/blob/master/documentation/testcovmain.png)

##### game-package

![Test coverage game-package](https://github.com/ShootingStar91/Javachess/blob/master/documentation/testcovgame.png)

##### dao-package

![Test coverage dao-package](https://github.com/ShootingStar91/Javachess/blob/master/documentation/testcovdao.png)

### gametest-package

GameTest.java class tests the main functionality both without and with AI opponent.

These tests also test the Board-class well, as these two classes are closely tied in functionality.

There is also PieceTest.java file which tests the Piece-class independently to cover areas which the integration tests did not cover.

### daotest-package

The Dao-class for saving and reading games from database is tested also through JUnit-tests, by the daotest package which has the DaoTest.java class.
