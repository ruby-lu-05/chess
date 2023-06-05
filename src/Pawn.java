/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Pawn class: subclass of Piece class, representing a pawn piece
 */

import java.io.IOException;
import java.util.ArrayList;

public class Pawn extends Piece{
    // fields representing whether the pawn has already moved, and whether it moved two tiles on its first move
    private boolean firstMoveDone = false;
    private boolean movedTwo = false;

    /**
     * @param name: name of the chess piece ("pawn")
     * @param isWhite: boolean representing whether the piece is white
     * instantiates a Pawn object
     */
    public Pawn(String name, boolean isWhite) throws IOException {
        super(name, isWhite);
    }

    // setter and getter methods for the firstMoveDone and movedTwo fields
    @Override
    public boolean getFirstMoveDone() {
        return firstMoveDone;
    }
    @Override
    public void setFirstMoveDone(boolean done) {
        firstMoveDone = done;
    }
    @Override
    public boolean getMovedTwo() {
        return movedTwo;
    }
    @Override
    public void setMovedTwo(boolean movedTwo) {
        this.movedTwo = movedTwo;
    }

    // returns the point value of a pawn (1)
    @Override
    public int getValue() {
        return 1;
    }

    /**
     * @param board: a 2D array of Tiles representing the current chessboard configuration
     * @return an ArrayList of Tiles representing the pawn's available moves
     */
    public ArrayList<Tile> getMoves(Tile[][] board) {
        ArrayList<Tile> moves = new ArrayList<>();
        // adding one forward step if it's available
        if (getY() > 0 && board[getY()-1][getX()].getPiece() == null) {
            moves.add(board[getY()-1][getX()]);
            if (!firstMoveDone) {
                // adding the second forward step if the pawn hasn't moved yet
                if (getY()-2 >= 0 && board[getY()-2][getX()].getPiece() == null) {
                    moves.add(board[getY()-2][getX()]);
                }
            }
        }

        // checking whether the pawn can capture diagonally
        if (getX()+1<=7 && getY()-1>=0 && board[getY()-1][getX()+1].getPiece() != null && !board[getY()-1][getX()+1].getPiece().getColor().equals(getColor()))
            moves.add(board[getY()-1][getX()+1]);
        if (getX()-1>=0 && getY()-1>=0 && board[getY()-1][getX()-1].getPiece() != null && !board[getY()-1][getX()-1].getPiece().getColor().equals(getColor()))
            moves.add(board[getY()-1][getX()-1]);

        // implementing en passant rule
        // (checking whether the pawn's adjacent pawns (if they exist) moved two steps
        if (getY()==3 && getX()<7) {
            Piece rightPiece = board[getY()][getX()+1].getPiece();
            if (rightPiece != null && rightPiece.getName().equals("pawn") && !rightPiece.getColor().equals(getColor()) && rightPiece.getMovedTwo()) {
                moves.add(board[getY()-1][getX()+1]);
            }
        }
        if (getY()==3 && getX()>0) {
            Piece leftPiece = board[getY()][getX()-1].getPiece();
            if (leftPiece != null && leftPiece.getName().equals("pawn") && !leftPiece.getColor().equals(getColor()) &&
            leftPiece.getMovedTwo()) {
                moves.add(board[getY()-1][getX()-1]);
            }
        }

        return moves;
    }
}
