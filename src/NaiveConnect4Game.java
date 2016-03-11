import java.util.Scanner;



public class NaiveConnect4Game {

	private static Connect4Board board = new Connect4Board("" +
	 "| | | | | |\r\n" +
	 "| | | |X| |\r\n" +
	 "| |O|X|O|O|\r\n" +
	 "|X|X|X|O|O|");

//	private static Connect4Board board = Connect4Board.parse("" +
//			 "| | | | |\r\n" +
//			 "| | | | |\r\n" +
//			 "| | | | |");
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		while (!board.hasWinner() && !board.isFilled()) {
			
			
			if (board.getCurrentPlayer() == 'X') {
				board = getComputerMove(board);
			} else {
				board = getPlayerMove(board);
			}
			System.out.println(board);
		}
		
		if (board.hasWinner()) {
			if (board.getWinner() == 'X') {
				System.out.println("The Computer Won!");
			} else {
				System.out.println("The Computer Lost! :(");
			}

		}

		if (!board.hasWinner() && board.isFilled()) {
			System.out.println("The game is finished. No one won the game.");
		}
		
	}
	

	static Connect4Board getPlayerMove(Connect4Board board) {
		System.out.println("Human player please select a cell number to fill:");
		while (true) {
			try {
				Connect4Board newBoard = new Connect4Board(board);
				int row = scanner.nextInt();
				int col = scanner.nextInt();
				newBoard.move(row, col);
				System.out.println("Thanks. Now the board looks like:");
				System.out.println();
				return newBoard;
			} catch (IllegalMoveException e) {
				System.out.println("Not a valid move, please try again. Please select a cell to fill:");
			}
		}
	}

	static Connect4Board getComputerMove(Connect4Board board) {
		GameTree gt = new GameTree(board);
		GameTree winner = null;
		int maxUtl = Integer.MIN_VALUE;
		for (GameTree child : gt.children) {
			if (child.board.hasWinner() && child.board.getWinner() == 'X') {
				winner = child;
				break;
			}
			int utl = child.utility(board.getCurrentPlayer());
			if (maxUtl < utl) {
				winner = child;
				maxUtl = utl;
			}
		}

		Connect4Board newBoard = new Connect4Board(board);
		newBoard.move(winner.board.getLastMove());
		System.out.println("The computer makes move " + winner.board.getLastMove());
		System.out.println();
		return newBoard;
	}
	
}
