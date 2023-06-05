/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * CaptureVariant class: inherits Game class and represents a "Just Capture" variant of chess
 */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class CaptureVariant extends Game {
    /**
     * @param frame: the container to display the game on
     * @param player1: the Player object playing white
     * @param player2: the Player object playing black
     * instantiates an object representing a chess variant ("Just Capture")
     */
    public CaptureVariant(Container frame, Player player1, Player player2) {
        super(frame, player1, player2);
    }

    /**
     * @param player: name of the player
     * @param color: colour of the player
     * sets the heading text (changes each round to indicate whose turn it is)
     */
    @Override
    public void setPlayerTurn(String player, String color) {
        getBoard().setHeadingString("Just Capture  /  " + color.toUpperCase() + " to move (" + player + ")");
    }

    /**
     * @param piece: the piece to generate moves from
     * @return an ArrayList of Tiles representing the piece's available moves
     */
    @Override
    public ArrayList<Tile> getPieceMoves(Piece piece) {
        // for this variant, checks & checkmates don't exist
        // so, it uses Piece's getMoves method rather than getLegalMoves method
        return piece.getMoves(getBoard().getBoard());
    }

    /**
     * @param color: String representing the player's colour ("WHITE" or "BLACK")
     * @return a boolean representing whether the player has won
     */
    public boolean playerWon(String color) {
        if (getBoard().getCurrentPieces().size()<=16) {
            for (Piece piece : getBoard().getCurrentPieces()) {
                // if the other player still has pieces, the player hasn't won yet
                if (!piece.getColor().equals(color)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param x: x coordinate of the user's mouseclick
     * @param y: y coordinate of the user's mouseclick
     * handles clicks and win/draw conditions
     */
    @Override
    public void manageClick(int x, int y) throws IOException {
        manageMove(x,y);
        getFrame().repaint();
        // ending the game when a player is done capturing the other player's pieces
        if (playerWon(getPlayer1().getColor())) {
            JOptionPane.showMessageDialog(getFrame(),getPlayer1().getName() + " wins!", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getPlayer1().getName().toUpperCase(),1);
            Main.addData(getPlayer2().getName().toUpperCase(),-1);
            Main.addMatch(getPlayer1().getName() + " won against " + getPlayer2().getName());
            Main.endGame();
        } else if (playerWon(getPlayer2().getColor())) {
            JOptionPane.showMessageDialog(getFrame(),getPlayer2().getName() + " wins!","Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getPlayer2().getName().toUpperCase(),1);
            Main.addData(getPlayer1().getName().toUpperCase(),-1);
            Main.addMatch(getPlayer2().getName() + " won against " + getPlayer1().getName());
            Main.endGame();
        }
    }
}
