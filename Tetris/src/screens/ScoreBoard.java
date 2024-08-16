package screens;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScoreBoard {
    private static final String FILE_PATH = "ScoreBoardTetris.csv";

    public static void main(String[] args) {
        ensureFileExists();

        // Example usage
        String username = "player1";
        int score = 100;
        getUserConfirmationAndSave(username, score);
    }

    public static void ensureFileExists() {
        if (!Files.exists(Paths.get(FILE_PATH))) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                // Write the headers to the file
                String headers = "Username,Score";
                writer.println(headers);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getUserConfirmationAndSave(String username, int score) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Username: " + username);
        System.out.println("Score: " + score);
        System.out.print("Do you want to save this score? (yes/no): ");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("yes")) {
            saveScore(username, score);
            System.out.println("Score saved.");
        } else {
            System.out.println("Score not saved.");
        }

        scanner.close();
    }

    public static void saveScore(String username, int score) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(username + "," + score);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[][] getScores() {
        List<String[]> scores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                scores.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores.toArray(new String[0][0]);
    }
}
