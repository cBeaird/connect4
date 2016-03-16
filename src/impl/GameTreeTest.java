package impl;

import api.C4Board;
import api.C4Game;
import api.C4Strategy;
import api.WinConditionEvaluator;

public class GameTreeTest {

	
	public static void main(String[] args) {
		C4Strategy human =  new HumanStrategy();
		C4Strategy bfs = new BruteForceTreeSearchStrategy();
		C4Board board = new ArrayBackedBoard(3, 4);
		WinConditionEvaluator eval =  new VariableRunLengthWinConditionEvaluator(3);
		C4Game game =  new C4Game(bfs, human, board, eval);
		
		game.playGame();
	}
}
