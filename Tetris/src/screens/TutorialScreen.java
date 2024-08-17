package screens;

import javax.swing.*;
import tetris.GameManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TutorialScreen extends JFrame {
    private BufferedImage backgroundImage;

    public TutorialScreen(boolean openGame) {
        setTitle("Tetris Tutorial");
        setSize(400, 340);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the tutorial content
        JPanel tutorialPanel = new JPanel();
        tutorialPanel.setLayout(new BoxLayout(tutorialPanel, BoxLayout.Y_AXIS));
        tutorialPanel.setOpaque(false); // Make the panel transparent
        tutorialPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add tutorial explanation
        JLabel titleLabel = new JLabel("How to Play Tetris");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tutorialPanel.add(titleLabel);

        JLabel explanationLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Tetris is a game where you stack falling blocks to create full lines. "
                + "The game ends when the blocks reach the top of the screen."
                + "</div></html>");
        explanationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        explanationLabel.setForeground(Color.WHITE);
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        explanationLabel.setHorizontalAlignment(JLabel.CENTER);
        explanationLabel.setVerticalAlignment(JLabel.CENTER);
        explanationLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        tutorialPanel.add(explanationLabel);

        // Add controls explanation
        JLabel controlsLabel = new JLabel("Controls:");
        controlsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        controlsLabel.setForeground(Color.WHITE);
        controlsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tutorialPanel.add(controlsLabel);

        JLabel controlsTextLabel = new JLabel("<html><div style='text-align: center;'>"
                + "Move Left: Left Arrow Key<br>"
                + "Move Right: Right Arrow Key<br>"
                + "Move Down: Down Arrow Key<br>"
                + "Rotate Piece: Space Bar<br>"
                + "Pause game: Escape"
                + "</div></html>");
        controlsTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        controlsTextLabel.setForeground(Color.WHITE);
        controlsTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlsTextLabel.setHorizontalAlignment(JLabel.CENTER);
        controlsTextLabel.setVerticalAlignment(JLabel.CENTER);
        tutorialPanel.add(controlsTextLabel);

        // Add Continue button
        CustomRoundedButton continueButton = new CustomRoundedButton("Continue");
        continueButton.setPreferredSize(new Dimension(120, 30)); // Make button larger
        continueButton.setBackground(new Color(0, 255, 0)); // Green background
        continueButton.setForeground(Color.WHITE); // Black text
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if(openGame)
                	new GameManager();
            }
        });

        // Add button with some vertical space
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(continueButton);
        tutorialPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add space before button
        tutorialPanel.add(buttonPanel);

        // Create a panel with the background image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Tetris\\src\\score_board_image.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(tutorialPanel, BorderLayout.CENTER);

        // Add the panel to the frame
        add(backgroundPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
