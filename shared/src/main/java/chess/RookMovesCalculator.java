package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int newRow = row;
        int newCol = col;

        while (newRow < 8) {
            //move straight up
            newRow += 1;
            if (addMoves(board, myPosition, possibleMoves, newRow, col)) {
                break;
            }
        }

        newRow = row;
        while (newRow > 1) {
            //move straight down
            newRow -= 1;
            if (addMoves(board, myPosition, possibleMoves, newRow, col)) {
                break;
            }
        }

        while (newCol > 1) {
            //move left
            newCol -= 1;
            if (addMoves(board, myPosition, possibleMoves, row, newCol)) {
                break;
            }
        }

        newCol = col;
        while (newCol < 8) {
            // move right
            newCol += 1;
            if (addMoves(board, myPosition, possibleMoves, row, newCol)) {
                break;
            }
        }

        return possibleMoves;
    }

    private boolean addMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves, int newRow, int newCol) {
        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        if (board.getPiece(newPosition) == null) {
            possibleMoves.add(new ChessMove(myPosition, newPosition, null));
        } else {
            if (board.getPiece(newPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                return true;
            } else {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                return true;
            }
        }
        return false;
    }
}
