package server;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.ResponseException;
import ui.ChessBoardArtist;
import ui.EscapeSequences;
import ui.Repl;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
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

    ChessGame.TeamColor teamColor;

    public Session session;

    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI uri = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    var receivedMessage = new Gson().fromJson(message, ServerMessage.class);
                    String result = switch (receivedMessage.getServerMessageType()) {
                        case LOAD_GAME -> receiveLoadGame(message);
                        case NOTIFICATION -> receiveNotification(message);
                        default -> "wrong";
                    };
                    System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_BLUE + result + "\n" +
                            EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + ">>> " + EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private String receiveLoadGame(String message) {
        LoadGameMessage gameMessage = new Gson().fromJson(message, LoadGameMessage.class);
        ChessBoard board = gameMessage.getGame().getBoard();
        return "\n" + new ChessBoardArtist().drawBoard(board, teamColor) + "\n";
    }

    private String receiveNotification(String message) {
        ServerNotification notification = new Gson().fromJson(message, ServerNotification.class);
        return notification.getMessage();
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void joinGame(int gameID, String authToken, ChessGame.TeamColor teamColor) throws Exception {
        this.teamColor = teamColor;
        var command  = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        send(new Gson().toJson(command));
    }
}
