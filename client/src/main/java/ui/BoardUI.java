package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardUI {

    public static String displayBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        int row;
        int col;
        String borderLetters;
        if (perspective == ChessGame.TeamColor.WHITE) {
            borderLetters = "    a   b  c   d   e  f   g   h    ";
        } else {
            borderLetters = "    h   g   f  e   d  c   b   a    ";
        }

        StringBuilder drawnBoard = new StringBuilder();
        drawnBoard.append(SET_BG_COLOR_DARK_GREY).append(RESET_TEXT_COLOR).append(borderLetters).append(RESET_BG_COLOR).append("\n");
        for (int i = 0; i < 8; i++) {
            if (perspective == ChessGame.TeamColor.WHITE) {
                row = 8 - i;
            } else {
                row = i + 1;
            }
            drawnBoard.append(SET_BG_COLOR_DARK_GREY).append(" ").append(row).append(" ");
            for (int j = 0; j < 8; j++) {
                if (perspective == ChessGame.TeamColor.WHITE) {
                    col = j + 1;
                } else {
                    col = 8 - j;
                }
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));
                if ((row + col) % 2 == 0) {
                    drawnBoard.append(SET_BG_COLOR_BLACK).append(getPieceAtSquare(currentPiece));
                } else {
                    drawnBoard.append(SET_BG_COLOR_LIGHT_GREY).append(getPieceAtSquare(currentPiece));
                }
            }
            drawnBoard.append(SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR).append(" ").append(row).append(" ").append(RESET_BG_COLOR).append("\n");
        }
        drawnBoard.append(SET_BG_COLOR_DARK_GREY).append(borderLetters).append(RESET_BG_COLOR);

        return drawnBoard.toString();
    }

    private static String getPieceAtSquare(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        StringBuilder chessPiece = new StringBuilder();
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            chessPiece.append(SET_TEXT_COLOR_GREEN);
            switch (piece.getPieceType()) {
                case QUEEN -> chessPiece.append(WHITE_QUEEN);
                case KING -> chessPiece.append(WHITE_KING);
                case BISHOP -> chessPiece.append(WHITE_BISHOP);
                case KNIGHT -> chessPiece.append(WHITE_KNIGHT);
                case ROOK -> chessPiece.append(WHITE_ROOK);
                case PAWN -> chessPiece.append(WHITE_PAWN);
            }
        } else {
            chessPiece.append(SET_TEXT_COLOR_BLUE);
            switch (piece.getPieceType()) {
                case QUEEN -> chessPiece.append(BLACK_QUEEN);
                case KING -> chessPiece.append(BLACK_KING);
                case BISHOP -> chessPiece.append(BLACK_BISHOP);
                case KNIGHT -> chessPiece.append(BLACK_KNIGHT);
                case ROOK -> chessPiece.append(BLACK_ROOK);
                case PAWN -> chessPiece.append(BLACK_PAWN);
            }
        }
        return chessPiece.toString();
    }
}
