package tetris;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.io.File;
import java.io.IOException;

import screens.EndScreen;
import screens.GameScreen;
import screens.IThread;
import screens.MusicPlayer;
import screens.TetrisOpeningScreen;

public class GameManager{	
    Helper gameTask;
	boolean isRunning = false;
	private Board board;
	private GameScreen gameScreen;    
	private ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> scheduledFuture;
	int previousScore = 0; // Add a variable to track the previous score
    MusicPlayer game_music_player = MusicPlayer.getInstance();
    MusicPlayer totach_player = MusicPlayer.getInstance();
	
	public GameManager() {
		
		this.board = new Board();
    	gameScreen = new GameScreen(board);
		
    	gameScreen.addWindowListener(new WindowAdapter() {
      	  public void windowClosing(WindowEvent e) {
      		  stopGame(false);
      		  game_music_player.stopMusic();
    		  TetrisOpeningScreen.openScreen();
    	  }
    	});

        timer = Executors.newSingleThreadScheduledExecutor();
        gameTask = new Helper(board, timer);
		KeyPressHandler handler = new KeyPressHandler(board, gameScreen, game_music_player); 
    	
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        gameScreen.add(panel);
        // Bind actions to key strokes
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "spaceAction");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escapeAction");
        

        actionMap.put("moveLeft", handler.left);
        actionMap.put("moveRight", handler.right);
        actionMap.put("moveDown", handler.down);
        actionMap.put("spaceAction", handler.rotate);
        actionMap.put("escapeAction", handler.togglePause);
        
        
        gameScreen.set_Current_Piece_Color(board.current_piece.pieceColor);
    	
    	
//        KeypressSimulation test = new KeypressSimulation(board);
//        test.run();
        isRunning = true;
        System.out.println((int) (250 / board.multiplier));
        scheduledFuture = timer.scheduleAtFixedRate(gameTask, 250, (int) (250 / board.multiplier), TimeUnit.MILLISECONDS); // Use scheduleAtFixedRate for fixed interval
	    game_music_player.playMusic("Tetris\\src\\game_music.wav", true);
//        while(isRunning) {
//        	try {
//        		gameTask.run();
//				Thread.sleep((long) (250 / board.multiplier));
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//        }
	}
	public boolean stopGame(boolean openEndScreen) {
		if (isRunning) {
	        scheduledFuture.cancel(true); // Cancel without interrupting current task
	        timer.shutdown();
	        if(openEndScreen)
	        	EndScreen.showEndScreen(board.score);
	        gameScreen.dispose();
	        isRunning = false;
	        return true;
	    }
	    return false;
	}
	
	public class Helper implements Runnable, IThread {
		ScheduledExecutorService _timer;
		int counter;
		Board board;
		public Helper(Board board, ScheduledExecutorService timer) {
			this.board = board;
			_timer = timer;
			counter = 0;
			board.multiplier = 1.0;
		}
	    public void run()
	    {	
	    	if(board.isPaused)
	    	{
	    		return;
	    	}
	    	if(board.gameOver) {
	    		System.out.println("quitting game");
	    		stopGame(true);
	    	}
	    	if(counter > 10) {
	    		counter = 0;
	    		board.multiplier += 0.005;
	    		gameScreen.flashLevelUp();
	    		System.out.println("********tick speed = "+(int) (50 + 300* Math.exp(-0.8*board.multiplier))+"**********");
	    		scheduledFuture.cancel(true);
	    		scheduledFuture = timer.scheduleAtFixedRate(gameTask, 250, (int) (50 + 300* Math.exp(-0.8*board.multiplier)), TimeUnit.MILLISECONDS);	    	}

	    	
			board.move_down();
//			_board.print_board();
			
			updateBoard();
			if(board.updateNextPiece)
			{
				gameScreen.set_Current_Piece_Color(board.current_piece.pieceColor);
				updateNextPiece();
				board.updateNextPiece = false;
				counter += 1;
			}
	    }

	    private void updateNextPiece() {
//	    	try{
	    		int x = board.next_piece.SHAPES[0][0].length, y = board.next_piece.SHAPES[0].length;
	    	for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++)
				{
					if(i < y && j < x)
						gameScreen.setNextPieceSquareColor(i,j,board.next_piece.SHAPES[0][i][j]);
					else
						gameScreen.setNextPieceSquareColor(i,j,-2);
				}
			}
//	    	}catch(Exception e)
//    		{System.out.println(e.getStackTrace());}
		}
		public void updateBoard() {
			for(int i = 0; i < board.BOARD_SIZE_Y; i++) {
				for(int j = 0; j< board.BOARD_SIZE_X; j++) {
					gameScreen.setGridSquareColor(i,j,board.board[i][j]);
				}
			}
			gameScreen.updateScore(board.score);
			if(previousScore<board.score) {
				previousScore = board.score;
				new Thread(() -> 
				{
					long loc = game_music_player.clip.getMicrosecondPosition();
					System.out.println("stopping game music");
					game_music_player.stopMusic();
					System.out.println("starting totach music");
					totach_player.playMusic("Tetris\\src\\totach.wav", false);
					while(totach_player.clip.getMicrosecondLength() != totach_player.clip.getMicrosecondPosition()) {}
					System.out.println("starting game music");
					game_music_player.playMusic("Tetris\\src\\game_music.wav", true);
					game_music_player.clip.setMicrosecondPosition(loc);
					game_music_player.clip.start();
		        }
				).start();
			}
		}
	}	
}




