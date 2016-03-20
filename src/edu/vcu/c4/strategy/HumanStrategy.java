package edu.vcu.c4.strategy;

import java.util.Scanner;

import edu.vcu.c4.C4Game;
import edu.vcu.c4.C4Move;
import edu.vcu.c4.C4Strategy;

public class HumanStrategy implements C4Strategy {

	private static Scanner scanner = new Scanner(System.in);
	
	@Override
	public C4Move move(C4Game game) {
		System.out.println("Please select a cell number to fill:");
		int row = scanner.nextInt();
		int col = scanner.nextInt();
		C4Move move =  new C4Move(row, col);
		return move;
	}

}
