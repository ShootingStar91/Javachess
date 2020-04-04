package javachess.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author Arttu Kangas
 */


public class Game {
    boolean whiteKingCastling, whiteQueenCastling, blackQueenCastling, blackKingCastling;
    int turns;
    boolean detectingCheck;
    boolean whiteToMove;
    boolean checkDetected;
    boolean whiteChecked, blackChecked;
    Spot[] bishopDirections;
    Spot[] rookDirections;
    Piece [][] board;
    Spot[] knightMoves;
    int enpassant;

    
    public Game(boolean againstAI) {
        /*
        Move directions to aid with move generation
        */
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
        
        /*
        Whose turn to move
        */
        whiteToMove = true;
        turns = 0;
        
        /*
        When king or rooks move, castling needs to be disabled for it
        Other checks need to be written in castling check method later
        */
        whiteKingCastling = true;
        whiteQueenCastling = true;
        blackQueenCastling = true;
        blackKingCastling = true;
        /*
        
        */
        checkDetected = false;
        whiteChecked = false;
        blackChecked = false;
        detectingCheck = false;
        enpassant = -1;
        
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
    
    public boolean move(Spot from, Spot to) {
        if (whiteToMove) {
            whiteToMove=false;
        }
        else {
            turns++;
            whiteToMove=true;
        }
        
        Piece piece = board[from.getX()][from.getY()];
        
        // pawn initial doublemove? en passant? castling?
        if (piece.getType()==PieceType.PAWN && Math.abs(from.getY()-to.getY())==2) {
            enpassant = from.getX();
        } else {
            enpassant = -1;
        }
        if (piece.getType()==PieceType.PAWN) {
            if (from.getX()!=to.getX() && board[to.getX()][to.getY()] == null) {
                if (to.getY()==2) {
                    board[to.getX()][3] = null;
                } else if (to.getY()==5) {
                    board[to.getX()][4] = null;
                }
            }
        }
        if (piece.getType()==PieceType.KING) {
            if (piece.isWhite()) {
                whiteKingCastling = false;
                whiteQueenCastling = false;
            } else {
                blackKingCastling = false;
                blackQueenCastling = false;
            }
        } else if (piece.getType()==PieceType.ROOK) {
            if (piece.isWhite()) {
                if (from.getX()==0) whiteKingCastling=false;
                else whiteQueenCastling=false;
                if (from.getX()==0) blackKingCastling=false;
                else blackQueenCastling=false;
            }
        }
        
        if (board[to.getX()][to.getY()]!=null)
            if ((piece.getType()==PieceType.ROOK && board[to.getX()][to.getY()].getType() == PieceType.KING) ||
                    (piece.getType()==PieceType.KING && board[to.getX()][to.getY()].getType() == PieceType.ROOK)) {
                int x, y;
                if (!whiteToMove) y = 7;
                else y = 0;
                if (from.getX()==0 || to.getX()==0) x = 2;
                else x=6;

                board[from.getX()][from.getY()] = null;
                board[to.getX()][to.getY()] = null;
                board[x][y] = new Piece(PieceType.KING, !whiteToMove);
                int rookX;
                if (x==2) rookX=3;
                else rookX=5;
                board[rookX][y] = new Piece(PieceType.ROOK, !whiteToMove);
            }
        
        
        board[to.getX()][to.getY()] = board[from.getX()][from.getY()];
        board[from.getX()][from.getY()] = null;
        
        return checkMate();
        
    }
    
    public boolean checkMate() {
        for (Spot spot : getPiecesOnBoard()) {
            if (board[spot.getX()][spot.getY()].isWhite()==whiteToMove) {
                if (getPotentialMoves(spot).size()>0) return false;
            }
        }
        return true;
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
                        // pawn initial doublemove
                        if ((spot.getY()==1 && !whiteToMove) || (spot.getY()==6 && whiteToMove)) {
                            if ((new Spot(x, y+dir*2).onBoard()) && board[x][y+dir*2]==null && board[x][y+dir]==null) moves.add(new Spot(x, y+dir*2));
                        }
                        // en passant
                        if (enpassant!=-1 && (enpassant==x-1 || enpassant==x+1)) {
                            if (board[enpassant][y+dir]==null) {
                                moves.add(new Spot(enpassant, y+dir));
                            }
                        }
                    } else {
                        if (board[x+dx][y+dir]!=null && board[x+dx][y+dir].isWhite()!=isWhite) {
                            moves.add(newspot);
                            if (board[x+dx][y+dir].getType()==PieceType.KING) {
                                checkDetected = true;
                                checked();
                            }
                        }
                    }
                }
            }
        } else if (board[x][y].getType() == PieceType.KNIGHT) {
            Spot[] knightMoves = generateKnightMoves(spot);
            for (Spot move : knightMoves) {
                if (!move.onBoard()) continue;
                Piece piece = board[move.getX()][move.getY()];
                if (move.onBoard() && (piece==null ||
                   piece.isWhite() != isWhite)) {
                    moves.add(move);
                    
                    if (piece!=null && piece.getType()==PieceType.KING) {
                        checkDetected = true;
                        checked();
                    }
                }
            }
        } else if (board[x][y].getType() == PieceType.KING) {
            ArrayList<Spot> dirs = new ArrayList<>();
            Collections.addAll(dirs, bishopDirections);
            Collections.addAll(dirs, rookDirections);
            for (Spot dir : dirs) {
                Spot newspot = new Spot(spot.getX()+dir.getX(), spot.getY()+dir.getY());
                if (!newspot.onBoard()) continue;
                Piece piece = board[newspot.getX()][newspot.getY()];
                if (newspot.onBoard()) {
                    if (board[newspot.getX()][newspot.getY()]!=null) {
                        if (piece.isWhite() == isWhite) {
                            continue;
                        }
                        if (piece.getType() == PieceType.KING) {
                            checkDetected = true;
                            checked();
                        }
                    }
                    moves.add(newspot);
                    
                }
            }
            if (whiteToMove) {
                if (castling(true, true)) {
                    moves.add(new Spot(7,7));
                }
                if (castling(true, false)) {
                    moves.add(new Spot(0, 7));
                }
            } else {
                if (castling(false, true)) {
                    moves.add(new Spot(7, 0));
                }
                if (castling(false, false)) {
                    moves.add(new Spot(0, 0));
                }
            }
        } else {
            moves.addAll(generateMovesFor(spot, board[spot.getX()][spot.getY()].getType()));
        }
        
        if (detectingCheck) {
            detectingCheck = false;
            return null;
        }
        
        for (Iterator<Spot> iterator = moves.iterator(); iterator.hasNext();) {
            Piece[][] savedBoard = copyBoard();
            
            Spot move = iterator.next();
            
            board[move.getX()][move.getY()] = board[spot.getX()][spot.getY()];
            board[spot.getX()][spot.getY()] = null;
            
            checkDetected = false;
            
            for (Spot piece : getPiecesOnBoard()) {
                if (board[piece.getX()][piece.getY()].isWhite()!=whiteToMove) {
                    detectingCheck = true;
                    
                    getPotentialMoves(piece);
                }
                if (checkDetected) {
                    break;
                }
            }
            if (checkDetected) {
                iterator.remove();
            }
            
            board = savedBoard;
            
        }
        
        
        return moves;
    }
    
    private boolean castling(boolean white, boolean kingSide) {
        int x=0;
        int y=0;
        
        if (kingSide && white) {
            x = 5;
            y = 7;
            if (!whiteKingCastling) return false;
        } else if (!kingSide && white) {
            x = 1;
            y = 7;
            if (!whiteQueenCastling) return false;
        } else if (kingSide && !white) {
            x = 5;
            y = 0;
            if (!blackKingCastling) return false;
        } else if (!kingSide && !white) {
            x = 1;
            y = 0;
            if (!blackQueenCastling) return false;
        }
        
        if (!(board[x][y]==null && board[x+1][y]==null)) return false;
        if (!kingSide) {
            if (board[x+2][y]!=null) return false;
        }

        return true;
    }
    
    private Piece[][] copyBoard () {
        Piece[][] tempBoard = new Piece[8][8];
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                tempBoard[x][y] = board[x][y];
            }
        }
        return tempBoard;
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
                        checked();
                    }
                }
                break;
            }
        }
        Spot whiteKingSpot = new Spot(4, 7);
        Spot blackKingSpot = new Spot(4, 0);
        if (type==PieceType.ROOK) {
            if (spot.getX()==7 && spot.getY()==7) {
                if (castling(true, true)) moves.add(whiteKingSpot);
            }
            if (spot.getX()==0 && spot.getY()==7) {
                if (castling(true, false)) moves.add(whiteKingSpot);
            }
            if (spot.getX()==7 && spot.getY()==0) {
                if (castling(false, true)) moves.add(blackKingSpot);
            }
            if (spot.getX()==0 && spot.getY()==0) {
                if (castling(false, false)) moves.add(blackKingSpot);
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
    
    public void checked() {
        if (!detectingCheck) {
            if (whiteToMove) blackChecked=true;
            else whiteChecked=true;
        }
    }
    
    public Piece[][] getBoard() {
        return board;
    }
    
    
    
    public boolean whiteToMove() {
        return whiteToMove;
    }
    
    public ArrayList<Spot> getPiecesOnBoard() {
        ArrayList<Spot> pieces = new ArrayList<>();
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                if (board[x][y]!=null) pieces.add(new Spot(x, y));
            }
        }
        return pieces;
    }
}
