package edu.vcu.c4.winner;

import static edu.vcu.c4.Color.NONE;

import edu.vcu.c4.C4Board;
import edu.vcu.c4.C4Game;
import edu.vcu.c4.Color;
import edu.vcu.c4.WinConditionEvaluator;

public class VariableRunLengthWinConditionEvaluator implements WinConditionEvaluator {

	private int runLen;
	int h;
	int w;

	public VariableRunLengthWinConditionEvaluator(int runLen) {
		this.runLen = runLen;
	}

	@Override
	public Color getWinner(final C4Game game) {
		C4Board board = game.getBoard();
		h = board.getBoardHeight();
		w = board.getBoardWidth();

		Color[][] state = new Color[h][w];

		for (int i = 0; i < h; i++) {
			state[i] = new Color[w];
			for (int j = 0; j < w; j++) {
				state[i][j] = board.get(i, j);
			}
		}

		Color winner = getRowWinner(state);
		if (winner == NONE) {
			winner = getColumnWinner(state);
		} else {
			return winner;
		}
		if (winner == NONE) {
			winner = getDiagonalWinner(state);
		} else {
			return winner;
		}
		if (winner == NONE) {
		} else {
			return winner;
		}
		return winner;
	}

	private Color getRowWinner(final Color[][] state) {
		Color prev = NONE;
		int len = 0;

		// Search the rows
		for (int row = 0; row < h; row++) {
			len = 0;
			prev = NONE;
			for (int col = 0; col < w; col++) {
				if (state[row][col] == NONE) {
					len = 0;
					prev = NONE;
					continue;
				}

				if (prev == NONE) {
					if (state[row][col] != NONE) {
						len++;
					}
				} else {
					if (prev == state[row][col] && prev != NONE) {
						len++;
					} else {
						len = 1;
					}
				}

				if (len == runLen) {
					return prev;
				}

				prev = state[row][col];
			}
		}

		return NONE;
	}

	private Color getColumnWinner(final Color[][] state) {

		Color prev = NONE;
		int len = 0;

		for (int col = 0; col < w; col++) {
			len = 0;
			prev = NONE;
			for (int row = 0; row < h; row++) {

				if (state[row][col] == NONE) {
					len = 0;
					prev = NONE;
					continue;
				}

				if (prev == NONE) {
					if (state[row][col] != NONE) {
						len++;
					}
				} else {
					if (prev == state[row][col] && prev != NONE) {
						len++;
					} else {
						len = 1;
					}
				}

				if (len == runLen) {
					return prev;
				}

				prev = state[row][col];

			}
		}

		return NONE;
	}

	private Color getDiagonalWinner(final Color[][] state) {

		Color[][] backDiagonal = new Color[h][w + h - 1];
		Color[][] forwardDiagonal = new Color[h][w + h - 1];

		for (int i = 0; i < backDiagonal.length; i++) {
			for (int j = 0; j < backDiagonal[0].length; j++) {
				backDiagonal[i][j] = Color.NONE;
			}
		}

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				backDiagonal[i][j + i] = state[i][j];
			}
		}

		for (int i = 0; i < forwardDiagonal.length; i++) {
			for (int j = 0; j < forwardDiagonal[0].length; j++) {
				forwardDiagonal[i][j] = Color.NONE;
			}
		}

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				forwardDiagonal[i][j + state.length - i - 1] = state[i][j];
			}
		}

		Color winner = getColumnWinner(forwardDiagonal);
		if (winner == NONE) {
			winner = getColumnWinner(backDiagonal);
		}
		return winner;
	}

}
