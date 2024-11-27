package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataaccessConfig;
import dataaccess.GameDAO;
import exceptions.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerNotification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    AuthDAO authDAO;
    GameDAO gameDAO;

    public WebSocketHandler() {
        this.authDAO = DataaccessConfig.getInstance().getAuthDAO();
        this.gameDAO = DataaccessConfig.getInstance().getGameDAO();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        try {
            switch (command.getCommandType()) {
                case CONNECT -> joinGame(command.getAuthToken(), command.getGameID(), session);
                case LEAVE -> leaveGame(command.getAuthToken(), command.getGameID());
            }
        }catch (DataAccessException e) {
            session.getRemote().sendString("Unable to connect");
        }
    }

    private void joinGame(String authToken, int gameID, Session session) throws DataAccessException, IOException {
        connections.add(authToken, gameID, session);

        //Load Game notification
        LoadGameMessage message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        GameData gameData = gameDAO.getGame(gameID);
        message.setGame(gameData.game());
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.SELF, message, gameID);

        //send notification to everyone else
        String username = authDAO.getAuthData(authToken).username();
        ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION);
        String color;
        if (gameData.blackUsername().equals(username) && gameData.whiteUsername().equals(username)) {
            color  = "both players";
        } else if (gameData.blackUsername().equals(username)) {
            color = "black";
        } else if(gameData.whiteUsername().equals(username)) {
            color = "white";
        } else {
            color = "an observer";
        }
        notification.addMessage(String.format("%s has joined the game as %s", username, color));
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.ALL_BUT_SELF, notification, gameID);
    }

    private void leaveGame(String authToken, int gameID) {
        connections.remove(authToken, gameID);
    }
}
