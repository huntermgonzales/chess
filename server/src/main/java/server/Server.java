package server;

import dataaccess.DataaccessConfig;
import dataaccess.MySQLAccess;
import exceptions.DataAccessException;
import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
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
        Spark.post("/user", (req, res) -> new RegisterHandler().handleRegister(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler().handleLogin(req, res));
        Spark.delete("/session", (req, res) -> new LogoutHandler().handleLogout(req, res));
        Spark.post("/game", (req, res) -> new CreateGameHandler().handleCreateGame(req, res));
        Spark.get("/game", (req, res) -> new ListGamesHandler().handleListGames(req, res));
        Spark.put("/game", (req, res) -> new JoinGameHandler().handleJoinGame(req, res));
        Spark.delete("/db", (req, res) -> new ClearHandler().handleClear(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
