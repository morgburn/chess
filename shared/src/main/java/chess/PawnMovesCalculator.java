package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = myPosition.getRow();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        if (color == ChessGame.TeamColor.WHITE) {
            if (row < 8) {
                moveForward(board, myPosition, possibleMoves, 1, 7, 2);
                addCapture(board, myPosition, -1, possibleMoves, 1, 7, ChessGame.TeamColor.BLACK);
                addCapture(board, myPosition, 1, possibleMoves, 1, 7, ChessGame.TeamColor.BLACK);
            }
        }

        if (color == ChessGame.TeamColor.BLACK) {
            if (row > 1) {
                moveForward(board, myPosition, possibleMoves, -1, 2, 7);
                addCapture(board, myPosition, -1, possibleMoves, -1, 2, ChessGame.TeamColor.WHITE);
                addCapture(board, myPosition, 1, possibleMoves, -1, 2, ChessGame.TeamColor.WHITE);
            }
        }
        return possibleMoves;
    }

    private void moveForward(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves,
                             int direction, int promotionRow, int startRow) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition newPosition = new ChessPosition(row + direction, col);
        if (board.getPiece(newPosition) == null) {
            if (row == promotionRow) {
                addPromotionPiece(myPosition, possibleMoves, newPosition);
            } else {
                possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                newPosition = new ChessPosition(row + (direction * 2), col);
                if (row == startRow && board.getPiece(newPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    private void addCapture(ChessBoard board, ChessPosition myPosition, int colOffset, Collection<ChessMove> possibleMoves,
                            int direction, int promotionRow, ChessGame.TeamColor enemyColor) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition newPosition;
        if (0 < col + colOffset && col + colOffset < 9) {
            newPosition = new ChessPosition(row + direction, col + colOffset);
            if (board.getPiece(newPosition) != null) {
                if (row == promotionRow && board.getPiece(newPosition).getTeamColor() == enemyColor) {
                    addPromotionPiece(myPosition, possibleMoves, newPosition);
                } else if (board.getPiece(newPosition).getTeamColor() == enemyColor) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
    }

    private void addPromotionPiece(ChessPosition myPosition, Collection<ChessMove> possibleMoves, ChessPosition newPosition) {
        possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
        possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
        possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
        possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
    }
}
