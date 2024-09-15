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

    public abstract void addMovesInDirection(List<ChessMove> moves, ChessPosition myPosition, int row, int column, int rowIncrement, int colIncrement, ChessBoard board);

    public abstract Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition);
}
