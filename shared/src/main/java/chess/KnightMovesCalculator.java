package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (row < 7) {
            if (col < 8) {
                //check the square two squares up and one square right
                addMoves(board, myPosition, possibleMoves, row+2, col+1);
            }
            if (col > 1) {
                //check the square two squares up and one square left
                addMoves(board, myPosition, possibleMoves, row+2, col-1);
            }
        }

        if (row > 2) {
            if (col < 8) {
                //check the square two squares down and one square right
                addMoves(board, myPosition, possibleMoves, row-2, col+1);
            }
            if (col > 1) {
                //check the square two squares down and one square left
                addMoves(board, myPosition, possibleMoves, row-2, col-1);
            }
        }

        if (col < 7) {
            if (row < 8) {
                //check the square two squares right and one square up
                addMoves(board, myPosition, possibleMoves, row+1, col+2);
            }
            if (row > 1) {
                //check the square two squares right and one square down
                addMoves(board, myPosition, possibleMoves, row-1, col+2);
            }
        }

        if (col > 2) {
            if (row < 8) {
                //check the square two squares left and one square up
                addMoves(board, myPosition, possibleMoves, row+1, col-2);
            }
            if (row > 1) {
                //check the square two squares left and one square down
                addMoves(board, myPosition, possibleMoves, row-1, col-2);
            }
        }

        return possibleMoves;
    }

    private void addMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves, int row, int col) {
        ChessPosition newPosition = new ChessPosition(row, col);
        if (board.getPiece(newPosition) == null) {
            possibleMoves.add(new ChessMove(myPosition, newPosition, null));
        } else {
            if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
    }
}
