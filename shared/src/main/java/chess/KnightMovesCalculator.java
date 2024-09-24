package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator extends ChessMovesCalculator {
    public KnightMovesCalculator(ChessGame.TeamColor teamColor) {
        super(teamColor);
    }


    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();
        addMovesOnceInDirection(moves, myPosition, row, column, 1, 2, board); //positive positive
        addMovesOnceInDirection(moves, myPosition, row, column, 1, -2, board); //negative negative
        addMovesOnceInDirection(moves, myPosition, row, column, -1, 2, board); //positive negative
        addMovesOnceInDirection(moves, myPosition, row, column, -1, -2, board); //negative positive
        addMovesOnceInDirection(moves, myPosition, row, column, 2, 1, board); //up
        addMovesOnceInDirection(moves, myPosition, row, column, -2, 1, board); //down
        addMovesOnceInDirection(moves, myPosition, row, column, -2, -1, board); //right
        addMovesOnceInDirection(moves, myPosition, row, column, 2, -1, board); //left
        return moves;
    }
}
