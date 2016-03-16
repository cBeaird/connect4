package api;

import java.util.List;

public interface C4Board {
	public int getBoardWidth();
	public int getBoardHeight();
	public void processMove(Color color, C4Move move)throws IllegalMoveException;
	public List<C4Move> getLegalMoves();
	public void display();
	public Color get(int i, int j);
	public C4Board copy();
}
