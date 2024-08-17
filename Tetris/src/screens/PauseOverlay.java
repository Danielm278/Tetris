package screens;

import javax.swing.*;
import java.awt.*;

public class PauseOverlay extends JPanel {

    public PauseOverlay() {
        setOpaque(false); // Make the panel transparent

        // Set layout to center the content vertically and horizontally
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Arrange components vertically
        gbc.insets = new Insets(10, 0, 10, 0); // Add some space between the labels
        gbc.anchor = GridBagConstraints.CENTER; // Center the labels

        // Create a label for the "Game Paused" text
        JLabel pauseLabel = new JLabel("Game is paused");
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pauseLabel.setForeground(Color.WHITE);
        add(pauseLabel, gbc);

        // Create a label for the "Hit 'ESC' to continue" text
        JLabel pauseText = new JLabel("Hit 'ESC' to continue");
        pauseText.setFont(new Font("Arial", Font.PLAIN, 16));
        pauseText.setForeground(Color.WHITE);
        add(pauseText, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw a semi-transparent black rectangle
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
