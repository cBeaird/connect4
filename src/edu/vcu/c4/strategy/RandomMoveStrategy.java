package edu.vcu.c4.strategy;

import java.util.Collections;
import java.util.List;

import edu.vcu.c4.C4Game;
import edu.vcu.c4.C4Move;
import edu.vcu.c4.C4Strategy;

public class RandomMoveStrategy implements C4Strategy{

	@Override
	public C4Move move(C4Game game) {
		List<C4Move> moves =  game.getBoard().getLegalMoves();
		Collections.shuffle(moves);
		return moves.get(0);
	}

}
