package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static chess.ChessPiece.PieceType.KING;

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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }



    public Collection<ChessPosition> getPiecesOfColor(ChessGame.TeamColor color) {
        Collection<ChessPosition> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j] != null && squares[i][j].getTeamColor() == color) {
                    positions.add(new ChessPosition(i + 1, j + 1));
                }
            }
        }
        return positions;
    }


    public boolean isKingOnSquare(int i, int j) {
        Collection<ChessMove> moves = squares[i][j].pieceMoves(this, new ChessPosition(i + 1,j + 1));
        for (ChessMove move : moves) {
            ChessPiece piece = squares[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1];
            if (piece != null && piece.getPieceType() == KING) {
                return true;
            }
        }
        return false;
    }

    //This function will return whether the king of the team who's turn it is is currently being attacked
    //(this means it cant make that move)
    public boolean isCurrentKingAttacked(ChessGame.TeamColor color) {
        boolean kingAttacked = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j] != null && squares[i][j].getTeamColor() != color) {
                    if (isKingOnSquare(i,j)) {
                        return true;
                    }
                }
            }
        }

        return false;
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

    private void createAndAddPiece(int row, int col, ChessGame.TeamColor teamColor, ChessPiece.PieceType pieceType) {
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
            createAndAddPiece(2, col, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }
        createAndAddPiece(1, 1, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        createAndAddPiece(1,2, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        createAndAddPiece(1,3, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        createAndAddPiece(1,4, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        createAndAddPiece(1,5, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        createAndAddPiece(1,6, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        createAndAddPiece(1,7, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        createAndAddPiece(1,8, ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        //For Black
        for (int col = 1; col <= 8; col++) {
            createAndAddPiece(7, col, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        createAndAddPiece(8,1, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        createAndAddPiece(8,2, ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        createAndAddPiece(8,3,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        createAndAddPiece(8,4,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        createAndAddPiece(8,5,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        createAndAddPiece(8,6,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        createAndAddPiece(8,7,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        createAndAddPiece(8,8,ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    }
}
