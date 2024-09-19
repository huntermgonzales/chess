package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessPiece.PieceType.PAWN;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares =  new ChessPiece[8][8];
    public ChessBoard() {
    }

    @Override
    public String toString() {
        return "{" +
                Arrays.deepToString(squares) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    private void CreateAndAddPiece(int row, int col, ChessGame.TeamColor teamColor, ChessPiece.PieceType pieceType) {
        ChessPiece piece = new ChessPiece(teamColor, pieceType);
        ChessPosition position = new ChessPosition(row, col);
        addPiece(position, piece);
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //for white
        for (int col = 1; col <= 8; col++) {
            CreateAndAddPiece(2, col, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }
        CreateAndAddPiece(1, 1, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        CreateAndAddPiece(1,2, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        CreateAndAddPiece(1,3, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        CreateAndAddPiece(1,4, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        CreateAndAddPiece(1,5, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        CreateAndAddPiece(1,6, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        CreateAndAddPiece(1,7, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        CreateAndAddPiece(1,8, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        //For Black
        for (int col = 1; col <= 8; col++) {
            CreateAndAddPiece(7, col, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        CreateAndAddPiece(8,1, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        CreateAndAddPiece(8,2, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        CreateAndAddPiece(8,3,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        CreateAndAddPiece(8,4,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        CreateAndAddPiece(8,5,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        CreateAndAddPiece(8,6,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        CreateAndAddPiece(8,7,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        CreateAndAddPiece(8,8,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    }
}
