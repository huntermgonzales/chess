package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator extends ChessMovesCalculator {
    public BishopMovesCalculator(ChessGame.TeamColor teamColor) {
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
        return moves;
    }
}
