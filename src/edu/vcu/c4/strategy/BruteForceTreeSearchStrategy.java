package edu.vcu.c4.strategy;

import static edu.vcu.c4.Color.O;
import static edu.vcu.c4.Color.X;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import edu.vcu.c4.C4Game;
import edu.vcu.c4.C4Move;
import edu.vcu.c4.C4Strategy;
import edu.vcu.c4.Color;
import edu.vcu.c4.GameTree;

public class BruteForceTreeSearchStrategy implements C4Strategy {
	
	private Random rnd =  new Random();
	
	
	@Override
	public C4Move move(C4Game game) {
		
		
		Color me = game.getCurrentColor();
		GameTree gt = createGameTree(game, null);

		GameTree winner = null;
		int maxUtility = Integer.MIN_VALUE;
		
		
		// check to see if I can win outright
		List<C4Move> moves = getWinningMoves(gt);
		if(!moves.isEmpty()){
			// I won, pick a random winning move
			return moves.get(rnd.nextInt(moves.size()));
		}
		
		C4Move move;
		for (GameTree childTree : gt.getChildren()) {
			C4Game childGame = childTree.getGame();
			int utility = childTree.utility(game.getCurrentColor());
			
			move = childGame.getMoves().peek();
			
			// If making this move will allow the opponent to win on their turn then I can't do it.
			for(GameTree opponentTree: childTree.getChildren()){
				C4Game opponentGame = opponentTree.getGame();
				if(opponentGame.hasWinner() && opponentGame.getWinner() == childGame.getCurrentColor()){
					System.out.println("Not taking "+ move + " because the opponent would win.");
					utility = Integer.MIN_VALUE;
				}
			}
		
			if (maxUtility < utility) {
				winner = childTree;
				maxUtility = utility;
				System.out.println("u(" + game.getCurrentColor() + ")-> " + childGame.getMoves().peek() + "=" + utility + " is the best move");
			}

		}

		if(winner == null){
			System.out.println("I cant win this game :(");
			return  game.getBoard().getLegalMoves().get(0);
		}
		
		return winner.getGame().getMoves().peek();
	}

	private List<C4Move> getWinningMoves(GameTree tree){
		List<C4Move> moves = new ArrayList<>();
		Color me = tree.getGame().getCurrentColor(); 
		for (GameTree gt : tree.getChildren()) {
			C4Game game = gt.getGame();
			if(game.hasWinner() && game.getWinner() == me){
				moves.add(game.getMoves().peek());
			}
		}
		return moves;
	}
	
	private GameTree createGameTree(C4Game game, GameTree parent) {
		GameTree root = new GameTree(game, parent);
		if (!(game.hasWinner() || game.isDraw())) {
			for (C4Move move : game.getBoard().getLegalMoves()) {
				C4Game child = new C4Game(game, move);
				root.getChildren().add(createGameTree(child, root));
			}
		}
		return root;
	}
}


//// I can't win this turn, so I'll make to move that maximizes my utility
//List<GameTree> possibleMoves = gt.getChildren();
//Collections.sort(possibleMoves, new Comparator<GameTree>() {
//	@Override
//	public int compare(GameTree gt1, GameTree gt2) {
//		return Integer.compare(gt2.utility(me),gt1.utility(me));
//	}
//});
//
//List<GameTree> bestMoves = new ArrayList<>();
//for(GameTree next: possibleMoves){
//	for(GameTree opponentNext: next.getChildren()){
//		if(opponentNext.getGame().hasWinner()){
//			// My opponent has a winning move if I make this move
//		} else {
//			if(!bestMoves.isEmpty()){
//				
//			} else {
//				
//			}
//			bestMoves.add(next);
//		}
//	}
//}
//
//// My opponent would win with perfect play, so just minimize their payoff
//return possibleMoves.get(0).getGame().getMoves().peek();