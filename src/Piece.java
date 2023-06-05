/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Piece class: abstract parent class of all chess piece objects
 *
 * Sources:
 * - piece images downloaded from https://lichess.org/ ("California" piece theme)
 * - https://www.w3schools.com/java/java_abstract.asp
 * - Chapter 13 Exercises (for implementing Comparable)
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Piece implements Comparable<Piece> {
    private final String NAME;
    private final Image IMG;
    private final String COLOR;
    private int x;  // the piece's x position (column) on the chessboard
    private int y;  // the piece's y position (row) on the chessboard

    /**
     * @param name: name of the chess piece (e.g "pawn", "bishop")
     * @param isWhite: boolean representing whether the piece is white
     * instantiates a Piece object based on colour and type
     */
    public Piece(String name,boolean isWhite) throws IOException {
        NAME = name;
        if (isWhite) {
            COLOR = "WHITE";
            IMG = ImageIO.read(new File(name+"_white.png"));
        } else {
            COLOR = "BLACK";
            IMG = ImageIO.read(new File(name+"_black.png"));
        }
    }

    // getter methods for NAME, COLOR, IMG, x, and y
    public String getName() {
        return NAME;
    }
    public String getColor() {
        return COLOR;
    }
    public Image getImg() {
        return IMG;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    // setter methods for x and y
    public void setX(int newX) {
        x = newX;
    }
    public void setY(int newY) {
        y = newY;
    }

    /**
     * @param board: 2D array of Tiles representing the current chessboard configuration
     * @param isVertical: boolean representing whether moves will be found vertically
     * @param shift: boolean representing whether moves will be found forward or backward (1 or -1)
     * @return an ArrayList of Tiles, vertical or horizontal, representing ONE of the linear paths of the piece
     */
    public ArrayList<Tile> getLinear(Tile[][] board, boolean isVertical, int shift) {
        ArrayList<Tile> availableTiles = new ArrayList<>();
        int current;   // coordinate that will be changed (x or y)
        int boundary;  // boundary (top boundary, bottom boundary, left boundary, or right boundary)
        if (isVertical) {
            current = y;
            if (shift<0)
                boundary = 0;
            else
                boundary = 7;
        } else {
            current = x;
            if (shift>0)
                boundary = 7;
            else
                boundary = 0;
        }
        while (current != boundary) {
            // incrementing the coordinate by 1 or -1 (based on isVertical and shift parameters) until boundary is reached
            Tile tile;
            if (isVertical)
                tile = board[current+shift][x];
            else
                tile = board[y][current+shift];
            // continuing if the current Tile is empty, stopping if a piece is reached
            if (tile.getPiece() == null) {
                availableTiles.add(tile);
            } else if (tile.getPiece() != null && !tile.getPiece().getColor().equals(COLOR)) {
                availableTiles.add(tile);
                break;
            } else if (tile.getPiece() != null && tile.getPiece().getColor().equals(COLOR)) {
                break;
            }
            current += shift;
        }
        return availableTiles;
    }

    /**
     * @param board: 2D array of Tiles representing the current chessboard configuration
     * @param up: boolean representing whether moves will be found upward
     * @param right: boolean representing whether moves will be found rightward
     * @return an ArrayList of Tiles representing ONE of the diagonal paths of the piece
     */
    public ArrayList<Tile> getDiagonal(Tile[][] board, boolean up, boolean right) {
        ArrayList<Tile> availableTiles = new ArrayList<>();
        int currentX = x;
        int currentY = y;
        int xBoundary;  // horizontal boundary: left or right side
        int yBoundary;  // vertical boundary: top or bottom side
        if (up)
            yBoundary = 0;
        else
            yBoundary = 7;
        if (right)
            xBoundary = 7;
        else
            xBoundary = 0;

        while (currentX != xBoundary && currentY != yBoundary) {
            Tile tile;
            // incrementing the coordinates up-right, up-left, down-right, or down-left
            if (up && right) {
                currentY--;
                currentX++;
            } else if (up) {
                currentY--;
                currentX--;
            } else if (right) {
                currentY++;
                currentX++;
            } else {
                currentY++;
                currentX--;
            }
            tile = board[currentY][currentX];
            // continuing if the current Tile is empty, stopping if a piece is reached
            if (tile.getPiece() == null) {
                availableTiles.add(tile);
            } else if (tile.getPiece() != null && !tile.getPiece().getColor().equals(COLOR)) {
                availableTiles.add(tile);
                break;
            } else if (tile.getPiece() != null && tile.getPiece().getColor().equals(COLOR)) {
                break;
            }
        }
        return availableTiles;
    }

    /**
     * @param board: 2D array of Tiles representing the current chessboard configuration
     * @return an ArrayList of Tiles representing the knight's moves
     */
    public ArrayList<Tile> getLShape(Tile[][] board) {
        ArrayList<Tile> availableTiles = new ArrayList<>();
        if (y-2>=0 && x+1<=7)
            availableTiles.add(board[y-2][x+1]);
        if (y-1>=0 && x+2<=7)
            availableTiles.add(board[y-1][x+2]);
        if (y+1<=7 && x+2<=7)
            availableTiles.add(board[y+1][x+2]);
        if (y+2<=7 && x+1<=7)
            availableTiles.add(board[y+2][x+1]);
        if (y+2<=7 && x-1>=0)
            availableTiles.add(board[y+2][x-1]);
        if (y+1<=7 && x-2>=0)
            availableTiles.add(board[y+1][x-2]);
        if (y-1>=0 && x-2>=0)
            availableTiles.add(board[y-1][x-2]);
        if (y-2>=0 && x-1>=0)
            availableTiles.add(board[y-2][x-1]);
        return removeInvalidMoves(availableTiles);
    }

    /**
     * @param moves: ArrayList of Tiles representing a piece's moves
     * @return a new ArrayList with the invalid moves removed
     */
    public ArrayList<Tile> removeInvalidMoves(ArrayList<Tile> moves) {
        for (int i=0; i<moves.size(); i++) {
            // move is invalid if there is already a same-colour piece on the Tile
            if (moves.get(i).getPiece() != null && moves.get(i).getPiece().getColor().equals(COLOR)) {
                moves.remove(i);
                i--;
            }
        }
        return moves;
    }

    /**
     * @param b: Board object representing the current chessboard
     * @return a boolean indicating whether the piece is able to capture another piece
     */
    public boolean canCapture(Board b) {
        // iterating through the piece's moves, looking for an opposite-colour piece
        for (Tile tile : getMoves(b.getBoard())) {
            if (tile.getPiece() != null && !tile.getPiece().getColor().equals(getColor())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param b: Board object representing the current chessboard
     * @return an ArrayList containing the piece's legal moves
     */
    public ArrayList<Tile> getLegalMoves(Board b) {
        Tile[][] board = b.getBoard();
        ArrayList<Tile> moves = getMoves(board);
        // adding the option to castle (if the piece is a king)
        if (NAME.equals("king") && !b.kingInCheck(COLOR) && !this.getFirstMoveDone()) {
            Piece leftPiece = board[7][0].getPiece();
            Piece rightPiece = board[7][7].getPiece();
            // checking whether the king and rook have moved, and whether there is open space between them
            if (leftPiece != null && leftPiece.getName().equals("rook") && !leftPiece.getFirstMoveDone() &&
            board[7][1].getPiece() == null && board[7][2].getPiece() == null && (x==3 || board[7][3].getPiece() == null))
                moves.add(board[7][x-2]);
            if (rightPiece != null && rightPiece.getName().equals("rook") && !rightPiece.getFirstMoveDone() &&
            board[7][6].getPiece() == null && board[7][5].getPiece() == null && (x==4 || board[7][4].getPiece() == null))
                moves.add(board[7][x+2]);
        }
        for (int i=0; i<moves.size(); i++) {
            // pseudo-moving (checking whether the move would put the king in check)
            Tile move = moves.get(i);
            Piece temp = board[move.getY()][move.getX()].getPiece();
            int tempX = x;
            int tempY = y;
            b.movePiece(false,this,board[move.getY()][move.getX()]);
            if (b.kingInCheck(COLOR)) {
                moves.remove(move);
                i--;
            }
            b.movePiece(false,this,board[tempY][tempX]);
            if (temp != null) {  // moving the piece back to its position
                board[move.getY()][move.getX()].setPiece(temp);
                b.getCurrentPieces().add(temp);
            }
        }
        return moves;
    }

    /**
     * @param b: Board object representing the current chessboard
     * @param canCapture: boolean representing whether the piece is able to capture any enemy pieces
     * @return an ArrayList of legal moves for the Suicide Chess variant (if a capture is available, you must capture)
     */
    public ArrayList<Tile> getSuicideMoves(Board b, boolean canCapture) {
        ArrayList<Tile> suicideMoves = new ArrayList<>();
        if (canCapture) {
            // if a capture is available, it's the only legal move
            for (Tile move : getMoves(b.getBoard())) {
                if (move.getPiece() != null && !move.getPiece().getColor().equals(getColor())) {
                    suicideMoves.add(move);
                }
            }
        } else {
            // if a capture isn't available, you can move normally
            return getMoves(b.getBoard());
        }
        return suicideMoves;
    }

    /**
     * @return an integer representing the value of the piece
     */
    public abstract int getValue();

    /**
     * @param board: a 2D array of Tiles representing the current chessboard configuration
     * @return an ArrayList of Tiles representing every possible move for the piece
     */
    public abstract ArrayList<Tile> getMoves(Tile[][] board);

    /**
     * @return a boolean representing whether the piece has made its first move (only used by pawns, kings, and rooks)
     */
    public boolean getFirstMoveDone() {
        return false;
    }

    // getter method for the movedTwo field for pawns
    public boolean getMovedTwo() {
        return false;
    }

    // setter methods for the movedTwo field (for pawns) and firstMoveDone field (for pawns, kings, and rooks)
    public void setMovedTwo(boolean movedTwo) {
    }
    public void setFirstMoveDone(boolean done) {
    }

    /**
     * @param o: the object to be compared
     * @return an integer representing which piece is greater
     */
    public int compareTo(Piece o) {
        // comparisons are based on the point values of the pieces
        return Integer.compare(this.getValue(), o.getValue());
    }
}
