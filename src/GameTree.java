
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Use the corresponding class in package edu.vcu.c4 instead
 * 
 */
@Deprecated
public class GameTree {

    GameTree parent;
    Connect4Board board;
    List<GameTree> children = new ArrayList<GameTree>();

    GameTree(Connect4Board board, GameTree parent) {
        this.board = board;
        this.parent = parent;
        build();
    }

    GameTree(Connect4Board board) {
        this(board, null);
    }

    void build() {
        if (board.hasWinner()) {
            return;
        }
        if (board.isFilled()) {
            return;
        }
        for (Connect4Board.Move move : board.getRemainingMoves()) {
            Connect4Board possibleBoard = new Connect4Board(board);
            possibleBoard.move(move);
            GameTree gt = new GameTree(possibleBoard, this);
            children.add(gt);

        }
    }

    int utility(Character player) {
        if (board.hasWinner() && board.getWinner() == player) {
            return 1;
        }

        if (board.hasWinner() && board.getWinner() != player) {
            return -1;
        }

        if (board.isFilled()) {
            return 0;
        }

        int utl = 0;

        for (GameTree child : children) {
            utl += child.utility(player);
        }

        return utl;
    }

    void print(String indent) {
        System.out.println(indent + board + " ux " + utility('X') + " uo " + utility('O'));
        for (GameTree child : children) {
            child.print(indent + "\t");
        }
    }

}