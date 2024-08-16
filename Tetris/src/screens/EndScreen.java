package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;





public class EndScreen {
    public static void showEndScreen(int score) {
    	// Stop the music from GameScreen
    	MusicPlayer player = MusicPlayer.getInstance();
    	player.stopMusic();

    	// Play the loser sound once when the end screen is shown
    	player.playMusic("Tetris\\src\\loser.wav", false);

       

        // Create and set up the window
        JFrame frame = new JFrame("Game Over");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                TetrisOpeningScreen.openScreen();
            }
        });
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the frame

        // Create the background panel with the background image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Tetris\\src\\game_over_image.jpg");
        backgroundPanel.setLayout(null); // Use null layout for absolute positioning

        // Create and add a label for the game over message
        JLabel gameOverLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gameOverLabel.setForeground(Color.BLACK);
        gameOverLabel.setBounds(0, 30, 400, 50); // Centered horizontally
        backgroundPanel.add(gameOverLabel);

        // Create and add a label for the score
        JLabel scoreLabel = new JLabel("Your score is " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(0, 80, 400, 30); // Centered horizontally
        backgroundPanel.add(scoreLabel);

        // Create and add a text field for the username input
        CustomInputbox usernameField = new CustomInputbox();
        backgroundPanel.add(usernameField);

        // Create and add a save score button with rounded corners
        CustomRoundedButton1 saveScoreButton = new CustomRoundedButton1("Save Score");
        saveScoreButton.setBounds(145, 160, 110, 30);
        saveScoreButton.setBackground(Color.BLACK);
        saveScoreButton.setForeground(Color.WHITE);
        saveScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the score with the entered username
                String username = usernameField.getText().trim();
                if (!username.isEmpty()) {
                    ScoreBoard.saveScore(username, score);
                    ScoreboardDisplay.showScoreboard();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        backgroundPanel.add(saveScoreButton);

        // Create and add a home page button with rounded corners
        CustomRoundedButton1 homeButton = new CustomRoundedButton1("Home Page");
        homeButton.setBounds(145, 200, 110, 30);
        homeButton.setBackground(Color.BLACK);
        homeButton.setForeground(Color.WHITE);
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the home page
                TetrisOpeningScreen.openScreen();
                frame.dispose(); // Close the end screen
            }
        });
        backgroundPanel.add(homeButton);

        // Add the panel to the frame
        frame.getContentPane().add(backgroundPanel);

        // Display the window
        frame.setVisible(true);
    }

   
}

// Custom JPanel class for drawing background image
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
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
class CustomRoundedButton extends JButton {
    public CustomRoundedButton(String label) {
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


class CustomInputbox extends JTextField {

    public CustomInputbox() {
        // Create and set properties for the input box
        setBounds(150, 120, 200, 30);
        setFont(new Font("Arial", Font.PLAIN, 16));
        setHorizontalAlignment(JTextField.CENTER);
        
        // Limit the number of characters to 10
        int maxCharacters = 10;
        ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= maxCharacters) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() + text.length() - length) <= maxCharacters) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // Add the label directly within the class
        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBounds(50, 120, 100, 30);  // Adjusted to be next to the input box
        
        // Add the label to the parent container
        Container parent = getParent();
        if (parent != null) {
            parent.add(nameLabel);
        }
    }

    // Overriding the addNotify method to ensure the label is added after the component is added to the container
    @Override
    public void addNotify() {
        super.addNotify();
        Container parent = getParent();
        if (parent != null) {
            JLabel nameLabel = new JLabel("Your Name:");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameLabel.setBounds(50, 120, 100, 30);  // Adjusted to be next to the input box
            parent.add(nameLabel);
        }
    }
    
    
   

}