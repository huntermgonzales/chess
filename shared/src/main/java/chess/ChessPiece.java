package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves;

        switch (type) {
            case BISHOP:
                BishopMovesCalculator bishopMoves = new BishopMovesCalculator(pieceColor);
                moves = bishopMoves.calculatePossibleMoves(board, myPosition);
                break;
            case KNIGHT:
                KnightMovesCalculator knightMoves = new KnightMovesCalculator(pieceColor);
                moves = knightMoves.calculatePossibleMoves(board, myPosition);
                break;
            case PAWN:
                PawnMovesCalculator pawnMoves = new PawnMovesCalculator(pieceColor);
                moves = pawnMoves.calculatePossibleMoves(board, myPosition);
                break;
            case PieceType.QUEEN:
                QueenMovesCalculator queenMoves = new QueenMovesCalculator(pieceColor);
                moves = queenMoves.calculatePossibleMoves(board, myPosition);
                break;
            case KING:
                KingMovesCalculator kingMoves = new KingMovesCalculator(pieceColor);
                moves = kingMoves.calculatePossibleMoves(board, myPosition);
                break;
            case ROOK:
                RookMovesCalculator rookMoves = new RookMovesCalculator(pieceColor);
                moves = rookMoves.calculatePossibleMoves(board, myPosition);
                break;
            default:
                return null;
        }
        return moves;
    }
}
