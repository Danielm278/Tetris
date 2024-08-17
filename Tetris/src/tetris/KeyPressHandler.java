package tetris;

import javax.swing.*;

import screens.GameScreen;
import screens.MusicPlayer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyPressHandler {
	Board board;
	GameScreen gameScrean;
	int BOARD_X, BOARD_Y;
	boolean isPaused;
	MusicPlayer game_music_player;
	long game_music_player_loc = 0;

	public KeyPressHandler(Board board, GameScreen gameScrean, MusicPlayer game_music_player) {
		this.board = board;
		this.gameScrean = gameScrean;
		BOARD_X = board.BOARD_SIZE_X;
		BOARD_Y = board.BOARD_SIZE_Y;
		this.game_music_player = game_music_player;
		isPaused = false;
	}

	Action togglePause = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			isPaused = !isPaused;
			board.isPaused = isPaused;
			gameScrean.togglePauseOverlay(isPaused);
			if(isPaused) {
				game_music_player_loc = game_music_player.clip.getMicrosecondPosition();
				game_music_player.clip.stop();
			}
			else {
				game_music_player.playMusic("Tetris\\src\\game_music.wav", true);
				game_music_player.clip.setMicrosecondPosition(game_music_player_loc);
				game_music_player.clip.start();
			}
			System.out.println("ESC registered");
		}
	};

	Action left = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (board.isMoveable) {
				boolean collision = false;
				int[][] temp = new int[BOARD_Y][BOARD_X];
				board.board_copy(temp);
        		board.current_piece.Get_Piece_Bound();
        		if(board.current_piece.pos.get(1) == 0) return;
        		
            	for(int j = 1; j < BOARD_X; j++) {
            		
            		if(collision) {
            			break;
            		}
    				for(int i = BOARD_Y-1; i >=0; i--) {
/*    					if(board.board[i][0] == 1 || board.board[i][0] == -1) {
							System.out.println("*********WALL***********");
    						board.board = temp;
    						collision = true;
    						break;
    					}*/
    					//System.out.println(" "+i + " " + j);
    					if(board.board[i][j] == 1|| board.board[i][j] == -1) {
    							if(board.board[i][j-1] != 2) {
	    						//System.out.println("Found");
    	    					board.board[i][j-1] = board.board[i][j];
	    						board.board[i][j] += -1;
    							}
    							else{
	        						collision = true;
	        						board.board = temp;
	        						break;
        					}
    					}
    					
    				}
    			}
				updateScrean();
			}
		}
	};

	Action down = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (board.isMoveable) {
				board.move_down();
				updateScrean();
			}
		}
	};

	Action right = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (board.isMoveable) {
				int[][] temp = new int[BOARD_Y][BOARD_X];
				board.board_copy(temp);
				boolean collision = false;
				board.current_piece.Get_Piece_Bound();
        		if(board.current_piece.pos.get(1) == BOARD_X-board.current_piece.size[1]) return;
        		
				for(int j = BOARD_X-2; j >= 0; j--) {

            		if(collision) {
            			break;
            		}
					for(int i = 0; i < BOARD_Y; i++) {
						/*if(board.board[i][BOARD_X-1] == 1 || board.board[i][BOARD_X-1] == -1) {
							System.out.println("*********WALL***********");
    						board.board = temp;
    						collision = true;
    						break;
    					}*/
    					//System.out.println(" "+i + " " + j);
    					if(board.board[i][j] == 1 || board.board[i][j] == -1) {
    						if(board.board[i][j+1] != 2) {
    						//System.out.println("Found");
        						board.board[i][j+1] = board.board[i][j];
        						board.board[i][j] += -1;
    						}
    						else{
        						System.out.println("\nCollision*******");
        						collision = true;
        						board.board = temp;
        						break;
        					}
    					}

    					
    				}
    			}
				updateScrean();
			}
		}
	};

	Action rotate = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			board.current_piece.Rotate();
			updateScrean();

		}
	};

	void updateScrean() {
		for(int i = 0; i < board.BOARD_SIZE_Y; i++) {
			for(int j = 0; j< board.BOARD_SIZE_X; j++) {
				gameScrean.setGridSquareColor(i,j,board.board[i][j]);
			}
		}
	}
}