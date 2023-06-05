/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Player class: represents one of the players of the game
 */

public class Player {
    private final String NAME;
    private final String COLOR;
    private int points = 39;                    // the total value of each player's pieces starts off as 39
    private boolean reachedEnd = false;         // represents whether the player has reached the opposite side of the board
    private boolean kingReachedCenter = false;  // represents whether the player's king has reached the middle of the board
    private int checks = 0;                     // represents how many times they've checked their opponent's king

    /**
     * @param name: String representing the name of the player
     * @param color: String representing the colour of the player's pieces ("WHITE" or "BLACK")
     * instantiates a Player object
     */
    public Player(String name, String color) {
        NAME = name;
        COLOR = color;
    }

    // setter and getter methods for checks, reachedEnd, points, kingReachedCenter
    public int getChecks() {
        return checks;
    }
    public void addCheck() {
        checks++;
    }
    public boolean isReachedEnd() {
        return reachedEnd;
    }
    public void setReachedEnd(boolean reachedEnd) {
        this.reachedEnd = reachedEnd;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public int getPoints() {
        return points;
    }
    public void removePoints(int points) {
        this.points -= points;
    }
    public boolean isKingReachedCenter() {
        return kingReachedCenter;
    }
    public void setKingReachedCenter(boolean kingReachedCenter) {
        this.kingReachedCenter = kingReachedCenter;
    }

    // getter methods for the player's colour and name
    public String getColor() {
        return COLOR;
    }
    public String getName() {
        return NAME;
    }
}