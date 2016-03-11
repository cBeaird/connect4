
import static java.util.Arrays.copyOf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Connect4Board {
	private static final int RUN_LENGTH = 4;
	private static final char PLAYER1 = 'X';
	private static final char PLAYER2 = 'O';
	static final char OPEN = ' ';

	private static final int BOARD_WIDTH = 5;
	private static final int BOARD_HEIGHT = 4;

//	private char[] rowMajorState;


	char[][] state;

	private int boardWidth = BOARD_WIDTH;
	private int boardHeight = BOARD_HEIGHT;

	private char currentPlayer = PLAYER1;
	
	private Move lastMove;

	private static Connect4Board parse(String boardString) {
		String[] lines = boardString.split("\\r\\n?|\\n");
		int boardHeight = lines.length;

		int boardWidth = lines[0].substring(1, lines[0].length() - 1).split("\\|").length;

		Connect4Board board = new Connect4Board(boardHeight, boardWidth);

		int player1Moves = 0;
		int player2Moves = 0;

		for (int i = 0; i < boardHeight; i++) {
			String[] line = lines[i].substring(1, lines[0].length() - 1).split("\\|");
			for (int j = 0; j < boardWidth; j++) {
				if (line[j].charAt(0) == PLAYER1) {
					player1Moves++;
				}
				if (line[j].charAt(0) == PLAYER2) {
					player2Moves++;
				}
//				board.set(i, j, line[j].charAt(0));
				board.state[i][j] = line[j].charAt(0);
			}
		}
		if (player1Moves > player2Moves) {
			board.currentPlayer = PLAYER2;
		}
		return board;
	}

	public Connect4Board(String boardString) {
		String[] lines = boardString.split("\\r\\n?|\\n");
		boardHeight = lines.length;
		boardWidth = lines[0].substring(1, lines[0].length() - 1).split("\\|").length;
		
		state =  new char[boardHeight][boardWidth];
		
		int player1Moves = 0;
		int player2Moves = 0;

		for (int i = 0; i < boardHeight; i++) {
			String[] line = lines[i].substring(1, lines[0].length() - 1).split("\\|");
			for (int j = 0; j < boardWidth; j++) {
				if (line[j].charAt(0) == PLAYER1) {
					player1Moves++;
				}
				if (line[j].charAt(0) == PLAYER2) {
					player2Moves++;
				}
				state[i][j] = line[j].charAt(0);
			}
		}
		if (player1Moves > player2Moves) {
			currentPlayer = PLAYER2;
		} else {
			currentPlayer = PLAYER1;
		}
	}
	
	public Connect4Board() {
		this(BOARD_HEIGHT, BOARD_WIDTH);
	}

	public Connect4Board(int boardHeight, int boardWidth) {
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		this.state = new char[boardHeight][boardWidth];
//		rowMajorState = new char[boardHeight * boardWidth];
//		fill(rowMajorState, ' ');
	}

	public Connect4Board(Connect4Board board) {
		this(board.state.length , board.state[0].length);
		for(int i =0; i < board.state.length; i++){
			state[i] = copyOf(board.state[i], board.state[i].length);
		}
		this.currentPlayer = board.currentPlayer;
//		this.rowMajorState = copyOf(board.rowMajorState, board.rowMajorState.length);
	}

	public void move(Move move) throws IllegalMoveException {
		move(move.row, move.col, getCurrentPlayer());
	}
	
	public void move(int row, int col) throws IllegalMoveException {
		move(row, col, getCurrentPlayer());
	}

	public void move(int row, int col, char player) throws IllegalMoveException {
		if (row < 0 || row > boardHeight) {
			throw new IllegalMoveException("Row " + row + " is out of bounds");
		}
		if (col < 0 || col > boardWidth) {
			throw new IllegalMoveException("Column " + col + " is out of bounds");
		}
		if (state[row][col] != OPEN) {
			throw new IllegalMoveException("Move " + row + "" + col + " is already taken");
		}
		
		if (!getRemainingMoves().contains(new Move(row, col))) {
			throw new IllegalMoveException("Move " + row + "" + col + " is not a legal move");
		}
		state[row][col] = player;
//		set(row, col, player);
		currentPlayer = currentPlayer == PLAYER2 ? PLAYER1 : PLAYER2;
		
		setLastMove(new Move(row, col));
	}

	private int rowMajIdx(int row, int col) {
		return (boardWidth * row) + col;
	}
//
//	private char get(int row, int col) {
//		return rowMajorState[rowMajIdx(row, col)];
//	}
//
//	private void set(int row, int col, char val) {
//		rowMajorState[rowMajIdx(row, col)] = val;
//	}

	public char getCurrentPlayer() {
		return currentPlayer;
	}

	private char getRowWinner() {
		char prev = OPEN;
		int len = 0;

		// Search the rows
		for (int row = 0; row < boardHeight; row++) {
			len = 0;
			prev = OPEN;
			for (int col = 0; col < boardWidth; col++) {
//				System.out.println("[" + row + "][" + col + "] is '" +state[row][col] + "'");

				if (state[row][col] == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (state[row][col] != OPEN) {
						len++;
//						System.out.println("Streak of " + len);
					}
				} else {
					if (prev == state[row][col] && prev != OPEN) {
						len++;
//						System.out.println("Streak of " + len);
					} else {
						len = 1;
//						System.out.println("Streak of " + len);
					}
				}
				if (len == RUN_LENGTH) {
					return prev;
				}

				prev = state[row][col];
			}
		}

		return OPEN;
	}

	char getColumnWinner(char[][] state) {

		char prev = OPEN;
		int len = 0;

		for (int col = 0; col < boardWidth; col++) {
			len = 0;
			prev = OPEN;
			for (int row = 0; row < boardHeight; row++) {

			System.out.println("[" + row + "][" + col + "] is '" + state[row][col] + "'");

				if (state[row][col] == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (state[row][col] != OPEN) {
						len++;
						System.out.println("Streak of " + len);
					}
				} else {
					if (prev == state[row][col] && prev != OPEN) {
						len++;
						System.out.println("Streak of " + len);
					} else {
						len = 1;
						System.out.println("Streak of " + len);
					}
				}
				if (len == RUN_LENGTH) {
					return prev;
				}

				prev = state[row][col];

			}
		}

		return OPEN;
	}
	
	
	protected char getDiagonalWinner() {
		char prev = OPEN;
		int len = 0;
		
		char[][] d = new char[state.length][state[0].length + state.length - 1];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				d[i][j] = ' ';
			}
		}
		
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				d[i][j + i] = state[i][j];
			}
		}
		
		for (int col = 0; col < d[0].length; col++) {
			len = 0;
			prev = OPEN;
			for (int row = 0; row < d.length; row++) {

//				System.out.println("[" + row + "][" + col + "] is '" + d[row][col] + "'");

				if (d[row][col] == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (d[row][col] != OPEN) {
						len++;
//						System.out.println("Streak of " + len);
					}
				} else {
					if (prev == d[row][col] && prev != OPEN) {
						len++;
//						System.out.println("Streak of " + len);
					} else {
						len = 1;
//						System.out.println("Streak of " + len);
					}
				}
				if (len == RUN_LENGTH) {
					return prev;
				}

				prev = d[row][col];

			}
		}

		d = new char[state.length][state[0].length + state.length - 1];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				d[i][j] = ' ';
			}
		}
		
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				d[i][j + state.length - i - 1] = state[i][j];
			}
		}

		
		for (int col = 0; col < d[0].length; col++) {
			len = 0;
			prev = OPEN;
			for (int row = 0; row < d.length; row++) {

//				System.out.println("[" + row + "][" + col + "] is '" + d[row][col] + "'");

				if (d[row][col] == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (d[row][col] != OPEN) {
						len++;
//						System.out.println("Streak of " + len);
					}
				} else {
					if (prev == d[row][col] && prev != OPEN) {
						len++;
//						System.out.println("Streak of " + len);
					} else {
						len = 1;
//						System.out.println("Streak of " + len);
					}
				}
				if (len == RUN_LENGTH) {
					return prev;
				}

				prev = d[row][col];

			}
		}
		
		return OPEN;
	}

	public char getWinner() {
		char winner = OPEN;
		winner = getRowWinner();
		if (winner != OPEN) {
			return winner;
		}
		winner = getColumnWinner(state);
		if (winner != OPEN) {
			return winner;
		}
		return getDiagonalWinner();
	}
	
	public boolean hasWinner(){
		return getWinner()!=OPEN;
	}
	
	public boolean isFilled(){
		return getRemainingMoves().isEmpty();
	}

	// legal moves are encoded as row major indices so that List.contains can be use to check if a move is legal (see move()).
//	public List<Integer> getRemainingMoves() {
//		List<Integer> legalMoves = new ArrayList<>();
//		for(int i = 1; i < boardHeight; i++){
//			for(int j = 0; j < boardWidth; j++){
//				if(state[i][j] != OPEN){
//					if(state[i-1][j] == OPEN){
//						legalMoves.add(rowMajIdx(i - 1, j));
//					}
//				}
//				if(state[i][j] == OPEN && i == boardHeight - 1){
//					legalMoves.add(rowMajIdx(i, j));
//				}
//			}
//		}
//		return legalMoves;
//	}

	public List<Move> getRemainingMoves() {
		List<Move> legalMoves = new ArrayList<>();
		for(int i = 1; i < boardHeight; i++){
			for(int j = 0; j < boardWidth; j++){
				if(state[i][j] != OPEN){
					if(state[i-1][j] == OPEN){
						legalMoves.add(new Move(i - 1, j));
					}
				}
				if(state[i][j] == OPEN && i == boardHeight - 1){
					legalMoves.add(new Move(i, j));
				}
			}
		}
		return legalMoves;
	}
	
	public String boardIndices() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < boardHeight; i++) {
			sb.append('|');
			for (int j = 0; j < boardWidth; j++) {
				sb.append(i + "" + j);
				sb.append('|');
			}
			if (i != boardWidth) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}
	
	

	public void print(char[][] m) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < m.length; i++) {
			sb.append('|');
			for (int j = 0; j < m[0].length; j++) {
				sb.append(Character.toChars(m[i][j]));
				sb.append('|');
			}
			if (i != m[0].length) {
				sb.append('\n');
			}
		}
		System.out.println(sb.toString());
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < boardHeight; i++) {
			sb.append('|');
			for (int j = 0; j < boardWidth; j++) {
				sb.append(Character.toChars(state[i][j]));
				sb.append('|');
			}
			if (i != boardWidth) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}
	

	class Move{
		int row;
		int col;
		Move(int row, int col){
			this.row = row;
			this.col = col;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + col;
			result = prime * result + row;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Move other = (Move) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (col != other.col)
				return false;
			if (row != other.row)
				return false;
			return true;
		}
		private Connect4Board getOuterType() {
			return Connect4Board.this;
		}
		@Override
		public String toString() {
			return "Move [row=" + row + ", col=" + col + "]";
		}
		
		
		
	}
	
	
	public static void main(String[] args) throws IllegalMoveException {
		
		Scanner scn =  new Scanner(System.in);

		 Connect4Board board= Connect4Board.parse("" +
		 "| | | | |O|\r\n" +
		 "| | | | |X|\r\n" +
		 "| | | | |O|\r\n" +
		 "|X|O|X|O|X|");

		 
		 
		 
		 
		 GameTree gt =  new GameTree(board);
		 
		 //gt.print(" ");
		 
		 System.out.println(board);
		 
		 for(GameTree child: gt.children){
			 System.out.println("up1 = " + child.utility(PLAYER1));
			 System.out.println("up2 = " + child.utility(PLAYER2));
		 }
		 
//		 while(!board.hasWinner() && !board.isFilled()){
//			 List<Move> moves = board.getRemainingMoves();
//			 System.out.println(board);
//			 System.out.println(moves);
//			 Collections.shuffle(moves);
//			 board.move(moves.get(0));
//		 }
//		 
//		 System.out.println(board);
//		 System.out.println(board.getRemainingMoves());
//		 
//		 
//		 if(board.hasWinner()){
//			 System.out.println(board.getWinner() + " wins");
//		 } else {
//			 System.out.println("draw");
//		 }
		 
		 
	}

	public Move getLastMove() {
		return lastMove;
	}

	public void setLastMove(Move lastMove) {
		this.lastMove = lastMove;
	}

}
