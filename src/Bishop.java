/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Bishop class: subclass of Piece class, representing a bishop piece
 */

import java.io.IOException;
import java.util.ArrayList;

public class Bishop extends Piece{
    /**
     * @param name: name of the chess piece ("bishop")
     * @param isWhite: boolean representing whether the piece is white
     * instantiates a Bishop object
     */
    public Bishop(String name, boolean isWhite) throws IOException {
        super(name, isWhite);
    }

    // returns the point value of a bishop (3)
    @Override
    public int getValue() {
        return 3;
    }

    /**
     * @param board: a 2D array of Tiles representing the current chessboard configuration
     * @return an ArrayList of Tiles representing the bishop's available moves
     */
    public ArrayList<Tile> getMoves(Tile[][] board) {
        // adding all diagonal paths (up-right, up-left, down-right, down-left)
        ArrayList<Tile> moves = getDiagonal(board,true,true);
        moves.addAll(getDiagonal(board,true,false));
        moves.addAll(getDiagonal(board,false,true));
        moves.addAll(getDiagonal(board,false,false));
        return moves;
    }
}
