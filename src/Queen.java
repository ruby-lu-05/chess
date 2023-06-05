/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Queen class: subclass of Piece class, representing a queen piece
 */

import java.io.IOException;
import java.util.ArrayList;

public class Queen extends Piece{
    /**
     * @param name: name of the chess piece ("queen")
     * @param isWhite: boolean representing whether the piece is white
     * instantiates a Queen object
     */
    public Queen(String name, boolean isWhite) throws IOException {
        super(name, isWhite);
    }

    // returns the point value of a queen (9)
    @Override
    public int getValue() {
        return 9;
    }

    /**
     * @param board: a 2D array of Tiles representing the current chessboard configuration
     * @return an ArrayList of Tiles representing the queen's available moves
     */
    public ArrayList<Tile> getMoves(Tile[][] board) {
        // adding all linear and diagonal paths (up, down, left, right, up-right, up-left, down-right, down-left)
        ArrayList<Tile> moves = getLinear(board,true,1);
        moves.addAll(getLinear(board,true,-1));
        moves.addAll(getLinear(board,false,1));
        moves.addAll(getLinear(board,false,-1));
        moves.addAll(getDiagonal(board,true,true));
        moves.addAll(getDiagonal(board,true,false));
        moves.addAll(getDiagonal(board,false,true));
        moves.addAll(getDiagonal(board,false,false));
        return moves;
    }
}
