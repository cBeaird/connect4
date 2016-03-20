package api;

import static api.Color.WHITE;
import static api.Color.BLACK;
import static api.Color.NONE;

import java.util.ArrayDeque;
import java.util.Deque;
/**
 * 
 * Use the corresponding class in package edu.vcu.c4 instead
 * 
 */
@Deprecated
public class C4Game {

	private C4Strategy whiteStrategy;
	private C4Strategy blackStrategy;
	private C4Strategy currentStrategy;

	private C4Board board;
	private WinConditionEvaluator winChecker;

	private Deque<C4Move> moves = new ArrayDeque<C4Move>();

	private Color currentColor = WHITE;
	
	private Color winner = NONE;

	public C4Game(C4Strategy player1Strategy, C4Strategy player2Strategy, C4Board board,
			WinConditionEvaluator winChecker) {
		this.whiteStrategy = player1Strategy;
		this.blackStrategy = player2Strategy;
		this.board = board;
		this.winChecker = winChecker;
	}

	public C4Game(C4Game original, C4Move move){
		this.whiteStrategy = original.whiteStrategy;
		this.blackStrategy = original.blackStrategy;
		this.currentStrategy = original.currentStrategy;
		this.currentColor = original.currentColor;
		this.winChecker = original.winChecker;
		this.board = original.board.copy();
		this.winner = original.winner;
		
		playMove(move);
	}
	
	public void playGame() {
		do {

			currentColor = currentColor == WHITE ? BLACK : WHITE;
			switch (currentColor) {
			case WHITE:
				currentStrategy = whiteStrategy;
				break;
			case BLACK:
				currentStrategy = blackStrategy;
				break;
			default:
				throw new AssertionError();
			}

			C4Move move = currentStrategy.move(this);
			
			playMove(move, true);
			
			//board.display();

		} while (!isDraw() && !hasWinner());
		
		if(hasWinner()){
			System.out.println("Winner is " + winner);
		} else {
			System.out.println("Draw");
		}
		
		
	}	
	
	
	private void playMove(C4Move move, boolean announce){
		C4Move tmpMove = move;
		while (true) {
			try {
				board.processMove(currentColor, tmpMove);
				moves.push(tmpMove);
				if(announce){
					System.out.println(currentColor + " plays " + move + " (below):");
				}
				break;
			} catch (IllegalMoveException e) {
				System.out.println(e.getMessage());
				tmpMove = currentStrategy.move(this);
			}
		}
	}
	private void playMove(C4Move move){
		playMove(move, false);
	}
	
	public void setBoard(C4Board board) {
		this.board = board;
	}

	public void setWinChecker(WinConditionEvaluator winChecker) {
		this.winChecker = winChecker;
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
	
	public void setCurrentStrategy(C4Strategy strategy){
		this.currentStrategy = strategy;
	}

	public WinConditionEvaluator getWinChecker() {
		return winChecker;
	}

	
	
}
