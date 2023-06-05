/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Game class: represents a standard chess game
 *
 * Sources:
 * - https://docs.oracle.com/javase/7/docs/api/java/awt/event/MouseListener.html
 * - https://www.w3schools.com/java/java_try_catch.asp
 * - https://www.educba.com/joptionpane-in-java/
 */

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    private boolean chosen = false;  // represents whether the current player has chosen a piece to move
    private Tile chosenTile = null;  // represents the tile the current player chose
    private final Player PLAYER1;
    private final Player PLAYER2;
    private final Board B;
    private Player currentPlayer;    // represents the player whose turn it is
    private Player otherPlayer;
    private final Container FRAME;
    private Tile prevPiece = null;   // represents the previous piece that was moved
    private Tile prevMove = null;    // represents where the previous piece moved

    /**
     * @param frame: the container to display the game on
     * @param player1: the Player object playing white
     * @param player2: the Player object playing black
     * instantiates a Game object representing a chess game
     */
    public Game(Container frame, Player player1, Player player2) {
        FRAME = frame;
        PLAYER1 = player1;
        PLAYER2 = player2;
        currentPlayer = PLAYER1;
        otherPlayer = PLAYER2;
        B = new Board(150,75,600,this.PLAYER1,this.PLAYER2);
        setPlayerTurn(currentPlayer.getName(),currentPlayer.getColor());
    }

    /**
     * @param player: name of the player
     * @param color: colour of the player
     * sets the heading text (changes each round to indicate whose turn it is)
     */
    public void setPlayerTurn(String player, String color) {
        B.setHeadingString("Standard Chess  /  " + color.toUpperCase() + " to move (" + player + ")");
    }

    // getter methods for B, FRAME, PLAYER1, PLAYER2
    public Board getBoard() {
        return B;
    }
    public Container getFrame() {
        return FRAME;
    }
    public Player getPlayer1() {
        return PLAYER1;
    }
    public Player getPlayer2() {
        return PLAYER2;
    }

    // getter and setter methods for currentPlayer and otherPlayer
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }
    public Player getOtherPlayer() {
        return otherPlayer;
    }
    public void setOtherPlayer(Player player) {
        otherPlayer = player;
    }

    /**
     * @param x: the chessboard's x position to place the piece
     * @param y: the chessboard's y position to place the piece
     * @param piece: the piece to be placed
     * adds a piece to the chessboard based on the x and y position given
     */
    public void addPiece(int x, int y, Piece piece) {
        B.getBoard()[y][x].setPiece(piece);
        B.getCurrentPieces().add(piece);
    }

    /**
     * sets up the chessboard in the standard opening position
     */
    public void addPieces() throws IOException {
        // two rows of pawns, 2nd and 7th row
        for (int i=0; i<8; i++) {
            addPiece(i,6,new Pawn("pawn",true));
            addPiece(i,1,new Pawn("pawn", false));
        }

        // top row and back row
        for (int col=0; col<8; col++) {
            if (col == 1 || col == 6) {
                addPiece(col,7,new Knight("knight",true));
                addPiece(col,0,new Knight("knight",false));
            } else if (col == 2 || col == 5) {
                addPiece(col,7,new Bishop("bishop", true));
                addPiece(col,0,new Bishop("bishop", false));
            } else if (col == 0 || col == 7) {
                addPiece(col,7,new Rook("rook",true));
                addPiece(col,0, new Rook("rook",false));
            } else if (col == 3) {
                addPiece(col,7,new Queen("queen",true));
                addPiece(col,0,new Queen("queen",false));
            } else {
                addPiece(col,7,new King("king",true));
                addPiece(col,0,new King("king", false));
            }
        }
    }

    /**
     * @param piece: the piece to generate moves from
     * @return an ArrayList of Tiles representing the piece's available moves
     */
    public ArrayList<Tile> getPieceMoves(Piece piece) {
        return piece.getLegalMoves(B);
    }

    /**
     * switches the player and flips the chessboard (corresponding to whose turn it is)
     */
    public void switchPlayer() {
        // switching the reference of currentPlayer and otherPlayer
        if (currentPlayer == PLAYER1) {
            setCurrentPlayer(PLAYER2);
            setOtherPlayer(PLAYER1);
        } else {
            setCurrentPlayer(PLAYER1);
            setOtherPlayer(PLAYER2);
        }
        B.flipBoard();                                                    // flipping the board
        setPlayerTurn(currentPlayer.getName(),currentPlayer.getColor());  // changing the heading
        FRAME.repaint();
    }

    /**
     * @param tile: the tile that the pawn landed on
     * allows the user to choose which piece to promote their pawn to
     */
    public void promotePawn(Tile tile) throws IOException {
        Piece newPiece;
        boolean isWhite = currentPlayer.getColor().equals("WHITE");
        char piece;
        // using a JOptionPane to let the user input which piece they want to promote to
        // asks continuously until they enter a valid response
        prevPiece.setShowPrev(false);
        prevMove.setShowPrev(false);
        while (true) {
            String input = JOptionPane.showInputDialog(FRAME, "Promote your pawn!\n" +
                    "Enter \"n\" for knight\n" +
                    "Enter \"b\" for bishop\n" +
                    "Enter \"r\" for rook\n" +
                    "Enter \"q\" for queen\n","Pawn Promotion",JOptionPane.QUESTION_MESSAGE);
            if (input != null && input.length()==1 && "nbrq".contains(input.toLowerCase())) {
                piece = input.charAt(0);
                break;
            } else {
                JOptionPane.showMessageDialog(FRAME,"Invalid response","Pawn Promotion",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        // switching the pawn to the new piece (removing the pawn and adding the new piece)
        switch (piece) {
            case 'n': newPiece = new Knight("knight",isWhite); break;
            case 'b': newPiece = new Bishop("bishop",isWhite); break;
            case 'r': newPiece = new Rook("rook",isWhite); break;
            default: newPiece = new Queen("queen",isWhite); break;
        }
        currentPlayer.removePoints(-newPiece.getValue()+1);
        B.getCurrentPieces().remove(tile.getPiece());
        tile.removePiece();
        tile.setPiece(newPiece);
        B.getCurrentPieces().add(newPiece);
        prevPiece.setShowPrev(true);
        prevMove.setShowPrev(true);
    }

    /**
     * allows the player to offer a draw, and the other player to accept or decline
     */
    public void offerDraw () throws IOException {
        // removing the highlights on the previous move and available tiles (if they exist)
        if (prevMove != null) {
            prevMove.setShowPrev(false);
            prevPiece.setShowPrev(false);
        }
        if (chosen && chosenTile != null) {
            chosenTile.setActive(false,getPieceMoves(chosenTile.getPiece()));
            chosen = false;
        }
        // confirming whether the player wants to offer a draw
        int answer = JOptionPane.showConfirmDialog(FRAME,"Are you sure you'd like to offer a draw?","Offer Draw",JOptionPane.YES_NO_OPTION);
        switchPlayer();
        if (answer == JOptionPane.YES_OPTION) {
            // asking the other player to confirm the draw
            int answer2 = JOptionPane.showConfirmDialog(FRAME,otherPlayer.getName()+" would like to offer a draw. Accept?","Accept or Decline Draw",JOptionPane.YES_NO_OPTION);
            if (prevMove != null) {
                prevMove.setShowPrev(true);
                prevPiece.setShowPrev(true);
            }
            if (answer2 == JOptionPane.YES_OPTION) {
                // ending the game (and updating the match history & player data)
                JOptionPane.showMessageDialog(FRAME,"Draw via agreement","Game Outcome",JOptionPane.INFORMATION_MESSAGE);
                Main.addData(otherPlayer.getName().toUpperCase(),0);
                Main.addData(currentPlayer.getName().toUpperCase(),0);
                Main.addMatch(currentPlayer.getName() + " and " + otherPlayer.getName() + " drew");
                Main.endGame();
            } else {
                // going back to the game
                switchPlayer();
                JOptionPane.showMessageDialog(FRAME, "Your offer for a draw has been declined","Offer of Draw",JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            if (prevMove != null) {
                prevMove.setShowPrev(true);
                prevPiece.setShowPrev(true);
            }
            switchPlayer();
        }
    }

    /**
     * allowing the player to resign from the game (other player is given the win)
     */
    public void resign() throws IOException {
        // confirming whether the player wants to resign
        int answer = JOptionPane.showConfirmDialog(FRAME,"Are you sure you'd like to resign?","Resign",JOptionPane.YES_NO_OPTION);
        // removing the highlights on the previous move and available tiles (if they exist)
        if (chosen && chosenTile != null && chosenTile.getPiece() != null)
            chosenTile.setActive(false,getPieceMoves(chosenTile.getPiece()));
        if (prevMove != null) {
            prevMove.setShowPrev(false);
            prevPiece.setShowPrev(false);
        }
        switchPlayer();
        if (answer == JOptionPane.YES_OPTION) {
            // ending the game (and updating match history & player data)
            JOptionPane.showMessageDialog(FRAME,currentPlayer.getName()+" wins via resignation","Game Outcome",JOptionPane.INFORMATION_MESSAGE);
            Main.addData(currentPlayer.getName().toUpperCase(),1);
            Main.addData(otherPlayer.getName().toUpperCase(),-1);
            Main.addMatch(currentPlayer.getName() + " won against " + otherPlayer.getName());
            Main.endGame();
        } else {
            switchPlayer();
        }
    }

    /**
     * @param x: the x coordinate of the user's mouseclick
     * @param y: the y coordinate of the user's mouseclick
     * handles piece choosing & moving
     */
    public void manageMove (int x, int y) {
        // iterating over all the tiles to check whether the user clicked any of them
        for (Tile[] row : B.getBoard()) {
            for (Tile tile : row) {
                if (x>tile.getXCoord() && x<tile.getXCoord()+tile.getWidth() && y>tile.getYCoord() && y<tile.getYCoord()+tile.getWidth()) {
                    // checking whether the user clicked on their own piece
                    if (tile.getPiece() != null && tile.getPiece().getColor().equals(currentPlayer.getColor())) {
                        if (chosen) {
                            chosen = false;
                            chosenTile.setActive(false,getPieceMoves(chosenTile.getPiece()));
                        } else {
                            chosen = true;
                            chosenTile = tile;
                            chosenTile.setActive(true,getPieceMoves(chosenTile.getPiece()));
                        }
                    } else if (chosen && getPieceMoves(chosenTile.getPiece()).contains(tile)) {
                        if (prevPiece != null) {
                            prevPiece.setPrev(false);
                            prevMove.setPrev(false);
                        }
                        // checking whether a pawn moved two steps (this only matters when the next move is en passant)
                        if (chosenTile.getPiece().getName().equals("pawn") && Math.abs(chosenTile.getPiece().getY()-tile.getY())==2) {
                            chosenTile.getPiece().setMovedTwo(true);
                        }
                        if (prevMove != null && prevMove.getPiece().getName().equals("pawn") && Math.abs(prevMove.getY()-prevPiece.getY())==2) {
                            prevMove.setActive(false,getPieceMoves(chosenTile.getPiece()));
                            prevMove.getPiece().setMovedTwo(false);
                        }
                        // updating the previous piece & move
                        prevPiece = B.getBoard()[7-chosenTile.getY()][7-chosenTile.getX()];
                        prevPiece.setPrev(true);
                        prevMove = B.getBoard()[7-tile.getY()][7-tile.getX()];
                        prevMove.setPrev(true);
                        // checking whether the king has reached the center squares
                        if (chosenTile.getPiece().getName().equals("king") && tile.getX()>=3 && tile.getX()<=4 &&
                        tile.getY()>=3 && tile.getY()<=4) {
                            currentPlayer.setKingReachedCenter(true);
                        }
                        // checking whether the piece has reached the opposite end of the chessboard
                        if (tile.getY()==0) {
                            currentPlayer.setReachedEnd(true);
                        }
                        // handling captures
                        if (tile.getPiece() != null) {
                            // removing the piece from the board and removing points from the opponent
                            B.addToGraveyard(tile.getPiece());
                            otherPlayer.removePoints(tile.getPiece().getValue());
                        } else {
                            // handling en passant
                            if (chosenTile.getPiece().getName().equals("pawn") && chosenTile.getPiece().getX() != tile.getX()) {
                                otherPlayer.removePoints(1);
                            }
                        }
                        // after the piece has moved, the move options are no longer highlighted
                        chosenTile.setActive(false,getPieceMoves(chosenTile.getPiece()));
                        B.movePiece(true,chosenTile.getPiece(),tile);
                        // checking whether the piece has checked the opponent's king
                        if (B.kingInCheck(otherPlayer.getColor())) {
                            currentPlayer.addCheck();
                        }
                        // user can promote the pawn after it reaches the top of the screen
                        if (tile.getPiece().getName().equals("pawn") && tile.getY() == 0) {
                            FRAME.repaint();
                            try {
                                promotePawn(tile);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        switchPlayer();  // switch player after each move
                        chosen = false;
                    }
                }
            }
        }
    }

    /**
     * @param x: x coordinate of the user's mouseclick
     * @param y: y coordinate of the user's mouseclick
     * handles clicks and win/draw conditions
     */
    public void manageClick(int x, int y) throws IOException {
        manageMove(x,y);
        // clearing the game update at the bottom
        if (!B.getUpdateString().equals("")) {
            B.updateString("");
        }
        if (B.kingInCheck(currentPlayer.getColor())) {
            if (B.checkForCheckmate(currentPlayer.getColor())) {
                // detecting checkmate and ending the game (and updating match history & player data)
                FRAME.repaint();
                JOptionPane.showMessageDialog(FRAME, otherPlayer.getName() + " wins via checkmate!", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
                Main.addMatch(otherPlayer.getName() + " won against " + currentPlayer.getName());
                Main.addData(otherPlayer.getName().toUpperCase(),1);
                Main.addData(currentPlayer.getName().toUpperCase(),-1);
                Main.endGame();
            } else {
                // detecting checks and notifying the player
                B.updateString("king is in check!");
            }
        } else {
            // checking whether the player has any available moves (stalemate if no available moves + not in check)
            if (B.checkForCheckmate(currentPlayer.getColor())) {
                FRAME.repaint();
                // ending the game (and updating match history & player data)
                JOptionPane.showMessageDialog(FRAME,"Draw via stalemate", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
                Main.addData(otherPlayer.getName().toUpperCase(),0);
                Main.addData(currentPlayer.getName().toUpperCase(),0);
                Main.addMatch(currentPlayer.getName() + " and " + otherPlayer.getName() + " drew");
                Main.endGame();
            }
        }
        // checking whether the players have sufficient material
        if (B.getCurrentPieces().size()<=4 && !B.hasSufficientMaterial()) {
            // ending the game (and updating match history & player data)
            JOptionPane.showMessageDialog(FRAME,"Draw via insufficient material", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(otherPlayer.getName().toUpperCase(),0);
            Main.addData(currentPlayer.getName().toUpperCase(),0);
            Main.addMatch(currentPlayer.getName() + " and " + otherPlayer.getName() + " drew");
            Main.endGame();
        }
        FRAME.repaint();
    }

    /**
     * handles the events of the game and the mouse input
     */
    public void play () {
        FRAME.setBackground(Main.BLACK);
        FRAME.addMouseListener(
                new MouseInputAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int x = (int)e.getPoint().getX();
                        int y = (int)e.getPoint().getY();
                        try {
                            manageClick(x,y);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
    }
}
