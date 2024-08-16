package tetris;
import screens.TetrisOpeningScreen;

public class Main extends Thread {
  public static void main(String[] args) {
    TetrisOpeningScreen screenThread = new TetrisOpeningScreen();
	screenThread.start();
  }
}