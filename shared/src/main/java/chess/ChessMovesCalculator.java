package chess;
import chess.ChessPiece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class ChessMovesCalculator {
    ChessGame.TeamColor teamColor;
    public ChessMovesCalculator(ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public void addMovesInDirection(List<ChessMove> moves, ChessPosition myPosition, int row, int column, int rowIncrement, int colIncrement, ChessBoard board){
        int newRow = myPosition.getRow() + rowIncrement;
        int newCol = myPosition.getColumn() + colIncrement;
        while (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == teamColor) {
                return;
            }
            moves.add(new ChessMove(myPosition, newPosition, null));
            if (board.getPiece(newPosition) != null) { //this is so it doesn't continue passed a taken piece
                return;
            }

            newRow += rowIncrement;
            newCol += colIncrement;
        }
    }

    public void addMovesOnceInDirection(List<ChessMove> moves, ChessPosition myPosition, int row, int column, int rowIncrement, int colIncrement, ChessBoard board) {
        int newRow = myPosition.getRow() + rowIncrement;
        int newCol = myPosition.getColumn() + colIncrement;
        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == teamColor) {
                return;
            }
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
    }

    public abstract Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition);
}
