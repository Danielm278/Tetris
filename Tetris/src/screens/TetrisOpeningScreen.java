package screens;
import javax.swing.*;

import tetris.Board;
import tetris.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class TetrisOpeningScreen  extends Thread implements IThread{
    public void run() {
    	MusicPlayer player = MusicPlayer.getInstance();
 	    player.playMusic("Tetris\\src\\welcome to the show.wav", false);
    	openScreen();
//        GameManager gameManager = new GameManager();
    }
    
    public static void openScreen() {
    	 
        // Ensure the scoreboard file exists
        ScoreBoard.ensureFileExists();

        // Create and set up the window
        JFrame frame = new JFrame("Tetris Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the frame

        // Use the absolute path for the image
        String imagePath = "Tetris\\src\\tetris_Image.jpg"; // Replace with your absolute path
        BackgroundPanel backgroundPanel = new BackgroundPanel(imagePath);
        backgroundPanel.setLayout(null); // Use null layout for absolute positioning

        
        // Create and add a start game button with rounded corners
        CustomRoundedButton startButton = new CustomRoundedButton("Start Game");
        startButton.setBounds(145, 135, 100, 30); // Adjust position and size for block 1
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//************************************************************************************************************************
        	    MusicPlayer player = MusicPlayer.getInstance();
        	    player.playMusic("Tetris\\src\\game_music.wav", true);
                // Code to start the game goes here
                GameManager gameManager = new GameManager();
                frame.dispose();
                // new TetrisGame().start(); // Un-comment and replace with actual game start code
            }
        });
        backgroundPanel.add(startButton);

        // Create and add a scoreboard button with rounded corners
        CustomRoundedButton scoreboardButton = new CustomRoundedButton("Scoreboard");
        scoreboardButton.setBounds(135, 170, 120, 30); // Position above the exit button
        scoreboardButton.setBackground(Color.BLUE);
        scoreboardButton.setForeground(Color.WHITE);
        scoreboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScoreboardDisplay.showScoreboard();
                frame.dispose();
            }
        });
        backgroundPanel.add(scoreboardButton);

        // Create and add an exit button with rounded corners
        CustomRoundedButton exitButton = new CustomRoundedButton("Exit");
        exitButton.setBounds(145, 205, 100, 30); // Adjust position and size for block 2
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });
        backgroundPanel.add(exitButton);

        // Create and add a small yellow button to go to the end screen
        CustomRoundedButton endScreenButton = new CustomRoundedButton("End Screen");
        endScreenButton.setBounds(10, 10, 100, 30); // Position the button
        endScreenButton.setBackground(Color.YELLOW);
        endScreenButton.setForeground(Color.BLACK);
        endScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the end screen
                EndScreen.showEndScreen(123); // Replace with actual username and score
                frame.dispose(); // Close the opening screen
            }
        });
        //backgroundPanel.add(endScreenButton);

        // Add the panel to the frame
        frame.getContentPane().add(backgroundPanel);

        // Display the window
        frame.setVisible(true);
    }
}

// Custom JPanel class for drawing background image
class OpenningScreenBackgroundPanel extends JPanel {
    private Image backgroundImage;

    public OpenningScreenBackgroundPanel(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.err.println("Image file does not exist: " + imageFile.getAbsolutePath());
            } else {
                backgroundImage = ImageIO.read(imageFile);
                System.out.println("Image loaded successfully: " + imageFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

// Custom JButton class for rounded corners
class CustomRoundedButton1 extends JButton {
    public CustomRoundedButton1(String label) {
        super(label);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        int width = getWidth();
        int height = getHeight();
        int arc = 15;
        return (x >= 0 && x <= width && y >= 0 && y <= height);
    }
}
