package edu.vcu.c4;

import edu.vcu.c4.board.ArrayBackedBoard;
import edu.vcu.c4.strategy.BruteForceTreeSearchStrategy;
import edu.vcu.c4.strategy.HumanStrategy;
import edu.vcu.c4.winner.VariableRunLengthWinConditionEvaluator;

public class Driver {
	public static void main(String[] args) {
		C4Game game = new C4Game(new BruteForceTreeSearchStrategy(), new HumanStrategy(), new ArrayBackedBoard(3, 4),
				new VariableRunLengthWinConditionEvaluator(3));
		game.playGame();
	}

}