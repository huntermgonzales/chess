package client;

import chess.ChessBoard;
import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.ChessBoardArtist;

public class BoardTest {

    @Test
    void drawBoard() {
        String drawing = new ChessBoardArtist().drawBoard(new ChessBoard(), ChessGame.TeamColor.WHITE);
        System.out.print(drawing);
    }

    @Test
    void pseudoDraw() {
        ChessBoardArtist artist = new ChessBoardArtist();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                System.out.print(artist.getPieceString(board, i, j));
            }
            System.out.print("\n");
        }
    }
}
