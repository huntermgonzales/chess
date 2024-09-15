package chess;
import chess.ChessPiece;

import java.util.ArrayList;
import java.util.Collection;


public abstract class ChessMovesCalculator {
    ChessGame.TeamColor teamColor;
    public ChessMovesCalculator(ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public abstract Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition);
}
