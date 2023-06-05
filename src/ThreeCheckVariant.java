/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * ThreeCheckVariant class: inherits Game class and represents the "3-Check" variant of chess
 *
 * Sources:
 * - https://www.chess.com/terms/3-check-chess
 */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ThreeCheckVariant extends Game {
    /**
     * @param frame: the container to display the game on
     * @param player1: the Player object playing white
     * @param player2: the Player object playing black
     * instantiates an object representing a chess variant (3-check)
     */
    public ThreeCheckVariant(Container frame, Player player1, Player player2) {
        super(frame, player1, player2);
        getBoard().updateString(getPlayer1().getName() + ": " + getPlayer1().getChecks() + "  |  "
        + getPlayer2().getName() + ": " + getPlayer2().getChecks());
    }

    /**
     * @param player: name of the player
     * @param color: colour of the player
     * sets the heading text (changes each round to indicate whose turn it is)
     */
    @Override
    public void setPlayerTurn(String player, String color) {
        getBoard().setHeadingString("3-Check  |  " + color.toUpperCase() + " to move (" + player + ")");
    }

    /**
     * @param x: x coordinate of the user's mouseclick
     * @param y: y coordinate of the user's mouseclick
     * handles clicks and win/draw conditions
     */
    @Override
    public void manageClick(int x, int y) throws IOException {
        manageMove(x, y);
        // checks whether a player has won by checkmate
        if (getBoard().kingInCheck(getCurrentPlayer().getColor())) {
            if (getBoard().checkForCheckmate(getCurrentPlayer().getColor())) {
                getFrame().repaint();
                JOptionPane.showMessageDialog(getFrame(),getOtherPlayer().getName() + " wins via checkmate!", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
                Main.addData(getOtherPlayer().getName().toUpperCase(),1);
                Main.addData(getCurrentPlayer().getName().toUpperCase(),-1);
                Main.addMatch(getOtherPlayer().getName() + " won against " + getCurrentPlayer().getName());
                Main.endGame();
            } else {
                getBoard().updateString(getPlayer1().getName() + ": " + getPlayer1().getChecks() + "  |  "
                        + getPlayer2().getName() + ": " + getPlayer2().getChecks());
                // checks whether a player has won by checking 3 times
                if (getOtherPlayer().getChecks()>=3) {
                    JOptionPane.showMessageDialog(getFrame(),getOtherPlayer().getName() + " wins by checking 3 times!","Game Outcome", JOptionPane.INFORMATION_MESSAGE);
                    Main.addData(getOtherPlayer().getName().toUpperCase(),1);
                    Main.addData(getCurrentPlayer().getName().toUpperCase(),-1);
                    Main.addMatch(getOtherPlayer().getName() + " won against " + getCurrentPlayer().getName());
                    Main.endGame();
                }
            }
        } else {
            // check whether a stalemate has occurred
            if (getBoard().checkForCheckmate(getCurrentPlayer().getColor())) {
                getFrame().repaint();
                JOptionPane.showMessageDialog(getFrame(),"Draw via stalemate", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
                Main.addData(getCurrentPlayer().getName().toUpperCase(),0);
                Main.addData(getOtherPlayer().getName().toUpperCase(),0);
                Main.addMatch(getCurrentPlayer().getName() + " and " + getOtherPlayer().getName() + " drew");
                Main.endGame();
            }
        }

        // check whether there is sufficient material left
        if (getBoard().getCurrentPieces().size()==2 && getBoard().getCurrentPieces().get(0).getName().equals("king") &&
                getBoard().getCurrentPieces().get(0).getName().equals("king")) {
            JOptionPane.showMessageDialog(getFrame(),"Draw via insufficient material", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getCurrentPlayer().getName().toUpperCase(),0);
            Main.addData(getOtherPlayer().getName().toUpperCase(),0);
            Main.addMatch(getCurrentPlayer().getName() + " and " + getOtherPlayer().getName() + " drew");
            Main.endGame();
        }
        getFrame().repaint();
    }
}
