/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * King class: subclass of Piece class, representing a king piece
 */

import java.io.IOException;
import java.util.ArrayList;

public class King extends Piece{
    // field representing whether the king has moved
    private boolean firstMoveDone = false;

    /**
     * @param name: name of the chess piece ("king")
     * @param isWhite: boolean representing whether the piece is white
     * instantiates a King object
     */
    public King(String name, boolean isWhite) throws IOException {
        super(name, isWhite);
    }

    // getter and setter methods for the firstMoveDone field
    public boolean getFirstMoveDone() {
        return firstMoveDone;
    }
    public void setFirstMoveDone(boolean done) {
        firstMoveDone = done;
    }

    // returns the point value of a king (10)
    @Override
    public int getValue() {
        return 10;
    }

    /**
     * @param board: a 2D array of Tiles representing the current chessboard configuration
     * @return an ArrayList of Tiles representing the king's available moves
     */
    public ArrayList<Tile> getMoves(Tile[][] board) {
        ArrayList<Tile> availableTiles = new ArrayList<>();
        // adding all the possible moves within a one-tile radius
        for (int x=-1; x<2; x++) {
            for (int y=-1; y<2; y++) {
                try {
                    availableTiles.add(board[getY()+y][getX()+x]);
                } catch (ArrayIndexOutOfBoundsException ignored){
                }
            }
        }
        return removeInvalidMoves(availableTiles);
    }
}
