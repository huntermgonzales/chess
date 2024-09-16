package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator extends ChessMovesCalculator {

    public QueenMovesCalculator(ChessGame.TeamColor teamColor) {
        super(teamColor);
    }

    @Override
    public void addMovesInDirection(List<ChessMove> moves, ChessPosition myPosition, int row, int column, int rowIncrement, int colIncrement, ChessBoard board) {
        int newRow = row + rowIncrement;
        int newColumn = column + colIncrement;
        if (newRow <1 || newRow > 8 || newColumn < 1 || newColumn > 8) {
            return;
        }
        // Continue moving in the direction as long as within the bounds
        while (newRow >= 1 && newRow <= 8 && newColumn >= 1 && newColumn <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newColumn);
            ChessPiece pieceOnSquare = board.getPiece(newPosition);
            //piece is blocked if the same team color is on the square currently on
            if (pieceOnSquare != null && pieceOnSquare.getTeamColor() == teamColor) {
                return;
            }
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moves.add(newMove);
            System.out.println(newMove); //debug
            //piece can not continue past this square if opposite team is on current square (it is captured)
            pieceOnSquare = board.getPiece(newPosition);
            if (pieceOnSquare != null && pieceOnSquare.getTeamColor() != teamColor) {
                return;
            }
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
