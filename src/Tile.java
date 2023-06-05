/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Tile class: represents a single tile on a chessboard
 *
 * Sources:
 * - https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tile extends JComponent {
    private final Color COLOR;
    private final int X;
    private final int Y;
    private final int X_COORD;
    private final int Y_COORD;
    private final int SIZE;
    private boolean active = false;   // represents whether the tile has been chosen
    private boolean isOption = false;
    private boolean isPrev = false;   // represents whether the tile was the previous move
    private boolean showPrev = true;  // represents whether the previous move should be shown
    private Piece piece = null;       // the piece currently on the tile (null = no piece on the tile)

    /**
     * @param x: the tile's x position (column) on the chessboard
     * @param y: the tile's y position (row) on the chessboard
     * @param xCoord: the tile's x coordinate on the screen
     * @param yCoord: the tile's y coordinate on the screen
     * @param size: the height and width of the tile
     * instantiates a Tile object
     */
    public Tile(int x, int y, int xCoord, int yCoord, int size) {
        X = x;
        Y = y;
        X_COORD = xCoord;
        Y_COORD = yCoord;
        SIZE = size;
        // creating checkered pattern (alternating)
        if (y%2!=0 && x%2!=0 || y%2==0 && x%2==0) {
            COLOR = Main.LIGHT_GREEN;
        } else {
            COLOR = Main.DARK_GREEN;
        }
    }

    // getter and setter methods for X, Y, X_COORD, Y_COORD, SIZE, piece, isOption, isActive, isPrev, and showPrev
    public int getX() {
        return X;
    }
    public int getY() {
        return Y;
    }
    public int getXCoord() {
        return X_COORD;
    }
    public int getYCoord() {
        return Y_COORD;
    }
    public int getWidth() {
        return SIZE;
    }
    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
        // the piece's x and y values will be set to the tile's x and y values
        piece.setX(X);
        piece.setY(Y);
    }
    public void removePiece() {
        piece = null;
    }
    public void setOption(boolean option) {
        isOption = option;
    }
    public void setActive(boolean active, ArrayList<Tile> moves) {
        this.active = active;
        for (Tile move : moves) {
            // all the tile's available moves (if they exist) should be set as options or not as options
            move.setOption(active);
        }
    }
    public void setPrev(boolean isPrev) {
        this.isPrev = isPrev;
    }
    public void setShowPrev(boolean showPrev) {
        this.showPrev = showPrev;
    }

    /**
     * @param g: the Graphics context in which to paint
     * draws the Tile object
     */
    public void paint(Graphics g) {
        // colouring the tile
        if (active)
            g.setColor(Main.WHITE);
        else
            g.setColor(COLOR);
        // adding a layer of translucent black if the tile was a previous move
        g.fillRect(X_COORD, Y_COORD, SIZE, SIZE);
        if (isPrev && showPrev) {
            g.setColor(Main.TRANSPARENT_BLACK);
            g.fillRect(X_COORD, Y_COORD, SIZE, SIZE);
        }
        // adding a small white filled-in circle if the tile is an option and doesn't have a piece,
        // and a large white hollow circle if the tile is an object and has a piece (represents capturing)
        g.setColor(Main.WHITE);
        if (isOption && piece == null) {
            g.fillOval(X_COORD +(int)(SIZE/3.0), Y_COORD +(int)(SIZE/3.0),(int)(SIZE/3.0),(int)(SIZE/3.0));
        }
        if (isOption && piece != null) {
            g.drawOval(X_COORD, Y_COORD, SIZE, SIZE);
        }
        // drawing the piece (if it exists) on the tile
        if (piece != null) {
            g.drawImage(piece.getImg(), X_COORD, Y_COORD,null);
        }
    }
}
