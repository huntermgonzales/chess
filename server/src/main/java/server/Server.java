package server;

import dataaccess.DataaccessConfig;
import dataaccess.MySQLAccess;
import exceptions.DataAccessException;
import handler.*;
import spark.*;
import websocket.WebSocketHandler;

public class Server {

    private final WebSocketHandler webSocketHandler;

    public Server() {
        this.webSocketHandler = new WebSocketHandler();
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        DataaccessConfig.initialize(true);
        try {
            new MySQLAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        if (!DataaccessConfig.isInitialized()) {
            DataaccessConfig.initialize(true);
            try {
                new MySQLAccess();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.webSocket("/ws", webSocketHandler);

        Spark.post("/user", (req, res) -> new RegisterHandler().handleRegister(req, res));      //register
        Spark.post("/session", (req, res) -> new LoginHandler().handleLogin(req, res));         //login
        Spark.delete("/session", (req, res) -> new LogoutHandler().handleLogout(req, res));     //logout
        Spark.post("/game", (req, res) -> new CreateGameHandler().handleCreateGame(req, res));  //create game
        Spark.get("/game", (req, res) -> new ListGamesHandler().handleListGames(req, res));     //list games
        Spark.put("/game", (req, res) -> new JoinGameHandler().handleJoinGame(req, res));       //join game
        Spark.delete("/db", (req, res) -> new ClearHandler().handleClear(req, res));            //clear all

        Spark.awaitInitialization();
        return Spark.port();
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
