package screens;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import javax.imageio.ImageIO;

public class ScoreboardDisplay {
    public static void showScoreboard() {
        JFrame frame = new JFrame("Scoreboard");
        frame.addWindowListener(new WindowAdapter() {
      	  public void windowClosing(WindowEvent e) {
      		  TetrisOpeningScreen.openScreen();
      	  }
      	});
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the frame

        // Create a panel with a background image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Tetris\\src\\podium.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        // Transparent panel for holding components
        JPanel transparentPanel = new JPanel();
        transparentPanel.setOpaque(false);
        transparentPanel.setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Score"};
        String[][] data = ScoreBoard.getScores();

        // Sort the data by scores in descending order
        Arrays.sort(data, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Integer.compare(Integer.parseInt(o2[1]), Integer.parseInt(o1[1]));
            }
        });

        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(false);
                }
                c.setForeground(Color.BLACK); // Set text color to black
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER); // Center text
                return c;
            }
        };

        table.setFont(new Font("Arial", Font.BOLD, 14));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setOpaque(false);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);

        // Make table header with white background
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(Color.WHITE); // Set white background
                c.setForeground(Color.BLACK); // Set text color to black
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        tableHeader.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        transparentPanel.add(scrollPane, BorderLayout.CENTER);

        // Create and add a home page button
        JButton homeButton = new JButton("Home Page");
        homeButton.setBackground(Color.BLUE);
        homeButton.setForeground(Color.WHITE);
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				// Show the home page
                TetrisOpeningScreen.openScreen();
                frame.dispose(); // Close the scoreboard screen
            }
        });
        transparentPanel.add(homeButton, BorderLayout.SOUTH);

        // Add the transparent panel to the background panel
        backgroundPanel.add(transparentPanel, BorderLayout.CENTER);

        // Add the background panel to the frame
        frame.getContentPane().add(backgroundPanel);

        // Display the window
        frame.setVisible(true);
    }
}

// Custom JPanel class for drawing background image
class ScoreboardBackgroundPanel extends JPanel {
    private Image backgroundImage;

    public ScoreboardBackgroundPanel(String imagePath) {
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
