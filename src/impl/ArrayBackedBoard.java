package impl;


import static api.Color.BLACK;
import static api.Color.NONE;
import static api.Color.WHITE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import api.C4Board;
import api.C4Move;
import api.Color;
import api.IllegalMoveException;

public class ArrayBackedBoard implements C4Board {

	private int w;
	private int h;
	private Color[][] data;
	
	public ArrayBackedBoard(int height, int width) {
		this.w = width;
		this.h = height;
		data = new Color[h][w];
		for(int i = 0; i < h; i++){
			data[i] =  new Color[w];
			for(int j= 0; j < w; j++){
				data[i][j] = NONE;
			}
		}
	}
	
	public ArrayBackedBoard(String boardDisplay) {
		String[] lines = boardDisplay.split("\\r\\n?|\\n");
		h = lines.length;
		w = lines[0].substring(1, lines[0].length() - 1).split("\\|").length;
		for (int i = 0; i < h; i++) {
			String[] line = lines[i].substring(1, lines[0].length() - 1).split("\\|");
			for (int j = 0; j < w; j++) {	
				switch (line[j].charAt(0)) {
				case 'X':
					data[i][j] = WHITE;
					break;
				case 'O':
					data[i][j] = BLACK;
					break;
				case ' ':
					data[i][j] = Color.NONE;
					break;
				default:
					throw new AssertionError();
				}
			}
		}
	}
	
	
	public ArrayBackedBoard(int height, int width, int progress){
		this(height, width);
		int i = 0;
		Color c = WHITE;
		while (i < progress){
			List<C4Move> moves = getLegalMoves();
			Collections.shuffle(moves);
			processMove(c, moves.get(0));
			c = c == WHITE ? BLACK : WHITE;
			i++;
		}
	}

	@Override
	public int getBoardWidth() {
		return w;
	}

	@Override
	public int getBoardHeight() {
		return h;
	}

	@Override
	public void processMove(Color color, C4Move move) throws IllegalMoveException {
		if(!getLegalMoves().contains(move)){
			throw new IllegalMoveException(move + " is not a legal move");
		}
		data[move.getRow()][move.getCol()] = color;
	}

	@Override
	public List<C4Move> getLegalMoves() {
		List<C4Move> moves =  new ArrayList<C4Move>();
		for(int i = h - 1; i >= 0; i--){
			for(int j= w - 1; j >= 0; j--){
				if(i == h - 1){
					if(data[i][j] == NONE){
						moves.add(new C4Move(i, j));
					}
				} else {
					if(data[i][j] == NONE && data[i + 1][j] != NONE){
						moves.add(new C4Move(i, j));
					}
				}
				
				// there are at most w legal moves
				if(moves.size() == w){
					break;
				}
			}
		}
		return moves;
	}

	@Override
	public void display() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			sb.append('|');
			for (int j = 0; j < data[0].length; j++) {
				switch (data[i][j]) {
				case WHITE:
					sb.append('X');
					break;
				case BLACK:
					sb.append('O');
					break;
				case NONE:
					sb.append(' ');
					break;
				default:
					throw new AssertionError();
				}
				sb.append('|');
			}
			if (i != data[0].length) {
				sb.append('\n');
			}
		}
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) {
		C4Board b =  new ArrayBackedBoard(6, 7, 7);
		b.display();
		System.out.println(b.getLegalMoves());
	}

	@Override
	public Color get(int i, int j) {
		return data[i][j];
	}

	@Override
	public C4Board copy() {
		ArrayBackedBoard copy =  new ArrayBackedBoard(h, w);
		 for(int i = 0; i < h; i++){
			 for(int j =0; j< w; j++){
				 copy.data[i][j] = data[i][j];
			 }
		 }
		 return (C4Board) copy;
	}
}
