/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * SuicideVariant class: inherits Game class and represents the "Suicide Chess" variant of chess
 *
 * Sources:
 * - https://en.wikipedia.org/wiki/Losing_chess#:~:text=Losing%20chess%20(also%20known%20as,that%20is%2C%20a%20mis%C3%A8re%20version.
 */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SuicideVariant extends Game {
    /**
     * @param frame: the container to display the game on
     * @param player1: the Player object playing white
     * @param player2: the Player object playing black
     * instantiates an object representing a chess variant (Suicide Chess)
     */
    public SuicideVariant(Container frame, Player player1, Player player2) {
        super(frame, player1, player2);
    }

    /**
     * @param player: name of the player
     * @param color: colour of the player
     * sets the heading text (changes each round to indicate whose turn it is)
     */
    @Override
    public void setPlayerTurn(String player, String color) {
        getBoard().setHeadingString("Suicide Chess  |  " + color.toUpperCase() + " to move (" + player + ")");
    }

    /**
     * @param piece: the piece to generate moves from
     * @return an ArrayList of Tiles representing the piece's available moves
     */
    @Override
    public ArrayList<Tile> getPieceMoves(Piece piece) {
        // generating moves based on whether there is a capture available
        boolean canCapture = false;
        for (Piece otherPiece : getBoard().getCurrentPieces()) {
            if (otherPiece.getColor().equals(getCurrentPlayer().getColor()) && otherPiece.canCapture(getBoard())) {
                canCapture = true;
            }
        }
        return piece.getSuicideMoves(getBoard(), canCapture);
    }

    /**
     * @param x: x coordinate of the user's mouseclick
     * @param y: y coordinate of the user's mouseclick
     * handles clicks and win/draw conditions
     */
    @Override
    public void manageClick(int x, int y) throws IOException {
        manageMove(x, y);

        // checking whether there is sufficient material left
        boolean hasSufficientPieces = false;
        if (getBoard().getCurrentPieces().size()==2) {
            Piece piece1 = getBoard().getCurrentPieces().get(0);
            Piece piece2 = getBoard().getCurrentPieces().get(1);
            if (!piece1.getName().equals("bishop") && !piece2.getName().equals("bishop")) {
                hasSufficientPieces = true;
            } else {
                if (piece1.getY()%2 == piece2.getY()%2 && piece1.getX()%2 == piece2.getX()%2) {
                    hasSufficientPieces = true;
                }
            }
        } else {
            hasSufficientPieces = true;
        }
        if (!hasSufficientPieces) {
            JOptionPane.showMessageDialog(getFrame(),"Draw via insufficient material", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getCurrentPlayer().getName().toUpperCase(),0);
            Main.addData(getOtherPlayer().getName().toUpperCase(),0);
            Main.addMatch(getCurrentPlayer().getName() + " and " + getOtherPlayer().getName() + " drew");
            Main.endGame();
        }

        // checking whether any of the players have won by losing all pieces
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
            JOptionPane.showMessageDialog(getFrame(),getPlayer1().getName()+" wins!", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getPlayer1().getName().toUpperCase(),1);
            Main.addData(getPlayer2().getName().toUpperCase(),-1);
            Main.addMatch(getPlayer1().getName() + " won against " + getPlayer2().getName());
            Main.endGame();
        }
        if (!player2HasPieces) {
            JOptionPane.showMessageDialog(getFrame(),getPlayer2().getName()+" wins!", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getPlayer2().getName().toUpperCase(),1);
            Main.addData(getPlayer1().getName().toUpperCase(),-1);
            Main.addMatch(getPlayer2().getName() + " won against " + getPlayer1().getName());
            Main.endGame();
        }

        boolean hasMoves = false;
        for (Piece piece : getBoard().getCurrentPieces()) {
            if (piece.getColor().equals(getCurrentPlayer().getColor()) && getPieceMoves(piece).size()>0) {
                hasMoves = true;
            }
        }

        // checking for stalemate (still have pieces but no legal moves)
        if (!hasMoves && player1HasPieces && player2HasPieces) {
            JOptionPane.showMessageDialog(getFrame(),getOtherPlayer().getName()+" wins via stalemate!", "Game Outcome", JOptionPane.INFORMATION_MESSAGE);
            Main.addData(getOtherPlayer().getName().toUpperCase(),1);
            Main.addData(getCurrentPlayer().getName().toUpperCase(),-1);
            Main.addMatch(getOtherPlayer().getName() + " won against " + getCurrentPlayer().getName());
            Main.endGame();
        }
        getFrame().repaint();
    }
}
