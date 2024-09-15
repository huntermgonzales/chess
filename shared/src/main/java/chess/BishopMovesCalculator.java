package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator extends ChessMovesCalculator {
    public BishopMovesCalculator(ChessGame.TeamColor teamColor) {
        super(teamColor);
    }

    private void addMovesInDirection(List<ChessMove> moves, ChessPosition myPosition, int row, int column, int rowIncrement, int colIncrement) {
        int newRow = row + rowIncrement;
        int newColumn = column + colIncrement;

        // Continue moving in the direction as long as within the bounds
        while (newRow >= 1 && newRow <= 8 && newColumn >= 1 && newColumn <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newColumn);
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moves.add(newMove);
            System.out.println(newMove);
            // Move further in the current direction
            newRow += rowIncrement;
            newColumn += colIncrement;
        }
    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();
        addMovesInDirection(moves, myPosition, row, column, 1, 1); //positive positive
        addMovesInDirection(moves, myPosition, row, column, -1, -1); //negative negative
        addMovesInDirection(moves, myPosition, row, column, 1, -1); //positive negative
        addMovesInDirection(moves, myPosition, row, column, -1, 1); //negative positive
        return moves;
    }
}
