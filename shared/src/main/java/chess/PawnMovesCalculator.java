package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator extends ChessMovesCalculator {

    public PawnMovesCalculator(ChessGame.TeamColor teamColor) {
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
        //if not going diagonal and there is a piece in the position, it cannot move forward
        if (colIncrement == 0) {
            if (pieceOnSquare != null) {
                return;
            }
            if (rowIncrement == 2 || rowIncrement == -2) {
                ChessPosition possibleBlocked = new ChessPosition(row + rowIncrement/2, newColumn);
                ChessPiece possiblePiece = board.getPiece(possibleBlocked);
                if (possiblePiece != null) {
                    return;
                }
            }
        } else if (pieceOnSquare == null || pieceOnSquare.getTeamColor() == teamColor) {
                return;
        }
        if (newPosition.getRow() == 8 || newPosition.getRow() == 1) {
            ChessMove newMove = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN);
            moves.add(newMove);
            newMove = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP);
            moves.add(newMove);
            newMove = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK);
            moves.add(newMove);
            newMove = new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT);
            moves.add(newMove);
        } else {
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            moves.add(newMove);
        }
//        System.out.println(newMove); //debug
    }

    @Override
    public Collection<ChessMove> calculatePossibleMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();
        if (teamColor == ChessGame.TeamColor.BLACK) {
            addMovesInDirection(moves, myPosition, row, column, -1, 0, board);
            addMovesInDirection(moves, myPosition, row, column, -1, 1, board);
            addMovesInDirection(moves, myPosition, row, column, -1, -1, board);
            //only can move 2 if in column 6
            if (row == 7) {
                addMovesInDirection(moves, myPosition, row, column, -2, 0, board);
            }
        } else {
            addMovesInDirection(moves, myPosition, row, column, 1, 0, board);
            addMovesInDirection(moves, myPosition, row, column, 1, 1, board);
            addMovesInDirection(moves, myPosition, row, column, 1, -1, board);
            if (row == 2) {
                addMovesInDirection(moves, myPosition, row, column, 2, 0, board);
            }
        }

        return moves;
    }
}
