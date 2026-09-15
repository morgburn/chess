package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        //check which color the piece in that position is
        //if the color is white
            //if the pawn can move up, check the space directly above, if it's empty add that move to the possibleMoves
                //if the new move makes the pawn end in row 8, promote the pawn
            //check the space above and to the left, if a black piece is there, add to possibleMoves
            //check the space above and to the right, if a black piece is there, add to possibleMoves
            //if the piece started in row 2, check the space 2 above current position, if empty add to possibleMoves
        //if the color is black
            //if the pawn can move down, check the space directly below, if it's empty add to possibleMoves
                //if the new move makes the pawn end up in row 1, promote the pawn
            //check the space below and to the left, if a white piece is there, add to possibleMoves
            //check the space below and to the right, if a white piece is there, add to possibleMoves
            //if the piece started in row 7, check the space 2 below current position, if empty add to possibleMoves

        return possibleMoves;
    }
}
