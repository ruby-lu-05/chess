/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Knight class: subclass of Piece class, representing a knight piece
 */

import java.io.IOException;
import java.util.ArrayList;

public class Knight extends Piece{
    // instantiates a Knight object
    public Knight(String name, boolean isWhite) throws IOException {
        super(name, isWhite);
    }

    // returns the point value of a pawn (3)
    @Override
    public int getValue() {
        return 3;
    }

    /**
     * @param board: a 2D array of Tiles representing the current chessboard configuration
     * @return an ArrayList of Tiles representing the knight's available moves
     */
    public ArrayList<Tile> getMoves(Tile[][] board) {
        return getLShape(board);
    }
}
