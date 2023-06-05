/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Rook class: subclass of Piece class, representing a rook piece
 */

import java.io.IOException;
import java.util.ArrayList;

public class Rook extends Piece{
    // field representing whether the rook has moved yet
    private boolean firstMoveDone = false;

    /**
     * @param name: name of the chess piece ("rook")
     * @param isWhite: boolean representing whether the piece is white
     * instantiates a Rook object
     */
    public Rook(String name, boolean isWhite) throws IOException {
        super(name, isWhite);
    }

    // getter and setter methods for the firstMoveDone field
    public boolean getFirstMoveDone() {
        return firstMoveDone;
    }
    public void setFirstMoveDone(boolean done) {
        firstMoveDone = done;
    }

    // returns the point value of a rook (5)
    @Override
    public int getValue() {
        return 5;
    }

    /**
     * @param board: a 2D array of Tiles representing the current chessboard configuration
     * @return an ArrayList of Tiles representing the rook's available moves
     */
    public ArrayList<Tile> getMoves(Tile[][] board) {
        // adding all linear paths (up, down, left, right)
        ArrayList<Tile> moves = getLinear(board,true,1);
        moves.addAll(getLinear(board,true,-1));
        moves.addAll(getLinear(board,false,1));
        moves.addAll(getLinear(board,false,-1));
        return moves;
    }
}
