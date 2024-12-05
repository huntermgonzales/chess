package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
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

    private String squareShade;

    private void highlightSquare() {
        if (Objects.equals(squareShade, EscapeSequences.SET_BG_COLOR_LIGHT_GREY)) {
            squareShade = EscapeSequences.SET_BG_COLOR_GREEN;
        } else {
            squareShade = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
        }
    }

    private void changeShade() {
        if (Objects.equals(squareShade, EscapeSequences.SET_BG_COLOR_LIGHT_GREY) ||
                Objects.equals(squareShade, EscapeSequences.SET_BG_COLOR_GREEN)) {
            squareShade = EscapeSequences.SET_TEXT_COLOR_DARK_GREY;
        } else {
            squareShade = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        }
    }

    private String drawTopOrBottom(ChessGame.TeamColor perspective) {
        String borderColor = EscapeSequences.SET_BG_COLOR_MAGENTA;
        StringBuilder drawing = new StringBuilder();
        drawing.append(borderColor).append(EscapeSequences.SET_TEXT_COLOR_WHITE);
        if (perspective != ChessGame.TeamColor.BLACK) {
            drawing.append("    a  b  c  d  e  f  g  h    ");
        } else {
            drawing.append("    h  g  f  e  d  c  b  a    ");
        }
        drawing.append(EscapeSequences.SET_BG_COLOR_BLACK + "\n");
        return drawing.toString();
    }

    public String drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        String borderColor = EscapeSequences.SET_BG_COLOR_MAGENTA;
        squareShade = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        int inc;
        int start;
        if (perspective != ChessGame.TeamColor.BLACK) {
            inc = -1;
            start = 8;
        } else {
            inc = 1;
            start = 1;
        }

        StringBuilder drawing = new StringBuilder();
        drawing.append(drawTopOrBottom(perspective));

        for (int row = start; row <=8 && row >=1; row += inc) {
            //draws the left border
            drawing.append(EscapeSequences.SET_BG_COLOR_MAGENTA + EscapeSequences.SET_TEXT_COLOR_WHITE + " ").
                    append(row).append(" ").append(EscapeSequences.RESET_BG_COLOR);
            for (int col = start; col <= 8 && col >= 1; col += inc) {
                drawing.append(squareShade).append(getPieceString(board, row, 9 - col)).append(EscapeSequences.RESET_BG_COLOR);
                changeShade();
            }
            //draws the right border
            drawing.append(borderColor).append(" ").append(row).append(" ").append(EscapeSequences.SET_BG_COLOR_BLACK).append("\n");
            changeShade();
        }
        drawing.append(borderColor);
        drawing.append(drawTopOrBottom(perspective));
        drawing.append(EscapeSequences.RESET_BG_COLOR);

        return drawing.toString();
    }

    public String highlightBoard(ChessGame game, ChessGame.TeamColor perspective, ChessPosition chessPosition) {
        ChessBoard board = game.getBoard();
        String borderColor = EscapeSequences.SET_BG_COLOR_MAGENTA;
        squareShade = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        Collection<ChessMove> moves = game.validMoves(chessPosition);

        int inc;
        int start;
        if (perspective != ChessGame.TeamColor.BLACK) {
            inc = -1;
            start = 8;
        } else {
            inc = 1;
            start = 1;
        }

        StringBuilder drawing = new StringBuilder();
        drawing.append(drawTopOrBottom(perspective));
        for (int row = start; row <=8 && row >=1; row += inc) {
            //draws the left border
            drawing.append(EscapeSequences.SET_BG_COLOR_MAGENTA + EscapeSequences.SET_TEXT_COLOR_WHITE + " ").
                    append(row).append(" ").append(EscapeSequences.RESET_BG_COLOR);
            for (int col = start; col <= 8 && col >= 1; col += inc) {
                //figures out if square needs to be shaded or not
                for (ChessMove move : moves) {
                    if (Objects.equals(move.getEndPosition(), new ChessPosition(row, 9 - col))) {
                        highlightSquare();
                    }
                }

                drawing.append(squareShade).append(getPieceString(board, row, 9 - col)).append(EscapeSequences.RESET_BG_COLOR);
                changeShade();
            }
            //draws the right border
            drawing.append(borderColor).append(" ").append(row).append(" ").append(EscapeSequences.SET_BG_COLOR_BLACK).append("\n");
            changeShade();
        }
        drawing.append(borderColor);
        drawing.append(drawTopOrBottom(perspective));
        drawing.append(EscapeSequences.RESET_BG_COLOR);


        return drawing.toString();
    }

}
