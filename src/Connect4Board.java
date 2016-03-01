
import static java.util.Arrays.fill;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.copyOf;

public class Connect4Board {
	private static final char PLAYER1 = 'X';
	private static final char PLAYER2 = 'O';
	private static final char OPEN = ' ';

	private static final int BOARD_WIDTH = 5;
	private static final int BOARD_HEIGHT = 4;

	private char[] rowMajorState;

	private int boardWidth = BOARD_WIDTH;
	private int boardHeight = BOARD_HEIGHT;

	private char currentPlayer = PLAYER1;

	public static Connect4Board parse(String boardString) {
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
				board.set(i, j, line[j].charAt(0));
			}
		}
		if (player1Moves > player2Moves) {
			board.currentPlayer = PLAYER2;
		}
		return board;
	}

	public Connect4Board() {
		this(BOARD_HEIGHT, BOARD_WIDTH);
	}

	public Connect4Board(int boardHeight, int boardWidth) {
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		rowMajorState = new char[boardHeight * boardWidth];
		fill(rowMajorState, ' ');
	}

	public Connect4Board(Connect4Board board) {
		this.rowMajorState = copyOf(board.rowMajorState, board.rowMajorState.length);
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
		if (get(row, col) != OPEN) {
			throw new IllegalMoveException("Move " + row + "" + col + " is already taken");
		}
		if (!getLegalMoves().contains(rowMajIdx(row, col))) {
			throw new IllegalMoveException("Move " + row + "" + col + " is not a legal move");
		}
		set(row, col, player);
		currentPlayer = currentPlayer == PLAYER2 ? PLAYER1 : PLAYER2;
	}

	private int rowMajIdx(int row, int col) {
		return (boardWidth * row) + col;
	}

	private char get(int row, int col) {
		return rowMajorState[rowMajIdx(row, col)];
	}

	private void set(int row, int col, char val) {
		rowMajorState[rowMajIdx(row, col)] = val;
	}

	public char getCurrentPlayer() {
		return currentPlayer;
	}

	private char getRowWinner() {
		char prev = OPEN;
		int len = 0;

		// Search the rows
		for (int row = 0; row < boardHeight; row++) {
			System.out.println("New row reset");
			len = 0;
			prev = OPEN;
			for (int col = 0; col < boardWidth; col++) {
				System.out.println("[" + row + "][" + col + "] is '" + get(row, col) + "'");

				if (get(row, col) == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (get(row, col) != OPEN) {
						len++;
						System.out.println("Streak of " + len);
					}
				} else {
					if (prev == get(row, col) && prev != OPEN) {
						len++;
						System.out.println("Streak of " + len);
					} else {
						len = 1;
						System.out.println("Streak of " + len);
					}
				}
				if (len == 4) {
					return prev;
				}

				prev = get(row, col);
			}
		}

		return OPEN;
	}

	private char getColumnWinner() {

		char prev = OPEN;
		int len = 0;

		for (int col = 0; col < boardWidth; col++) {
			len = 0;
			prev = OPEN;
			for (int row = 0; row < boardHeight; row++) {

				System.out.println("[" + row + "][" + col + "] is '" + get(row, col) + "'");

				if (get(row, col) == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (get(row, col) != OPEN) {
						len++;
						System.out.println("Streak of " + len);
					}
				} else {
					if (prev == get(row, col) && prev != OPEN) {
						len++;
						System.out.println("Streak of " + len);
					} else {
						len = 1;
						System.out.println("Streak of " + len);
					}
				}
				if (len == 4) {
					return prev;
				}

				prev = get(row, col);

			}
		}

		return 0;
	}

	private char getBackDiagonalWinner() {

		for (int row = 0; row < boardWidth; row++) {
			for (int col = 0; col < boardHeight; col++) {
				System.out.println("[" + col + "][" + col + "]");
			}
		}

		return 0;
	}

	private char getWinner() {
		// char winner = getRowWinner();
		// if(winner!=OPEN){
		// return winner;
		// }
		char winner = getColumnWinner();
		if (winner != OPEN) {
			return winner;
		}
		return OPEN;
	}

	private List<Integer> getLegalMoves() {
		List<Integer> legalMoves = new ArrayList<>();
		for (int col = 0; col < boardWidth; col++) {
			for (int row = 0; row < boardHeight; row++) {
				if (get(row, col) != OPEN) {
					if (row < boardHeight) {
						legalMoves.add(rowMajIdx(row - 1, col));
						break;
					}
				}
				if (row == boardHeight - 1) {
					legalMoves.add(rowMajIdx(row, col));
					break;
				}
			}
		}
		return legalMoves;
	}

	public String boardIndices() {
		StringBuffer sb = new StringBuffer();
		DecimalFormat df = new DecimalFormat("00");
		for (int i = 0; i < boardHeight; i++) {
			sb.append('|');
			for (int j = 0; j < boardWidth; j++) {
				// sb.append(df.format(rowMajIdx(i, j)));
				sb.append(i + "" + j);
				sb.append('|');
			}
			if (i != boardWidth) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < boardHeight; i++) {
			sb.append('|');
			for (int j = 0; j < boardWidth; j++) {
				sb.append(Character.toChars(get(i, j)));
				sb.append('|');
			}
			if (i != boardWidth) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	private void diagonalize() {
		int[][] m = new int[][] {
			new int[] { 0, 1, 2 }, 
			new int[] { 3, 4, 5 }, 
			new int[] { 6, 7, 8 }, };

		for (int[] row : m) {
			System.out.println(Arrays.toString(row));
		}

		int rowLen = m[0].length;

		int[][] d1 = new int[m.length][m[0].length];
		int[][] d2 = new int[m.length][m[0].length];

		for (int i = 0; i < rowLen; i++) {
			int k = 0;
			for (int j = rowLen - i; j < rowLen; j++) {
				d1[i][k++] = -1;
				System.out.print(m[i][j]);
//				d1[i][k++] = m[i][j];
//				System.out.print(m[i][j]);
			}
			System.out.print("->");
			for (int j = 0; j < rowLen - i; j++) {
				d1[i][k++] = m[i][j];
				System.out.println(m[i][j]);
			}
			System.out.println();
			
//			k = 0;
//			for (int j = i; j < rowLen; j++) {
//				d2[i][k++] = m[i][j];
//				System.out.print(m[i][j]);
//			}
//			System.out.print("->");
//			for (int j = 0; j < i; j++) {
//				d2[i][k++] = m[i][j];
//				System.out.print(m[i][j]);
//			}
//			System.out.println();
		}

		for (int[] row : d1) {
			System.out.println(Arrays.toString(row));
		}
		for (int[] row : d2) {
			System.out.println(Arrays.toString(row));
		}

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				System.out.print(m[i][j] + "");
			}
			System.out.println();
		}
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				System.out.print(m[j][i] + "");
			}
			System.out.println();
		}
		for (int[] row : m) {
			System.out.println(Arrays.toString(row));
		}

	}

	private void diagonalize2() {
		int[][] m = new int[][] {
			new int[] { 0, 1, 2 }, 
			new int[] { 3, 4, 5 }, 
			new int[] { 6, 7, 8 }, };
			
			// backward diagonal
			for(int i = 0; i < m.length; i++){
				for(int j = 0; i+j < m[0].length; j++){
					System.out.print(m[j][i+j] +"");
				}
				System.out.println();
			}
			
			System.out.println();
			
			// forward diagonal
			for(int i = 0; i < m.length; i++){
				for(int j = m[0].length - 1; j > 0; j--){
					System.out.print(m[i][j - i] +"");
				}
				System.out.println();
			}

	}
	
	public static void main(String[] args) throws IllegalMoveException {

		Connect4Board board = new Connect4Board();
		board.diagonalize2();
		// Connect4Board board = new Connect4Board();
		// System.out.println(board);
		// System.out.println(board.getLegalMoves());
		// System.out.println(board.getCurrentPlayer());
		// board.move(3, 0);
		// System.out.println(board);
		// System.out.println(board.getLegalMoves());
		// System.out.println(board.getCurrentPlayer());
		// board.move(3, 1);
		// System.out.println(board);
		// System.out.println(board.getLegalMoves());
		// System.out.println(board.getCurrentPlayer());
		// board.move(2, 0);
		// System.out.println(board);
		// System.out.println(board.getLegalMoves());
		// System.out.println(board.getCurrentPlayer());

		// Connect4Board board= Connect4Board.parse("" +
		// "| | |O| | |\r\n" +
		// "| | |O| | |\r\n" +
		// "|X| |O| |X|\r\n" +
		// "|X|O|O|O|O|");
		//
		// System.out.println(board.getBackDiagonalWinner());

		// System.out.println(board);
		// System.out.println(board.getWinner());
		//
		// System.out.println(board.boardIndices());

	}

}
