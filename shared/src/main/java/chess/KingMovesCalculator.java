package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        //int newRow = row;
        //int newCol = col;

        if (col < 8) {
            //check directly to the right of the current position
            addMoves(board, myPosition, possibleMoves, row, col+1);

            if (row < 8) {
                //check to the right and up from current position
                addMoves(board, myPosition, possibleMoves, row+1, col+1);
            }
            if (row > 1) {
                //check to the right and down from current position
                addMoves(board, myPosition, possibleMoves, row-1, col+1);
            }
        }
        if (col > 1) {
            //check directly to the left of the current position
            addMoves(board, myPosition, possibleMoves, row, col-1);
            if (row < 8) {
                //check to the left and up from current position
                addMoves(board, myPosition, possibleMoves, row+1, col-1);
            }
            if (row > 1) {
                //check to the left and down from current position
                addMoves(board, myPosition, possibleMoves, row-1, col-1);
            }
        }
        if (row < 8) {
            //check directly up from current position
            addMoves(board, myPosition, possibleMoves, row+1, col);
        }
        if (row > 1) {
            //check directly down from current position
            addMoves(board, myPosition, possibleMoves, row-1, col);
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
