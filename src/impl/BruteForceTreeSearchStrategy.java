package impl;

import api.C4Game;
import api.C4Move;
import api.C4Strategy;

public class BruteForceTreeSearchStrategy implements C4Strategy {
	@Override
	public C4Move move(C4Game game) {
		
		// The opposing player must play the brute for strategy as well for this
		
		//C4Game hypotheticalGame = new C4Game(new BruteForceTreeSearchStrategy(), new BruteForceTreeSearchStrategy(), game.getBoard().copy(), game.getWinChecker());
		
		//GameTree gt =  new GameTree(hypotheticalGame);
		GameTree gt =  new GameTree(game);
		GameTree winner = null;
		int maxUtility = Integer.MIN_VALUE;
		for(GameTree childTree: gt.getChildren()){
			C4Game childGame = childTree.getGame();
			System.out.println("u("+childGame.getCurrentColor()+"-> " + childGame.getMoves().pop()+"="+childTree.utility(childGame.getCurrentColor()));
			if(childGame.hasWinner() && childGame.getWinner() == childGame.getCurrentColor()){
				winner = childTree;
				break;
			}
			
			int utility = childTree.utility(childGame.getCurrentColor());
			if(maxUtility < utility){
				winner = childTree;
				maxUtility = utility;
			}

		}
		return winner.getGame().getMoves().pop();
	}
}
