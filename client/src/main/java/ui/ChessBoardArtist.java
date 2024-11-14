package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardArtist {

    public String getPieceString(ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            return "   ";
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
        return pieceString;
    }

    public String drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        String drawing = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE;
        drawing += "    1 " + EscapeSequences.SET_BG_COLOR_BLUE + " N " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " 3 \n" ;
        drawing += "    8 " + EscapeSequences.SET_BG_COLOR_BLUE + " 2 " + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " 3 \n";

        return drawing;
    }
}
