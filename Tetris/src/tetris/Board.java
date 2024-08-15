package tetris;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import screens.GameScreen;


public class Board{
	public int[][] board;
	private Piece_Factory piece_gen;
	public final int BOARD_SIZE_X = 10;
	public final int BOARD_SIZE_Y = 20;
	public Boolean isMoveable = true;
	public Piece current_piece, next_piece;
	final int EMPTY_ROWS = 5;
	Random rng = new Random();
	ReadWriteLock thread_lock = new ReentrantReadWriteLock();
	public int score = 0;
	double multiplier = 1.0;
	boolean gameOver = false;
	public boolean updateNextPiece = true;
	
	
	public Board(){
		board = new int[BOARD_SIZE_Y][BOARD_SIZE_X];
		

		for(int i = 0; i < EMPTY_ROWS; i++) {
			for(int j = 0; j< BOARD_SIZE_X; j++) {
				board[i][j] = -2;
			}
		}
		piece_gen = new Piece_Factory(this);

		next_piece  = piece_gen.Gen_piece();
		spawn_piece();
	}
	

	private Piece spawn_piece() {
		current_piece = next_piece;
		next_piece = piece_gen.Gen_piece();
		int num_rotate = rng.nextInt(3);
		
		for(int i = 0; i < current_piece.size[0]; i++) {
			for(int j = 0; j < current_piece.size[1]; j++) {
				board[i][j+4] = current_piece.shape[i][j];
			}
		}
		
		//for(int i = 0; i < num_rotate; i++) {
		//	piece.Rotate();
		//}
		return current_piece;
	}
	
	public void board_copy(int[][] temp) {
		thread_lock.readLock().lock();
		for(int i = 0; i < BOARD_SIZE_Y; i++) {
			for(int j = 0; j< BOARD_SIZE_X; j++) {
				temp[i][j] = board[i][j];
			}
		}

		thread_lock.readLock().unlock();
	}
	
	public void print_board() {
		for(int i = 0; i < BOARD_SIZE_Y; i++) {
			for(int j = 0; j< BOARD_SIZE_X; j++) {
				if(board[i][j] == -1) {
					System.out.print(" * ");
				}
				else if (board[i][j] == -2) {
					System.out.print("   ");
				}
				else if(board[i][j] == 0) {
					System.out.print("[ ]");
				}
				else if(board[i][j] == 1) {
					System.out.print("[*]");
				}
				else if(board[i][j] == 2) {
					System.out.print("[X]");
				}
			}
			System.out.println();
		}
		System.out.println("_________________________________");

	}
	
	public void piece_has_fallen(){
		thread_lock.writeLock().lock();

		for (int i = 0; i < BOARD_SIZE_Y; i++) {
			for(int k = 0; k < BOARD_SIZE_X; k++) {
				if(board[i][k] == 1) { 
					board[i][k] = 2;
					isMoveable = true;
				}
				
			}
		}
		thread_lock.writeLock().unlock();

	}
	
	public boolean full_row(int i) {
		thread_lock.readLock().lock();

		for(int k = 0; k < BOARD_SIZE_X; k++) {
			if(board[i][k] != 2) {
				thread_lock.readLock().unlock();

				return false;
			}
		}
		thread_lock.readLock().unlock();

		return true;
	}
	
	private void clear_row(int i) {
		for(int j = 0; j < BOARD_SIZE_X; j++) {
			board[i][j] = 0;
		}
		score  += 10*(1+10*(multiplier-1));

	}
	
	
	public void move_down() {
		boolean piece_alive = false;
		boolean drop_flag = true;
		int[][] temp = new int[BOARD_SIZE_Y][BOARD_SIZE_X];
		
		for (int j = 0; j < BOARD_SIZE_X; j++) {
			if(board[BOARD_SIZE_Y-1][j] == 1) {
				drop_flag = false;
				this.piece_has_fallen();
				break;
			}
		}
		
		
		if(drop_flag) {
			thread_lock.writeLock().lock();

			this.board_copy(temp);
			for(int i = BOARD_SIZE_Y-2; i >= 0; i--) {
				for(int j = 0; j< BOARD_SIZE_X; j++) {
					//System.out.println(" "+i + " " + j);
					if(board[i][j] == 1 || board[i][j] == -1) {
						if(board[i+1][j] == 2) {
							board = temp;
							this.piece_has_fallen();
							if(i<=EMPTY_ROWS)
								gameOver = true;
							drop_flag = false;
							break;
						}
						piece_alive = true;
						//System.out.println("Found");
						if(board[i][j] == 1) {
							board[i][j] = 0;
							board[i+1][j] = 1;
						}
						else if(board[i][j] == -1) {
							board[i][j] = -2;
							if(board[i+1][j] == 0) {
								board[i+1][j] = 1;
							}
							else {
								board[i+1][j] = -1;
								
							}
						}
						
					}
					
				}
				if(!drop_flag) break;
			}
			
			thread_lock.writeLock().unlock();
		}
		
		for(int i = BOARD_SIZE_Y-1; i >= 0; i--) {
			if(full_row(i)) {
				System.out.println("**********Clear row************");
				piece_alive = true;
				drop_flag = true;
				isMoveable = false;
				clear_row(i);
				for(int k = 0; k <= i; k++) {
					for(int j = 0; j < BOARD_SIZE_X; j++) {
						if(board[k][j] == 2) board[k][j] = 1;
					}
							
				}
				break;
			}
		}
		
		if(!piece_alive) {
			current_piece = spawn_piece();
			updateNextPiece = true;
		}
	}
}