package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator extends ChessMovesCalculator {
    public KingMovesCalculator(ChessGame.TeamColor teamColor) {
        super(teamColor);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }



    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();
        addMovesOnceInDirection(moves, myPosition, row, column, 1, 1, board); //positive positive
        addMovesOnceInDirection(moves, myPosition, row, column, -1, -1, board); //negative negative
        addMovesOnceInDirection(moves, myPosition, row, column, 1, -1, board); //positive negative
        addMovesOnceInDirection(moves, myPosition, row, column, -1, 1, board); //negative positive
        addMovesOnceInDirection(moves, myPosition, row, column, 0, 1, board); //up
        addMovesOnceInDirection(moves, myPosition, row, column, 0, -1, board); //down
        addMovesOnceInDirection(moves, myPosition, row, column, 1, 0, board); //right
        addMovesOnceInDirection(moves, myPosition, row, column, -1, 0, board); //left
        return moves;
    }


}
