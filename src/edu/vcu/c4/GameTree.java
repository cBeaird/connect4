package edu.vcu.c4;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Represents the game tree for a game. Building the tree and calculation
 * utility from a tree is up to GameTree's clients.
 *
 * Add fields to GameTree if you need to carry more data in the nodes of the
 * tree (eg counts, winrate, etc).
 *
 */
public class GameTree {

	private GameTree parent;
	private C4Game game;
	private List<GameTree> children = new ArrayList<>();

	public GameTree(C4Game game, GameTree parent) {
		this.game = game;
		this.parent = parent;
	}

	public GameTree(C4Game game) {
		this(game, null);
	}

	// TODO: This ought to be the responsibility of the strategy implementations
	// right? Maybe DumbassStrategy.java is trying to force his own loss in
	// which case this calculation is wrong.
	public int utility(Color player) {
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

	public int size() {
		if (children.size() == 0) {
			return 1;
		}
		int size = 0;
		for (GameTree gt : children) {
			size += gt.size();
		}
		return size;
	}

	public void print(String indent) {
		System.out.println(indent + "utility(X)=" + utility(Color.X));
		System.out.println(indent + "utility(O)=" + utility(Color.O));
		game.display(indent);
		for (GameTree child : children) {
			child.print(indent + "  ");
		}
	}

	public GameTree getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return size() + "";
	}

}
