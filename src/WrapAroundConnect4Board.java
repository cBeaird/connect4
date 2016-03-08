import java.util.Arrays;

public class WrapAroundConnect4Board extends Connect4Board {

	@SuppressWarnings("unused")
	private char[][] tmp =  new char[][]{
		new char[]{'1','2','3'},
		new char[]{'4','5','6'},
		new char[]{'7','8','9'},
	};
	
	public WrapAroundConnect4Board(String string) {
		super(string);
	}

	@Override
	protected char getDiagonalWinner() {
		System.out.println("	@Override\r\n" + 
				"	protected char getDiagonalWinner()");
		int h = state.length;
		int w = state[0].length;
		
		char[][] backDiagonal =  new char[h][w];
		char[][] forwardDiagonal =  new char[h][w];
		
		int k = 0;
		for(int i = 0; i < h; i++){
			backDiagonal[i] = new char[w];
			for(int j = 0; j < w; j++){
				backDiagonal[i][(j + k) % w] = state[i][j];
			}
			k++;
			System.out.println(Arrays.toString(backDiagonal[i]));
		}
		
		k = 0;
		for(int i = 0; i < h; i++){
			forwardDiagonal[i] = new char[w];
			for(int j = 0; j < w; j++){
				forwardDiagonal[i][j] = state[i][(j + k) % w];
			}
			k++;
			System.out.println(Arrays.toString(forwardDiagonal[i]));
		}
		
		char winner = getColumnWinner(forwardDiagonal);
		System.out.println(winner);
		if(winner == OPEN){
			winner = getColumnWinner(backDiagonal);
		} 
		return winner;
		
	}

}
