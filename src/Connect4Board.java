
import static java.util.Arrays.copyOf;

import java.util.ArrayList;
import java.util.List;

public class Connect4Board {
	private static final int RUN_LENGTH = 4;
	private static final char PLAYER1 = 'X';
	private static final char PLAYER2 = 'O';
	static final char OPEN = ' ';

	private static final int BOARD_WIDTH = 5;
	private static final int BOARD_HEIGHT = 4;

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

		state = new char[boardHeight][boardWidth];

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
	}

	public Connect4Board(Connect4Board board) {
		this(board.state.length, board.state[0].length);
		for (int i = 0; i < board.state.length; i++) {
			state[i] = copyOf(board.state[i], board.state[i].length);
		}
		this.currentPlayer = board.currentPlayer;
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
		// set(row, col, player);
		currentPlayer = currentPlayer == PLAYER2 ? PLAYER1 : PLAYER2;

		setLastMove(new Move(row, col));
	}


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
				if (state[row][col] == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (state[row][col] != OPEN) {
						len++;
					}
				} else {
					if (prev == state[row][col] && prev != OPEN) {
						len++;
					} else {
						len = 1;
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

				if (state[row][col] == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (state[row][col] != OPEN) {
						len++;
					}
				} else {
					if (prev == state[row][col] && prev != OPEN) {
						len++;
					} else {
						len = 1;
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

				if (d[row][col] == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (d[row][col] != OPEN) {
						len++;
					}
				} else {
					if (prev == d[row][col] && prev != OPEN) {
						len++;
					} else {
						len = 1;
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

				if (d[row][col] == OPEN) {
					len = 0;
					prev = OPEN;
					continue;
				}

				if (prev == OPEN) {
					if (d[row][col] != OPEN) {
						len++;
					}
				} else {
					if (prev == d[row][col] && prev != OPEN) {
						len++;
					} else {
						len = 1;
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

	private char getWinnerInOne(int k) {
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardHeight; j++) {
				// Open cell cannot start a win in any direction
				if (state[i][j] == OPEN)
					break;

				// Find row winner if exists
				if ((i + k) < boardWidth) {
					for (int l = i + 1; l < k; l++) {
						if (state[i][j] != state[l][j])
							break;
						if (i + l == k)
							return state[i][j];
					}
				}

				// Find Column winner if exists
				if ((j + k) < boardHeight) {
					for (int l = j + 1; l < boardHeight; l++) {
						if (state[i][j] != state[i][l])
							break;
						if (j + l == k)
							return state[i][j];
					}
				}

				// Find Diagonal winner if exists
				// look for (upRight vector wins)
				if (((i + k) < boardWidth) && ((j + k) < boardHeight)) {
					for (int ur = 1; ur < k; ur++) {
						if (((i + ur) > boardWidth) || ((j + ur) > boardHeight))
							break;
						if (state[i][j] != state[i + ur][j + ur])
							break;
						if (i + ur == k)
							return state[i][j];
					}
				}
				// look for (upLeft vector wins)
				if (((i - k) > 0) && ((j + k) < boardHeight)) {
					for (int ul = 1; ul < k; ul++) {
						if (((i + (-1 * ul)) < 0) || ((j + ul) > boardHeight))
							break;
						if (state[i][j] != state[i + (-1 * ul)][j + ul])
							break;
						if (j + ul == k)
							return state[i][j];
					}
				}
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

	public boolean hasWinner() {
		return getWinner() != OPEN;
	}

	public boolean isFilled() {
		return getRemainingMoves().isEmpty();
	}


	public List<Move> getRemainingMoves() {
		List<Move> legalMoves = new ArrayList<>();
		for (int i = 1; i < boardHeight; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (state[i][j] != OPEN) {
					if (state[i - 1][j] == OPEN) {
						legalMoves.add(new Move(i - 1, j));
					}
				}
				if (state[i][j] == OPEN && i == boardHeight - 1) {
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

	class Move {
		int row;
		int col;

		Move(int row, int col) {
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

	public Move getLastMove() {
		return lastMove;
	}

	public void setLastMove(Move lastMove) {
		this.lastMove = lastMove;
	}

}
