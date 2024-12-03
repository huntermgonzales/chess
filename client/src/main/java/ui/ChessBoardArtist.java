package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

public class ChessBoardArtist {

    public String getPieceString(ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            return "   " + EscapeSequences.SET_TEXT_COLOR_WHITE;
        }
        String pieceString;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pieceString = EscapeSequences.SET_TEXT_COLOR_YELLOW;
        } else {
            pieceString = EscapeSequences.SET_TEXT_COLOR_RED;
        }
        pieceString += switch (piece.getPieceType()) {
            case BISHOP -> " B ";
            case ROOK -> " R ";
            case KNIGHT -> " N ";
            case QUEEN -> " Q ";
            case KING -> " K ";
            case PAWN -> " P ";
        };
        return pieceString + EscapeSequences.SET_TEXT_COLOR_WHITE;
    }

    public String changeShade(String squareShade) {
        if (Objects.equals(squareShade, EscapeSequences.SET_BG_COLOR_LIGHT_GREY)) {
            return EscapeSequences.SET_TEXT_COLOR_DARK_GREY;
        } else {
            return EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        }
    }

    public String drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        String borderColor = EscapeSequences.SET_BG_COLOR_MAGENTA;
        StringBuilder drawing = new StringBuilder(borderColor + EscapeSequences.SET_TEXT_COLOR_WHITE);
        String squareShade = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        int inc;
        int start;
        if (perspective == ChessGame.TeamColor.WHITE || perspective == null) {
            inc = -1;
            start = 8;
            drawing.append("    a  b  c  d  e  f  g  h    ");
        } else {
            inc = 1;
            start = 1;
            drawing.append("    h  g  f  e  d  c  b  a    ");
        }
        drawing.append(EscapeSequences.SET_BG_COLOR_BLACK + "\n");

        for (int row = start; row <=8 && row >=1; row += inc) {
            drawing.append(EscapeSequences.SET_BG_COLOR_MAGENTA + EscapeSequences.SET_TEXT_COLOR_WHITE + " ").
                    append(row).append(" ").append(EscapeSequences.RESET_BG_COLOR);
            for (int col = start; col <= 8 && col >= 1; col += inc) {
                drawing.append(squareShade).append(getPieceString(board, row, 9 - col)).append(EscapeSequences.RESET_BG_COLOR);
                squareShade = changeShade(squareShade);
            }
            drawing.append(borderColor).append(" ").append(row).append(" ").append(EscapeSequences.SET_BG_COLOR_BLACK).append("\n");
            squareShade = changeShade(squareShade);
        }
        drawing.append(borderColor);
        if (perspective == ChessGame.TeamColor.WHITE) {
            drawing.append("    a  b  c  d  e  f  g  h    ");
        } else {
            drawing.append("    h  g  f  e  d  c  b  a    ");
        }
        drawing.append(EscapeSequences.RESET_BG_COLOR);

        return drawing.toString();
    }
}
