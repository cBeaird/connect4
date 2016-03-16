package impl;

import static api.Color.BLACK;
import static api.Color.NONE;
import static api.Color.WHITE;

import api.C4Board;
import api.C4Game;
import api.Color;
import api.WinConditionEvaluator;

public class WrappingRowWinner implements WinConditionEvaluator {

	private int runLen;

	public WrappingRowWinner(int runLen) {
		this.runLen = runLen;
	}



	@Override
	public Color getWinner(C4Game game) {
		C4Board board = game.getBoard();

		int blackRun = 0;
		int whiteRun = 0;

		int h = board.getBoardHeight();
		int w = board.getBoardWidth();

		for (int i = 0; i < h; i++) {
			blackRun = 0;
			whiteRun = 0;
			for (int j = 0; j < w + 1; j++) {
				switch (board.get(i, j % runLen)) {
				case NONE:
					blackRun = 0;
					whiteRun = 0;
					break;
				case BLACK:
					blackRun++;
					whiteRun = 0;
					break;
				case WHITE:
					blackRun = 0;
					whiteRun++;
					break;
				default:
					throw new AssertionError();
				}
				
				if(blackRun == runLen){
					return BLACK;
				}
				if(whiteRun == runLen){
					return WHITE;
				}
				
			}
		}

		return NONE;
	}

}
