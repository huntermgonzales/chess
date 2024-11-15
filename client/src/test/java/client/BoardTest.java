package client;

import chess.ChessBoard;
import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.ChessBoardArtist;

public class BoardTest {

    @Test
    void drawBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        String drawing = new ChessBoardArtist().drawBoard(board, ChessGame.TeamColor.WHITE);
        System.out.print(drawing);
    }

}
