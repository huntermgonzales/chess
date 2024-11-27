package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGameMessage extends ServerMessage{
    private ChessGame game;

    public LoadGameMessage(ServerMessageType type) {
        super(type);
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
