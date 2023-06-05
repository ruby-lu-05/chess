/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * KOTHVariant class: inherits Game class and represents the "King of the Hill" variant of chess
 *
 * Sources:
 * - https://www.chess.com/terms/king-of-the-hill
 */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class KOTHVariant extends Game {
    /**
     * @param frame: the container to display the game on
     * @param player1: the Player object playing white
     * @param player2: the Player object playing black
     * instantiates an object representing a chess variant (King of the Hill)
     */
    public KOTHVariant(Container frame, Player player1, Player player2) {
        super(frame, player1, player2);
    }

    /**
     * @param player: name of the player
     * @param color: colour of the player
     * sets the heading text (changes each round to indicate whose turn it is)
     */
    @Override
    public void setPlayerTurn(String player, String color) {
        getBoard().setHeadingString("King of the Hill  /  " + color.toUpperCase() + " to move (" + player + ")");
    }

    /**
     * @param x: x coordinate of the user's mouseclick
     * @param y: y coordinate of the user's mouseclick
     * handles clicks and win/draw conditions
     */
    @Override
    public void manageClick(int x, int y) throws IOException {
        manageMove(x, y);
        if (!getBoard().getUpdateString().equals("")) {
            getBoard().updateString("");
        }
        // checking whether a player's king has reached the top of the hill yet
        if (getOtherPlayer().isKingReachedCenter()) {
            JOptionPane.showMessageDialog(getFrame(),getOtherPlayer().getName()+" wins by reaching the top of the hill!", "Game Outcome",JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getOtherPlayer().getName().toUpperCase(),1);
            Main.addData(getCurrentPlayer().getName().toUpperCase(),-1);
            Main.addMatch(getOtherPlayer().getName() + " won against " + getCurrentPlayer().getName());
            Main.endGame();
        }
        // detecting checkmates and stalemates
        if (getBoard().kingInCheck(getCurrentPlayer().getColor())) {
            if (getBoard().checkForCheckmate(getCurrentPlayer().getColor())) {
                getFrame().repaint();
                JOptionPane.showMessageDialog(getFrame(),getOtherPlayer().getName() + " wins via checkmate!", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
                Main.addData(getOtherPlayer().getName().toUpperCase(),1);
                Main.addData(getCurrentPlayer().getName().toUpperCase(),-1);
                Main.addMatch(getOtherPlayer().getName() + " won against " + getCurrentPlayer().getName());
                Main.endGame();
            } else {
                getBoard().updateString("king is in check!");
            }
        } else {
            if (getBoard().checkForCheckmate(getCurrentPlayer().getColor())) {
                getFrame().repaint();
                JOptionPane.showMessageDialog(getFrame(),"Draw via stalemate", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
                Main.addData(getCurrentPlayer().getName().toUpperCase(),0);
                Main.addData(getOtherPlayer().getName().toUpperCase(),0);
                Main.addMatch(getCurrentPlayer().getName() + " and " + getOtherPlayer().getName() + " drew");
                Main.endGame();
            }
        }
        getFrame().repaint();
    }
}
