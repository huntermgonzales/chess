package server;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.ResponseException;
import ui.ChessBoardArtist;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.ServerNotification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    public Session session;

    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI uri = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    var receivedMessage = new Gson().fromJson(message, ServerNotification.class);
                    String result = switch (receivedMessage.getServerMessageType()) {
                        case LOAD_GAME -> receiveLoadGame(receivedMessage.getMessage());
                        default -> "wrong";
                    };
                    System.out.print(result);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public String receiveLoadGame(String message) {
        ChessBoard board = new Gson().fromJson(message, ChessBoard.class);
        return "\n" + new ChessBoardArtist().drawBoard(board, ChessGame.TeamColor.WHITE) + "\n";
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void joinGame(int gameID, String authToken) throws Exception {
        var command  = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        send(new Gson().toJson(command));
    }
}
