package impl;

import java.util.Scanner;

import api.C4Game;
import api.C4Move;
import api.C4Strategy;
/**
 * 
 * Use the corresponding class in package edu.vcu.c4 instead
 * 
 */
@Deprecated
public class HumanStrategy implements C4Strategy {

	private static Scanner scanner = new Scanner(System.in);
	
	@Override
	public C4Move move(C4Game game) {
		game.getBoard().display();
		System.out.println("Human player please select a cell number to fill:");
		int row = scanner.nextInt();
		int col = scanner.nextInt();
		C4Move move =  new C4Move(row, col);
		return move;
	}

}
