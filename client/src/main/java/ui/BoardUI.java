package ui;

import chess.ChessBoard;
import chess.ChessGame;

public class BoardUI {
    public static void main(String[] args) {
        String practiceBoard = displayBoard(new ChessBoard(), ChessGame.TeamColor.WHITE);
        System.out.print(practiceBoard);
    }

    public static String displayBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        StringBuilder drawnBoard = new StringBuilder();
        drawnBoard.append("  a  b  c  d  e  f  g  h  \n");
        for (int row = 8; row > 0; row--) {
            drawnBoard.append(row);
            for (int col = 1; col < 9; col++) {
                drawnBoard.append(" . ");
            }
            drawnBoard.append(row).append("\n");
        }
        drawnBoard.append("  a  b  c  d  e  f  g  h  ");

        return drawnBoard.toString();
    }
}
