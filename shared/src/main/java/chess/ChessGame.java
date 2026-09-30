package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard currentBoard;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.currentBoard = new ChessBoard();
        currentBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessBoard board = currentBoard;
        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        TeamColor color = piece.getTeamColor();
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        for (ChessMove move : possibleMoves) {
            ChessBoard boardCopy = board.copy();
            boardCopy.addPiece(move.getStartPosition(), null);
            boardCopy.addPiece(move.getEndPosition(), piece);
            setBoard(boardCopy);
            if (!isInCheck(color)) {
                validMoves.add(move);
            }
            setBoard(board);
        }

        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = currentBoard.getPiece(startPosition);
        if (piece == null) {
            throw new InvalidMoveException("No piece at start position");
        }

        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (!validMoves.contains(move) || piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Invalid Move");
        }
        else {
            currentBoard.addPiece(startPosition, null);
            if (move.getPromotionPiece() != null) {
                piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            }
            currentBoard.addPiece(endPosition, piece);
            setBoard(currentBoard);

            if (teamTurn == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {setTeamTurn(TeamColor.WHITE);}
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = kingPosition(teamColor);

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = currentBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> possibleMoves = piece.pieceMoves(currentBoard, position);
                    for (ChessMove move : possibleMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    ChessPosition kingPosition(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPosition(0, 0);
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = currentBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = position;
                }
            }
        }
        return kingPosition;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return noValidTeamMoves(teamColor);
    }

    public boolean noValidTeamMoves(TeamColor teamcolor) {
        boolean noValidMoves = true;

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece piece = currentBoard.getPiece(currentPosition);
                if (piece != null && piece.getTeamColor() == teamcolor) {
                    Collection<ChessMove> validPieceMoves = validMoves(currentPosition);
                    if (!validPieceMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return noValidMoves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, currentBoard);
    }
}
