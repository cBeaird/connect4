
public class Connect4Game<T extends Connect4Board> {
	T board;

	public Connect4Game(T board) {
		this.board = board;
	}
	
	public static void main(String[] args) {
		WrapAroundConnect4Board board = new WrapAroundConnect4Board("" +
				 "| | | | |O|\r\n" +
				 "|O|X| |X|X|\r\n" +
				 "|X|O|X|O|O|\r\n" +
				 "|X|X|O|O|O|");
		Connect4Game<WrapAroundConnect4Board> game = new Connect4Game<>(board);
		
		System.out.println(board.hasWinner());
	}
	
}
