package screens;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameScreen extends JFrame {
    private static final int GRID_ROWS = 20;
    private static final int GRID_COLS = 10;
    private static final int HORIZONTAL_PADDING = 20;
    private static final int VERTICAL_PADDING = 50;
    private int score = 0;

    private JPanel gamePanel;
    private JLabel scoreLabel;
    private JLabel levelUpLabel;
    private BufferedImage backgroundImage;
    private JPanel[][] gridSquares;
    private JPanel[][] nextPieceSquares;
    private int pageWidth = GRID_COLS * 20 + 140 + 2 * HORIZONTAL_PADDING, pageHeight = GRID_ROWS * 20 + 2 * VERTICAL_PADDING;
    Color gridColor = Color.WHITE;
    Color emptyColor = Color.LIGHT_GRAY;
   
    Color lockedColor = Color.GRAY;
    
    Color current_piece_color=Color.BLUE;
    Color next_piece_color=Color.BLUE;
    tetris.Board gameBoard;

    
    private JLayeredPane lpane = new JLayeredPane();
    PauseOverlay pauseOverlayPanel;
    
    public GameScreen(tetris.Board game_board) {
    	this.gameBoard = game_board;
    	
    	//MusicPlayer musicPlayer = new MusicPlayer();
    	//musicPlayer.playMusic("C:\\Users\\roniw\\eclipse-workspace\\Teris_Project\\Tetris\\src\\game_music.wav"); // Replace with your actual music file path

        setTitle("Tetris");
        setSize(pageWidth, pageHeight);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String backgroundImagePath = "Tetris\\src\\background_image.jpg";
        try {
            backgroundImage = ImageIO.read(new File(backgroundImagePath));
            System.out.println("Background image loaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        gamePanel = new GameScreenBackgroundPanel();
        gamePanel.setLayout(null);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(VERTICAL_PADDING, HORIZONTAL_PADDING, VERTICAL_PADDING, HORIZONTAL_PADDING));
        
        
        // Create a layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(pageWidth, pageHeight));
        setContentPane(layeredPane);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);

        
        JPanel gridPanel = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS));
        gridPanel.setBounds(150, VERTICAL_PADDING, GRID_COLS * 20, GRID_ROWS * 20);
        gridPanel.setOpaque(false);

        gridSquares = new JPanel[GRID_ROWS][GRID_COLS];
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                JPanel square = new JPanel();
                square.setOpaque(true);
                square.setBackground(Color.LIGHT_GRAY);
                square.setBorder(BorderFactory.createLineBorder(gridColor));
                gridSquares[row][col] = square;
                gridPanel.add(square);
            }
        }

        gamePanel.add(gridPanel);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        scorePanel.setBounds(HORIZONTAL_PADDING + 120, 15, 150, VERTICAL_PADDING);
        scorePanel.setOpaque(false);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        scorePanel.add(scoreLabel);

        // Create level up label
        levelUpLabel = new JLabel("LEVEL UP!", SwingConstants.LEFT);
        levelUpLabel.setFont(new Font("Arial", Font.BOLD, 24));
        levelUpLabel.setForeground(Color.RED);
        levelUpLabel.setBounds(10, 30 , 150, VERTICAL_PADDING); // Positioned next to the score label
        levelUpLabel.setVisible(false); // Initially hidden
        gamePanel.add(levelUpLabel);

        gamePanel.add(scorePanel);

        JPanel nextPiecePanel = new JPanel(new GridLayout(4, 4));
        nextPiecePanel.setBounds(HORIZONTAL_PADDING, (int) (getHeight() * 0.25), 80, 80);
        nextPiecePanel.setBackground(new Color(0, 0, 0, 0));

        nextPieceSquares = new JPanel[4][4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                JPanel square = new JPanel();
                square.setOpaque(true);
                square.setBackground(Color.LIGHT_GRAY);
                square.setBorder(BorderFactory.createLineBorder(gridColor));
                nextPieceSquares[row][col] = square;
                nextPiecePanel.add(square);
            }
        }

        JLabel nextPieceLabel = new JLabel("Next Piece", SwingConstants.CENTER);
        nextPieceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nextPieceLabel.setForeground(Color.WHITE);
        nextPieceLabel.setBounds(HORIZONTAL_PADDING, (int) (getHeight() * 0.25) - 20, 80, 20);

        gamePanel.add(nextPieceLabel);
        gamePanel.add(nextPiecePanel);

        //add(gamePanel, BorderLayout.CENTER);
        add(lpane, BorderLayout.CENTER);
        lpane.setBounds(0, 0, pageWidth, pageHeight);
        gamePanel.setBackground(Color.BLUE);
        gamePanel.setBounds(0, 0, pageWidth, pageHeight);
        gamePanel.setOpaque(true);
        
        // Add the pause overlay on top of the game panel
        pauseOverlayPanel = new PauseOverlay();
        pauseOverlayPanel.setBounds(100, 100, 200, 200);
        pauseOverlayPanel.setVisible(false);
        
        
        pauseOverlayPanel.setOpaque(true);
        lpane.add(gamePanel, 0, 0);
        lpane.add(pauseOverlayPanel, 1, 0);
        pack();
        setVisible(true);
        
    }
    
    public void togglePauseOverlay(boolean show) {
    	pauseOverlayPanel.setVisible(show);
    }

    public void setGridSquareColor(int row, int col, int value) {
    	current_piece_color = gameBoard.current_piece.piece_color;
        if (row >= 0 && row < GRID_ROWS && col >= 0 && col < GRID_COLS) {
            JPanel square = gridSquares[row][col];
            switch (value) {
                case 0:
                    square.setBackground(emptyColor);
                    break;
                case -2:
                    square.setBackground(gridColor);
                    break;
                case 1:
                    square.setBackground(current_piece_color);
                    break;
                case -1:
                    square.setBackground(current_piece_color);
                    break;
                case 2:
                    square.setBackground(lockedColor);
                    break;
                default:
                    square.setBackground(emptyColor);
                    break;
            }
            square.repaint();
        }
    }

    public void setNextPieceSquareColor(int row, int col, int value) {
    	next_piece_color = gameBoard.next_piece.piece_color;
    	
        if (row >= 0 && row < 4 && col >= 0 && col < 4) {
            JPanel square = nextPieceSquares[row][col];
            switch (value) {
                case 0:
                    square.setBackground(emptyColor);
                    break;
                case -2:
                    square.setBackground(gridColor);
                    break;
                case 1:
                    square.setBackground(next_piece_color);
                    break;
                case -1:
                    square.setBackground(next_piece_color);
                    break;
                case 2:
                    square.setBackground(lockedColor);
                    break;
                default:
                    square.setBackground(emptyColor);
                    break;
            }
            square.repaint();
        }
    }

    public void updateScore(int score) {
    	
        scoreLabel.setText("Score: " + score);
    }

    public void flashLevelUp() {
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    levelUpLabel.setVisible(true);
                    Thread.sleep(300);
                    levelUpLabel.setVisible(false);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class GameScreenBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                System.out.println("Background image is null.");
            }
        }
    }
    public void set_Current_Piece_Color(Color c)
    {
    	current_piece_color=c;
    }
    
    public Color get_Current_Piece_Color()
    {
    	return this.current_piece_color;
    }
    
}