package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        if (color == ChessGame.TeamColor.WHITE) {
            if (row < 8) {
                ChessPosition newPosition = new ChessPosition(row + 1, col);
                if (row == 7) {
                    addPromotionPiece(board, myPosition, possibleMoves, newPosition);
                } else if (board.getPiece(newPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                if (col > 1) {
                    newPosition = new ChessPosition(row + 1, col - 1);
                    if (row == 7) {
                        addPromotionPiece(board, myPosition, possibleMoves, newPosition);
                    } else if (board.getPiece(newPosition) != null) {
                        if (board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                if (col < 8) {
                    newPosition = new ChessPosition(row + 1, col + 1);
                    if (row == 7) {
                        addPromotionPiece(board, myPosition, possibleMoves, newPosition);
                    } else if (board.getPiece(newPosition) != null) {
                        if (board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                if (row == 2) {
                    newPosition = new ChessPosition(row + 2, col);
                    if (board.getPiece(newPosition) == null) {
                        possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }

        if (color == ChessGame.TeamColor.BLACK) {
            if (row > 1) {
                ChessPosition newPosition = new ChessPosition(row - 1, col);
                if (row == 2) {
                    addPromotionPiece(board, myPosition, possibleMoves, newPosition);
                } else if (board.getPiece(newPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                if (col > 1) {
                    newPosition = new ChessPosition(row - 1, col - 1);
                    if (row == 2) {
                        addPromotionPiece(board, myPosition, possibleMoves, newPosition);
                    } else if (board.getPiece(newPosition) != null) {
                        if (board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                if (col < 8) {
                    newPosition = new ChessPosition(row - 1, col + 1);
                    if (row == 2) {
                        addPromotionPiece(board, myPosition, possibleMoves, newPosition);
                    } else if (board.getPiece(newPosition) != null) {
                        if (board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                if (row == 7) {
                    newPosition = new ChessPosition(row - 2, col);
                    if (board.getPiece(newPosition) == null) {
                        possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        }
        return possibleMoves;
    }

    private void addPromotionPiece(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> possibleMoves, ChessPosition newPosition) {
        if (board.getPiece(newPosition) == null) {
            possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
            possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
            possibleMoves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
        }
    }
}
