/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Chess960Variant class: inherits Game class and represents the "Chess960" variant of chess
 *
 * Sources:
 * - https://en.wikipedia.org/wiki/Fischer_random_chess
 */

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Chess960Variant extends Game {
    /**
     * @param frame: the container to display the game on
     * @param player1: the Player object playing white
     * @param player2: the Player object playing black
     * instantiates an object representing a chess variant (Chess960)
     */
    public Chess960Variant(Container frame, Player player1, Player player2) {
        super(frame, player1, player2);
    }

    /**
     * @param player: name of the player
     * @param color: colour of the player
     * sets the heading text (changes each round to indicate whose turn it is)
     */
    @Override
    public void setPlayerTurn(String player, String color) {
        getBoard().setHeadingString("Chess960  /  " + color.toUpperCase() + " to move (" + player + ")");
    }

    /**
     * sets up the chessboard in a random opening positioning
     */
    @Override
    public void addPieces() throws IOException {
        // placing pawns in the standard location
        for (int i=0; i<8; i++) {
            addPiece(i,6,new Pawn("pawn",true));
            addPiece(i,1,new Pawn("pawn", false));
        }

        ArrayList<String> positions = new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7"));

        // adding the king, queen, rook, bishop, and knight pieces randomly
        String position = positions.get((int)(Math.random()*7));
        addPiece(Integer.parseInt(position),7,new King("king",true));
        addPiece(Integer.parseInt(position),0,new King("king",false));
        positions.remove(position);

        position = positions.get((int)(Math.random()*6));
        addPiece(Integer.parseInt(position),7,new Queen("queen",true));
        addPiece(Integer.parseInt(position),0,new Queen("queen",false));
        positions.remove(position);

        position = positions.get((int)(Math.random()*5));
        addPiece(Integer.parseInt(position),7,new Rook("rook",true));
        addPiece(Integer.parseInt(position),0,new Rook("rook",false));
        positions.remove(position);

        position = positions.get((int)(Math.random()*4));
        addPiece(Integer.parseInt(position),7,new Rook("rook",true));
        addPiece(Integer.parseInt(position),0,new Rook("rook",false));
        positions.remove(position);

        position = positions.get((int)(Math.random()*3));
        addPiece(Integer.parseInt(position),7,new Bishop("bishop",true));
        addPiece(Integer.parseInt(position),0,new Bishop("bishop",false));
        positions.remove(position);

        position = positions.get((int)(Math.random()*2));
        addPiece(Integer.parseInt(position),7,new Bishop("bishop",true));
        addPiece(Integer.parseInt(position),0,new Bishop("bishop",false));
        positions.remove(position);

        position = positions.get((int)(Math.random()*1));
        addPiece(Integer.parseInt(position),7,new Knight("knight",true));
        addPiece(Integer.parseInt(position),0,new Knight("knight",false));
        positions.remove(position);

        position = positions.get(0);
        addPiece(Integer.parseInt(position),7,new Knight("knight",true));
        addPiece(Integer.parseInt(position),0,new Knight("knight",false));
        positions.remove(position);
    }
}
