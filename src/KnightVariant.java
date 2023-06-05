/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * KnightVariant class: inherits Game class and represents a "Knight Frenzy" variant of chess
 */

import java.awt.*;
import java.io.IOException;

public class KnightVariant extends Game {
    /**
     * @param frame: the container to display the game on
     * @param player1: the Player object playing white
     * @param player2: the Player object playing black
     * instantiates an object representing a chess variant ("Knight Frenzy")
     */
    public KnightVariant(Container frame, Player player1, Player player2) {
        super(frame, player1, player2);
    }

    /**
     * @param player: name of the player
     * @param color: colour of the player
     * sets the heading text (changes each round to indicate whose turn it is)
     */
    @Override
    public void setPlayerTurn(String player, String color) {
        getBoard().setHeadingString("Knight Frenzy  /  " + color.toUpperCase() + " to move (" + player + ")");
    }

    /**
     * sets up an opening position with 15 knights and 1 king
     */
    @Override
    public void addPieces() throws IOException {
        addPiece(4,7,new King("king",true));
        addPiece(4,0,new King("king",false));
        for (int i=0; i<=7; i++) {
            addPiece(i,6,new Knight("knight",true));
            addPiece(i,1,new Knight("knight",false));
            if (i != 4) {
                addPiece(i,7,new Knight("knight",true));
                addPiece(i,0,new Knight("knight",false));
            }
        }
        // the new starting point value of each player is 55
        getCurrentPlayer().setPoints(55);
        getOtherPlayer().setPoints(55);
    }
}
