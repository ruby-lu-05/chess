/*
 * Ruby Lu
 * 1/23/2023
 * ICS4U Final Project: Chess
 * Main class: handles the menu screens and overall game
 *
 * Sources:
 * - https://www.geeksforgeeks.org/jlabel-java-swing/
 * - https://www.educba.com/joptionpane-in-java/
 * - https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html
 * - https://docs.oracle.com/javase/7/docs/api/javax/swing/JFrame.html
 * - https://www.geeksforgeeks.org/read-file-into-an-array-in-java/
 * - https://www.baeldung.com/java-write-to-file
 */

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    // public static constants to be accessed by all classes
    public static final Color LIGHT_GREEN = new Color(221, 226, 198);
    public static final Color DARK_GREEN = new Color(187, 197, 170);
    public static final Color BLACK = new Color(30, 45, 47);
    public static final Color TRANSPARENT_BLACK = new Color (30,45,47,125);
    public static final Color WHITE = Color.WHITE;
    public static final Font BODY_FONT = new Font("Lucida Sans Typewriter", Font.PLAIN, 16);
    public static final Font TITLE_FONT = new Font("Lucida Sans Typewriter", Font.BOLD,100);
    public static final int HEIGHT = 750;  // height of GUI window
    public static final int WIDTH = 900;   // width of GUI window

    // arrays containing the names of variants and their rules
    public static final String[] variants = {"Standard Chess", "Chess960", "Just Capture", "3-Check", "Race", "King of the Hill", "Knight Frenzy", "Suicide Chess"};
    public static final String[] variantRules = {
            "Standard chess rules apply (castling, en passant, and pawn\n" +
                    "promotion included).\n\n" +
                    "Win conditions:\n" +
                    "- Checkmate\n" +
                    "- Opponent resigns\n\n" +
                    "Draw conditions:\n" +
                    "- Stalemate\n" +
                    "- Insufficient Material\n" +
                    "- Agreement",
            "Standard chess rules apply, but the starting positions\n" +
                    "of the back row for each player are shuffled.\n\n" +
                    "Win conditions:\n" +
                    "- Checkmate\n" +
                    "- Opponent resigns\n\n" +
                    "Draw conditions:\n" +
                    "- Stalemate\n" +
                    "- Insufficient Material\n" +
                    "- Agreement",
            "Rather than checkmating, the goal is to capture all of\n" +
                    "your opponent's pieces. Checks and checkmates do not\n" +
                    "exist (in other words, the king has no royal power and\n" +
                    "can be captured just like any other piece).\n\n" +
                    "Win conditions:\n" +
                    "- Opponent has no more pieces\n" +
                    "- Opponent resigns\n\n" +
                    "Draw conditions:\n" +
                    "- Agreement",
            "Standard chess rules apply, but there is an extra win\n" +
                    "condition: you can also win by checking your opponent's\n" +
                    "king three times.\n\n" +
                    "Win conditions:\n" +
                    "- Checkmate\n" +
                    "- Check three times\n" +
                    "- Opponent resigns\n\n" +
                    "Draw conditions:\n" +
                    "- Stalemate\n" +
                    "- Insufficient Material\n" +
                    "- Agreement",
            "Rather than checkmating, the goal is to get any of\n" +
                    "your pieces to the other side of the board. Checks and\n" +
                    "checkmates don't exist (in other words, the king has no\n" +
                    "royal power and can be captured just like any other piece).\n\n" +
                    "Win conditions:\n" +
                    "- Reach the other side of the board with any piece\n" +
                    "- Opponent has no more pieces\n" +
                    "- Opponent resigns\n\n" +
                    "Draw conditions:\n" +
                    "- Agreement",
            "Standard chess rules apply, but there is an extra win\n" +
                    "condition: you can also win by getting your king to one\n" +
                    "of the four center squares (the \"top of the hill\").\n\n" +
                    "Win conditions:\n" +
                    "- Checkmate\n" +
                    "- King reaches the \"top of the hill\"\n" +
                    "- Opponent resigns\n\n" +
                    "Draw conditions:\n" +
                    "- Stalemate\n" +
                    "- Agreement",
            "Standard chess rules apply, but instead of the standard opening\n" +
                    "position, each player starts with one king and 15 knights.\n\n" +
                    "Win conditions:\n" +
                    "- Checkmate\n" +
                    "- Opponent resigns\n\n" +
                    "Draw conditions:\n" +
                    "- Stalemate\n" +
                    "- Insufficient material\n" +
                    "- Agreement",
            "The goal is to lose all your pieces or get stalemated. However,\n" +
                    "if you have the opportunity to capture one of your opponent's\n" +
                    "pieces, you are forced to capture it (if you're able to capture\n" +
                    "more than one, you can choose which one to capture).\n\n" +
                    "Win conditions:\n" +
                    "- Lose all your pieces\n" +
                    "- Stalemate (no more legal moves)\n" +
                    "- Opponent resigns\n\n" +
                    "Draw conditions:\n" +
                    "- Insufficient Material\n" +
                    "- Agreement"
    };
    public static String variant = "Standard Chess";  // which variant of chess the player is currently playing
    public static int variantIndex = 0;               // the index of the variant within the variants field
    public static JFrame gameFrame;
    private static Game game;
    private static Player player1;
    private static Player player2;
    private static final Path dataFile = Paths.get("player_data.txt");        // file to store player data (win/loss ratios)
    private static final Path matchHistory = Paths.get("match_history.txt");  // file to store match history

    /**
     * creates a JFrame for the intro screen, where players can view player data, view match history, or continue
     */
    public static void introScreen() {
        JFrame frame = new JFrame();
        frame.setTitle("Multiverse of Chess");
        frame.setIconImage(new ImageIcon("icon.png").getImage());
        frame.setSize(WIDTH,HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        // two pawn icons on the sides of the screen
        ImageIcon blackPawnPic = new ImageIcon("pawn_black.png");
        ImageIcon whitePawnPic = new ImageIcon("pawn_white.png");
        JLabel whitePawn = new JLabel(whitePawnPic);
        whitePawn.setBounds(WIDTH/2-WIDTH*3/10-37,HEIGHT/2+32,75,75);
        frame.add(whitePawn);
        JLabel blackPawn = new JLabel(blackPawnPic);
        blackPawn.setBounds(WIDTH/2+WIDTH*3/10-37,HEIGHT/2+32,75,75);
        frame.add(blackPawn);

        // titles and subtitles
        JLabel mainTitle = new JLabel("CHESS");
        mainTitle.setBounds(WIDTH/2-WIDTH*3/10,100,WIDTH*3/5,HEIGHT/2);
        mainTitle.setBackground(DARK_GREEN);
        mainTitle.setForeground(WHITE);
        mainTitle.setHorizontalAlignment(JLabel.CENTER);
        mainTitle.setFont(TITLE_FONT);
        mainTitle.setOpaque(true);
        frame.getContentPane().add(mainTitle);

        JLabel subtitleOne = new JLabel("Multiverse of");
        subtitleOne.setBounds(0,100,WIDTH*3/5,20);
        subtitleOne.setForeground(BLACK);
        subtitleOne.setHorizontalAlignment(JLabel.CENTER);
        subtitleOne.setFont(BODY_FONT);
        mainTitle.add(subtitleOne);

        JLabel subtitleTwo = new JLabel("[play chess variants!]");
        subtitleTwo.setBounds(0,260,WIDTH*3/5,20);
        subtitleTwo.setForeground(BLACK);
        subtitleTwo.setHorizontalAlignment(JLabel.CENTER);
        subtitleTwo.setFont(BODY_FONT);
        mainTitle.add(subtitleTwo);

        // button to view player data
        JButton seeData = new JButton("See Player Data");
        seeData.setBounds(WIDTH/2-WIDTH*3/10,500,175,40);
        seeData.setBackground(WHITE);
        seeData.setForeground(BLACK);
        seeData.setFont(BODY_FONT);
        seeData.setBorder(BorderFactory.createEmptyBorder());
        seeData.addActionListener(e -> {
            // displaying data from the player data file on a JOptionPane
            if (getText(dataFile).trim().equals("")) {
                JOptionPane.showMessageDialog(frame,"no player data to show","Player Data",
                        JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,getText(dataFile),"Player Data",JOptionPane.PLAIN_MESSAGE);
            }
        });
        frame.getContentPane().add(seeData);

        // button to view match history
        JButton seeMatchHistory = new JButton("See Match History");
        seeMatchHistory.setBounds(WIDTH/2-WIDTH*3/10+183,500,175,40);
        seeMatchHistory.setBackground(WHITE);
        seeMatchHistory.setForeground(BLACK);
        seeMatchHistory.setFont(BODY_FONT);
        seeMatchHistory.setBorder(BorderFactory.createEmptyBorder());
        seeMatchHistory.addActionListener(e -> {
            // displaying text from the match history file on a JOptionPane
            if (getText(matchHistory).trim().equals("")) {
                JOptionPane.showMessageDialog(frame, "no match history to show", "Match History", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, getText(matchHistory), "Match History", JOptionPane.PLAIN_MESSAGE);
            }
        });
        frame.getContentPane().add(seeMatchHistory);

        // button to continue to the next screen
        JButton playButton = new JButton("START");
        playButton.setBounds(WIDTH/2+WIDTH*3/10-175,500,175,40);
        playButton.setBackground(BLACK);
        playButton.setForeground(WHITE);
        playButton.setFont(BODY_FONT);
        playButton.setBorder(BorderFactory.createEmptyBorder());
        playButton.addActionListener(e -> {
            // disposing of the current JFrame and moving to the next screen
            frame.dispose();
            choiceScreen();
        });
        frame.getContentPane().add(playButton);

        frame.getContentPane().setBackground(LIGHT_GREEN);
        frame.setVisible(true);
    }

    /**
     * @param x: x coordinate of the button
     * @param y: y coordinate of the button
     * @param frame: frame to draw the button on
     * @param name: name of the variant to display on the button
     * @param instructions: rules for the variant
     */
    public static void addVariantButton(int x, int y, JFrame frame, String name, String instructions) {
        JButton button = new JButton(name);
        button.setBounds(x,y,265,40);
        button.setBackground(WHITE);
        button.setForeground(BLACK);
        button.setFont(BODY_FONT);
        button.setBorder(BorderFactory.createEmptyBorder());
        // JOptionPane displays the variant's rules when pressed
        button.addActionListener(e -> JOptionPane.showMessageDialog(frame,instructions,"About " + name,JOptionPane.INFORMATION_MESSAGE));
        frame.add(button);

        // adding an "info" icon onto the button
        ImageIcon infoImg = new ImageIcon("info.png");
        JLabel info = new JLabel(infoImg);
        button.add(info);
    }

    /**
     * creates a JFrame for a screen where players can choose which variant to play
     */
    public static void choiceScreen() {
        JFrame frame = new JFrame();
        frame.setTitle("Multiverse of Chess");
        frame.setIconImage(new ImageIcon("icon.png").getImage());
        frame.setSize(WIDTH,HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        JLabel mainTitle = new JLabel("variants:");
        mainTitle.setBounds(0,50,WIDTH,120);
        mainTitle.setForeground(WHITE);
        mainTitle.setHorizontalAlignment(JLabel.CENTER);
        mainTitle.setFont(TITLE_FONT);
        frame.getContentPane().add(mainTitle);

        // adding buttons that players can click to view rules for each variant
        int column1 = WIDTH/2-WIDTH*3/10;
        int column2 = WIDTH/2+WIDTH*3/10-265;
        addVariantButton(column1,200,frame,"Standard Chess",variantRules[0]);
        addVariantButton(column2,200,frame,"Chess960",variantRules[1]);
        addVariantButton(column1,250,frame,"Just Capture",variantRules[2]);
        addVariantButton(column2,250,frame,"3-Check",variantRules[3]);
        addVariantButton(column1,300,frame,"Race",variantRules[4]);
        addVariantButton(column2,300,frame,"King of the Hill",variantRules[5]);
        addVariantButton(column1,350,frame,"Knight Frenzy",variantRules[6]);
        addVariantButton(column2,350,frame,"Suicide Chess",variantRules[7]);

        // question to display as text
        JLabel chooseQuestion = new JLabel("What kind of chess do you want to play?");
        chooseQuestion.setBounds(column1,450,WIDTH*3/5,20);
        chooseQuestion.setForeground(BLACK);
        chooseQuestion.setFont(BODY_FONT);
        frame.add(chooseQuestion);
        JLabel learnMore = new JLabel("(click buttons above to learn more about each variant)");
        learnMore.setBounds(column1,475,WIDTH*3/5,20);
        learnMore.setForeground(BLACK);
        learnMore.setFont(BODY_FONT);
        frame.add(learnMore);

        // dropdown menu for players to choose which variant they want to play
        JComboBox<String> selectVariant = new JComboBox<>(variants);
        selectVariant.setBackground(WHITE);
        selectVariant.setFont(BODY_FONT);
        selectVariant.setForeground(BLACK);
        selectVariant.setBounds(column1,525,265,40);
        selectVariant.addActionListener(e -> {
            // storing the player's selection
            variantIndex = selectVariant.getSelectedIndex();
            variant = variants[variantIndex];
        });
        frame.add(selectVariant);

        // button for starting the game
        JButton startButton = new JButton("START GAME");
        startButton.setBounds(column2,525,265,40);
        startButton.setBackground(BLACK);
        startButton.setForeground(WHITE);
        startButton.setFont(BODY_FONT);
        startButton.setBorder(BorderFactory.createEmptyBorder());
        startButton.addActionListener(e -> {
            frame.dispose();


            String player1Name;
            String player2Name;
            while (true) {
                player1Name = JOptionPane.showInputDialog(gameFrame, "Player 1 (WHITE) Name:", "Enter Player Names", JOptionPane.QUESTION_MESSAGE);
                if (player1Name == null || player1Name.trim().equals(""))
                    JOptionPane.showMessageDialog(gameFrame, "Invalid response", "Enter Player Names", JOptionPane.ERROR_MESSAGE);
                else
                    break;
            }
            while (true) {
                player2Name = JOptionPane.showInputDialog(gameFrame, "Player 2 (BLACK) Name:", "Enter Player Names", JOptionPane.QUESTION_MESSAGE);
                if (player2Name == null || player2Name.trim().equals("") || player2Name.trim().equals(player1Name) || player2Name.trim().equals(","))
                    JOptionPane.showMessageDialog(gameFrame, "Invalid response", "Enter Player Names", JOptionPane.ERROR_MESSAGE);
                else
                    break;
            }
            player1 = new Player(player1Name, "WHITE");
            player2 = new Player(player2Name, "BLACK");
            try {
                gameScreen();  // moving onto the actual gameplay screen
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        frame.add(startButton);

        frame.getContentPane().setBackground(DARK_GREEN);
        frame.setVisible(true);
    }

    /**
     * displays a screen for players to play the game
     */
    public static void gameScreen() throws IOException {
        gameFrame = new JFrame();
        gameFrame.setTitle("Multiverse of Chess");
        gameFrame.setIconImage(new ImageIcon("icon.png").getImage());
        gameFrame.setSize(WIDTH,HEIGHT);
        gameFrame.getContentPane().setPreferredSize(new Dimension(900,750));
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        // making new game objects depending on what the user inputted
        switch (variantIndex) {
            case 1: game = new Chess960Variant(gameFrame.getContentPane(),player1,player2); break;
            case 2: game = new CaptureVariant(gameFrame.getContentPane(),player1,player2); break;
            case 3: game = new ThreeCheckVariant(gameFrame.getContentPane(),player1,player2); break;
            case 4: game = new RaceVariant(gameFrame.getContentPane(),player1,player2); break;
            case 5: game = new KOTHVariant(gameFrame.getContentPane(),player1,player2); break;
            case 6: game = new KnightVariant(gameFrame.getContentPane(),player1,player2); break;
            case 7: game = new SuicideVariant(gameFrame.getContentPane(),player1,player2); break;
            default: game = new Game(gameFrame.getContentPane(),player1,player2); break;
        }

        // button for user to offer a draw
        ImageIcon drawIcon = new ImageIcon("draw.png");
        JButton drawButton = new JButton("Offer Draw",drawIcon);
        drawButton.setBounds(390,685,175,30);
        drawButton.setBackground(LIGHT_GREEN);
        drawButton.setForeground(BLACK);
        drawButton.setFont(BODY_FONT);
        drawButton.addActionListener(e -> {
            try {
                game.offerDraw();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gameFrame.getContentPane().add(drawButton);

        // button for user to resign
        ImageIcon resignIcon = new ImageIcon("resign.png");
        JButton resignButton = new JButton("Resign",resignIcon);
        resignButton.setBounds(575,685,175,30);
        resignButton.setBackground(LIGHT_GREEN);
        resignButton.setForeground(BLACK);
        resignButton.setFont(BODY_FONT);
        resignButton.addActionListener(e -> {
            try {
                game.resign();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gameFrame.getContentPane().add(resignButton);

        // adding a help button so users can be reminded of the variant's rules, and how to use the GUI
        ImageIcon infoImg = new ImageIcon("info.png");
        JButton info = new JButton(infoImg);
        info.setBackground(BLACK);
        info.setBorder(BorderFactory.createEmptyBorder());
        info.setBounds(WIDTH-190,35,40,40);
        info.addActionListener(e -> JOptionPane.showMessageDialog(gameFrame,
                variantRules[variantIndex] + "\n\n" +
                        "How to play:\n" +
                        "- Click any piece of your colour\n" +
                        "- Every available move for that piece will be highlighted\n" +
                        "- Click on one of the highlighted moves to move the piece\n" +
                        "- The board will flip 180 degrees based on whose turn it is", "How to Play",
                JOptionPane.INFORMATION_MESSAGE));
        gameFrame.getContentPane().add(info);

        // starting the game
        gameFrame.getContentPane().add(game.getBoard());
        game.addPieces();
        game.play();

        gameFrame.setBackground(BLACK);
        gameFrame.setVisible(true);
    }

    /**
     * closes the game and asks the players what they want to do next
     */
    public static void endGame() throws IOException {
        // allowing players to choose between a rematch, a new game, returning to the home screen, or exiting
        String option = JOptionPane.showInputDialog(null,
                "Enter \"r\" for a rematch\n" +
                        "Enter \"n\" for a new game\n" +
                        "Enter \"h\" to return to home screen\n" +
                        "Enter anything else to exit","Game Over",JOptionPane.QUESTION_MESSAGE);
        if (option != null && option.equalsIgnoreCase("r")) {
            // if a rematch is chosen, the players can choose to switch colours
            String temp = player2.getName();
            int answer = JOptionPane.showConfirmDialog(gameFrame,"Switch colours?\n" +
                            player1.getName()+" --> BLACK\n" +
                            player2.getName()+" --> WHITE",
                    "Switch colours?",JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                player2 = new Player(player1.getName(),"BLACK");
                player1 = new Player(temp,"WHITE");
            } else {
                player1 = new Player(player1.getName(),"WHITE");
                player2 = new Player(player2.getName(),"BLACK");
            }
            // starting a new game
            gameFrame.dispose();
            gameScreen();
        } else if (option != null && option.equalsIgnoreCase("n")) {
            // going back to the screen where users can choose which variant to play
            variant = "Standard Chess";
            variantIndex = 0;
            gameFrame.dispose();
            choiceScreen();
        } else if (option != null && option.equalsIgnoreCase("h")) {
            // going back to the home screen
            variant = "Standard Chess";
            variantIndex = 0;
            gameFrame.dispose();
            introScreen();
        } else {
            // exiting the program
            gameFrame.dispose();
        }
    }

    /**
     * @param winner: String in the format "[winner's name] won against [loser's name]" OR "[player 1] and [player 2] drew"
     */
    public static void addMatch(String winner) {
        // adding the new match results to the top of the match history
        String currentMatches = getText(matchHistory);
        String newMatches = new Date() + ": " + winner + " in " + variant + "\n" + currentMatches;
        try {
            // adding the match to the file (while removing possible extra newlines)
            FileWriter writer = new FileWriter(matchHistory.toFile());
            writer.write(newMatches.replaceAll("\n\n","\n"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file: the file to read from
     * @return a String representing the text on the file
     */
    public static String getText(Path file) {
        StringBuilder text = new StringBuilder();
        try {
            // turning the lines in the file into an ArrayList
            ArrayList<String> lines = (ArrayList<String>)Files.readAllLines(file);
            // iterating each line in the ArrayList to make a String
            for (int i=0; i<lines.size(); i++) {
                text.append(lines.get(i)).append("\n");
                // only ~40 lines will be displayed at a time
                if (i==30) {
                    text.append("...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString().replaceAll("\n\n","\n").trim();
    }

    /**
     * @param name: name of the player
     * @param result: integer representing their outcome (1 = win, -1 = loss, 0 = draw)
     */
    public static void addData(String name, int result) {
        String currentData = getText(dataFile);
        if (!currentData.contains(name)) {
            // adding the player's name and stats if it isn't in the file yet
            try {
                FileWriter writer = new FileWriter(dataFile.toFile(), true);
                String newData = "\n" + name + ": ";
                if (result == -1) {
                    writer.write(newData + "0 win, 1 loss, 0 draw\n");
                } else if (result == 1) {
                    writer.write(newData + "1 win, 0 loss, 0 draw\n");
                } else {
                    writer.write(newData + "0 win, 0 loss, 1 draw\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // updating the player's stats if their name is already in the file
            String newLine;
            String newData;
            // getting the line of text with the player's name on it, and extracting the number of wins, losses, and draws
            String currentLine = currentData.substring(currentData.indexOf(name),currentData.indexOf("draw",currentData.indexOf(name))+4);
            int numWins = Integer.parseInt(currentLine.substring(currentLine.indexOf(":")+2,currentLine.lastIndexOf(" win")));
            int numLosses = Integer.parseInt(currentLine.substring(currentLine.lastIndexOf("win, ")+5,currentLine.lastIndexOf(" loss")));
            int numDraws = Integer.parseInt(currentLine.substring(currentLine.lastIndexOf("loss, ")+6,currentLine.lastIndexOf(" draw")));
            // adding to the number of wins, losses, or draws based on the outcome of the game
            if (result == -1)
                numLosses++;
            else if (result == 1)
                numWins++;
            else
                numDraws++;
            newLine = name + ": " + numWins + " win, " + numLosses + " loss, " + numDraws + " draw";

            newData = currentData.substring(0, currentData.indexOf(name)) + newLine + currentData.substring(currentData.indexOf("draw",currentData.indexOf(name))+4);

            // updating the file (while removing possible extra newlines)
            try {
                FileWriter writer = new FileWriter(dataFile.toFile());
                writer.write(newData.replaceAll("\n\n","\n").trim());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * executing the program
     */
    public static void main(String[] args) {
        introScreen();
    }
}
