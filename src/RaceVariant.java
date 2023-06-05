/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * RaceVariant class: inherits Game class and represents a "Race" variant of chess
 */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class RaceVariant extends Game{
    /**
     * @param frame: the container to display the game on
     * @param player1: the Player object playing white
     * @param player2: the Player object playing black
     * instantiates an object representing a chess variant ("Race")
     */
    public RaceVariant(Container frame, Player player1, Player player2) {
        super(frame, player1, player2);
    }

    /**
     * @param player: name of the player
     * @param color: colour of the player
     * sets the heading text (changes each round to indicate whose turn it is)
     */
    @Override
    public void setPlayerTurn(String player, String color) {
        getBoard().setHeadingString("Chess Race  /  " + color.toUpperCase() + " to move (" + player + ")");
    }

    /**
     * @param piece: the piece to generate moves from
     * @return
     */
    @Override
    public ArrayList<Tile> getPieceMoves(Piece piece) {
        // for this variant, checks & checkmates don't exist
        // so, it uses Piece's getMoves method rather than getLegalMoves method
        return piece.getMoves(getBoard().getBoard());
    }

    /**
     * @param x: x coordinate of the user's mouseclick
     * @param y: y coordinate of the user's mouseclick
     * handles clicks and win/draw conditions
     */
    @Override
    public void manageClick(int x, int y) throws IOException {
        manageMove(x, y);
        getFrame().repaint();
        // checking if a player has reached the other side
        if (getOtherPlayer().isReachedEnd()) {
            JOptionPane.showMessageDialog(getFrame(),getOtherPlayer().getName() + " wins the race!", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getOtherPlayer().getName().toUpperCase(),1);
            Main.addData(getCurrentPlayer().getName().toUpperCase(),-1);
            Main.addMatch(getOtherPlayer().getName() + " won against " + getCurrentPlayer().getName());
            Main.endGame();
        }

        // checking if there is sufficient material left
        boolean player1HasPieces = false;
        boolean player2HasPieces = false;
        for (Piece piece : getBoard().getCurrentPieces()) {
            if (piece.getColor().equals("WHITE")) {
                player1HasPieces = true;
            } else {
                player2HasPieces = true;
            }
            if (player1HasPieces && player2HasPieces) {
                break;
            }
        }
        if (!player1HasPieces) {
            JOptionPane.showMessageDialog(getFrame(),getPlayer2().getName()+" wins via insufficient material", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getPlayer2().getName().toUpperCase(),1);
            Main.addData(getPlayer1().getName().toUpperCase(),-1);
            Main.addMatch(getPlayer2().getName() + " won against " + getPlayer1().getName());
            Main.endGame();
        }
        if (!player2HasPieces) {
            JOptionPane.showMessageDialog(getFrame(),getPlayer1().getName()+" wins via insufficient material", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getPlayer1().getName().toUpperCase(),1);
            Main.addData(getPlayer2().getName().toUpperCase(),-1);
            Main.addMatch(getPlayer1().getName() + " won against " + getPlayer2().getName());
            Main.endGame();
        }
    }
}
