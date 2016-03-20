package edu.vcu.c4;

import static edu.vcu.c4.Color.NONE;
import static edu.vcu.c4.Color.O;
import static edu.vcu.c4.Color.X;

import java.util.ArrayDeque;
import java.util.Deque;

public class C4Game {

	private C4Strategy whiteStrategy;
	private C4Strategy blackStrategy;
	private C4Strategy currentStrategy;

	private C4Board board;
	private WinConditionEvaluator winChecker;

	private Deque<C4Move> moves;

	private Color currentColor = X;

	private Color winner = NONE;

	public C4Game(C4Strategy player1Strategy, C4Strategy player2Strategy, C4Board board,
			WinConditionEvaluator winChecker) {
		this.whiteStrategy = currentStrategy = player1Strategy;
		this.blackStrategy = player2Strategy;
		this.board = board;
		this.winChecker = winChecker;
		this.moves = new ArrayDeque<C4Move>();
	}

	public C4Game(C4Game original, C4Move move) {
		// copy the game state
		this.whiteStrategy = original.whiteStrategy;
		this.blackStrategy = original.blackStrategy;
		this.currentStrategy = original.currentStrategy;
		this.currentColor = original.currentColor;
		this.winChecker = original.winChecker;
		this.board = original.board.copy();
		this.winner = original.winner;
		this.moves = new ArrayDeque<>(original.getMoves());
		
		// execute the new move

		board.processMove(currentColor, move);
		moves.push(move);
		
		if (!moves.isEmpty()) {
			currentColor = currentColor == X ? O : X;
			currentStrategy = currentStrategy == blackStrategy ? whiteStrategy : blackStrategy;
		}
	}

	public void playTurn(C4Move move) {

	}

	public void playGame() {
		do {

			display("");

			if (!moves.isEmpty()) {
				currentColor = currentColor == X ? O : X;
				currentStrategy = currentStrategy == blackStrategy ? whiteStrategy : blackStrategy;
			}

			C4Move move = currentStrategy.move(this);
			while (true) {
				try {
					board.processMove(currentColor, move);
					moves.push(move);
					break;
				} catch (IllegalMoveException e) {
					System.out.println(e.getMessage());
					move = currentStrategy.move(this);
				}
			}

		} while (!isDraw() && !hasWinner());

		display("");
	}

	public C4Board getBoard() {
		return board;
	}

	public boolean hasWinner() {
		winner = winChecker.getWinner(this);
		return winner != NONE;
	}

	public Color getWinner() {
		return winner;
	}

	public boolean isDraw() {
		return board.getLegalMoves().isEmpty();
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public Deque<C4Move> getMoves() {
		return new ArrayDeque<>(moves);
	}


	public void display(String indent) {
		Color previousPlayer;

		if (moves.isEmpty()) {
			previousPlayer = X;
			System.out.print(indent + "New game.");
		} else {
			previousPlayer = currentColor == X ? O : X;
			System.out.print(indent + "Player '" + currentColor + "' played " + moves.peek() + ".");
		}

		if (hasWinner()) {
			System.out.print(" Winner: " + getWinner());
		} else if (isDraw()) {
			System.out.print(" Draw");
		} else {
			System.out.print(" Player " + previousPlayer + "'s turn.");
		}

		System.out.println();
		getBoard().display(indent);
	}

	@Override
	public String toString() {
		StringBuffer sb =  new StringBuffer();
		sb.append("Current Turn: " + currentColor);
		sb.append("\n");
		sb.append(moves);
		sb.append("\n");
		sb.append(board);
		return sb.toString();
	}

	
	
}
