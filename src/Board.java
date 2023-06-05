/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Board class: represents a chessboard
 *
 * Sources:
 * - https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
 * - https://support.chess.com/article/128-what-does-insufficient-mating-material-mean
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Board extends JComponent {
    private final ArrayList<Piece> CURRENT_PIECES = new ArrayList<>();  // current pieces on the board
    private final ArrayList<Piece> GRAVEYARD = new ArrayList<>();       // every captured piece
    private final int START_X;
    private final int START_Y;
    private final int SIZE;
    private final Tile[][] BOARD = new Tile[8][8];  // 2D array of Tiles representing the chessboard
    private String headingString = "";              // text shown on top of the chessboard
    private String gameUpdate = "";                 // text shown beneath the chessboard
    private final Player WHITE_PLAYER;
    private final Player BLACK_PLAYER;

    /**
     * @param startX: the starting x coordinate of the board on the screen
     * @param startY: the starting y coordinate of the board on the screen
     * @param width: the width of the whole chessboard
     * @param whitePlayer: a Player object representing a player playing with the white pieces
     * @param blackPlayer: a Player object representing a player playing with the black pieces
     * instantiates a Board object
     */
    public Board(int startX, int startY, int width, Player whitePlayer, Player blackPlayer) {
        START_X = startX;
        START_Y = startY;
        SIZE = width;
        WHITE_PLAYER = whitePlayer;
        BLACK_PLAYER = blackPlayer;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                BOARD[row][col] = new Tile(col, row, (int)(startX+col*width/8.0), (int)(startY+row*width/8.0), (int)(width/8.0));
            }
        }
    }

    // getter methods for the 2D BOARD array and current pieces ArrayList
    public Tile[][] getBoard() {
        return BOARD;
    }
    public ArrayList<Piece> getCurrentPieces() {
        return CURRENT_PIECES;
    }

    // adds a piece to the graveyard
    public void addToGraveyard(Piece deadPiece) {
        GRAVEYARD.add(deadPiece);
    }

    // getter and setter methods for gameUpdate and headingString
    public String getUpdateString() {
        return gameUpdate;
    }
    public void updateString(String update) {
        gameUpdate = update;
    }
    public void setHeadingString(String heading) {
        headingString = heading;
    }

    /**
     * @param real: whether the piece is actually being moved, or whether a pseudo-move is being performed
     * @param piece: the Piece object to be moved
     * @param newTile: the destination Tile
     * moves the Piece object to newTile
     */
    public void movePiece(boolean real, Piece piece, Tile newTile) {
        // setting the pawn's, rook's, or king's firstMoveDone field as true
        if (real && !piece.getFirstMoveDone() && (piece.getName().equals("pawn") || piece.getName().equals("rook") || piece.getName().equals("king")))
            piece.setFirstMoveDone(true);
        // removing the piece on newTile (if it exists), and replacing it with the piece
        int tempX = piece.getX();
        int tempY = piece.getY();
        if (newTile.getPiece() != null) {
            CURRENT_PIECES.remove(newTile.getPiece());
            newTile.removePiece();
        } else {
            // implementing en passant (capturing the adjacent pawn)
            if (real && piece.getName().equals("pawn") && piece.getX() != newTile.getX()) {
                Piece capturedPawn = BOARD[newTile.getY()+1][newTile.getX()].getPiece();
                addToGraveyard(capturedPawn);
                CURRENT_PIECES.remove(capturedPawn);
                BOARD[newTile.getY()+1][newTile.getX()].removePiece();
            }
        }
        newTile.setPiece(piece);
        BOARD[tempY][tempX].removePiece();

        // implementing castling
        if (real && piece.getName().equals("king")) {
            // moving the rook to the opposite side of the king
            if (newTile.getX()-tempX==2) {
                BOARD[7][7].getPiece().setFirstMoveDone(true);
                BOARD[7][tempX+1].setPiece(BOARD[7][7].getPiece());
                BOARD[7][7].removePiece();
            } else if (tempX-newTile.getX()==2) {
                BOARD[7][0].getPiece().setFirstMoveDone(true);
                BOARD[7][tempX-1].setPiece(BOARD[7][0].getPiece());
                BOARD[7][0].removePiece();
            }
        }
    }

    /**
     * flips the chessboard 180 degrees
     */
    public void flipBoard() {
        ArrayList<Piece> tempList = new ArrayList<>(CURRENT_PIECES);
        // moving the pieces to the opposite side of the chess board
        // corresponding x and y coordinates: 7-x and 7-y
        for (int i=0; i<tempList.size(); i++) {
            Piece piece = tempList.get(i);
            int x = piece.getX();
            int y = piece.getY();
            if (BOARD[7-y][7-x].getPiece() == null) {
                BOARD[7-y][7-x].setPiece(piece);
                BOARD[y][x].removePiece();
            } else {
                Piece temp = BOARD[7-y][7-x].getPiece();
                BOARD[7-y][7-x].setPiece(piece);
                BOARD[y][x].setPiece(temp);
                tempList.remove(BOARD[y][x].getPiece());
            }
        }
    }

    /**
     * @param piece: piece to check
     * @return a boolean representing whether the piece is checking the opponent's king
     */
    public boolean checkForCheck(Piece piece) {
        flipBoard();
        for (Tile move : piece.getMoves(BOARD))
            // checking if the opponent's king is an available capture
            if (move.getPiece() != null && !move.getPiece().getColor().equals(piece.getColor()) && move.getPiece().getName().equals("king")) {
                flipBoard();
                return true;
            }
        flipBoard();
        return false;
    }

    /**
     * @param color: the colour of the player to check
     * @return a boolean representing whether the player is checking their opponent's king
     */
    public boolean kingInCheck(String color) {
        // checking whether any of the current pieces are checking the king
        for (Piece piece : CURRENT_PIECES)
            if (!piece.getColor().equals(color) && checkForCheck(piece))
                return true;
        return false;
    }

    /**
     * @param color: the colour of the player to check
     * @return a boolean representing whether the player has been checkmated
     */
    public boolean checkForCheckmate(String color) {
        // checking the two checkmate conditions: the king is in check AND no piece can move
        for (int i=0; i<CURRENT_PIECES.size(); i++)
            if (CURRENT_PIECES.get(i).getColor().equals(color) && CURRENT_PIECES.get(i).getLegalMoves(this).size()>0)
                return false;
        return true;
    }

    /**
     * @return a boolean representing whether the current pieces are sufficient for a checkmate
     */
    public boolean hasSufficientMaterial() {
        boolean player1Sufficient = false;
        boolean player2Sufficient = false;
        // insufficient material conditions: lone king, or king alone with a bishop or knight
        for (Piece piece : CURRENT_PIECES) {
            if (!piece.getName().equals("bishop") && !piece.getName().equals("knight")) {
                if (piece.getColor().equals("WHITE"))
                    player1Sufficient = true;
                else
                    player2Sufficient = true;
            }
        }
        return player1Sufficient && player2Sufficient;
    }

    /**
     * @param g: the Graphics context in which to paint
     * draws a Board object
     */
    public void paint(Graphics g) {
        // painting the tiles on the chessboard
        for (int rows = 0; rows < 8; rows++) {
            for (int cols = 0; cols < 8; cols++) {
                Tile tile = BOARD[rows][cols];
                tile.paint(g);
            }
        }
        // painting the captured pieces on the left and right sides of the board
        int leftY = START_Y;
        int leftX = START_X-150;
        int rightY = START_Y;
        int rightX = START_X+SIZE;
        // the captured pieces will be displayed in ascending order of their point value
        // black captured pieces on the left side, white captured pieces on the right side
        Collections.sort(GRAVEYARD);
        for (Piece deadPiece : GRAVEYARD) {
            if (deadPiece.getColor().equals("BLACK")) {
                g.drawImage(deadPiece.getImg(), leftX, leftY, null);
                leftY += 75;
                if (leftY > START_Y+SIZE-75) {
                    leftY = START_Y;
                    leftX += 75;
                }
            } else {
                g.drawImage(deadPiece.getImg(), rightX, rightY, null);
                rightY += 75;
                if (rightY > START_Y+SIZE-75) {
                    rightY = START_Y;
                    rightX += 75;
                }
            }
        }
        g.setColor(Main.LIGHT_GREEN);
        g.setFont(Main.BODY_FONT);
        // displaying the difference in overall point value between the two players
        if (BLACK_PLAYER.getPoints() > WHITE_PLAYER.getPoints()) {
            g.drawString("+" + (BLACK_PLAYER.getPoints() - WHITE_PLAYER.getPoints()), rightX + 25, rightY + 20);
        } else if (WHITE_PLAYER.getPoints() > BLACK_PLAYER.getPoints()) {
            g.drawString("+" + (WHITE_PLAYER.getPoints() - BLACK_PLAYER.getPoints()), leftX + 25, leftY + 20);
        }
        // displaying text at the top and bottom of the chessboard
        g.drawString(headingString,START_X, START_Y-17);
        g.drawString(gameUpdate,START_X, START_Y+SIZE+33);
    }
}
