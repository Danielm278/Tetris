package tetris;

import java.util.ArrayList;
import java.util.Vector;
import java.awt.Color;


public abstract class Piece {
	int[][] shape;
	int[] size = new int[2];
	Board board;
	int orientation;
	protected int[][][] SHAPES;
	
	public Color piece_color;

	protected Vector<Integer> pos;
	protected Vector<Integer> pos2;
	ArrayList<Vector<Integer>> location = new ArrayList<Vector<Integer>>();
	
	
	protected Color pieceColor;
	
	private final int X  = 1;
	private final int Y = 0;
	
	public void Piece() {
		orientation = 0;
	}

	public void Piece(Board board) {
		this.board = board;
		orientation = 0;
	}
	
	public boolean check_problem_rotation() {
		for(int i = pos.get(Y); i < pos.get(Y) + size[Y]; i++) {
			for(int j = pos.get(X); j < pos.get(X) + size[X]; j++) {
				if(board.board[i][j] == 2) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean Check_Collision() {
		int size_Max = Math.max(size[0], size[1]);
		for(int i = 0; i < size_Max; i++) {
			for(int j = 0; j < size_Max; j++) {
				if(board.board[pos.get(0) + i][pos.get(1) + j] == 2) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void Implement_Rotation() {
		int max_size =  Math.max(size[0], size[1]);
		for(int i = 0; i < max_size; i++) {
			for(int j = 0; j < max_size; j++) {
				if(board.board[i+pos.get(0)][j+pos.get(1)] >= 0) {
					board.board[i+pos.get(0)][j+pos.get(1)] = SHAPES[orientation][i][j]+2;
				}
				else {
					board.board[i+pos.get(0)][j+pos.get(1)] = SHAPES[orientation][i][j];
				}
			}
		}
	}
	
	public void Get_Piece_Bound() {

		pos = new Vector<Integer>();
		pos2 = new Vector<Integer>();
		pos.add(board.BOARD_SIZE_Y);
		pos.add(board.BOARD_SIZE_X-1);
		pos2.add(0);
		pos2.add(0);
		location = new ArrayList<Vector<Integer>>(); 
		
		for(int i = 0; i < board.BOARD_SIZE_Y; i++) {
			for(int j = 0; j < board.BOARD_SIZE_X; j++) {
				if(board.board[i][j] == 1 || board.board[i][j] ==-1) {
					Vector<Integer> temp = new Vector<Integer>();
					temp.add(i);
					temp.add(j);
					location.add(temp);
					if(i < pos.get(0)) pos.set(0, i);
					if(j < pos.get(1)) pos.set(1, j);
					if(i > pos2.get(0)) pos2.set(0, i);
					if(j > pos2.get(1)) pos2.set(1, j);
				}
			}
		}

		size[Y] = pos2.get(0)- pos.get(0)+1;
		size[X] = pos2.get(1)- pos.get(1)+1;

	}
	
	public void Set_Shape(int[][] new_shape) {
		shape = new_shape;
		size[Y] = new_shape.length;
		size[X] = new_shape[0].length;
	}
	
	public void Rotate() {
		Get_Piece_Bound();
	}
	
	public void Set_Board(Board board)
	{
		this.board = board;
	}
	
	public Color getPieceColor()
	{
		return pieceColor;
	}
}
