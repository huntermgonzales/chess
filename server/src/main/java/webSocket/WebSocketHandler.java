package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataaccessConfig;
import dataaccess.GameDAO;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerNotification;

import java.io.IOException;
import java.util.Objects;

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

        //checks if the authToken is valid
        if (authDAO.getAuthData(authToken) == null) {
            String message = "Error: you do not have permission to do this";
            sendErrorMessage(message, authToken, gameID);
            return;
        }

        //checks if gameID is valid
        if (gameDAO.getGame(gameID) == null) {
            String message = "Error: invalid game ID";
            sendErrorMessage(message, authToken, gameID);
            return;
        }

        //Load Game notification
        LoadGameMessage message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        GameData gameData = gameDAO.getGame(gameID);
        message.setGame(gameData.game());
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.SELF, message, gameID);

        //send notification to everyone else
        String username = authDAO.getAuthData(authToken).username();
        ServerNotification notification = createServerNotification(gameData, username);
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.ALL_BUT_SELF, notification, gameID);
    }

    private ServerNotification createServerNotification(GameData gameData, String username) {
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
        return notification;
    }

    private void leaveGame(String authToken, int gameID) throws DataAccessException, IOException {
        //checks if the authToken is valid
        AuthData authData = authDAO.getAuthData(authToken);
        if (authData == null) {
            String message = "Error: you do not have permission to do this";
            sendErrorMessage(message, authToken, gameID);
            return;
        }

        //checks if gameID is valid
        if (gameDAO.getGame(gameID) == null) {
            String message = "Error: invalid game ID";
            sendErrorMessage(message, authToken, gameID);
            return;
        }
        GameData gameData = gameDAO.getGame(gameID);
        GameData updatedGame = updateUsers(gameID, gameData, authData);
        gameDAO.updateGameData(updatedGame);

        //create a message
        ServerNotification notification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.addMessage(String.format("%s has left the game", authData.username()));
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.ALL_BUT_SELF, notification, gameID);

        connections.remove(authToken, gameID);
    }

    private GameData updateUsers(int gameID, GameData gameData, AuthData authData) {
        GameData updatedGame;
        //figures out which color the requesting player is
        if (Objects.equals(gameData.whiteUsername(), authData.username())) {
            updatedGame = new GameData(gameID, null, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else if (Objects.equals(gameData.blackUsername(), authData.username())) {
            updatedGame = new GameData(gameID, gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
        } else {
            updatedGame = gameData;  //observing player does not need to be removed
        }
        return updatedGame;
    }

    private void sendErrorMessage(String message, String authToken, int gameID) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR);
        errorMessage.setErrorMessage(message);
        connections.broadcast(authToken, ConnectionManager.BroadcastReceivers.SELF, errorMessage, gameID);
        connections.remove(authToken, gameID);
    }
}
