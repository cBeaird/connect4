package impl;

import java.util.Collections;
import java.util.List;

import api.C4Game;
import api.C4Move;
import api.C4Strategy;
/**
 * 
 * Use the corresponding class in package edu.vcu.c4 instead
 * 
 */
@Deprecated
public class RandomMoveStrategy implements C4Strategy{

	@Override
	public C4Move move(C4Game game) {
		List<C4Move> moves =  game.getBoard().getLegalMoves();
		Collections.shuffle(moves);
		return moves.get(0);
	}

}
