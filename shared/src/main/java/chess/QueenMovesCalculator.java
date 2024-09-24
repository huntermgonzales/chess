package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator extends ChessMovesCalculator {

    public QueenMovesCalculator(ChessGame.TeamColor teamColor) {
        super(teamColor);
    }


    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();
        addMovesInDirection(moves, myPosition, row, column, 1, 1, board); //positive positive
        addMovesInDirection(moves, myPosition, row, column, -1, -1, board); //negative negative
        addMovesInDirection(moves, myPosition, row, column, 1, -1, board); //positive negative
        addMovesInDirection(moves, myPosition, row, column, -1, 1, board); //negative positive
        addMovesInDirection(moves, myPosition, row, column, 1, 0, board);
        addMovesInDirection(moves, myPosition, row, column, -1, 0, board);
        addMovesInDirection(moves, myPosition, row, column, 0, 1, board);
        addMovesInDirection(moves, myPosition, row, column, 0, -1, board);
        return moves;
    }
}
