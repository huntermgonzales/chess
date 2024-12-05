package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    //This will create a map of sessions for each game
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> gameConnections = new ConcurrentHashMap<>();

    public enum BroadcastReceivers {
        SELF,
        EVERYONE,
        ALL_BUT_SELF
    }

    //if there is no map of connections for the gameID, it will create a new one for the game and add the connection
    public void add(String authToken, Integer gameID, Session session) {
        var connection = new Connection(authToken, gameID, session);
        gameConnections.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>()).put(authToken, connection);
    }

    public void remove(String authToken, Integer gameID) {
        var connections = gameConnections.get(gameID);
        if (connections != null && connections.get(authToken) != null) {
            connections.get(authToken).session.close();
            connections.remove(authToken);
            if (connections.isEmpty()) {
                gameConnections.remove(gameID);
            }
        }
    }

    public void broadcast(String authToken, BroadcastReceivers receivers, ServerMessage message, Integer gameID) throws IOException {
        switch (receivers) {
            case SELF -> broadcastToSelf(authToken, message, gameID);
            case ALL_BUT_SELF -> broadcastToAllButSelf(authToken, message, gameID);
            case EVERYONE -> broadcastToEveryone(message, gameID);
        }
    }

    private void broadcastToEveryone(ServerMessage message, int gameID) throws IOException {
        ConcurrentHashMap<String, Connection> connections = gameConnections.get(gameID);
        for (Connection connection : connections.values()) {
            connection.send(new Gson().toJson(message));
        }
    }
    private void broadcastToSelf(String authToken, ServerMessage message, Integer gameID) throws IOException {
        Connection connection = gameConnections.get(gameID).get(authToken);
        if (connection.session.isOpen()) {
            connection.send(new Gson().toJson(message));
        }
    }

    private void broadcastToAllButSelf(String authToken, ServerMessage message, Integer gameID) throws IOException {
        ConcurrentHashMap<String, Connection> connections = gameConnections.get(gameID);
        for (Connection connection : connections.values()) {
            if (!connection.authToken.equals(authToken) && connection.session.isOpen()) {
                connection.send(new Gson().toJson(message));
            }
        }
    }

}
