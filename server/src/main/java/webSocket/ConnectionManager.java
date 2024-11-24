package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    //This will create a map of sessions for each game
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> gameConnections = new ConcurrentHashMap<>();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> add(command.getAuthToken(), command.getGameID(), session);
            case LEAVE -> remove(command.getAuthToken(), command.getGameID());
        }
    }

    //if there is no map of connections for the gameID, it will create a new one for the game and add the connection
    public void add(String authToken, Integer gameID, Session session) {
        var connection = new Connection(authToken, session);
        gameConnections.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>()).put(authToken, connection);
    }

    public void remove(String authToken, Integer gameID) {
        var connections = gameConnections.get(gameID);
        if (connections != null) {
            connections.remove(authToken);
            if (connections.isEmpty()) {
                gameConnections.remove(gameID);
            }
        }

    }


}
