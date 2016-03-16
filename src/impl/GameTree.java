package impl;

import java.util.ArrayList;
import java.util.List;

import api.C4Game;
import api.C4Move;
import api.Color;

public class GameTree {
	private GameTree parent;
	private C4Game game;
	private List<GameTree> children =  new ArrayList<>();
	
	public GameTree(C4Game game, GameTree parent){
		this.game = game;
		this.parent = parent;
		build();
	}
	
	public GameTree(C4Game game){
		this(game, null);
	}
	
    void build() {
        if (game.hasWinner()) {
            return;
        }
        if (game.isDraw()) {
            return;
        }
        for(C4Move move: game.getBoard().getLegalMoves()){
        	C4Game newGame = new C4Game(game, move);
        	GameTree gt =  new GameTree(newGame, this);
        	children.add(gt);
        }
        
    }
    
    int utility(Color player) {
        if (game.hasWinner() && game.getWinner() == player) {
            return 1;
        }

        if (game.hasWinner() && game.getWinner() != player) {
            return -1;
        }

        if (game.isDraw()) {
            return 0;
        }

        int utl = 0;

        for (GameTree child : children) {
            utl += child.utility(player);
        }

        return utl;
    }

	public List<GameTree> getChildren() {
		return children;
	}

	public C4Game getGame() {
		return game;
	}

}
