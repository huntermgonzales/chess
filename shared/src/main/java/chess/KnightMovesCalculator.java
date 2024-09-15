package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator extends ChessMovesCalculator {
    public KnightMovesCalculator(ChessGame.TeamColor teamColor) {
        super(teamColor);
    }

    @Override
    public void addMovesInDirection(List<ChessMove> moves, ChessPosition myPosition, int row, int column, int rowIncrement, int colIncrement, ChessBoard board) {
        int newRow = row + rowIncrement;
        int newColumn = column + colIncrement;
        if (newRow <1 || newRow > 8 || newColumn < 1 || newColumn > 8) {
            return;
        }
        ChessPosition newPosition = new ChessPosition(newRow, newColumn);
        ChessPiece pieceOnSquare = board.getPiece(newPosition);
        //piece is blocked if the same team color is on the square currently on
        if (pieceOnSquare != null && pieceOnSquare.getTeamColor() == teamColor) {
            return;
        }
        ChessMove newMove = new ChessMove(myPosition, newPosition, null);
        moves.add(newMove);
        System.out.println(newMove); //debug
    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();
        addMovesInDirection(moves, myPosition, row, column, 1, 2, board); //positive positive
        addMovesInDirection(moves, myPosition, row, column, 1, -2, board); //negative negative
        addMovesInDirection(moves, myPosition, row, column, -1, 2, board); //positive negative
        addMovesInDirection(moves, myPosition, row, column, -1, -2, board); //negative positive
        addMovesInDirection(moves, myPosition, row, column, 2, 1, board); //up
        addMovesInDirection(moves, myPosition, row, column, -2, 1, board); //down
        addMovesInDirection(moves, myPosition, row, column, -2, -1, board); //right
        addMovesInDirection(moves, myPosition, row, column, 2, -1, board); //left
        return moves;
    }
}
