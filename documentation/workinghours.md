# Working hours tracking

| Date | Time (hrs) | Task |
| :----: | :-----| :-----|
| 24.3. | 1 | Planning the application idea further and writing a requirement specification |
| 25.3. | 3 | Got familiar with JavaFX and wrote first runnable version of the program |
| 26.3. | 2 | Started writing the game logic (potential moves method) and planned how to implement it |
| 27.3. | 4 | Finished first version of potential moves method, still needs tuning for check detection and testing. Also prepared the graphic files for the pieces and set them to work |
| 28.3. | 1 | Continued writing game logic |
| 29.3. | 2 | Continued writing game logic |
| 1.4.  | 1 | Implemented check detection |
| 3.4.  | 2 | Added game ending in checkmate, started adding support for complex moves (en passant, castling) |
| 5.4.  | 3 | Added en passant and most parts of castling (conditions still not complete for castling) |
| 7.4.  | 4 | Added saving the game to a text file after checkmate happens. Also added Dao-class and saving the game to a text file after it's over. |
| 13.4. | 4 | Heavily modified game-class to better detect castling conditions |
| 14.4. | 3 | Finished castling conditions, takes into account pawn attacking |
| 15.4. | 2 | Fixed hundreds of checkstyle errors from Game.java |
| 17.4. | 2 | Added selection of promotion |
| 19.4. | 2 | Made the promotion neater and added turn counter visible |
| 20.4. | 3 | Added rewatching played games |
| 21.4. | 8 | Rewrote lots of code to remove checkstyle errors, also added javafx-dialogues to save the game with a title, and fought with lots of bugs. Also updated documentation and made tests runnable. |
| 28.4. | 7 | Added AIEngine class, slight modifications elsewhere, javadoc started, documentation improved |
| 1.5.  | 2 | Fixed bug: pawn was attacking also the spot in front of it. This caused the stalemate/checkmate errors. Removed copypastey code. |
| 2.5.  | 4 | Fixed another stalemate-bug, started drafting AI move choosing algorithm |
| 3.5.  | 5 | Wrote, tested and adjusted the move choosing algorithm: minmax with alpha-beta-pruning |
| Total | 65 | |
