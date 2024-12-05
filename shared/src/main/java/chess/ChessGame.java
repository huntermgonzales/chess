package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teamTurn = TeamColor.WHITE;
    boolean gameFinished = false;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    //This function will tell you if a certain move is possible or not depending on check
    private boolean checkMove(ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        board.removePiece(move.getStartPosition());
        ChessPiece tempPiece = null;
        if (board.getPiece(move.getEndPosition()) != null) {
            tempPiece = board.getPiece(move.getEndPosition());
        }
        board.addPiece(move.getEndPosition(), piece);

        boolean attacked = board.isCurrentKingAttacked(piece.getTeamColor());

        board.removePiece(move.getEndPosition());
        board.addPiece(move.getEndPosition(), tempPiece);
        board.addPiece(move.getStartPosition(), piece);
        return attacked;
    }
    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Iterator<ChessMove> iterator = moves.iterator();
        while (iterator.hasNext()) {
            ChessMove move = iterator.next();
            if (checkMove(move)) {
                iterator.remove();  // Safely removes the element during iteration
            }
        }

        return moves;
    }

    private void changeTurn() {
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null || piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
        if (!possibleMoves.contains(move)) {
            throw new InvalidMoveException();
        }
        board.removePiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        board.addPiece(move.getEndPosition(), piece);
        changeTurn();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return board.isCurrentKingAttacked(teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessPosition> positions = board.getPiecesOfColor(teamColor);
        if (positions.isEmpty()) {
            return false;
        }
        Collection<ChessMove> moves;
        for (ChessPosition positionOfPiece : positions) {
            moves = validMoves(positionOfPiece);
            if (!moves.isEmpty()) {
                return false;
            }
        }
        gameFinished = true;
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessPosition> positions = board.getPiecesOfColor(teamColor);
        if (positions.isEmpty()) {
            return false;
        }
        Collection<ChessMove> moves;
        for (ChessPosition positionOfPiece : positions) {
            moves = validMoves(positionOfPiece);
            if (!moves.isEmpty()) {
                return false;
            }
        }
        gameFinished = true;
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
