package javachess.Game;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Arttu Kangas
 */


public class Game {
    boolean whiteKingCastling, whiteQueenCastling, blackQueenCastling, blackKingCastling;
    int turns;
    boolean whiteToMove;
    boolean checkDetected;
    Spot[] bishopDirections;
    Spot[] rookDirections;
    Piece [][] board;
    Spot[] knightMoves;
    
    public Game(boolean againstAI) {
        turns = 0;
        knightMoves = new Spot[8];
        knightMoves[0] = new Spot(1, 2);
        knightMoves[1] = new Spot(2, 1);
        knightMoves[2] = new Spot(-1, 2);
        knightMoves[3] = new Spot(1, -2);
        knightMoves[4] = new Spot(-1, -2);
        knightMoves[5] = new Spot(-2, 1);
        knightMoves[6] = new Spot(2, -1);
        knightMoves[7] = new Spot(-2, -1);
        bishopDirections = new Spot[4];
        bishopDirections[0] = new Spot(-1, -1);
        bishopDirections[1] = new Spot(-1, 1);
        bishopDirections[2] = new Spot(1, 1);
        bishopDirections[3] = new Spot(1, -1);        
        rookDirections = new Spot[4];
        rookDirections[0] = new Spot(0, -1);
        rookDirections[1] = new Spot(-1, 0);
        rookDirections[2] = new Spot(0, 1);
        rookDirections[3] = new Spot(1, 0);
        whiteToMove = true;
        whiteKingCastling = true;
        whiteQueenCastling = true;
        blackQueenCastling = true;
        blackKingCastling = true;
        checkDetected = false;
        board = new Piece [8][8];
        
        for (int x = 0; x < 8; x++) {
            board[x][1] = new Piece(PieceType.PAWN, false);
            board[x][6] = new Piece(PieceType.PAWN, true);
        }
        
        board[0][0] = new Piece(PieceType.ROOK, false);
        board[7][0] = new Piece(PieceType.ROOK, false);
        board[0][7] = new Piece(PieceType.ROOK, true);
        board[7][7] = new Piece(PieceType.ROOK, true);
        board[1][0] = new Piece(PieceType.KNIGHT, false);
        board[6][0] = new Piece(PieceType.KNIGHT, false);
        board[1][7] = new Piece(PieceType.KNIGHT, true);
        board[6][7] = new Piece(PieceType.KNIGHT, true);
        board[2][0] = new Piece(PieceType.BISHOP, false);
        board[5][0] = new Piece(PieceType.BISHOP, false);
        board[2][7] = new Piece(PieceType.BISHOP, true);
        board[5][7] = new Piece(PieceType.BISHOP, true);
        board[3][0] = new Piece(PieceType.QUEEN, false);
        board[3][7] = new Piece(PieceType.QUEEN, true);
        board[4][0] = new Piece(PieceType.KING, false);
        board[4][7] = new Piece(PieceType.KING, true);
        
    }
    
    
    public ArrayList<Spot> getPotentialMoves(Spot spot) {
        if (board[spot.getX()][spot.getY()] == null) return null;
        checkDetected = false;
        int x = spot.getX();
        int y = spot.getY();
        
        boolean isWhite = board[spot.getX()][spot.getY()].isWhite();
        
        ArrayList<Spot> moves = new ArrayList<>();
        
        if (board[x][y]==null) return new ArrayList<>();
        
        if (board[x][y].getType() == PieceType.PAWN) {
            int dir;
            if (board[x][y].isWhite()) dir=-1;
            else dir=1;
            
            for (int dx=-1; dx<2; dx++) {
                Spot newspot = new Spot(x+dx, y+dir);
                if (newspot.onBoard()) {
                    if (dx==0) {
                        if (board[x][y+dir]==null) {
                            moves.add(newspot);
                        }
                    } else {
                        if (board[x][y+dir]!=null && board[x][y+dir].isWhite()!=isWhite) {
                            moves.add(newspot);
                            if (board[x][y].getType()==PieceType.KING) {
                                checkDetected = true;
                            }
                        }
                    }
                }
            }
        } else if (board[x][y].getType() == PieceType.KNIGHT) {
            Spot[] knightMoves = generateKnightMoves(spot);
            for (Spot move : knightMoves) {
                
                if (move.onBoard() && board[move.getX()][move.getY()]!=null && 
                   board[move.getX()][move.getY()].isWhite() != isWhite) {
                    moves.add(move);
                    if (board[move.getX()][move.getY()].getType()==PieceType.KING) {
                        checkDetected = true;
                    }
                }
            }
        } else if (board[x][y].getType() == PieceType.KING) {
            ArrayList<Spot> dirs = new ArrayList<>();
            Collections.addAll(dirs, bishopDirections);
            Collections.addAll(dirs, rookDirections);
            for (Spot dir : dirs) {
                Spot newspot = new Spot(spot.getX()+dir.getX(), spot.getY()+dir.getY());
                Piece piece = board[newspot.getX()][newspot.getY()];
                if (newspot.onBoard()) {
                    if (board[newspot.getX()][newspot.getY()]!=null) {
                        if (piece.isWhite() == isWhite) {
                            continue;
                        }
                        if (piece.getType() == PieceType.KING) {
                            checkDetected = true;
                        }
                        moves.add(newspot);
                    }
                }
            }
        } else {
            moves.addAll(generateMovesFor(spot, board[spot.getX()][spot.getY()].getType()));
        }
        
        return moves;
    }
   
    private ArrayList<Spot> generateMovesFor(Spot spot, PieceType type) {
        ArrayList<Spot> moves = new ArrayList<>();
        boolean isWhite = board[spot.getX()][spot.getY()].isWhite();
        ArrayList<Spot> dirs = new ArrayList<>();
        
        if (type==PieceType.BISHOP) {
            Collections.addAll(dirs, bishopDirections);
        } else if (type==PieceType.ROOK) {
            Collections.addAll(dirs, rookDirections);
        } else {
            Collections.addAll(dirs, bishopDirections);
            Collections.addAll(dirs, rookDirections);
        }
        
        for (Spot dir : dirs) {
            Spot curSpot = new Spot(spot.getX(), spot.getY());
            while(true) {
                curSpot.setX(curSpot.getX()+dir.getX());
                curSpot.setY(curSpot.getY()+dir.getY());
                if (!curSpot.onBoard()) break;
                int x = curSpot.getX();
                int y = curSpot.getY();
                if (board[x][y] == null) {
                    moves.add(new Spot(x, y));
                    continue;
                }
                if (board[x][y].isWhite() != isWhite) {
                    moves.add(new Spot(x, y));
                    if (board[x][y].getType() == PieceType.KING) {
                        checkDetected = true;
                    }
                }
            }
        }
        return moves;
    }

    
    private Spot[] generateKnightMoves(Spot spot) {
        Spot[] moves = new Spot[8];
        for (int i=0; i<8; i++) {
            moves[i] = new Spot(spot.getX()+knightMoves[i].getX(), spot.getY()+knightMoves[i].getY());
        }
        return moves;
    }
    
}
