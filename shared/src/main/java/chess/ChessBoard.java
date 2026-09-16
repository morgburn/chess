package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;

        ChessPiece newWhitePawn = new ChessPiece(white, ChessPiece.PieceType.PAWN);
        ChessPiece newBlackPawn = new ChessPiece(black, ChessPiece.PieceType.PAWN);
        for (int i = 0; i < 8; i++) {
            board[1][i] = newWhitePawn;
            board[6][i] = newBlackPawn;

            ChessPiece.PieceType type = ChessPiece.PieceType.PAWN;
            if (i == 0 || i == 7) {
                type = ChessPiece.PieceType.ROOK;
            } else if (i == 1 || i == 6) {
                type = ChessPiece.PieceType.KNIGHT;
            } else if (i == 2 || i == 5) {
                type = ChessPiece.PieceType.BISHOP;
            } else if (i == 3) {
                type = ChessPiece.PieceType.QUEEN;
            } else {
                type = ChessPiece.PieceType.KING;
            }
            ChessPiece newPiece = new ChessPiece(white, type);
            board[0][i] = newPiece;
            newPiece = new ChessPiece(black, type);
            board[7][i] = newPiece;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
