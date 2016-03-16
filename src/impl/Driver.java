package impl;

import api.C4Board;
import api.C4Game;
import api.C4Strategy;
import api.WinConditionEvaluator;

public class Driver {

	public static void main(String[] args) {
		C4Strategy randomStrategy =  new RandomMoveStrategy();
		C4Board board = new ArrayBackedBoard(4, 5);
		WinConditionEvaluator eval =  new VariableRunLengthWinConditionEvaluator(4);
		C4Game game =  new C4Game(randomStrategy, randomStrategy, board, eval);
		
		game.playGame();
	}
	
}
