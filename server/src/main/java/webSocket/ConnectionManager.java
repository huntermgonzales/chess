package webSocket;

import org.eclipse.jetty.websocket.api.Session;
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
        if (connections != null) {
            connections.remove(authToken);
            if (connections.isEmpty()) {
                gameConnections.remove(gameID);
            }
        }
    }

    public void broadcast(String authToken, BroadcastReceivers receivers, ServerMessage message, Integer gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        var connections = gameConnections.get(gameID);
        switch (receivers) {
            case SELF -> broadcastToSelf(authToken, message, gameID);
        }
//        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
//                if (!c.authToken.equals(authToken)) {
//                    c.send(message.toString());
//                }
//            } else {
//                removeList.add(c);
//            }
//        }
//
//        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.remove(c.authToken);
//        }
    }

    public void broadcastToSelf(String authToken, ServerMessage message, Integer gameID) throws IOException {
        Connection connection = gameConnections.get(gameID).get(authToken);
        if (connection.session.isOpen()) {
            connection.send(message.toString());
        }
    }



    }
