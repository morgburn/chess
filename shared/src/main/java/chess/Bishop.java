package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends ChessPiece {
    public Bishop(ChessGame.TeamColor pieceColor, PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int newRow = row;
        int newCol = col;

        while (newRow < 8 && newCol < 8) {
            newRow += 1;
            newCol += 1;
            if (addMoves(board, myPosition, possibleMoves, newRow, newCol)) {
                break;
            }
        }

        newRow = row;
        newCol = col;
        while (newRow > 1 && newCol < 8) {
            newRow -= 1;
            newCol += 1;
            if (addMoves(board, myPosition, possibleMoves, newRow, newCol)) {
                break;
            }
        }

        newRow = row;
        newCol = col;
        while (newRow > 1 && newCol > 1) {
            newRow -= 1;
            newCol -= 1;
            if (addMoves(board, myPosition, possibleMoves, newRow, newCol)) {
                break;
            }
        }

        newRow = row;
        newCol = col;
        while (newRow < 8 && newCol > 1) {
            newRow += 1;
            newCol -= 1;
            if (addMoves(board, myPosition, possibleMoves, newRow, newCol)) {
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
            if (board.getPiece(newPosition).getTeamColor() == getTeamColor()) {
                return true;
            } else {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                return true;
            }
        }
        return false;
    }
}
